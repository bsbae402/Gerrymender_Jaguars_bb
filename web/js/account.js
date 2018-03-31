
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

			var user = $(".login .username").val(),
				pass = $(".login .password").val();

			$(".login .error").removeClass("show");
			APICall({
				name : "login",
				data : {
					username : user,
					password : pass,
				},
				response : (r) => {
					if (r.error) {
						$(".login .error").addClass("show");
					} else {
						jset("user", {
							loggedin : true,
							username : user,
							id : r.user_id,
							type : r.user_type,
						});

						window.location.href = "/map.html";
					}
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


