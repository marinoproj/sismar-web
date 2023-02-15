/* global L */

function Sismar() {
}

Sismar.berco = function () {

    var googleTerrain = L.tileLayer('http://{s}.google.com/vt/lyrs=p&x={x}&y={y}&z={z}', {
        maxZoom: 20,
        subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
    });

    var map;

    this.initialize = function (id) {

        map = L.map(id, {
            center: L.latLng(-23.802159, -45.391202),
            zoom: 15,
            fullscreenControl: true,
            layers: googleTerrain
        });

    };

    this.addPoin = function (poin) {

        var coords = [];

        for (var k = 0; k < poin.coordinates.length; k++) {
            var coord = poin.coordinates[k];
            coords.push([coord.latitude, coord.longitude]);
        }

        var new_opacity = poin.opacity - (55.0 * poin.opacity) / 100.0;

        var polygon = L.polygon([coords],
                {color: poin.color, // Cor da borda
                    opacity: 0.9, // Transparência da borda 
                    weight: 1, // Expressura da borda
                    fillColor: poin.color, // Cor de fundo
                    fillOpacity: new_opacity}); // Transparência da cor de fundo

        var tooltipNamePoin = L.tooltip({
            permanent: true,
            direction: "right",
            className: "leaflet_label"}, polygon).setLatLng(coords[0]);
        
        tooltipNamePoin.setContent(poin.name);
        
        var layerGroup = new L.layerGroup();
        layerGroup.addLayer(polygon);
        layerGroup.addLayer(tooltipNamePoin);

        map.addLayer(layerGroup);        
        
        map.fitBounds(coords);
        map.setZoom(14);

    };

};
