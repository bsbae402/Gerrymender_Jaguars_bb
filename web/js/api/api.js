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
        name : "logout",
        method : "POST",
        url : "user/logout",
        response : function(r={}, data={}) {
            return r;
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

    // states/districts/precincts
    {
        name : "getstates",
        method : "GET",
        url : "state/get/list",
        response : function(r={}, data={}) {
            r = handleVotingData(r);
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
    },
    {
        name : "getdistricts",
        method : "POST",
        url : "district/get/bystateid",
        response : (r, data) => handleVotingData(r),
    },
    {
        name : "getprecincts",
        method : "POST",
        url : "precinct/get/bydistrictid",
        response : (r, data) => handleVotingData(r),
    },

    // geojson
    {
        name : "getdistrictsgeojson",
        method : "POST",
        url : "geojson/district/bystateid",
        response : (r, data) => r,
    },
    {
        name : "getprecinctsgeojson",
        method : "POST",
        url : "geojson/precinct/bystateid",
        response : (r, data) => r,
    },

];


function handleVotingData(r) {
    r.forEach((a) => {
        if (!a.voting_data) return;
        a.votes = {
            total : a.voting_data.map((v) => v.votes).reduce((a, b) => a + b, 0),
            DEM : 0,
            REP : 0,
            OTHER : 0,
        };
        a.voting_data.forEach((v) => {
            a.votes[v.political_party] = v.votes;
        });
    });
    return r;
}



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



