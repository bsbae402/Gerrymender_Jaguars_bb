
$(function() {

	$(".subtext").click(() => {
		var $this = $("#app .carousel").toggleClass("a");

		setTimeout(() => {
			if ($this.hasClass("a"))
				$(".signup .username").focus();
			else
				$(".login .username").focus();
		}, 300);
	});

	if (window.location.hash.indexOf("signup") > -1)
		$("#app .carousel").addClass("a");


	$(".screen .button").click(function() {
		if ($(this).closest(".screen").hasClass("login")) {

			APICall({
				name : "login",
				response : (r) => {
					console.log(r);
				}
			});
			
		} else {


		}
	});
	$(".screen .last").on("keypress", function(e) {
		if (e.keyCode == 13 || e.key == "Enter")
			$(this).closest(".screen").find(".button").click();
	});



	

});


