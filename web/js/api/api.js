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
        method : "POST",
        url : "admin/user/delete/{id}",
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
        response : (r, data) => handleVotingData(r),
    },
    {
        name : "getprecincts",
        method : "POST",
        url : "precinct/get/bydistrictid",
        response : (r, data) => {
            var r = handleVotingData(r)
            r.forEach((d) => d.district_id = data.district_id);
            return r;
        },
    },

    // analytics
    {
        name : "analytics",
        method : "GET",
        url : "/analytics/{type}",
        response : (r, data) => r,
        dummy : (data) => {
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
        name : "startalgorithm",
        method : "POST",
        url : "/algorithm/start",
        response : (r, data) => r,
        dummy : () => {
            window.algloop = 0;
            return {
                algorithm_id : 0,
                loops : 100,
            }
        },
    },
    {
        name : "getalgorithmupdate",
        method : "POST",
        url : "/algorithm/update",
        response : (r, data) => r,
        dummy : (data={}) => {
            var extra = 3 + Math.floor(Math.random() * 8);
            window.algloop += extra;

            var ps = "33015DERR01,33015DERR02,33015DERR03,33015DERR04,33017DOVE01,33017DOVE02,33017DOVE03,33017DOVE04,33017DOVE05,33017DOVE06,33001LACO01,33001LACO02,33001LACO03,33001LACO04,33001LACO05,33001LACO06,33011MANC01,33011MANC10,33011MANC11,33011MANC12,33011MANC02,33011MANC03,33011MANC04,33011MANC05,33011MANC06,33011MANC07,33011MANC08,33011MANC09,33011NASH01,33015PORT01,33015PORT02,33015PORT03,33015PORT04,33015PORT05,33017ROCH01,33017ROCH02,33017ROCH03,33017ROCH04,33017ROCH05,33017ROCH06,33017SOME01,33017SOME02,33017SOME03,33017SOME04,33017SOME05,33003ALBA01,33001ALTO01,33015AUBU01,33001BARN01,33017BARR01,33003BART01,33011BEDF01,33001BELM01,33015BREN01,33003BROO01,33009CAMP01,33015CAND01,33003CHAT01,33015CHSR01,33003CONW01,33015DANV01,33017DURH01,33015EKIN01,33003EATO01,33003EFFI01,33015EPPI01,33015EXET01,33017FARM01,33003FREE01,33015FREM01,33001GILF01,33001GILM01,33011GOFF01,33015GRND01,33015HMST01,33015HMTN01,33015HAFA01,33003HART01,33013HOOK01,33003JACK01,33015KENS01,33015KING01,33017LEE01,33015LOND01,33017MADB01,33003MADI01,33001MERE01,33011MERR01,33017MIDD01,33017MILT01,33003MOUL01,33015NCAS01,33017NDUR01,33001NEWH01,33015NEWF01,33015NWIN01,33015NMAR01,33015NEWT01,33015NOHA01,33015NOTT01,33003OSSI01,33015PLAI01,33015RAYM01,33017ROLL01,33015RYE01,33001SANB01,33015SNDN01,33003SAND01,33015SEAB01,33015SOHA01,33017STFD01,33015STRM01,33003TAMW01,33001TILT01,33003TUFT01,33003WAKE01,33003WOLF01,33003HALE01,33015ZZZZZZ".split(",");
            changes = [];
            for(var i=0;i<extra;i++) {
                changes.push({
                    precinct_geoid : ps[Math.floor(Math.random() * ps.length)],
                    new_district_geoid : "3302", // 2 for district id
                });
                break;
            }

            return {
                is_running : window.algloop < 100,
                all_changes : changes,
                loop : window.algloop,
            }
        },
    },
    {
        name : "stopalgorithm",
        method : "POST",
        url : "/algorithm/stop",
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
                            if (typeof CALL.response == "function")
                                r = CALL.response(r, opts.data);

                        } catch (e) {
                            reject("Couldn't parse response for API call ["+opts.name+"]");
                        }

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



