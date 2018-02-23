
$(function() {

    function check() {
        if ($("#app").scrollTop() < 50)
            $("header").addClass("custom");
        else
            $("header").removeClass("custom");
    }

    $(window).resize(check);
    $("#app").on("scroll", check);

    check()    

});
