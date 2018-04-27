var MAPBOX_API_URL = "https://api.mapbox.com/styles/v1/roger-that/cjg6nbnkha3712rmuzpsgv4lq/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1Ijoicm9nZXItdGhhdCIsImEiOiJjamZmazFvdnM0dG1hMndxaGFqcmRiN2ViIn0.6CACaDuW3jp3eZwutrRrWQ";

class LeafletMap {

	constructor() {
		this.allLayers = [];

		this.layers = {
			geojsons : [],
			changes : [],
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

		this.changeIcon = new L.Icon({
			iconUrl : "img/marker3.png",
			iconSize : [16, 50],
			iconAnchor : [8, 50],
			popupAnchor:  [0, 0],
			className : "changemarker",
		});
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
			pos.x = Math.max(15, mx - 2 - w);
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

	removeLayers(layers) {
		layers.forEach((layer) => this.removeLayer(layer));
	}
	removeLayer(layer) {
		this.lmap.removeLayer(layer.layer);
		for(var l in this.layers) {
			this.layers[l] = this.layers[l].filter((l) => l !== layer);
		}
		this.allLayers = this.allLayers.filter((l) => l !== layer);
	}

	getLayer(name) {
		return this.allLayers.find((layer) => layer.name == name);
	}

	addGeoJSON(geojson, name="", settings={}) {
		var map = this;

		var layerObject = {
			name : name,
			data : [],
			mapToData : null,
			settings : {
				hidden : false,
				color : COLOR_SCHEME.REGULAR,
				hideIfInvalid : false,
				hover : () => false,
			},
			setData : function(data) {
				map.attachGeoJSONdata(this, data);
			},
			applySettings : function(settings={}) {
				map.setGeoJSONsettings(this, settings);
			},
			update : function() {this.applySettings()},
			show : function() {this.applySettings({hidden : false})},
			hide : function() {this.applySettings({hidden : true})},
			find : function(func) {
				var l = null;
				this.layer.eachLayer((layer) => {
					if (func(layer, layer.feature.properties)) l = layer;
				})
				return l;
			},
			findGID : function(gid) {
				return this.find((l, p) => p.GEOID10 == gid);
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
		if (mapToData)
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

			layer.setStyle({
				fillOpacity : 0.3,
				opacity : 1,
			})

			if (layerObject.settings.hidden) {
				this.lmap.removeLayer(layer);
			} else if (!data) {
				props.active = false;
				if (layerObject.settings.hideIfInvalid)
					this.lmap.removeLayer(layer);
				else {
					this.lmap.addLayer(layer);
					layer.setStyle({
						color : buildColor(COLOR.DISABLED),
						weight : 1
					});
				}
			} else {
				props.active = true;
				this.lmap.addLayer(layer);

				if (COLOR.FILL == "custom") {
					layer.setStyle({
						color : buildColor(data.color),
						fillColor : buildColor(data.color),
						weight : 1
					});
				} else {
					layer.setStyle({
						color : buildColor(COLOR.STANDARD),
						weight : 2
					});
					if (COLOR.FILL === false)
						layer.setStyle({
							fillColor : buildColor([0, 0, 0], 0.3),
						});
					else if (COLOR.FILL === "political") {
						var tot = data.votes.DEM + data.votes.REP,
							rep = data.votes.REP > data.votes.DEM,
							r = (Math.max(data.votes.REP, data.votes.DEM) / tot - 0.5) * 2;
						var fill = rep ? [200, 0, 0] : [0, 0, 200];
						fill = mergeColors([150, 150, 150], fill, r * 0.8 + 0.3);
						layer.setStyle({
							fillColor : buildColor(fill, 1),
						});
					}
				}
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
		        		opacity : 0.6,
		        		fillOpacity : 0.55,
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
		        		opacity : 1,
		        		fillOpacity : 0.3,
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

	removeMarkerChange() {
		this.removeLayers(this.layers.changes);
	}
	addMarkerChange(layerObject, dataObject) {
		var mapFunc = (d, props) => d.geo_id == props.GEOID10;
		if (layerObject.mapToData)
			mapFunc = layerObject.mapToData;
		
		var marker;
		layerObject.layer.eachLayer((layer) => {
			if (!mapFunc(dataObject, layer.feature.properties)) return;
			var center = layer.getBounds().getCenter();

			var lmarker = L.marker(center, {icon : this.changeIcon});
			marker = {
				marker : lmarker,
				show : function() {
					var $marker = $(this.marker.getElement());
					$marker.addClass("show");
				},
				hide : function() {
					var $marker = $(this.marker.getElement());
					$marker.removeClass("show");
				},
			};
			marker.layer = lmarker;
			lmarker.addTo(this.lmap);
		});
		if (marker) {
			setTimeout(() => marker.show(), 10);
			setTimeout(() => marker.hide(), 1010);

			this.layers.changes.push(marker);
			this.allLayers.push(marker);
			return marker;
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
			if (arrLayers.length) {
				var bounds = (new L.featureGroup(arrLayers)).getBounds();
				this.lmap.fitBounds(bounds, {
					paddingTopLeft:L.point(left, top)
				});
			}
		}
	}

}

var COLOR_SCHEME = {
	STATES : {
		STANDARD : [0, 0, 90],
		DISABLED : [150, 150, 150],
	},
	REGULAR : {
		STANDARD : [0, 0, 80],
		DISABLED : [150, 150, 150],
		FILL : "political",
	},
	REGULARNOPOLITICAL : {
		STANDARD : [0, 0, 80],
		DISABLED : [150, 150, 150],
	},
	CUSTOM : {
		STANDARD : [0, 0, 80],
		DISABLED : [150, 150, 150],
		FILL : "custom",
	},
	BACKGROUND : {
		STANDARD : [140, 190, 250],
		DISABLED : [210, 210, 210],
		FILL : false,
	},
}
function buildColor(arr, alpha=1) {
	return "rgba(" + arr.map((a) => Math.round(parseInt(a)).toFixed(0)).join(",") + ", " + alpha + ")";
}
function mergeColors(a, b, r=0.5) {
	return a.map((aa, i) => a[i] + r * (b[i] - a[i]));
}


