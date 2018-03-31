
(function() {


    var rs = [],
        ready = false;
    window.whenReady = function(r) {
        if (ready)
            r();
        else
            rs.push(r);
    }

    $(function() {
        
        var count = 0;

        var includes = $('[data-include]');
        count = includes.length;
        jQuery.each(includes, function() {
            $(this).load($(this).data('include') + '.html', function() {
                count--;
                if (count === 0) {
                    rs.forEach((r) => r());
                }
            });
        });



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
                pdelete("user");
                window.location.href = "/login.html";
            });

            $(".loading").addClass("hide");
        });

        
    });



})();


