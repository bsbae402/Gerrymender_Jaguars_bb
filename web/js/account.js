
whenReady(function() {

	var user = window.user;

	if (!user.loggedin) {
		setTimeout(() => {
			window.location.href = "/login.html";
		}, 100);
		return false;
	}

    var data = {
        username : user.username,
        email : user.email,
        password : user.password,
    }

    $(".account .username .value").html(user.username);
    $(".account .email .value").html(user.email);

    $(".account .field .edit").click(function() {
        var $field = $(this).closest(".field");
        if ($field.hasClass("password")) {
            $field.addClass("edit");

            $field.find("input").val("");
            $field.find("input").first().focus();
        } else {
            $field.addClass("edit");

            $field.find("input").val($field.find(".value").html()).focus();
        }
    });

    $(".account .field input").on("keypress", function(e) {
        var $field = $(this).closest(".field");
        if (e.keyCode == 13) {
            if ($(this).hasClass("old")) {
                $field.find(".new").focus();
            } else if ($(this).hasClass("new")) {
                var old = $field.find(".old").val();
                if (old != user.password) {
                    $field.find(".incorrect").css("opacity", 1);
                    setTimeout(function() {
                        $field.find(".incorrect").animate({ opacity: 0 }, 1000);
                    }, 1500);
                    return;
                }
                $field.removeClass("edit");
                data.password = $field.find(".new").val();
                submitUser();
            } else {
                var val = $(this).val();
                $field.find(".value").html(val);
                $field.removeClass("edit");
                if ($field.hasClass("username"))
                    data.username = val;
                if ($field.hasClass("email"))
                    data.email = val;
                submitUser();
            }
        }
    });

    function submitUser() {
        var d = $.extend({}, data);
        d.id = user.id;
        APICall("edituser", d)
            .then((r) => {
                user.username = data.username;
                user.email = data.email;
                user.password = data.password;
                jset("user", user);
                //window.location.href = window.location.href;
            });
    }


    if (user.type === 2) {
        $(".admin").addClass("show");

        function addUser(user) {
            var $user = $(".users .user.header").clone(true, true).removeClass("header");
            $(".users").append($user);
            $user.find(".id").html(user.user_id);
            $user.find(".email").html(user.email);
            $user.find(".username").html(user.username);
        }

        APICall("getusers")
            .then((r) => {
                r.forEach(addUser);
            });
    }



	

});


