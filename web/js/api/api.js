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
    {
        name : "signup",
        method : "POST",
        url : "user/signup",
        response : function(r={}, data={}) {
            return r;
        },
        dummy : function(data={}) {
            return {
                user_id : 0,
                error : 0,
            }
        },
    },
    {
        name : "getusers",
        method : "GET",
        url : "user/list",
        response : function(r={}, data={}) {
            return r;
        },
        dummy : function(data={}) {
            var a = [];
            for(var i=5;i<10;i++) {
                a.push({
                    user_id : i,
                    username : "User" + i,
                    email : "Randomemail"+i+"@gmail.com",
                })
            }
        },
    },

    // states
    {
        name : "getstates",
        method : "GET",
        url : "state/get/list",
        response : function(r={}, data={}) {
            var states = {};
            r.forEach((sy) => {
                if (!states.hasOwnProperty(sy.name))
                    states[sy.name] = {
                        name : sy.name,
                        years : [],
                        yearMap : {}
                    }
                var state = states[sy.name];

                state.years.push(sy.election_year);
                state.yearMap[sy.election_year] = sy;
            });
            var states2 = [];
            for (var i in states)
                if (states.hasOwnProperty(i))
                    states2.push(states[i]);
            return states2;
        },
        dummy : function(data={}) {
            var states = [];
            (["New Hampshire", "Wisconsin", "Ohio"]).forEach((a, index) => {
                var startYear = Math.floor(Math.random() * 4) + 2004,
                    endYear = Math.floor(Math.random() * 6) + 5 + startYear;
                var years = [];
                for(var i=startYear;i<=endYear;i++)
                    states.push({
                        id : index * 100 + (i - startYear),
                        name : a,
                        population : randomInt(100, 100000),
                        election_year : i,
                        area : randomFloat(1000, 50000),
                        perimeter : randomFloat(1000, 50000),
                        geoId : "dummy",
                        total_votes : randomInt(100, 10000),
                        code : "dummy",
                    });
            });
            return states;
        },
    },

    // state
    {
        name : "getstate",
        method : "GET",
        url : "state/get/{name}/{year}",
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

    var APICall = function(name="", data={}, opts={}) {
        if (typeof opts != "object") {
            defaultError("Invalid opts, non object, passed to APICall");
            return;
        }

        opts = $.extend({
            name : name,
            data : data,
            error : defaultError,
        }, opts);

        var CALL = API_CALLS.find((c) => c.name == opts.name);
        if (!CALL) {
            opts.error("Couldn't find API Call ["+opts.name+"]");
            return;
        }

        var useDummy = false;

        if (!ENDPOINT) {
            if (!CALL.dummy) {
                opts.error("No dummy endpoint for ["+opts.name+"]");
                return;
            }
            useDummy = true;
        } else if (CALL.dummy) {
            useDummy = true;
        }

        return new Promise(function (resolve=()=>{}, reject=()=>{}) {

            if (useDummy) {

                var data = CALL.dummy(opts.data);
                data = CALL.response(data, opts.data);
                resolve(data);

            } else {

                var URL = CALL.url;
                // add {} replacement swapping from data urls

                $.ajax({
                    url : ENDPOINT + "" + URL,
                    method : CALL.method,
                    data : opts.data,
                    success : function(r) {

                        try {

                            r = JSON.parse(r);
                            if (typeof CALL.response == "function")
                                r = CALL.response(r, opts.data);

                        } catch (e) {
                            reject("Couldn't parse response for API call ["+opts.name+"]");
                        }

                        resolve(r);

                    },
                    error : function(jqXHR, errorText) {
                        var status = error.status;

                        if (status == 401) {
                            console.log("Unauthorized");
                            return;
                        }

                        reject({
                            status : status,
                            text : error.responseText,
                            string : "Error for API Call ["+opts.name+"]: " + errorText,
                        });
                    },
                });

            }

        });


    }

    window.APICall = APICall;

}());



