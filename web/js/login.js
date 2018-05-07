
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
	$('[href="/login.html#signup"]').click(function(e) {
		if (!$(".carousel").hasClass("a"))
			$(".login .subtext").click();
	});
	$('[href="/login.html"]').click(function(e) {
		if ($(".carousel").hasClass("a"))
			$(".signup .subtext").click();
	});

	if (window.location.hash.indexOf("signup") > -1)
		$("#app .carousel").addClass("a");


	$(".screen .button").click(function() {
		if ($(this).closest(".screen").hasClass("login")) {

			var user = $(".login .username").val(),
				pass = $(".login .password").val();

			$(".login .error").removeClass("show");

			APICall("login",
				{
					username : user,
					password : pass,
				})
				.then((r) => {
					if (r.error) {
						$(".login .error").addClass("show");
					} else {

						APICall("getuser",
							{
								id : r.user_id,
							})
						.then((r) => {
							jset("user", {
								loggedin : true,
								username : user,
								email : r.email,
								password : pass,
								id : r.user_id,
								type : r.user_type,
							});

							window.location.href = "/map.html";
						});

					}
				});
			
		} else {

			var email = $(".signup .email").val(),
				user = $(".signup .username").val(),
				pass = $(".signup .password").val(),
				pass2 = $(".signup .password2").val();

			$(".signup .error").removeClass("show");

			if (pass !== pass2) {
				$(".signup .error").addClass("show").html("The passwords do not match");
				return;
			}

			APICall("signup",
				{
					username : user,
					password : pass,
					email : email,
					ignore_verify : false,
				})
				.then((r) => {
					if (r.error || r.user_id == -1) {
						$(".signup .error").addClass("show");
						if (r.error === 1 || r.user_id === -1)
							$(".signup .error").html("Email/Username already exists");
						else
							$(".signup .error").html("Unknown error");
					} else {
						/*
						jset("user", {
							loggedin : true,
							username : user,
							id : r.user_id,
							type : 1,
						});

						window.location.href = "/account.html";
						*/
						window.location.href = "/verify.html?signedup";
					}
				});

		}
	});
	$(".screen .last").on("keypress", function(e) {
		if (e.keyCode == 13 || e.key == "Enter")
			$(this).closest(".screen").find(".button").click();
	});



	

});


