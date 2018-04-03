
whenReady(function() {

	var user = window.user;
	console.log(window.user);

	if (!user.loggedin) {
		setTimeout(() => {
			window.location.href = "/login.html";
		}, 100);
		return false;
	}



	

});


