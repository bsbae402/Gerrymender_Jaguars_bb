var ENDPOINT = "";

var API_CALLS = [
    {
        name : "login",
        method : "POST",
        url : "login",
        response : function(r={}, data={}) {
            return r;
        },
        dummy : function(data={}) {
            return {
                user_id : 0,
                user_type : 1
            }
        },
    },
];



(function() {

    var defaultError = (str="") => {
        throw str;
    }

    var APICall = function(opts) {
        if (typeof opts != "object") {
            defaultError("Invalid opts, non object, passed to APICall");
            return;
        }

        opts = $.extend({
            name : "",
            data : {},
            response : () => {},
            error : defaultError,
        }, opts);

        var CALL = API_CALLS.find((c) => c.name == opts.name);
        if (!CALL) {
            opts.error("Couldn't find API Call ["+opts.name+"]");
            return;
        }

        if (!ENDPOINT) {
            if (!CALL.dummy) {
                opts.error("No dummy endpoint for ["+opts.name+"]");
                return;
            }

            var data = CALL.dummy(opts.data);
            opts.response(data);

        } else {

            $.ajax({
                url : ENDPOINT + "" + CALL.url,
                method : CALL.method,
                data : opts.data,
                success : function(r) {

                    try {

                        r = JSON.parse(r);
                        if (typeof CALL.response == "function")
                            r = CALL.response(r, opts.data);

                    } catch (e) {
                        opts.error("Couldn't parse response for API call ["+opts.name+"]");
                    }

                    opts.response(r);

                },
                error : function(jqXHR, errorText) {
                    opts.error("Error for API Call ["+opts.name+"]: " + errorText);
                },
            });

        }

    }

    window.APICall = APICall;

}());



