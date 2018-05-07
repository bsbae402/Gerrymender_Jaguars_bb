
whenReady(function() {

	if (user.loggedin) {
		window.location.href = "/";
	}

	var u = window.location.href;

	if (u.indexOf("?") > -1) {
		u  = u.split("?")[1];
		if (u.indexOf("&") > -1) {
			
		}
	}

	



	
	//return false;
});


