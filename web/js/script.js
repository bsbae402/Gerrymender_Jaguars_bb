
$(function() {
    
    var count = 0;

	var includes = $('[data-include]');
    count = includes.length;
    jQuery.each(includes, function() {
        $(this).load($(this).data('include') + '.html', function() {
            count--;
            if (count === 0)
                onLoad();
        });
    });
    
});

// user stuff
var onLoad = (function() {
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
