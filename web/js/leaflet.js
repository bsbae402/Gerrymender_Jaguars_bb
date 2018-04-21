var MAPBOX_API_URL = "https://api.mapbox.com/styles/v1/roger-that/cjg6nbnkha3712rmuzpsgv4lq/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1Ijoicm9nZXItdGhhdCIsImEiOiJjamZmazFvdnM0dG1hMndxaGFqcmRiN2ViIn0.6CACaDuW3jp3eZwutrRrWQ";

class LeafletMap {

	constructor() {
		this.allLayers = [];

		this.layers = {
			geojsons : [],
		};

		this.onClick = () => {};

		this.hover = {
			activeLayer : null,
			$el : null,
		};
	}

	init(id) {
		this.$map = $("#" + id);
		this.lmap = L.map(id).setView([43.19, -71.572], 7);

		L.tileLayer(MAPBOX_API_URL, {
		    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
		    maxZoom: 18,
		    id: 'mapbox.streets',
		    accessToken: 'pk.eyJ1Ijoicm9nZXItdGhhdCIsImEiOiJjamZmazFvdnM0dG1hMndxaGFqcmRiN2ViIn0.6CACaDuW3jp3eZwutrRrWQ'
		}).addTo(this.lmap);

		this.hover.$el = $("<div>").addClass("popup hide");
		this.$map.append(this.hover.$el);
		this.$map.mousemove((e) => this.hoverMousemove(e));
	}

	resize() {
		if (this.lmap)
			this.lmap.invalidateSize();
	}

	hoverMousemove(e) {
		var PADDING = 10;
		var mx = e.pageX - $("#map").offset().left, my = e.pageY - $("#map").offset().top;
		var w = this.hover.$el.outerWidth(), h = this.hover.$el.outerHeight(),
			maxw = this.$map.outerWidth(), maxh = this.$map.outerHeight();
		var pos = {
			x : mx + 15,
			y : my + 15,
		};
		if (pos.x + w >= maxw - PADDING)
			pos.x = mx - 2 - w;
		if (pos.y + h >= maxh - PADDING)
			pos.y = my - 3 - h;
		this.hover.$el.css({
			left : pos.x + "px",
			top : pos.y + "px",
		})
	}

	cleanLayers() {
		for(var l in this.layers) {
			this.layers[l].forEach((extraLayer) => {
				this.lmap.removeLayer(extraLayer.layer);
			});

			this.layers[l] = [];
		}
		this.allLayers = [];
	}

	getLayer(name) {
		return this.allLayers.find((layer) => layer.name == name);
	}

