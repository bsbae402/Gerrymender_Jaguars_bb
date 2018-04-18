
whenReady(function() {

	var openTab = -1,
		mapWPercent = 50;

	function resize() {return;
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

		$(".sidebox").css("width", (100 - mapWPercent) + "%");
		if (openTab == -1)
			$(".sidebox.right").css("right", "-" + (100 - mapWPercent) + "%");
		if (openTab == 1)
			$(".sidebox.left").css("left", "-" + (100 - mapWPercent) + "%");
	}
	var noToggle = false;
	function toggleTabs() {
		if (noToggle) return;
		noToggle = true;
		openTab = -openTab;

		var props = {
			duration : 600,
			complete : () => noToggle = false
		};

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

	$("#mapbox .switchregion, #mapbox .selectregion .x").click(() => {
		$("#mapbox .selectregion").toggleClass("show");
	});


	$("#sidebox > .tabs .tab").click(function() {
		var n = $(this).attr("name");
		$("#sidebox > .tabs .tab").removeClass("active");
		$(this).addClass("active");

		$("#sidebox > .containers .container").removeClass("active");
		$("#sidebox > .containers .container[name=\""+n+"\"]").addClass("active");
	});
	$("#sidebox > .tabs .tab").first().click();



	var states;

	var active = {
		state : null,
		sy : null,
		districtsLayer : null,
		districtLayer : null,
		districts : null,
		district : null,
		precinctsLayer : null,
		precinctLayer : null,
		precincts : null,
		precinct : null,
	};

	function loadStates() {
		APICall("getstates").then((r) => {
			states = r;
			states.forEach((region) => {
				$("#mapbox .selectregion .regions").append(
					$("<div>").addClass("region")
						.html(region.name)
						.click((e) => {
							$("#mapbox .selectregion").removeClass("show");
							selectState(region);
						})
					);
			});

			selectState(states[0]);
		});

		$("#cview .infoselects .infoselect").removeClass("ok active");
		$("#cview .infos .info").removeClass("active");
	}

	function selectState(state) {
		active.state = state;

		$("#cview .yearbox").empty();
		state.years.forEach((year) => {
			var $year = $("<div>").addClass("yearselect").attr("year", year);
			var yearData = state.yearMap[year];

			$year.append(
				$("<div>").addClass("year").html(year),
				$("<div>").addClass("votes").html(yearData.total_votes),
				$("<div>").addClass("population").html(yearData.population),
				);
			$year.click(function() {
				var year = $(this).attr("year"),	
					sy = active.state.yearMap[year];

				selectSY(sy);
			});

			$("#cview .yearbox").append($year);
		});

		selectSY(state.yearMap[state.years[0]]);
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
			if (active.districtsLayer)
				map.fitBounds(active.districtsLayer);
		} else if (show == "dinfo") {
			if (active.districtLayer)
				map.fitBounds2(active.districtLayer);
		} else if (show == "pinfo") {
			if (active.precinctLayer)
				map.fitBounds2(active.precinctLayer);
		}
	});
	function activateTabAndSelect(limit="") {
		var last = $("#cview .infoselect.ok").last();
		if (last.attr("info") == "pinfo") return;

		last = last.next();
		if (limit && limit != last.attr("info")) return;

		last.addClass("ok");
		last.click();
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

	function selectSY(sy) {
		if (active.sy == sy)
			return;

		active.sy = sy;

		unactivateTabs();
		$("#cview .infoselects [info=syinfo]").addClass("ok");
		clickTab("syinfo");

		$("#cview .syinfo .statename .right").html(sy.name);
		$("#cview .syinfo .year .right").html(sy.election_year);
		$("#cview .syinfo .population .right").html(sy.population);
		$("#cview .syinfo .area .right").html(sy.area + " sq mi");
		$("#cview .syinfo .perimeter .right").html(sy.perimeter + " mi");
		setupVoteBar(sy.votes, $("#cview .syinfo .votes"));

		$("#cview .yearbox .yearselect.active").removeClass("active");
		$("#cview .yearbox .yearselect[year="+sy.election_year+"]").addClass("active");

		APICall("getdistrictsgeojson",
			{
				state_id : sy.id,
			})
			.then((r) => {
				map.cleanLayers();

				var districtsLayer = map.addGeoJSON(r, "districts");
				active.districtsLayer = districtsLayer;

				map.fitBounds();

				APICall("getdistricts",
					{
						state_id : sy.id,
					})
					.then((r) => {
						active.districts = r;

						map.attachGeoJSONdata(districtsLayer, r);
					});

			});
	}

	function selectDistrict(id, districtLayer) {
		var district = active.districts.find((d) => d.id == id),
			sy = active.sy;
		if (!district) return;
		active.district = district;
		active.districtLayer = districtLayer;

		activateTabAndSelect("dinfo");
		
		$("#cview .dinfo .statename .right").html(sy.name);
		$("#cview .dinfo .year .right").html(sy.election_year);
		$("#cview .dinfo .code .right").html(district.code);
		$("#cview .dinfo .population .right").html(district.population);
		$("#cview .dinfo .area .right").html(district.area + " sq mi");
		$("#cview .dinfo .perimeter .right").html(district.perimeter + " mi");
		setupVoteBar(district.votes, $("#cview .dinfo .votes"));

		map.setGeoJSONsettings(active.districtsLayer, {
			color : COLOR_SCHEME.BACKGROUND
		});


		APICall("getprecincts",
			{
				district_id : id,
			})
			.then((r) => {
				active.precincts = r;

				APICall("getprecinctsgeojson",
					{
						state_id : active.sy.id,
					})
					.then((r) => {
						var precinctsLayer = map.addGeoJSON(r, "precincts", {
							hideIfInvalid : true,
						});
						active.precinctsLayer = precinctsLayer;

						map.attachGeoJSONdata(precinctsLayer, active.precincts);

						map.fitBounds2(districtLayer);
					});
			});
	}

	function selectPrecinct(id, precinctLayer) {
		var precinct = active.precincts.find((d) => d.id == id),
			sy = active.sy;
		if (!precinct) return;
		active.precinct = precinct;
		active.precinctLayer = precinctLayer;

		activateTabAndSelect("pinfo");

		$("#cview .pinfo .name .right").html(precinct.name);
		$("#cview .pinfo .statename .right").html(sy.name);
		$("#cview .pinfo .year .right").html(sy.election_year);
		$("#cview .pinfo .code .right").html(precinct.code);
		$("#cview .pinfo .population .right").html(precinct.population);
		$("#cview .pinfo .area .right").html(precinct.area + " sq mi");
		$("#cview .pinfo .perimeter .right").html(precinct.perimeter + " mi");
		setupVoteBar(precinct.votes, $("#cview .pinfo .votes"));

		map.fitBounds2(precinctLayer);
	}

	var map = new LeafletMap();

	map.init("map");

	map.onClick = function(layerObject, data, props, layer) {
		if (layerObject.name == "districts") {
			selectDistrict(data.id, layer);
		}
		if (layerObject.name == "precincts") {
			selectPrecinct(data.id, layer);
		}
	}

	loadStates();

});