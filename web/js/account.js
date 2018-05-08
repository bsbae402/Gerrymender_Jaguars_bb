
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
                window.location.href = window.location.href;
            });
    }


    if (user.type === 2) {
        $(".admin").addClass("show");

        $(".users .user input").on("keypress", function(e) {
            var $user = $(this).closest(".user");
            if (e.keyCode == 13) {
                if ($(this).parent().hasClass("username")) {
                    $user.find(".email input").focus();
                } else {
                    $user.find(".save").click();
                }
            }
        });

        $(".users .user .delete").click(function() {
            var $user = $(this).closest(".user");
            var id = parseInt($user.attr("uid"));

            if (!window.confirm("Are you sure?")) return;

            APICall("deleteuser", {id : id})
                .then((r) => {
                    $user.remove();
                });
        });

        $(".users .user .add").click(function() {
            var d = {
                email : $(".users .user.add .email input").val(),
                username : $(".users .user.add .username input").val(),
                password : $(".users .user.add .password input").val(),
                ignore_verify : true,
            };

            $(".users .user.add input").val("");

            APICall("signup", d)
                .then((r) => {
                    var uid = r.user_id;
                    if (!uid) return;
                    d.role = "USER";
                    d.user_id = uid;
                    addUser(d);
                });
        });

        $(".users .user .edit").click(function() {
            var $user = $(this).closest(".user");
            if ($user.hasClass("header")) return;

            $user.addClass("edit");
            $user.find(".email input").val($user.find(".email .text").html());
            $user.find(".username input").val($user.find(".username .text").html()).focus();
        });

        $(".users .user .save").click(function() {
            var $user = $(this).closest(".user");
            var id = parseInt($user.attr("uid"));
            var d = {
                id : id,
                username : $user.find(".username input").val(),
                email : $user.find(".email input").val(),
                password : $user.attr("pw"),
            };

            APICall("edituser", d)
                .then((r) => {
                    $user.find(".email .text").html(d.email);
                    $user.find(".username .text").html(d.username);
                    $user.removeClass("edit");
                });
        });

        function addUser(user) {
            var $user = $(".users .user.header").clone(true, true).removeClass("header");
            $user.insertBefore($(".users .user.add"));
            $user.attr("uid", user.user_id);
            $user.attr("pw", user.password);
            $user.find(".id").html(user.user_id);
            $user.find(".verified").html(user.verified ? "Yes" : "No");
            $user.find(".type").html(user.role);
            $user.find(".email .text").html(user.email);
            $user.find(".username .text").html(user.username);
        }

        APICall("getusers")
            .then((r) => {
                r.forEach((u) => {
                    if (u.user_id == user.id) return;
                    addUser(u);
                });
            });


        var sliders = {
            pcw : [0, 1, 0.3],
            scw : [0, 1, 0.3],
            ew : [0, 1, 0.4],
            pt : [0.001, 0.25, 0.1],
            lp : [0, 6, 1],
        };
        window.sliders = sliders;
        $("#account .slider").each(function(index) {
            var $slider = $(this), $c = $slider.closest(".constraint");
            var s = $slider.attr("slider");
            var slider = new Slider($slider, (sliders[s][2] - sliders[s][0]) / (sliders[s][1] - sliders[s][0]));
            sliders[s][3] = slider;
            slider.onChangeAlways((v) => {
                var n = sliders[s][0] + (sliders[s][1] - sliders[s][0]) * v,
                    ns = n.toFixed(4);;
                if (s == "lp") {
                    n = Math.round(Math.pow(10, n));
                    ns = commaNumbers(n);
                }
                $c.find(".label span").html(ns);
                sliders[s][4] = n;
                if (csavet)
                    window.clearTimeout(csavet);
                csavet = window.setTimeout(csave, 100);
            }, true);
        });
        (function() {
            var pair = [sliders.pcw, sliders.scw, sliders.ew];
            pair.forEach((p, index) => {
                p[3].onChange((v) => {
                    var remainingtotal = pair.filter((p2, index2) => index != index2).map((p) => p[4]).reduce((a, b) => a + b);
                    var newremainingtotal = 1 - v;
                    pair.forEach((p2, index2) => {
                        if (index == index2) return;
                        if (remainingtotal == 0)
                            p2[3].change(newremainingtotal / (pair.length - 1), false);
                        else
                            p2[3].change(p2[4] * newremainingtotal / remainingtotal, false);
                    });
                });
            });
            $("#account .constraint .reset").click(function() {
                var s = $(this).closest(".constraint").find(".slider").attr("slider");
                var slider = sliders[s][3];
                slider.change((sliders[s][2] - sliders[s][0]) / (sliders[s][1] - sliders[s][0]))
            });
        })();
        var csavet, cansave = false;
        var cmap = [
            ["pcw", "polsby_compactness_weight"],
            ["scw", "schwartzberg_compactness_weight"],
            ["ew", "efficiency_weight"],
            ["pt", "population_threshold"],
            ["lp", "loops"],
            ];
        function csave() {
            var data = {};
            cmap.forEach((a) => {
                data[a[1]] = sliders[a[0]][4];
            });

            if (cansave)
                APICall("editconstraints", data)
                    .then((r) => {}, () => {});
        };
        APICall("getconstraints")
            .then((r) => {
                cmap.forEach((a) => {
                    if (r.hasOwnProperty(a[1])) {
                        var s = sliders[a[0]], v = parseFloat(r[a[1]]);
                        if (a[0] == "lp") v = Math.log10(v);
                        s[3].change((v - s[0]) / (s[1] - s[0]), false);
                    }
                });
                setTimeout(function() {
                    cansave = true;
                }, 110);
            });

    }



	

});