	addGeoJSON(geojson, name="", settings={}) {
		var layerObject = {
			name : name,
			data : [],
			settings : {
				color : COLOR_SCHEME.REGULAR,
				hideIfInvalid : false,
				hover : () => false,
			},
		};
		layerObject.settings = $.extend(layerObject.settings, settings);

		var geojsonLayer = L.geoJSON(geojson, {
			/*style : {
				"color": "#0000ff",
				"weight" : 3,
			},*/
			onEachFeature: (feature, layer) => this.onGeoJSONFeature(feature, layer, layerObject),
		});
		layerObject.layer = geojsonLayer;

		this.layers.geojsons.push(layerObject);
		this.allLayers.push(layerObject);

		geojsonLayer.addTo(this.lmap);

		return layerObject
	}
	setGeoJSONsettings(layerObject, settings={}) {
		layerObject.settings = $.extend(layerObject.settings, settings);
		this.updateRender(layerObject);
	}
	attachGeoJSONdata(layerObject, data, mapToData=null) {
		layerObject.data = data;
		layerObject.mapToData = mapToData;
		this.updateRender(layerObject);
	}
	updateRender(layerObject) {
		var COLOR = layerObject.settings.color;

		layerObject.layer.eachLayer((layer) => {
			var feature = layer.feature,
				props = layer.feature.properties;

			var mapFunc = (d, props) => d.geo_id == props.GEOID10;
			if (layerObject.mapToData)
				mapFunc = layerObject.mapToData;
			var data = layerObject.data.find((d) => mapFunc(d, props));

			/*
			var a = [0, 0, 200], b = [200, 0, 0], c = Math.random();
			var d = a.map((value, index) => a[index] + (b[index] - a[index]) * c).map(Math.round);
			console.log(d);
			layer.setStyle({
				color : buildColor(d),
				weight : 1
			})
			props.active = true;
			return;
			*/

			if (!data) {
				props.active = false;
				if (layerObject.settings.hideIfInvalid)
					this.lmap.removeLayer(layer);
				else
					layer.setStyle({
						color : buildColor(COLOR.DISABLED),
						weight : 1
					})
			} else {
				props.active = true;
				layer.setStyle({
					color : buildColor(COLOR.STANDARD),
					weight : 2
				})
			}
		});
	}
	onGeoJSONFeature(feature, layer, layerObject) {
		var props = feature.properties;
		var settings = layerObject.settings;

		layer.on({
	        click: (e) => this.onGeoJSONclick(e, layerObject),
	        mouseover : (e) => {
	        	var layer = e.target, feature = layer.feature, props = feature.properties;

	        	if (props.active) {
	        		props.mouseovered = true;
		        	layer.setStyle({
		        		color : buildColor(settings.color.HOVER)
		        	})
		        }

		        this.hover.$el.attr("style", "").attr("css", "popup hide").html("");
		        var shouldHover = settings.hover(this.hover.$el, layer, props);
		        if (shouldHover) {
		        	this.hover.active = layer;
		        	this.hover.$el.removeClass("hide");
		        } else {
		        	this.hover.active = null;
		        }
	        },
	        mouseout : (e) => {
	        	var layer = e.target, feature = layer.feature, props = feature.properties;

	        	if (props.mouseovered) {
	        		props.mouseovered = false;
		        	layer.setStyle({
		        		color : buildColor(props.active ? settings.color.STANDARD : settings.color.DISABLED)
		        	})
		        }

		        if (this.hover.active == layer) {
		        	this.hover.active = null;
		        	this.hover.$el.addClass("hide");
		        }
	        },
	    });
	}
	onGeoJSONclick(e, layerObject) {
		var feature = e.target.feature, props = feature.properties;
		if (props.active) {
			var mapFunc = (d, props) => d.geo_id == props.GEOID10;
			if (layerObject.mapToData)
				mapFunc = layerObject.mapToData;
			var data = layerObject.data.find((d) => mapFunc(d, props));

			if (data) {
				this.onClick(layerObject, data, props, e.target);
			}
		}
	}

	fitBounds2(layer) {
		var bounds = layer.getBounds();
		this.lmap.fitBounds(bounds);
	}
	fitBounds(layer=null) {
		if (!layer)
			if (this.allLayers.length)
				layer = this.allLayers[0];
		if (layer) {
			layer = layer.layer;
			var bounds = layer.getBounds();
			this.lmap.fitBounds(bounds);
		}
    	this.hover.active = null;
    	this.hover.$el.addClass("hide");
	}
	fitBoundsActive(layer=null, left=0, top=0) {
		if (!layer)
			if (this.allLayers.length)
				layer = this.allLayers[0];
		if (layer) {
			var layers = layer.layer;
			var arrLayers = [];
			layers.eachLayer((layer) => layer.feature.properties.active ? arrLayers.push(layer) : null);
			var bounds = (new L.featureGroup(arrLayers)).getBounds();
			this.lmap.fitBounds(bounds, {
				paddingTopLeft:L.point(left, top)
			});
		}
	}

}

var COLOR_SCHEME = {
	STATES : {
		STANDARD : [0, 0, 90],
		HOVER : [40, 40, 160],
		DISABLED : [150, 150, 150],
	},
	REGULAR : {
		STANDARD : [0, 0, 80],
		HOVER : [70, 70, 190],
		DISABLED : [150, 150, 150],
	},
	BACKGROUND : {
		STANDARD : [140, 190, 250],
		HOVER : [90, 140, 190],
		DISABLED : [210, 210, 210],
	},
}
function buildColor(arr, alpha=1) {
	return "rgba(" + arr.join(",") + ", " + alpha + ")";
}


