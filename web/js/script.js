
(function() {


    var rs = [],
        ready = false;
    window.whenReady = function(r) {
        if (ready) {
            var result = r();
            if (result === false) {
                $("#loading").removeClass("hide");
                extracount++;
            }
        } else
            rs.push(r);
    }

    var extracount = 0;
    window.crossOff = function() {
        extracount--;
        if (extracount <= 0)
            $("#loading").addClass("hide");
    }

    window.user = {
        loggedin : false
    }

    var count = 1;

    // user stuff
    whenReady(function() {
        window.user = jget("user", {});

        if (user.loggedin) {
            $("header .loggedin.no").addClass("hide");
            $("header .accountname").html(user.username);
        } else {
            $("header .loggedin.yes").addClass("hide");
        }

        $("header .logout").click(function() {
            APICall("logout").then((r) => {
                pdelete("user");
                window.location.href = "/login.html";
            })
        });

        if (user.loggedin) {
            APICall("getuser", {id : user.id})
                .then((r) => {
                    user.username = r.username;
                    user.type = r.user_type;
                    user.email = r.email;
                    crossOff();
                });

            return false;
        }
    });
    
    $(function() {
        
        var includes = $('[data-include]');
        count = includes.length;
        jQuery.each(includes, function() {
            $(this).load($(this).data('include') + '.html', function() {
                count--;
                if (count === 0) {

                    var fail = rs
                        .map((r) => r() === false)
                        .reduce((a, b) => a || b);

                    if (fail) {

                    } else {
                        $("#loading").addClass("hide");
                    }

                }
            });
        });
        
        
    });



})();


