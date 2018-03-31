var ENDPOINT = "http://localhost:8080/";

var API_CALLS = [
    
    // account
    {
        name : "login",
        method : "POST",
        url : "user/login",
        response : function(r={}, data={}) {
            return r;
        },
        _dummy : function(data={}) {
            return {
                user_id : 0,
                user_type : 2,
                error : 0
            }
        },
    },

    // states
    {
        name : "getstates",
        method : "GET",
        url : "states/get",
        response : function(r={}, data={}) {
            var states = (r && r.states);
            return states || [];
        },
        dummy : function(data={}) {
            return (["New Hampsphire", "Wisconsin", "Ohio"]).map((a) => {
                var startYear = Math.floor(Math.random() * 4) + 2004,
                    endYear = Math.floor(Math.random() * 6) + 5 + startYear;
                var years = [];
                for(var i=startYear;i<=endYear;i++) years.push(i);
                return {
                    state_name : a,
                    years : years,
                }
            });
        },
    },

    // state
    {
        name : "getstate",
        method : "GET",
        url : "state/get",
        response : function(r={}, data={}) {
            var states = (r && r.states);
            return states || [];
        },
        dummy : function(data={}) {
            if (data.state_name == "Ohio") {
                var districts = [];
                for(var i=0;i<8;i++) {
                    var precincts = [];
                    for(var j=0;j<8;j++) {
                        var r = Math.floor(Math.random() * 5000) + 10;
                        var d = Math.floor(Math.random() * 5000) + 10;
                        precincts.push({
                            name : "Random " + i + " " + j,
                            voteR : r,
                            voteD : d,
                            outline : [
                                i * 48, j * 48, i * 48 + 48, j * 48,
                                i * 48 + 48, j * 48 + 48, i * 48, j * 48 + 48
                            ]
                        })
                    }
                    districts.push({
                        precincts,
                    })
                }
                return {
                    districts,
                }
            }
            return {
                districts : [
                    {
                        precincts: [
                            {
                                name: "Whatever",
                                voteR : 500,
                                voteD : 100,
                                outline: [10, 30, 90, 30, 90, 110, 100, 130, 0, 130, 0, 50]
                            },
                            {
                                name: "Whatever",
                                voteR : 100,
                                voteD : 500,
                                outline: [110, 50, 190, 50, 190, 140, 200, 180, 100, 130, 140, 60]
                            },
                        ]
                    }
                ]
            };
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

            if (CALL.dummy) {

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

    }

    window.APICall = APICall;

}());



