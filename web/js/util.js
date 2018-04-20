
function mapArrayToCoords(arr) {
    var a = [];
    for(var i=0;i<arr.length;i+=2) {
        a.push({
            x : arr[i],
            y : arr[i+1],
        })
    }
    return a;
}

function randomInt(a, b) {
    return a + Math.floor(Math.random() * (b-a));
}
function randomFloat(a, b) {
    return a + (Math.random() * (b-a));
}

function pdelete(key) {
    localStorage.removeItem(key);
}
function pget(key, defaultValue=0) {
    var value = localStorage.getItem(key);
    if (value == null)
        return defaultValue;
    return value;
}
function pgetint(key, defaultValue=0) {
    return parseInt(pget(key, defaultValue));
}
function jget(key, defaultValue={}) {
    var value = localStorage.getItem(key);
    if (value == null)
        return defaultValue;
    try {
        value = JSON.parse(value);
    } catch(e) {
        value = defaultValue;
    }
    return value;
}
function pset(key, value=0) {
    localStorage.setItem(key, value);
    return value;
}
function jset(key, value={}) {
    pset(key, JSON.stringify(value));
    return value;
}

function commaNumbers(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
