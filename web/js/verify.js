
whenReady(function() {

	if (user.loggedin) {
		window.location.href = "/";
	}

	var u = window.location.href;

	if (u.indexOf("?signedup") > -1) {
		$(".holder").css("color", "#009")
			.html("Thanks for signing up!<br><br>Please check your email for a verification link.");

		return;
	}

	function fail() {
		$("#loading").addClass("hide");
	}
	function succeed(u) {
		$(".holder").css("color", "#090")
			.html("Welcome, " + u + "<br><br>Email successfully verified.<br>You may now login.<br><br>Redirecting to login...");
		$("#loading").addClass("hide");
		setTimeout(function() {
			window.location.href = "/login.html"
		}, 4000);
	}

	if (u.indexOf("?") > -1) {
		u  = u.split("?")[1];
		if (u.indexOf("&") > -1) {
			var us = u.split("&");
			u = null;
			var v = null;
			us.forEach((p) => {
				if (p.indexOf("u=") === 0)
					u = p.substring(2);
				if (p.indexOf("v=") === 0)
					v = p.substring(2);
			});
			if (u && v) {
				APICall("verifyuser", {
					username : u,
					verify : v,
				})
					.then(function(r) {
						if (r.user_id == -1 || r.error !== 0)
							fail();
						else {
							succeed(u);
						}
					}, function() {
						fail();
					});
				return false;
			}
		}
	}

	



	
	//return false;
});


