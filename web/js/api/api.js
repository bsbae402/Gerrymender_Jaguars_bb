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
        adummy : function(data={}) { return { user_id : 0, error : 0, } },
    },
    {
        name : "verifyuser",
        method : "POST",
        url : "user/verify",
        response : function(r={}, data={}) {
            return r;
        },
    },

    // users
    {
        name : "getuser",
        method : "GET",
        url : "user/list/{id}",
        response : (r) => r,
    },
    {
        name : "getusers",
        method : "GET",
        url : "admin/user/list/",
        response : (r) => r,
        adummy : () => {
            var id = 0;
            return [0,0,0,0].map(() => {
                return {
                    user_id : 2 + (id++),
                    username : "User " + id,
                    email : "user" + id + "@gmail.com",
                }
            });
        },
    },
    {
        name : "edituser",
        method : "POST",
        url : "admin/user/{id}",
        response : (r) => r,
        //dummy : () => {error : 0},
    },
    {
        name : "deleteuser",
        method : "GET",
        url : "admin/user/{id}/delete",
        response : (r) => r,
        adummy : () => {error : 0},
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
                        geo_id : sy.geo_id,
                        code : sy.code,
                        years : [],
                        syears : [],
                        yearMap : {}
                    }
                var state = states[sy.name];

                state.years.push(sy.election_year);
                state.syears.push(sy);
                state.yearMap[sy.election_year] = sy;
            });
            var states2 = [];
            for (var i in states)
                if (states.hasOwnProperty(i))
                    states2.push(states[i]);

            // aggregate data of states
            states2.forEach((state) => {
                state.numOfYears = state.years.length;
                state.population = state.syears.map((sy) => sy.population).reduce((a, b) => a + b, 0) / state.numOfYears;
                state.total_votes = state.syears.map((sy) => sy.total_votes).reduce((a, b) => a + b, 0) / state.numOfYears;
                state.years.sort();
                state.recent = state.yearMap[state.years[state.years.length - 1]];
            });

            return states2;
        },
    },
    {
        name : "getdistricts",
        method : "POST",
        url : "district/get/bystateid",
        response : (r, data) => {
            var r = handleVotingData(r);
            r.forEach((d) => {
                d.ignoreRedistrict = false;
            });
            return r;
        },
    },
    {
        name : "getprecincts",
        method : "POST",
        url : "precinct/get/bydistrictid",
        response : (r, data) => {
            var r = handleVotingData(r);
            r.forEach((d) => {
                d.district_id = data.district_id;
                d.ignoreRedistrict = false;
                d.ignoreRedistrictDistrict = false;
            });
            return r;
        },
    },

    // analytics
    {
        name : "analytics",
        method : "GET",
        url : "analytics/{type}",
        response : (r, data) => r,
        adummy : (data) => {
            var m = [];
            if (data.type == "state") {
                m = [
                    ["compactness_score", "double"],
                    ["efficiency_gap_score", "double"],
                    ["lowest_district_population", "int"],
                    ["highest_district_population", "int"],
                    ["number_of_border_precincts", "int"],
                ];
            } else if (data.type == "district") {
                m = [
                    ["compactness_score", "double"],
                    ["efficiency_gap_score", "double"],
                    ["lowest_precinct_population", "int"],
                    ["highest_precinct_population", "int"],
                    ["number_of_precincts", "int"],
                    ["number_of_border_precincts", "int"],
                ];
            } else if (data.type == "precinct") {
                m = [
                    ["compactness_score", "double"],
                    ["efficiency_gap_score", "double"],
                ];
            }
            var r = {};
            m.forEach((a) => {
                r[a[0]] = (a[1] == "double" ? (Math.random() * 100) : Math.round((Math.random() * 100)))
            });
            return r;
        },
    },

    // algorithm
    {
        name : "getconstraints",
        method : "GET",
        url : "algorithm/constraints",
        response : (r, data) => r,
        adummy : () => {
            return {
                compactness_weight : 0.5,
                efficiency_weight : 0.5,
                population_threshold : 0.1,
                loops : 100,
            }
        },
    },
    {
        name : "editconstraints",
        method : "POST",
        url : "algorithm/constraints",
        response : (r, data) => r,
        adummy : () => {
            return {
                ok : true,
            }
        },
    },
    {
        name : "startalgorithm",
        method : "POST",
        url : "algorithm/start",
        response : (r, data) => r,
        adummy : () => {
            window.algloop = 0;
            window.alglooptotal = 10000;
            return {
                algorithm_id : 0,
                loops : window.alglooptotal,
                init_state_efficiency_gap : 0.5,
                init_district_compactness_list : [
                    {district_id : 1, compactness : 0,},
                    {district_id : 2, compactness : 0,},
                ],
            }
        },
    },
    {
        name : "getalgorithmupdate",
        method : "POST",
        url : "algorithm/update",
        response : (r, data) => r,
        adummy : (data={}) => {
            var extra = 2 + Math.floor(Math.random() * 5);
            window.algloop += extra;

            changes = [];
            for(var i=0;i<extra;i++) {
                if (Math.random() < 0.5)
                    changes.push({
                        precinct_id : Math.ceil(Math.random() * 119),
                        new_district_id : 2,
                    });
                else
                    changes.push({
                        precinct_id : 119 + Math.ceil(Math.random() * 150),
                        new_district_id : 1,
                    });
                changes[changes.length - 1].old_district_compactness = Math.random();
                changes[changes.length - 1].new_district_compactness = Math.random();
            }

            return {
                is_running : window.algloop < window.alglooptotal,
                all_changes : changes,
                loop : window.algloop,
            }
        },
    },
    {
        name : "stopalgorithm",
        method : "POST",
        url : "algorithm/stop",
        response : (r, data) => r,
        dummy : () => {
            is_running : false
        },
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

    // misc
    {
        name : "contact",
        method : "POST",
        url : "contact",
        response : (r, data) => r,
        dummy : () => {return {ok : true};},
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
        
        return;
        if (a.votes.DEM == 0) a.votes.DEM = 10 + Math.floor(Math.random() * 900)
        if (a.votes.REP == 0) a.votes.REP = 10 + Math.floor(Math.random() * 900)
        if (a.votes.OTHER == 0) a.votes.OTHER = 10 + Math.floor(Math.random() * 150)
        a.votes.total = a.votes.DEM + a.votes.REP + a.votes.OTHER;
        
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

                Object.keys(opts.data).forEach((key) => {
                    var str = "{" + key + "}";
                    if (URL.indexOf(str) > -1) {
                        URL = URL.replace(new RegExp(str, "g"), opts.data[key]);
                        delete opts.data[key];
                    }
                });

                $.ajax({
                    url : ENDPOINT + "" + URL,
                    method : CALL.method,
                    data : opts.data,
                    success : function(r) {

                        try {
                            r = JSON.parse(r);
                        } catch (e) {

                            try {
                                r = r.replace(/NaN/g, "null");
                                r = JSON.parse(r);
                            } catch (e) {
                                reject("Couldn't parse response for API call ["+opts.name+"]");
                                return;
                            }

                        }

                        if (typeof CALL.response == "function")
                            r = CALL.response(r, opts.data);

                        resolve(r);

                    },
                    error : function(error, errorText) {
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



