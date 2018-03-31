
$(function() {

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
	APICall({
		name : "getstates",
		response : (r) => {
			states = r;
			states.forEach((region) => {
				$("#mapbox .selectregion .regions").append(
					$("<div>").addClass("region")
						.html(region.state_name)
						.click((e) => {
							$("#mapbox .selectregion").removeClass("show");
							selectState(region)
						})
					);
			});

			selectState(states[0]);
		}
	});


	var mapData;
	function selectState(state) {
		APICall({
			name : "getstate",
			data : {
				state_name : state.state_name,
				year : state.years[0],
			},
			response : (r) => {
				mapData = r;
				mapData.state_name = state.state_name;

				mapData.districts.forEach((district) => {
					district.precincts.forEach((precinct) => {
						precinct.outline = mapArrayToCoords(precinct.outline);
					})
				});

				renderMap();
			}
		});
	}

	// LEAFLET INITIALIZATION
	var lmap = L.map('map').setView([51.505, -0.09], 13);
	L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
	    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
	    maxZoom: 18,
	    id: 'mapbox.streets',
	    accessToken: 'pk.eyJ1Ijoicm9nZXItdGhhdCIsImEiOiJjamZmazFvdnM0dG1hMndxaGFqcmRiN2ViIn0.6CACaDuW3jp3eZwutrRrWQ'
	}).addTo(lmap);

	function renderMap() {
		$("#map").empty();

		console.log(mapData);

	}


















});


