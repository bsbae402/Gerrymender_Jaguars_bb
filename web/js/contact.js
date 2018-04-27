
whenReady(function() {

	$("#contact .button").click(function() {

		$(".error").removeClass("ok show")
		APICall("contact",
			{
				name : $(".name").val(),
				email : $(".email").val(),
				message : $(".message").val(),
			})
			.then((r) => {
				if (r.ok) {
					$(".error").addClass("ok show");
				} else {
					$(".error").addClass("show").removeClass("ok");
				}
			}, () => {
				$(".error").addClass("show").removeClass("ok");
			});

	});



	

});


