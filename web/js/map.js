
$(function() {

	function resize() {
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

		var md = e.pageX - omx;
		var neww = oresizex + md,
			perc = 100 * neww / $("#app").outerWidth();
		$("#mapbox").css("width", perc + "%");
		$("#sidebox").css("width", (100 - perc) + "%");
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







});


