
whenReady(function() {

	var openTab = -1,
		mapWPercent = 50,
		fullMap = true,
		map = null;

	function resize() {
		if (map)
			map.resize();
		return;
		var w = $("#mapbox").width(),
			h = $("#mapbox").height();

		var side = Math.max(w, h);
		$("#map").css({
			width : side + "px",
			height : side + "px",
		});

		$("#map").css({
			left : ((w - side) / 2) + "px",
			top : ((h - side) / 2) + "px",
		});
	}

	$(window).resize(resize);
	resize();


	function adjustOpenTabs() {
		$("#mapbox").css("width", mapWPercent + "%");
		map.resize();

		$(".sidebox").css("width", (100 - mapWPercent) + "%");
		if (openTab == -1)
			$(".sidebox.right").css("right", "-" + (100 - mapWPercent) + "%");
		if (openTab == 1)
			$(".sidebox.left").css("left", "-" + (100 - mapWPercent) + "%");
	}
	var noToggle = false;
	var baseProps = {
		duration : 600,
		step : function(a) {
			map.resize();
		}
	}
	function toggleTabs() {
		if (noToggle) return;

		if (openTab == -1 && (!user || !user.loggedin))
			return alert("Please log in or sign up to redistrict the map");

		noToggle = true;
		openTab = -openTab;
		if (openTab == 1) {
			openRedistrict();
		} else {
			closeRedistrict();
		}

		var props = $.extend({}, baseProps);
		props.complete = () => noToggle = false;

		var lr = (openTab == 1) ? "left" : "right",
			rl = (openTab == -1) ? "left" : "right";

		var adjustCSS = {}, animateCSS = {};
		adjustCSS[lr] = (100 - mapWPercent) + "%";
		adjustCSS[rl] = "";
		animateCSS[lr] = "0%";
		$("#mapbox").css(adjustCSS).animate(animateCSS, props);

		var a = {}, b = {};
		a[lr] = -(100 - mapWPercent) + "%";
		b[rl] = "0%";
		$(".sidebox."+lr).animate(a, props);
		$(".sidebox."+rl).animate(b, props);
	}
	$(".sidebox .titletop .link").click(function() {
		if (!$(this).hasClass("active"))
			toggleTabs();
	});
	function showSides() {
		if (!fullMap) return;
		fullMap = false;
		$("#mapbox").removeClass("fullstates");
		$("#mapbox").animate({
			width : mapWPercent + "%"
		}, baseProps);

		var lr = (openTab == 1) ? "left" : "right",
			rl = (openTab == -1) ? "left" : "right";

		var a = {}, b = {};
		a[lr] = -(100 - mapWPercent) + "%";
		b[rl] = "0%";
		$(".sidebox."+lr).animate(a, baseProps);
		$(".sidebox."+rl).animate(b, baseProps);
	}
	function hideSides() {
		if (fullMap) return;
		fullMap = true;
		$("#mapbox").addClass("fullstates");
		$("#mapbox").animate({
			width : "100%"
		}, baseProps);

		var lr = (openTab == 1) ? "left" : "right",
			rl = (openTab == -1) ? "left" : "right";

		var a = {}, b = {};
		a[lr] = -(100 - mapWPercent) + "%";
		b[rl] = -(100 - mapWPercent) + "%";
		$(".sidebox."+lr).animate(a, baseProps);
		$(".sidebox."+rl).animate(b, baseProps);
	}

	var mdown = false,
		omx = 0,
		oresizex = 0;
	$("#app > div > .resizemove").on("mousedown", function(e) {
		if (mdown)
			return;
		
		mdown = true;
		omx = e.pageX;
		oresizex = $("#mapbox").outerWidth();
	});
	$(window).on("mousemove", function(e) {
		if (!mdown)
			return;

		var neww = (openTab == 1) ?
			oresizex + (e.pageX - omx) :
			oresizex - (e.pageX - omx);

		var perc = 100 * neww / $("#app").outerWidth();
		perc = Math.max(Math.min(perc, 70), 30);
		mapWPercent = perc;
		
		adjustOpenTabs();

		resize();
	}).on("mouseup", function(e) {
		mdown = false;
	});


	$("#sidebox > .tabs .tab").click(function() {
		var n = $(this).attr("name");
		$("#sidebox > .tabs .tab").removeClass("active");
		$(this).addClass("active");

		$("#sidebox > .containers .container").removeClass("active");
		$("#sidebox > .containers .container[name=\""+n+"\"]").addClass("active");
	});
	$("#sidebox > .tabs .tab").first().click();


	function genericHover($el, data, opts={}) {
		var o = $.extend({
			votes : data.votes,
			includeGeneric : true,
			includeRecent : false,
			includeYearsOfData : false,
			includeVoting : true,
			sayAverage : false,
			district : null,
			newDistrict : null,
		}, opts);

		var stuff = [];

		stuff.push(
			$("<div>").addClass("title").html(data.name + " [" + data.code + "]"),
			);

		if (o.includeYearsOfData)
			stuff.push(
				$("<div>").addClass("info").append(
					$("<div>").html("Years of Data"),
					$("<div>").html(data.numOfYears + " year(s)"),
					),
				);

		if (o.includeGeneric)
			stuff.push(
				$("<div>").addClass("info").append(
					$("<div>").html((o.sayAverage ? "Average " : "") + "Population"),
					$("<div>").html(commaNumbers(data.population)),
					),
				$("<div>").addClass("info").append(
					$("<div>").html((o.sayAverage ? "Average " : "") + "Voters"),
					$("<div>").html(commaNumbers(data.total_votes) + " (" + getPerc(data.total_votes, data.population).toFixed(2) + "%)"),
					),
				);

		if (o.includeRecent)
			stuff.push(
				$("<div>").addClass("info").append(
					$("<div>").html(data.recent.election_year + " Population"),
					$("<div>").html(commaNumbers(data.recent.population)),
					),
				$("<div>").addClass("info").append(
					$("<div>").html(data.recent.election_year + " Voters"),
					$("<div>").html(commaNumbers(data.recent.total_votes) + " (" + getPerc(data.recent.total_votes, data.recent.population).toFixed(2) + "%)"),
					),
				);

		if (o.district)
			stuff.push(
				$("<div>").addClass("info").append(
					$("<div>").html("District"),
					$("<div>").html(o.district.name),
					),
				);
		if (o.newDistrict)
			stuff.push(
				$("<div>").addClass("info").append(
					$("<div>").html("New District"),
					$("<div>").html(o.newDistrict.name),
					),
				);

		if (o.includeVoting) {
			var vtotal = o.votes.DEM + o.votes.REP,
				vratio = o.votes.DEM / vtotal;
			if (vtotal > 0) {
				var absratio = 0.5 - Math.abs(vratio - 0.5),
					amount = absratio < 0.1 ? "Extremely" : (absratio < 0.2 ? "Very" : (absratio < 0.35 ? "Moderately" : "Slightly")),
					party = vratio > 0.5 ? "Democratic" : "Republican",
					text = amount + " " + party;
			} else {
				var absratio = 0,
					text = "No Voting Data Available";
			}
			stuff.push(
				$("<div>").addClass("votepadding"),
				$("<div>").addClass("spectrum").append(
					!isNaN(vratio) && $("<div>").addClass("tick").css("left", (vratio * 100) + "%"),
					$("<div>").addClass("text").addClass(isNaN(vratio) ? "nodata" : "").html(text)
						.css(vratio <= 0.5 ? "left" : "right", "calc(6px + " + (absratio * 100) + "%)"),
					),
				);
		}

		$el.append(stuff);
	}


	var states;

	var active = {
		states : null,
		state : null,
		sy : null,
		districtsLayer : null,
		districtLayer : null,
		districts : null,
		district : null,
		precinctsLayer : null,
		precinctsLayer2 : null,
		precinctLayer : null,
		precincts : null,
		selectedPrecincts : null,
		allPrecincts : false,
		precinct : null,
	};
	window.active = active;
	function addPrecincts(p) {
		if (!active.precincts)
			return active.precincts = p;
		p.forEach((precinct) => {
			if (active.precincts.find((precinct2) => precinct.id == precinct2.d)) return;
			active.precincts.push(precinct);
		});
	}

	var statesGeoJSON, statesLayer;
	function loadStatesLoaded() {
		function hoverState($el, layer, props) {
			if (!props.active) return false;
			var state = states.find((state) => state.name == props.NAME);
			if (!state) return false;

			genericHover($el, state, {
				votes: state.recent.votes,
				includeRecent : true,
				includeYearsOfData : true,
				sayAverage : true,
			});

			return true;
		}

		hideSides();
		Object.keys(active).forEach((key) => active[key] = null);
		if (states && statesGeoJSON) {
			map.cleanLayers();
			statesLayer = map.addGeoJSON(statesGeoJSON, "states", {
				color: COLOR_SCHEME.STATES,
				hover : hoverState,
			});

			map.attachGeoJSONdata(statesLayer, states,
				(state, props) => state.name == props.NAME);
			map.fitBoundsActive(statesLayer, 0, 80);

			$(".mapapp").removeClass("loading");
		}
		clearPrecincts();
		clearAlgorithm();
	}
	function loadStates() {
		$.get("/data/states.json", function(r) {
			statesGeoJSON = r;
			loadStatesLoaded();
		});

		APICall("getstates").then((r) => {
			states = r;
			loadStatesLoaded();
		});

		$("#cview .infoselects .infoselect").removeClass("ok active");
		$("#cview .infos .info").removeClass("active");
	}
	$("#mapbox .switchregion").click(() => {
		loadStatesLoaded();
	});

	$("#mapbox .resetview").click(() => {
		if (active.districtsLayer)
			map.fitBounds(active.districtsLayer);
	});

	function selectState(state) {
		showSides();

		active.state = state;
		$(".mapapp").addClass("loading");

		$("#cview .yearbox").empty();
		state.years.forEach((year) => {
			var $year = $("<div>").addClass("yearselect").attr("year", year);
			var yearData = state.yearMap[year];

			$year.append(
				$("<div>").addClass("year").html(year),
				$("<div>").addClass("votes").html(commaNumbers(yearData.total_votes)),
				$("<div>").addClass("population").html(commaNumbers(yearData.population)),
				);
			$year.click(function() {
				var year = $(this).attr("year"),	
					sy = active.state.yearMap[year];

				selectSY(sy);
			});

			$("#cview .yearbox").append($year);
		});

		selectSY(state.yearMap[state.years[0]], true);
	}

	$("#cview .infoselects .infoselect").click(function() {
		if (!$(this).hasClass("ok") || $(this).hasClass("active"))
			return;

		var show = $(this).attr("info");
		$("#cview .infos .info.active").removeClass("active");
		$("#cview .infos .info."+show).addClass("active");

		$("#cview .infoselects .infoselect.active").removeClass("active");
		$(this).addClass("active");

		if (show == "syinfo") {
			if (active.districtsLayer) {
				map.fitBounds(active.districtsLayer);
				$(".analytics .fields").empty();
			}
		} else if (show == "dinfo") {
			if (active.districtLayer) {
				map.fitBounds2(active.districtLayer);
				$(".analytics .fields").empty();
			}
		} else if (show == "pinfo") {
			if (active.precinctLayer) {
				map.fitBounds2(active.precinctLayer);
				$(".analytics .fields").empty();
			}
		}
	});
	function activateTabAndSelect(limit="", disableafters=false) {
		var last = $("#cview .infoselect.ok").last();
		if (last.attr("info") != "pinfo") {
			last = last.next();
			if (limit && limit != last.attr("info")) return;

			last.addClass("ok");
			last.click();
		}
		$("#cview .infoselect[info='" + limit + "']").click();
		if (disableafters) {
			var stuff = $("#cview .infoselect[info='" + limit + "']").next();
			while (stuff.length && stuff.hasClass("infoselect")) {
				stuff.removeClass("ok active");
				stuff = stuff.next();
			}
		}
	}
	function unactivateTabs() {
		$("#cview .infos .info").removeClass("ok active");
		$("#cview .infoselects .infoselect").removeClass("ok active");
	}
	function clickTab(info) {
		$("#cview .infoselects .infoselect[info="+info+"]").click()
	}

	function setupVoteBar(votes, $el) {
		var parties = ["Democrat", "Republican", "Other"];
		(["DEM", "REP", "OTHER"]).forEach((party, index) => {
			var perc = (votes.total > 0) ? (votes[party] / votes.total * 100) : 0;
			$el.find("[" + party + "]").css("width", perc + "%");
			$el.find("[" + party + "] .hover div").html(
				parties[index]+
				"<br>"+
				votes[party] + "/" + votes.total+  " (" + perc.toFixed(1) + "%)");
		});
	}

	function hoverDistrict($el, layer, props) {
		if (!props.active) return false;
		if (!active.districts) return false;
		var district = active.districts.find((district) => district.geo_id == props.GEOID10);
		if (!district) return false;

		genericHover($el, district);
		return true;
	}
	function selectSY(sy, initial=false) {
		if (active.sy == sy)
			return;

		$(".mapapp").addClass("loading");

		active.sy = sy;

		unactivateTabs();
		$("#cview .infoselects [info=syinfo]").addClass("ok");
		clickTab("syinfo");

		$("#cview .syinfo .fields").empty();
		$("#cview .syinfo .right2").html("");
		$("#cview .syinfo .statename .right").html(sy.name);
		$("#cview .syinfo .year .right").html(sy.election_year);
		$("#cview .syinfo .population .right").html(commaNumbers(sy.population));
		$("#cview .syinfo .area .right").html(commaNumbers((sy.area / 1000000).toFixed(1)) + " sq km");
		$("#cview .syinfo .perimeter .right").html(commaNumbers((sy.perimeter / 1000).toFixed(1)) + " km");
		setupVoteBar(sy.votes, $("#cview .syinfo .votes"));

		$("#cview .yearbox .yearselect.active").removeClass("active");
		$("#cview .yearbox .yearselect[year="+sy.election_year+"]").addClass("active");

		$("#cview .syinfo .compareto select option").not(".empty").remove();
		$("#cview .syinfo .compareto select").val(-1);
		states.forEach((state) => {
			state.years.forEach((year) => {
				var s = state.yearMap[year];
				if (s == sy) return;
				$("#cview .syinfo .compareto select").append(
					$("<option>").html(s.name + " " + s.election_year).val(s.name+","+s.election_year)
					);
			});
		});

		APICall("getdistrictsgeojson",
			{
				state_id : sy.id,
			})
			.then((r) => {
				map.cleanLayers();

				var districtsLayer = map.addGeoJSON(r, "districts", {
					hover : hoverDistrict,
				});
				active.districtsLayer = districtsLayer;

				map.fitBounds();

				APICall("getdistricts",
					{
						state_id : sy.id,
					})
					.then((r) => {
						active.districts = r;

						map.attachGeoJSONdata(districtsLayer, r);

						$(".mapapp").removeClass("loading");

						if (initial)
							if (openTab == 1)
								openRedistrict();
					});

			});
	}
	$("#cview .syinfo .compareto select").on("change", function() {
		var val = $(this).val();
		if (val.indexOf(",") == -1) $("#cview .syinfo .right2").html("");
		val = val.split(",");
		val[1] = parseInt(val[1]);
		var sy = null;
		states.forEach((state) => {
			state.years.forEach((year) => {
				var s = state.yearMap[year];
				if (s.name == val[0] && s.election_year == val[1]) sy = s;
			});
		});
		if (sy) {
			$("#cview .syinfo .statename .right2").html(sy.name);
			$("#cview .syinfo .year .right2").html(sy.election_year);
			$("#cview .syinfo .population .right2").html(commaNumbers(sy.population));
			$("#cview .syinfo .area .right2").html(commaNumbers((sy.area / 1000000).toFixed(1)) + " sq km");
			$("#cview .syinfo .perimeter .right2").html(commaNumbers((sy.perimeter / 1000).toFixed(1)) + " km");
		}
		compareAnalytics();
	});


	function hoverPrecinct($el, layer, props) {
		if (!props.active) return false;
		if (!active.precincts) return false;
		var precinct = active.precincts.find((precinct) => precinct.geo_id == props.GEOID10);
		if (!precinct) return false;

		genericHover($el, precinct);
		return true;
	}
	function loadAllPrecincts(done=()=>{}) {
		if (active.allPrecincts) return done();
		if (!active.districts) return;

		$(".mapapp").addClass("loading");

		var ds = active.districts.map((d) => d);
		function loadNextDistrict() {
			if (!ds.length) {
				$(".mapapp").removeClass("loading");
				active.allPrecincts = true;
				if (!active.precincts) active.precincts = [];
				done();
				return;
			}
			var d = ds.shift();
			APICall("getprecincts",
				{
					district_id : d.id,
				})
				.then((r) => {
					addPrecincts(r);
					loadNextDistrict();
				});
		}
		loadNextDistrict();
	}
	function selectDistrict(id) {
		var district = active.districts.find((d) => d.id == id),
			sy = active.sy;
		if (!district) return;
		active.district = district;

		var districtLayer = null;
		active.districtsLayer.layer.eachLayer((layer) => {
			if (layer.feature.properties.GEOID10 == district.geo_id)
				districtLayer = layer;
		});
		if (!districtLayer) return;
		active.districtLayer = districtLayer;

		activateTabAndSelect("dinfo", true);
		
		$("#cview .dinfo .fields").empty();
		$("#cview .dinfo .statename .right").html(sy.name);
		$("#cview .dinfo .year .right").html(sy.election_year);
		$("#cview .dinfo .code .right").html(district.code);
		$("#cview .dinfo .population .right").html(commaNumbers(district.population));
		$("#cview .dinfo .area .right").html(commaNumbers((district.area / 1000000).toFixed(1)) + " sq km");
		$("#cview .dinfo .perimeter .right").html(commaNumbers((district.perimeter / 1000).toFixed(1)) + " km");
		$("#cview .dinfo .medianincome .right").html("$" + commaNumbers(district.median_income.toFixed(2)));
		$("#cview .dinfo .incumbent .right").html(district.incumbent);
		setupVoteBar(district.votes, $("#cview .dinfo .votes"));

		active.districtsLayer.applySettings({
			color : COLOR_SCHEME.BACKGROUND
		});

		$(".mapapp").addClass("loading");

		function getPrecincts() {
			var ps = null;
			if (active.precincts) {
				ps = active.precincts.filter((p) => p.district_id == id);
			}

			function attachData(data) {
				map.attachGeoJSONdata(active.precinctsLayer, data);

				map.fitBoundsActive(active.precinctsLayer);

				$(".mapapp").removeClass("loading");
			}

			if (!ps || !ps.length) {
				APICall("getprecincts",
					{
						district_id : id,
					})
					.then((r) => {
						addPrecincts(r);
						//active.selectedPrecincts = r;
						attachData(r.map((p) => p));
					});
			} else {
				attachData(ps);
			}
		}

		if (active.precinctsLayer) {
			getPrecincts();
		} else {
			APICall("getprecinctsgeojson",
				{
					state_id : active.sy.id,
				})
				.then((r) => {
					var precinctsLayer = map.addGeoJSON(r, "precincts", {
						hideIfInvalid : true,
						hover : hoverPrecinct,
					});
					active.precinctsLayer = precinctsLayer;

					map.fitBounds2(districtLayer);

					getPrecincts();
				});
		}
	}

	function clearPrecincts() {
		if (active.precincts) {
			if (active.precinctsLayer)
				map.removeLayer(active.precinctsLayer);
			active.precincts = null;
			active.selectedPrecincts = null;
			active.allPrecincts = false;
			active.precinct = null;
			active.precinctsLayer = null;
			active.precinctsLayer2 = null;
		}
	}
	function selectPrecinct(id) {
		var precinct = active.precincts.find((d) => d.id == id),
			sy = active.sy;
		if (!precinct) return;
		active.precinct = precinct;

		var precinctLayer = null;
		active.precinctsLayer.layer.eachLayer((layer) => {
			if (layer.feature.properties.GEOID10 == precinct.geo_id)
				precinctLayer = layer;
		});
		if (!precinctLayer) return;
		active.precinctLayer = precinctLayer;

		activateTabAndSelect("pinfo");

		$("#cview .pinfo .name .right").html(precinct.name);
		$("#cview .pinfo .statename .right").html(sy.name);
		$("#cview .pinfo .year .right").html(sy.election_year);
		$("#cview .pinfo .code .right").html(precinct.code);
		$("#cview .pinfo .population .right").html(commaNumbers(precinct.population));
		$("#cview .pinfo .area .right").html(commaNumbers((precinct.area / 1000000).toFixed(1)) + " sq km");
		$("#cview .pinfo .perimeter .right").html(commaNumbers((precinct.perimeter / 1000).toFixed(1)) + " km");
		setupVoteBar(precinct.votes, $("#cview .pinfo .votes"));

		map.fitBounds2(precinctLayer);
	}


	// initiate leaflet map
	map = new LeafletMap();
	window.map = map;

	map.init("map");

	map.onClick = function(layerObject, data, props, layer) {
		if (layerObject.name == "districts") {
			selectDistrict(data.id);
		}
		if (layerObject.name == "precincts") {
			selectPrecinct(data.id);
		}
		if (layerObject.name == "states") {
			states.forEach((state) => {
				if (state.name == props.NAME)
					selectState(state);
			})
		}
		if (layerObject.name == "precincts2") {
			redistrictPrecinctClick(data);
		}
	};



	// Search bar
	(function() {
		var blurTimeout = null,
			possibles = [];

		function clickState(obj) {
			var state = states.find((state) => state.name == obj.name_);
			if (!state) return;
			selectState(state);
		}

		function getPossibles() {
			var arr = [];
			if (states) {
				states.forEach((state) => {
					arr.push({
						obj : {
							name : state.name,
							type : "State",
						},
						click : clickState,
					})
				});
			}
			if (active.districts) {
				active.districts.forEach((district) => {
					arr.push({
						obj : {
							name : district.name,
							type : "District",
						},
						click : () => selectDistrict(district.id),
					})
				});
			}
			if (active.precincts) {
				active.precincts.forEach((precinct) => {
					if (precinct.district_id != active.district.id) return;
					arr.push({
						obj : {
							name : precinct.name,
							type : "Precinct",
						},
						click : () => selectPrecinct(precinct.id),
					})
				});
			}
			return arr;
		}

		$("#mapbox .search input").on("focus", function() {
			$("#mapbox .search").addClass("focused");
			possibles = getPossibles();
			getResults();
		}).on("blur", function() {
			if (blurTimeout)
				blurTimeout = clearTimeout(blurTimeout);
			blurTimeout = setTimeout(function() {
				$("#mapbox .search").removeClass("focused");
			}, 100);
		}).on("change keyup", function(e) {
			getResults();
		});

		var SEARCHES = ["name"];
		function getResults() {
			$("#mapbox .search .results").empty().removeClass("empty blank");
			var s = $("#mapbox .search input").val().trim(),
				re = new RegExp(s, 'ig');
			if (s == "") {
				$("#mapbox .search .results").addClass("blank");
				return;
			}

			var results = possibles.map((p) => {
				var found = false, p2 = $.extend(true, {}, p);

				SEARCHES.forEach((SEARCH) => {
					if (!p2.obj.hasOwnProperty(SEARCH)) return;
					p2.obj[SEARCH + "_"] = p2.obj[SEARCH];
					if (p2.obj[SEARCH].match(re)) {
						found = true;
						p2.obj[SEARCH] = p2.obj[SEARCH].replace(re, (match) => "<match>" + match + "</match>");
					}
				});

				if (!found)
					return false;
				return p2;
			});
			results = results.filter((r) => r !== false);

			if (results.length == 0) {
				$("#mapbox .search .results").addClass("empty");
				return;
			}
			results.forEach((result) => {
				var $result = $("<div>").addClass("result");
				$result.append(
					$("<div>").addClass("name").html(result.obj.name),
					$("<div>").addClass("type").html(result.obj.type),
					);
				$result.on("mousedown", (e) => result.click(result.obj));
				$("#mapbox .search .results").append($result);
			});
		}
	})();



	// Redistrict tabs
	$("#credistrict > .content > .tabs > .tab").click(function() {
		if ($(this).hasClass("active")) return;
		$("#credistrict > .content > .tabs > .tab.active").removeClass("active");
		$(this).addClass("active");
		var tab = $(this).attr("tab");
		$("#credistrict > .content > .rcontainers > .rcontainer.show").removeClass("show");
		$("#credistrict > .content > .rcontainers > .rcontainer."+tab).addClass("show");
	});
	// Algorithm result tabs
	$("#credistrict .results > .tabs > .tab").click(function() {
		if ($(this).hasClass("active")) return;
		$("#credistrict .results > .tabs > .tab.active").removeClass("active");
		$(this).addClass("active");
		var tab = $(this).attr("tab");
		$("#credistrict .results > .rcontainers > .rcontainer.show").removeClass("show");
		$("#credistrict .results > .rcontainers > .rcontainer."+tab).addClass("show");
	});

	// sliders
	var sliders = {
        pcw : [0, 1, 0.3],
        scw : [0, 1, 0.3],
		ew : [0, 1, 0.4],
		pt : [0.001, 0.25, 0.1],
		lp : [0, 6, 1],
	}
	$("#credistrict .slider").each(function(index) {
		var $slider = $(this), $c = $slider.closest(".constraint");
		var s = $slider.attr("slider");
		var slider = new Slider($slider, (sliders[s][2] - sliders[s][0]) / (sliders[s][1] - sliders[s][0]));
		sliders[s][3] = slider;
		slider.onChangeAlways((v) => {
			var n = sliders[s][0] + (sliders[s][1] - sliders[s][0]) * v,
				ns = n.toFixed(4);;
			if (s == "lp") {
				n = Math.round(Math.pow(10, n));
				ns = commaNumbers(n);
			}
			$c.find(".label span").html(ns);
			sliders[s][4] = n;
		}, true);
	});
	(function() {
		var pair = [sliders.pcw, sliders.scw, sliders.ew];
        pair.forEach((p, index) => {
            p[3].onChange((v) => {
                var remainingtotal = pair.filter((p2, index2) => index != index2).map((p) => p[4]).reduce((a, b) => a + b);
                var newremainingtotal = 1 - v;
                pair.forEach((p2, index2) => {
                    if (index == index2) return;
                    if (remainingtotal == 0)
                        p2[3].change(newremainingtotal / (pair.length - 1), false);
                    else
                        p2[3].change(p2[4] * newremainingtotal / remainingtotal, false);
                });
            });
        });
		$("#credistrict .constraint .reset").click(function() {
			var s = $(this).closest(".constraint").find(".slider").attr("slider");
			var slider = sliders[s][3];
			slider.change((sliders[s][2] - sliders[s][0]) / (sliders[s][1] - sliders[s][0]))
		});
		var cmap = [
			["pcw", "polsby_compactness_weight"],
            ["scw", "schwartzberg_compactness_weight"],
			["ew", "efficiency_weight"],
			["pt", "population_threshold"],
			["lp", "loops"],
		];
        APICall("getconstraints")
            .then((r) => {
                cmap.forEach((a) => {
                    if (r.hasOwnProperty(a[1])) {
                        var s = sliders[a[0]], v = parseFloat(r[a[1]]);
                        if (a[0] == "lp") v = Math.log10(v);
                        s[3].change((v - s[0]) / (s[1] - s[0]), false);
                    }
                });
            });
	})();

	// Redistricting
	function closeRedistrict() {
		active.districtsLayer.show();
		if (active.precinctsLayer) {
			active.precinctsLayer.show();
			map.fitBoundsActive(active.precinctsLayer);
		}
		if (active.precinctsLayer2)
			active.precinctsLayer2.hide();
		resetAlgorithm();
	}
	function openRedistrict() {
		active.districtsLayer.hide();

		clearAlgorithm();
		$("#credistrict .colors .color").not(".dummy").remove();

		loadAllPrecincts(() => {
			function whendone() {
				if (active.precinctsLayer)
					active.precinctsLayer.hide();
				if (active.precinctsLayer2) {
					active.precinctsLayer2.show();
					map.fitBoundsActive(active.precinctsLayer2);
				}

				if (active.districts && active.districts.length) {
					if (!active.districts[0].color) {
						// no colors yet, generate them
						var colors = generateColors(active.districts.length);
						colors.map((c, index) => {
							active.districts[index].color = c;
							active.districts[index].color_original = c.map((a) => a);
						});
					}

					active.districts.forEach((district) => {
						var $color = $("#credistrict .colors .color.dummy").clone(true, true).removeClass("dummy");
						$color.attr("district_id", district.id);
						$color.find(".name").html(district.name);
						$color.find(".c").css("background", buildColor(district.color));
						$("#credistrict .colors").append($color);

						var cp = new CP($color.find(".c")[0]);
						cp.set("rgb(" + district.color.join(",") + ")");
						cp.on("change", (color) => changeDColor($color, color));
					});
				}
				resetAlgorithm();
			}

			if (!active.precinctsLayer2) {
				$(".mapapp").addClass("loading");
				APICall("getprecinctsgeojson",
					{
						state_id : active.sy.id,
					})
					.then((r) => {
						var precinctsLayer2 = active.precinctsLayer2 = map.addGeoJSON(r, "precincts2", {
							color : COLOR_SCHEME.REGULARNOPOLITICAL,
						});

						map.attachGeoJSONdata(precinctsLayer2, active.precincts.map((p) => p));

						map.fitBounds(precinctsLayer2);

						$(".mapapp").removeClass("loading");
						whendone();
					});
			} else
				whendone();
		});
	}
	$("#credistrict .colors .color.dummy .enabled").click(function() {
		var id = parseInt($(this).closest(".color").attr("district_id"));
		var district = active.districts.find((d) => d.id == id);
		if (!district) return;

		var enabled = $(this).closest(".color").hasClass("disabled");
		if (!enabled) $(this).closest(".color").addClass("disabled");
		else $(this).closest(".color").removeClass("disabled");
		district.ignoreRedistrict = !enabled;

		active.precincts.forEach((p) => {
			if (p.district_id != id) return;
			p.ignoreRedistrictDistrict = district.ignoreRedistrict;

			var r = 0;
			if (p.ignoreRedistrict || p.ignoreRedistrictDistrict) r += 0.75;
			if (p.ignoreRedistrict && p.ignoreRedistrictDistrict) r += 0.15;
			p.color = mergeColors(district.color, [10, 10, 10], r);
		});
		active.precinctsLayer2.update();
	});
	$("#credistrict .colors .color.dummy .reset").click(function() {
		var id = parseInt($(this).closest(".color").attr("district_id"));
		var district = active.districts.find((d) => d.id == id);
		if (!district) return;

		$(this).closest(".color").find(".c").css("background", buildColor(district.color_original));
		district.color[0] = district.color_original[0];
		district.color[1] = district.color_original[1];
		district.color[2] = district.color_original[2];
		active.precinctsLayer2.update();
	});
	function changeDColor($el, color) {
		var rgb = CP.HEX2RGB(color);
		$el.find(".c").css("background", buildColor(rgb));
		var id = parseInt($el.attr("district_id"));
		var district = active.districts.find((d) => d.id == id);
		if (!district) return;

		district.color[0] = rgb[0];
		district.color[1] = rgb[1];
		district.color[2] = rgb[2];
		active.precinctsLayer2.update();
	}

	var redistrictPrecinctClick;
	var resetAlgorithm, clearAlgorithm;
	// algorithm
	(function() {
		var updates = [], UPDATE_TIME = 500,
			lastUpdate = 0,
			running = false, isQuerying = false,
			paused = false, renderTime = 0,
			TOTAL_LOOPS = 500, LOOP_ITERATE = 25,
			totalLoops = 0, loopCount = 0,
			aid = -1,
			markers = [];

		function hoverRedistrictedPrecinct($el, layer, props) {
			if (!props.active) return false;
			if (!active.precincts) return false;
			var precinct = active.precincts.find((precinct) => precinct.geo_id == props.GEOID10);
			if (!precinct) return false;

			var o = {};
			o.district = active.districts.find((d) => d.id == precinct.district_id);
			o.newDistrict = active.districts.find((d) => d.id == precinct.new_district_id);

			genericHover($el, precinct, o);
			if (precinct.ignoreRedistrict || precinct.ignoreRedistrictDistrict) {
				$("<div>").addClass("ignore")
					.insertAfter($el.find(".title"));
				$el.addClass("ignore");
			}
			return true;
		}

		clearAlgorithm = () => {
			reset();
		};

		resetAlgorithm = () => {
			if (!active.districts || !active.districts.length) return;

			if (!active.precincts) return;
			active.precincts.forEach((p) => {
				var d = active.districts.find((d) => d.id == p.district_id);
				if (!d || !d.color) return;
				p.color = d.color;
				p.colorChange = mergeColors(d.color, [200, 200, 200], 0.5);
				p.colorChange2 = mergeColors(d.color, [10, 10, 10], 0.65);
				if (p.new_district_id) delete p.new_district_id;

				if (p.ignoreRedistrict) {
					redistrictPrecinctClick(p, false, false);
				}
			});

			if (usechangemap)
				$(".usechange").click();

			map.removeMarkerChange();
			markers = [];
			if (active.precinctsLayer2)
				active.precinctsLayer2.applySettings({
					color : COLOR_SCHEME.CUSTOM,
					hover : hoverRedistrictedPrecinct,
				});

			isQuerying = false;
			running = false;
		};

		var usechangemap = false;
		$(".usechange").on("click", function() {
			var c = !$(this).hasClass("checked");
			$(this).toggleClass("checked");
			usechangemap = c;
			active.precincts.forEach((p) => {
				if (!c) {
					p.color = p.color_;
				} else {
					p.color_ = p.color;
					if (p.new_district_id)
						p.color = p.colorChange2;
					else
						p.color = p.colorChange;
				}
			});
			active.precinctsLayer2.update();
		});

		function reset() {
			if(aid != -1) {
				APICall("stopalgorithm", {algorithm_id : aid})
				.then((r) => {
					console.log("stop algorithm");
				});
			}
			aid = -1;
			$("#credistrict .algresults").removeClass("show");
			$("#credistrict .algorithm .pause").html("Pause algorithm");
			$("#credistrict .algorithm .run").removeClass("disabled");
			$("#credistrict .algresults .progressbar .scrolling").removeClass("stop");
			$("#credistrict .algorithm .pause, #credistrict .algorithm .stop, #credistrict .algorithm .reset").addClass("disabled");
			
			resetAlgorithm();

			$("#credistrict .changes .change").not(".dummy").remove();
		}

		$("#credistrict .changes .change").on("mouseover", function() {
			var id = parseInt($(this).attr("mid")); if (isNaN(id)) return;
			var marker = markers.find((m) => m.id == id);
			if (marker)
				marker.show();
		}).on("mouseout", function() {
			var id = parseInt($(this).attr("mid")); if (isNaN(id)) return;
			var marker = markers.find((m) => m.id == id);
			if (marker)
				marker.hide();
		}).on("click", function() {
			var id = parseInt($(this).attr("pid")); if (isNaN(id)) return;
			var precinct = active.precincts.find((p) => p.id == id);
			if (precinct) {
				var layer = active.precinctsLayer2.findGID(precinct.geo_id);
				if (layer)
					map.fitBounds2(layer);
			}
		});

		redistrictPrecinctClick = function(data, change=true, render=true) {
			if (running || aid != -1) return;
			var d = active.districts.find((d) => d.id == data.district_id);
			if (!d) return;

			if (change)
				data.ignoreRedistrict = !data.ignoreRedistrict;
			var r = 0;
			if (data.ignoreRedistrict || data.ignoreRedistrictDistrict) r += 0.75;
			if (data.ignoreRedistrict && data.ignoreRedistrictDistrict) r += 0.15;
			data.color = mergeColors(d.color, [10, 10, 10], r);
			if (render) {
				active.precinctsLayer2.update();
				map.updateHover();
			}
		}

		function editFieldSet($field, after, fix=8) {
			var val = parseFloat($field.find(".before").html());
			if (isNaN(after)) after = 0;
			$field.find(".after").html(after.toFixed(fix));
			var change = after - val;
			if (Math.abs(change) < 0.000001)
				$field.find(".change").html("--");
			else
				$field.find(".change").html((change>0 ? "+" : "") + change.toFixed(fix - 1));
		}
		function registerChange(change) {
			var d = active.districts.find((d) => d.id == change.new_district_id),
				p = active.precincts.find((p) => p.id == change.precinct_id);
			if (!d || !p) return;
			var doriginal = active.districts.find((d) => d.id == p.district_id);

			if (usechangemap) {
				p.color = p.colorChange2;
				p.color_ = d.color;
			} else {
				p.color = d.color;
			}
			p.new_district_id = d.id;
			active.precinctsLayer2.update();
			var marker = map.addMarkerChange(active.precinctsLayer2, p);

			if (marker) {
				markers.push(marker);
				marker.id = markers.length;
				var $change = $("#credistrict .changes .change.dummy").clone(true, true).removeClass("dummy");
				$change.attr("mid", marker.id);
				$change.attr("pid", p.id);
				$change.find(".precinct").html(p.name);
				$change.find(".district1").html(doriginal.name);
				$change.find(".district2").html(d.name);
				var a = change.old_district_compactness_pp + change.old_district_compactness_sch,
					b = change.new_district_compactness_pp + change.new_district_compactness_sch;
				$change.find(".compactness1").html(a.toFixed(4));
				$change.find(".compactness2").html(b.toFixed(4));
				$change.find(".objective1").html(change.district_old_objective_score.toFixed(4));
				$change.find(".objective2").html(change.district_new_objective_score.toFixed(4));
				$("#credistrict .changes").append($change);
			}

			editFieldSet($("#credistrict .updates .seg"), change.state_wide_efficiency_gap);
			editFieldSet($("#credistrict .updates .obj"), change.state_new_objective_score);
			editFieldSet($("#credistrict .updates .dc[did=" + d.id + "].p"), change.new_district_compactness_pp);
			editFieldSet($("#credistrict .updates .dc[did=" + d.id + "].s"), change.new_district_compactness_sch);
			editFieldSet($("#credistrict .updates .dc[did=" + d.id + "].o"), change.district_new_objective_score);
		}

		setInterval(() => {
			if (running) {
				if (new Date().getTime() > lastUpdate) {
					if (isQuerying) {
						
					} else {

						var iterate = LOOP_ITERATE;
						loopCount += iterate;

						APICall("getalgorithmupdate", { algorithm_id : aid, loop_count : iterate, })
							.then(function(r) {
								updates.push(r);
								r.loop = loopCount;
								lastUpdate = new Date().getTime() + UPDATE_TIME * 0.8;
								isQuerying = false;
								if (loopCount >= totalLoops) {
									lastUpdate = Infinity;
								}
							});
						isQuerying = true;
					}
				}

				if (!paused && updates.length) {
					if (new Date().getTime() > renderTime) {
						renderTime = new Date().getTime() + UPDATE_TIME * (Math.random() * 1 + 0.75);
						var update = updates.shift();

						var changes = update.all_changes;
						changes.forEach((change) => {
							registerChange(change);
						});

						var perc = update.loop / totalLoops * 100;
						$("#credistrict .algresults .progress").css("width", perc + "%");
						if (perc >= 100) {
							// finished
							$("#credistrict .algorithm .pause, #credistrict .algorithm .stop").addClass("disabled");
						}
					}
				}
			}
		}, 20);

		for(var i=0;i<60;i++) {
			$("#credistrict .algresults .progressbar .scrolling").append(
				$("<div>").addClass("ul"),
				$("<div>").addClass("br"),
				);
		}

		$("#credistrict .algorithm .reset").click(function() {
			running = false;
			reset();
		});
		$("#credistrict .algorithm .stop").click(function() {
			if ($(this).hasClass("disabled")) return;
			if (!running) return;
			running = false;
			$("#credistrict .algorithm .pause, #credistrict .algorithm .stop").addClass("disabled");
			$("#credistrict .algresults .progressbar .scrolling").addClass("stop");
		});
		$("#credistrict .algorithm .pause").click(function() {
			if ($(this).hasClass("disabled")) return;
			if (!running) return;
			if (!paused) {
				paused = true;
				$(this).html("Resume algorithm");
				$("#credistrict .algresults .progressbar .scrolling").addClass("stop");
			} else {
				renderTime = new Date().getTime() - UPDATE_TIME / 2;
				paused = false;
				$(this).html("Pause algorithm");
				$("#credistrict .algresults .progressbar .scrolling").removeClass("stop");
			}
		});
		$("#credistrict .algorithm .run").click(function() {
			if (!active.sy) return;
			if (running) return;
			$(this).addClass("disabled");

			var map = [
	            ["pcw", "polsby_compactness_weight"],
	            ["scw", "schwartzberg_compactness_weight"],
				["ew", "efficiency_weight"],
				["pt", "population_threshold"],
				["lp", "loops"],
				];
			var data = {
				state_id : active.sy.id,
				ignore_precinct_geo_ids : [-1],
				ignore_district_geo_ids : [-1],
				loops : TOTAL_LOOPS,
				heuristic : $("#credistrict .heuristic select").val(),
			};
			map.forEach((a) => {
				data[a[1]] = sliders[a[0]][4];
			});
			active.districts.forEach((d) => {
				if (d.ignoreRedistrict) data.ignore_district_geo_ids.push(d.geo_id);
			});
			active.precincts.forEach((p) => {
				if (p.ignoreRedistrict) data.ignore_precinct_geo_ids.push(p.geo_id);
			});

			TOTAL_LOOPS = data.loops;
			if (TOTAL_LOOPS < 100) LOOP_ITERATE = TOTAL_LOOPS / 10;
			else if (TOTAL_LOOPS < 1000) LOOP_ITERATE = TOTAL_LOOPS / 10;
			else if (TOTAL_LOOPS < 10000) LOOP_ITERATE = TOTAL_LOOPS / 50;
			else if (TOTAL_LOOPS < 100000) LOOP_ITERATE = TOTAL_LOOPS / 200;
			else if (TOTAL_LOOPS < 1000000) LOOP_ITERATE = TOTAL_LOOPS / 500;
			else if (TOTAL_LOOPS < 10000000) LOOP_ITERATE = TOTAL_LOOPS / 1000;
			else LOOP_ITERATE = TOTAL_LOOPS / 10000;
			LOOP_ITERATE = Math.round(LOOP_ITERATE);
			LOOP_ITERATE = Math.min(Math.max(LOOP_ITERATE, 10), 1000);


			APICall("startalgorithm", data)
				.then((r) => {
					updates = [];
					running = true;
					paused = false;
					aid = r.algorithm_id;

					totalLoops = TOTAL_LOOPS;

					loopCount = 0;
					lastUpdate = 0;
					renderTime = 0;

					$("#credistrict .updates .seg .before, #credistrict .updates .seg .after").html(r.init_state_efficiency_gap.toFixed(8));
					$("#credistrict .updates .seg .change").html("-");
					$("#credistrict .updates .obj .before, #credistrict .updates .obj .after").html(toFixed(r.init_state_objective_score, 8));
					$("#credistrict .updates .obj .change").html("-");
					$("#credistrict .updates .dc, #credistrict .updates .labelsremove").remove();
					r.init_district_compactness_list.forEach((dc, ind) => {
						var district = active.districts.find((d) => d.geo_id == dc.district_geoid);
						if (!district) return;
						$("#credistrict .updates .table").append(
							$("<div>").addClass("row labels labelsremove").append(
								$("<div>").addClass("field field1").html(district.name),
								),
							$("<div>").addClass("row dc p").attr("did", district.id).append(
								$("<div>").addClass("field field1").html("Polsby Compactness Score"),
								$("<div>").addClass("field before").html(dc.compactness_pp.toFixed(8)),
								$("<div>").addClass("field after").html(dc.compactness_pp.toFixed(8)),
								$("<div>").addClass("field change").html("-"),
								),
							$("<div>").addClass("row dc s").attr("did", district.id).append(
								$("<div>").addClass("field field1").html("Schwartzberg Compactness Score"),
								$("<div>").addClass("field before").html(dc.compactness_sch.toFixed(8)),
								$("<div>").addClass("field after").html(dc.compactness_sch.toFixed(8)),
								$("<div>").addClass("field change").html("-"),
								),
							$("<div>").addClass("row dc o").attr("did", district.id).append(
								$("<div>").addClass("field field1").html("Objective Score"),
								$("<div>").addClass("field before").html(toFixed(dc.objective_score, 8)),
								$("<div>").addClass("field after").html(toFixed(dc.objective_score, 8)),
								$("<div>").addClass("field change").html("-"),
								),
							);
					});

					$("#credistrict .algorithm .pause, #credistrict .algorithm .stop, #credistrict .algorithm .reset").removeClass("disabled");
					$("#credistrict .algresults .progress").css("width", 0);
					$("#credistrict .algresults").addClass("show");
				});
		});
	})();

	loadStates();

	var ANALYTIC_MAPPINGS = [
		["compactness_score", "Compactness Score", 3],
		["polsby_compactness_score", "Polsby Compactness Score", 3],
		["schwartzberg_compactness_score", "Schwartzberg Compactness Score", 3],
        ["efficiency_gap_score", "Efficiency Gap Score", 3],
        ["lowest_district_population", "Lowest District Population", 0],
        ["highest_district_population", "Highest District Population", 0],
        ["lowest_precinct_population", "Lowest Precinct Population", 0],
        ["highest_precinct_population", "Highest Precinct Population", 0],
        ["number_of_precincts", "Number of Precincts", 0],
        ["number_of_border_precincts", "Number of Border Precincts", 0],
        ["median_income", "Median Income", 2],
	];
	function compareAnalytics(sy) {
		var val = $("#cview .syinfo .compareto select").val(),
			sy = null;
		val = val.split(",");
		if (val.length > 1) {
			val[1] = parseInt(val[1]);
			states.forEach((state) => {
				state.years.forEach((year) => {
					var s = state.yearMap[year];
					if (s.name == val[0] && s.election_year == val[1]) sy = s;
				});
			});
		}
		$(".syinfo .analytics .fields .right2").html("");
		if (!sy) {
			return;
		}
		$(".syinfo .analytics .fields .field.l .right2").html(sy.name + " " + sy.election_year);
		APICall("analytics", {type : "state", state_id : sy.id})
			.then((r) => {
				Object.keys(r).forEach((k) => {
					var MAP = ANALYTIC_MAPPINGS.find((a) => a[0] == k);
					if (!MAP) return;

					var value = r[k], v2 = value;
					if (MAP[2] > -1) v2 = value.toFixed(MAP[2]);
					if (value > 1000) v2 = commaNumbers(v2);

					$(".syinfo .analytics .fields [key=" + k + "] .right2").html(v2);
				})
			});
	}
	$(".analytics .button").click(function() {
		var $an = $(this).closest(".analytics"),
			$info = $(this).closest(".info");

		var thing = null, data = {};
		if ($info.hasClass("syinfo")) {
			thing = active.sy; if (!thing) return;
			data.type = "state";
			data.state_id = thing.id;
		}
		if ($info.hasClass("dinfo")) {
			thing = active.district; if (!thing) return;
			data.type = "district";
			data.district_id = thing.id;
		}
		if ($info.hasClass("pinfo")) {
			thing = active.precinct; if (!thing) return;
			data.type = "precinct";
			data.precinct_id = thing.id;
		}
		if (!thing) return;

		$an.find(".fields").empty();

		APICall("analytics", $.extend({}, data))
			.then((r) => {
				var $fields = $an.find(".fields");
				$fields.append(
					$("<div>").addClass("field l").append(
						$("<div>").addClass("left"),
						$("<div>").addClass("right").html(thing.name + " " + thing.election_year),
						$("<div>").addClass("right2"),
						));

				Object.keys(r).forEach((k) => {
					var MAP = ANALYTIC_MAPPINGS.find((a) => a[0] == k);
					if (!MAP) return;
					var $field = $("<div>").addClass("field").attr("key", k);

					var value = r[k], v2 = value;
					if (MAP[2] > -1) v2 = value.toFixed(MAP[2]);
					if (value > 1000) v2 = commaNumbers(v2);

					$field.append(
						$("<div>").addClass("left").html(MAP[1]),
						$("<div>").addClass("right").html(v2),
						$("<div>").addClass("right2"),
						);
					$fields.append($field);
				});
				compareAnalytics();
			});
	});

});