
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
