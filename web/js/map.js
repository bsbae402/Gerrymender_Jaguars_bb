
$(function() {

	function resize() {
		var w = $("#mapbox").width(),
			h = $("#mapbox").height();



	}

	$(window).resize(resize);


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
	}).on("mouseup", function(e) {
		mdown = false;
	});


});


