
function createSVG(tag="svg") {
    var svg = document.createElementNS("http://www.w3.org/2000/svg", tag);
    svg.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xlink", "http://www.w3.org/1999/xlink");
    return svg;
}
