class LeafletMap {

	constructor() {
		this.allLayers = [];

		this.layers = {
			geojsons : [],
		};
	}

	init(id) {
		this.lmap = L.map(id).setView([43.19, -71.572], 7);

		L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
		    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
		    maxZoom: 18,
		    id: 'mapbox.streets',
		    accessToken: 'pk.eyJ1Ijoicm9nZXItdGhhdCIsImEiOiJjamZmazFvdnM0dG1hMndxaGFqcmRiN2ViIn0.6CACaDuW3jp3eZwutrRrWQ'
		}).addTo(this.lmap);
	}

	cleanLayers() {
		for(var l in this.layers) {
			this.layers[l].forEach((extraLayer) => {
				this.lmap.removeLayer(extraLayer);
			});

			this.layers[l] = [];
		}
		this.allLayers = [];
	}

	addGeoJSON(geojson) {
		var geojsonLayer = L.geoJSON(geojson, {
			style : {
				"color": "#0000ff",
				"weight" : 3,
			},
			onEachFeature: (feature, layer) => this.onGeoJSONFeature(feature, layer),
		});

		this.layers.geojsons.push(geojsonLayer);
		this.allLayers.push(geojsonLayer);

		geojsonLayer.addTo(this.lmap);
	}
	onGeoJSONFeature(feature, layer) {
		layer.on({
	        click: (e) => this.onGeoJSONclick(e),
	        mouseover : function() {
	        	this.setStyle({
	        		color : "#008800"
	        	})
	        },
	        mouseout : function() {
	        	this.setStyle({
	        		color : "#0000ff"
	        	})
	        },
	    });
	}
	onGeoJSONclick(e) {
		var feature = e.target.feature;
		console.log(feature.properties);
	}

	fitBounds() {
		if (this.allLayers.length) {
			var bounds = this.allLayers[0].getBounds();
			this.lmap.fitBounds(bounds);
		}
	}

}

