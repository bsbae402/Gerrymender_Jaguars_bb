
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

function getPerc(a, b) {
    if (b == 0) return 0;
    return 100 * a / b;
}

function mergeColors(a, b, r=0.5) {
    return a.map((aa, i) => a[i] + r * (b[i] - a[i]));
}
function generateColors(n) {
    var colors = [];
    for(var i=0;i<n;i++) {
        colors.push(HSVtoRGB(i / n, 1, 0.85));
    }
    return colors;
}

function HSVtoRGB(h, s, v) { // 0 <= h, s, v, <= 1
    var r, g, b, i, f, p, q, t;
    if (arguments.length === 1) {
        s = h.s, v = h.v, h = h.h;
    }
    i = Math.floor(h * 6);
    f = h * 6 - i;
    p = v * (1 - s);
    q = v * (1 - f * s);
    t = v * (1 - (1 - f) * s);
    switch (i % 6) {
        case 0: r = v, g = t, b = p; break;
        case 1: r = q, g = v, b = p; break;
        case 2: r = p, g = v, b = t; break;
        case 3: r = p, g = q, b = v; break;
        case 4: r = t, g = p, b = v; break;
        case 5: r = v, g = p, b = q; break;
    }
    return [
        Math.round(r * 255),
        Math.round(g * 255),
        Math.round(b * 255)
    ];
}