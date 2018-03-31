
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
    return JSON.parse(pget(key, defaultValue));
}
function pset(key, value=0) {
    localStorage.setItem(key, value);
    return value;
}
function jset(key, value={}) {
    pset(key, JSON.stringify(value));
    return value;
}
