
whenReady(function() {

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

			var user = $(".signup .username").val(),
				pass = $(".signup .password").val(),
				pass2 = $(".signup .password2").val();

			$(".signup .error").removeClass("show");

			if (pass !== pass2) {
				$(".signup .error").addClass("show").html("The passwords do not match");
				return;
			}

			APICall({
				name : "signup",
				data : {
					username : user,
					password : pass,
				},
				response : (r) => {
					if (r.error) {
						$(".signup .error").addClass("show");
						if (r.error === 1)
							$(".signup .error").html("Username already exists");
						else
							$(".signup .error").html("Unknown error");
					} else {
						jset("user", {
							loggedin : true,
							username : user,
							id : r.user_id,
							type : 1,
						});

						window.location.href = "/account.html";
					}
				}
			});

		}
	});
	$(".screen .last").on("keypress", function(e) {
		if (e.keyCode == 13 || e.key == "Enter")
			$(this).closest(".screen").find(".button").click();
	});



	

});


