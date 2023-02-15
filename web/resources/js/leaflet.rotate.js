/* global L */

L.Polygon.prototype.rotate = function (angle, latLng, map) {
    var coords = this.getLatLngs();
    var origin = map.latLngToLayerPoint(latLng);
    coords[0].forEach(function (point, index) {
        var pixelCoord = map.latLngToLayerPoint(point).rotate(angle, origin);
        coords[index] = map.layerPointToLatLng(pixelCoord);
    });
    this.setLatLngs(coords);
};

L.LatLng.prototype.rotate = function (angle, origin) {
    var radianAngle = angle * (Math.PI / 180);

    var radius = this.distanceTo(origin);
    var theta = radianAngle + Math.atan2(this.lng - origin.lng, this.lat - origin.lat);

    var x = origin.lat + (radius * Math.cos(theta));
    var y = origin.lng + (radius * Math.sin(theta));

    return L.latLng(x, y);
};

L.LatLng.prototype.distanceTo = function (point) {
    var lat = Math.pow(this.lat - point.lat, 2);
    var lng = Math.pow(this.lng - point.lng, 2);
    return Math.sqrt(lat + lng);
};


L.Point.prototype.rotate = function (angle, origin) {
    var rad = angle * (Math.PI / 180);

    var x = this.x - origin.x;
    var y = this.y - origin.y;

    return new L.Point(
            x * Math.cos(rad) - y * Math.sin(rad) + origin.x,
            x * Math.sin(rad) + y * Math.cos(rad) + origin.y
            );
};