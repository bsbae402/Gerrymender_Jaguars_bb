var Slider = function($el, defaultValue=0) {
    this.$el = $el;
    this.$circle = $("<div>").addClass("circle");
    this.$el.append(
        $("<div>").addClass("bar"),
        $("<div>").addClass("circleholder").append(
            this.$circle,
            ),
        );

    var value = 0;
    var handlers = [], ahandlers = [];
    this.getValue = () => value;
    this.onChange = function(h, immediateTrigger=false) {
        handlers.push(h);
        if (immediateTrigger)
            h(value);
    }
    this.onChangeAlways = function(h, immediateTrigger=false) {
        ahandlers.push(h);
        if (immediateTrigger)
            h(value);
    }
    this.change = function(v, trigger=true) {
        value = v;
        this.$circle.css("left", (value * 100) + "%");
        if (trigger)
            handlers.forEach((handler) => handler(value));
        ahandlers.forEach((handler) => handler(value));
    }
    this.change(defaultValue);

    var mdown = false, mx, sv;
    this.$circle.on("mousedown", function(e) {
        mdown = true;
        mx = e.pageX;
        sv = value;
    });
    $(window).on("mousemove", (e) => {
        if (!mdown) return;
        var outerwidth = this.$el.find(".circleholder").outerWidth();
        var change = (e.pageX - mx) / outerwidth;
        var nv = sv + change;
        nv = Math.max(Math.min(nv, 1), 0);
        this.change(nv);
    }).on("mouseup", (e) => {
        if (mdown)
            mdown = false;
    });
}

window.Slider = Slider;


