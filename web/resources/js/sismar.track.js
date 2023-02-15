/* global L, moment */

function Sismar() {
}

Sismar.track = function () {

    // Constantes
    // Tipos de navios
    const NAVIOS_CARGA = 1;
    const PETROLEIROS = 2;
    const PASSAGEIROS = 3;
    const REBOCADOR_EMBARCACOES_ESPECIAIS = 4;
    const EMBARCACAO_DESCONHECIDA = 5;
    const EMBARCACAO_NAO_ESPECIFICADA = 6;

    // Variáveis
    var idMap;
    var vessels = [];
    var map;
    var typeMaps;
    var nauticalCharts;
    var centerDefault;
    var typeVessels;
    var infoVessels;
    var layers;
    
    var layersMap;    

    this.initialize = function (id) {

        idMap = id;

        // Posição default do mapa
        centerDefault = L.latLng(-23.802159, -45.391202);

        // Carrega os tipos de mapas
        loadTypeMaps();

        // Carrega as cartas náuticas
        loadNauticalCharts();

        // Tipos de embarcações
        loadTypeVessels();

        // Informações das embarcações
        loadInfoVessels();

        // Layers
        getListLayers(false);
        loadLayersMap();

        // Inicializa o mapa
        map = L.map(idMap, {
            center: centerDefault,
            zoom: 15,
            fullscreenControl: true,
            layers: getLayersActive()
        });

        // Carrega os controles do mapa
        setControlsMap();

        // Adiciona os controles no mapa
        addControlsMap();

        // Define os layers
        var baseLayer = getBaseLayer();
        var overlayLayer = getOverlayLayer();
        L.control.layers.tree(baseLayer, overlayLayer).addTo(map);

    };

    addControlsMap = function () {

        var customControl = L.Control.extend({

            options: {
                position: 'topleft'
                        //control position - allowed: 'topleft', 'topright', 'bottomleft', 'bottomright'
            },

            onAdd: function () {
                var container = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom');

                container.type = "button";
                container.title = "Voltar";
                container.style.backgroundImage = "url(/sismar/faces/javax.faces.resource/img/back.png)";
                container.style.backgroundSize = "18px 18px";
                container.style.backgroundRepeat = "no-repeat";
                container.style.backgroundPosition = "center";
                container.style.cursor = "pointer";
                container.style.backgroundColor = 'white';
                container.style.width = '35px';
                container.style.height = '30px';

                container.onclick = function () {
                    window.location.href = "realtime.xhtml";
                };

                container.onmouseover = function () {
                    container.style.backgroundColor = '#f4f4f4';
                };

                container.onmouseout = function () {
                    container.style.backgroundColor = 'white';
                };

                return container;
            }

        });

        map.addControl(new customControl());

    };

    getLayersActive = function () {
        var layers = [getTypeMapPrimary()];
        for (var i = 0; i < typeVessels.length; i++) {
            var typeVessel = typeVessels[i];
            layers.push(typeVessel.layer);
        }
        for (var i = 0; i < layersMap.length; i++) {
            if (layersMap[i].name === "Sea") {
                layers.push(layersMap[i].layer);
                break;
            }
        }
        return layers;
    };

    loadTypeMaps = function () {
        var googleStreets = L.tileLayer('http://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}', {
            maxZoom: 20,
            subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
        });
        var googleHybrid = L.tileLayer('http://{s}.google.com/vt/lyrs=s,h&x={x}&y={y}&z={z}', {
            maxZoom: 20,
            subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
        });
        var googleSat = L.tileLayer('http://{s}.google.com/vt/lyrs=s&x={x}&y={y}&z={z}', {
            maxZoom: 20,
            subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
        });
        var googleTerrain = L.tileLayer('http://{s}.google.com/vt/lyrs=p&x={x}&y={y}&z={z}', {
            maxZoom: 20,
            subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
        });
        var surferRoads = L.tileLayer('https://maps.heigit.org/openmapsurfer/tiles/roads/webmercator/{z}/{x}/{y}.png', {
            maxZoom: 19
        });
        var hyddaFull = L.tileLayer('https://{s}.tile.openstreetmap.se/hydda/full/{z}/{x}/{y}.png', {
            maxZoom: 18
        });

        var seaMapGroup = new L.layerGroup();
        var seaLayer = L.tileLayer('https://tiles.openseamap.org/seamark/{z}/{x}/{y}.png', {
        });
        var seaMap = L.tileLayer('http://{s}.google.com/vt/lyrs=p&x={x}&y={y}&z={z}', {
            maxZoom: 20,
            subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
        });
        seaMapGroup.addLayer(seaMap);
        seaMapGroup.addLayer(seaLayer);

        typeMaps = [{name: "Streets", primary: false, layer: googleStreets},
            {name: "Hybrid", primary: false, layer: googleHybrid},
            {name: "Satellite", primary: false, layer: googleSat},
            {name: "Terrain", primary: false, layer: googleTerrain},
            {name: "Surfer Roads", primary: false, layer: surferRoads},
            {name: "Hydda Full", primary: false, layer: hyddaFull},
            {name: "Sea", primary: true, layer: seaMapGroup}];

    };

    loadNauticalCharts = function () {
        var nauticalChartSantos = new L.ImageOverlay('/sismar/faces/javax.faces.resource/img/new_santos-min.png',
                [[-23.95000000, -46.39305556], [-24.08416667, -46.27750000]]);
        nauticalCharts = [{name: "Santos", layer: nauticalChartSantos}];
    };

    loadTypeVessels = function () {
        typeVessels = [{name: "Navios de carga <img src='/sismar/faces/javax.faces.resource/img/icone_navio_carga.png'/>", type: NAVIOS_CARGA, layer: new L.layerGroup()},
            {name: "Petroleiros <img src='/sismar/faces/javax.faces.resource/img/icone_navio_petroleiro.png'/>", type: PETROLEIROS, layer: new L.layerGroup()},
            {name: "Passageiros <img src='/sismar/faces/javax.faces.resource/img/icone_navio_passageiros.png'/>", type: PASSAGEIROS, layer: new L.layerGroup()},
            {name: "Rebocadores e embarcações especiais <img src='/sismar/faces/javax.faces.resource/img/icone_rebocador.png'/>", type: REBOCADOR_EMBARCACOES_ESPECIAIS, layer: new L.layerGroup()},
            {name: "Embarcação desconhecida <img src='/sismar/faces/javax.faces.resource/img/icone_navio_desconhecido.png'/>", type: EMBARCACAO_DESCONHECIDA, layer: new L.layerGroup()},
            {name: "Embarcação não especificada <img src='/sismar/faces/javax.faces.resource/img/icone_navio_nao_especificada.png'/>", type: EMBARCACAO_NAO_ESPECIFICADA, layer: new L.layerGroup()}];
    };

    loadInfoVessels = function () {
        infoVessels = [{name: "Nome da embarcação", layer: new L.layerGroup()},
            {name: "Velocidade e direção", layer: new L.layerGroup()}];
    };

    loadLayersMap = function () {
        layersMap = [];
        for (var i = 0; i < layers.layers.length; i++) {

            var layer = layers.layers[i];
            var layerGroup = new L.layerGroup();

            for (var j = 0; j < layer.poins.length; j++) {
                var poin = layer.poins[j];
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

                layerGroup.addLayer(polygon);
                layerGroup.addLayer(tooltipNamePoin);

            }

            layersMap.push({name: layer.name, layer: layerGroup});
        }

        var seaMap = L.tileLayer('https://tiles.openseamap.org/seamark/{z}/{x}/{y}.png', {
        });
        layersMap.push({name: "Sea", layer: seaMap});

    };

    getTypeMapPrimary = function () {
        for (var i = 0; i < typeMaps.length; i++) {
            var typeMap = typeMaps[i];
            if (typeMap.primary) {
                return typeMap.layer;
            }
        }
        return typeMaps[0].layer;
    };

    getLayerByTypeVessel = function (type) {
        for (var i = 0; i < typeVessels.length; i++) {
            if (typeVessels[i].type === type) {
                return typeVessels[i].layer;
            }
        }
        return null;
    };

    getNauticalChartLayer = function () {
        var children = [];
        for (var i = 0; i < nauticalCharts.length; i++) {
            var nauticalChart = nauticalCharts[i];
            children.push({label: nauticalChart.name, layer: nauticalChart.layer});
        }
        var layer = {
            label: "<span class='leaflet-layerstree-header-title'>Cartas Náuticas</span>",
            children: children
        };
        return layer;
    };

    
    getResquestAjax = function () {
        try {
            request = new XMLHttpRequest();
        } catch (IEAtual) {
            try {
                request = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (IEAntigo) {
                try {
                    request = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (falha) {
                    request = false;
                }
            }
        }
        if (!request) {
        } else {
            return request;
        }
    };
    

    getListLayers = function (async) {

        var xmlreq = getResquestAjax();
        xmlreq.open("GET", "/sismar/api/layer/all_sys", async);
        xmlreq.setRequestHeader("Authorization", "Basic bGVvOjEyMw==");

        xmlreq.onreadystatechange = function () {
            if (xmlreq.readyState === 4 && xmlreq.status === 200) {
                var json = jQuery.parseJSON(xmlreq.responseText);
                layers = json;
            }
        };

        xmlreq.send(null);

    };

    getTypeVesselsLayer = function () {
        var children = [];
        for (var i = 0; i < typeVessels.length; i++) {
            var typeVessel = typeVessels[i];
            children.push({label: typeVessel.name, layer: typeVessel.layer});
        }
        var layer = {
            label: "<span class='leaflet-layerstree-header-title'>Tipos de Embarcações</span>",
            children: children
        };
        return layer;
    };
    
    getLayersFromLayer = function () {
        var children = [];
        for (var i = 0; i < layersMap.length; i++) {
            var layer = layersMap[i];
            children.push({label: layer.name, layer: layer.layer});
        }
        var layer = {
            label: "<span class='leaflet-layerstree-header-title'>Layers</span>",
            children: children
        };
        return layer;
    };

    getInfoVesselsLayer = function () {
        var children = [];
        for (var i = 0; i < infoVessels.length; i++) {
            var infoVessel = infoVessels[i];
            children.push({label: infoVessel.name, layer: infoVessel.layer});
        }
        var layer = {
            label: "<span class='leaflet-layerstree-header-title'>Exibir Informações</span>",
            children: children
        };
        return layer;
    };

    getBaseLayer = function () {
        var children = [];
        for (var i = 0; i < typeMaps.length; i++) {
            var typeMap = typeMaps[i];
            children.push({label: typeMap.name, layer: typeMap.layer});
        }
        var baseLayer = {
            label: "<span class='leaflet-layerstree-header-title'>Mapa</span>",
            children: children
        };
        return baseLayer;
    };

    getOverlayLayer = function () {
        var overlayLayer = [getNauticalChartLayer(), getInfoVesselsLayer(), getTypeVesselsLayer(), getLayersFromLayer()];
        return overlayLayer;
    };

    setControlsMap = function () {

        map.on('zoom', function () {
            var zoom = map.getZoom();
            for (var i = 0; i < vessels.length; i++) {
                vesselRefreshZoom(vessels[i], zoom);
            }
        });

        map.on('overlayadd', function (e) {
            for (var i = 0; i < typeVessels.length; i++) {
                if (e.layer === typeVessels[i].layer) {
                    var zoom = map.getZoom();
                    for (var j = 0; j < vessels.length; j++) {
                        if (vessels[j].data.printMap.codType === typeVessels[i].type) {
                            vesselRefreshZoom(vessels[j], zoom);
                        }
                    }
                    break;
                }
                if (e.layer === infoVessels[0].layer || e.layer === infoVessels[1].layer) {
                    var zoom = map.getZoom();
                    for (var j = 0; j < vessels.length; j++) {
                        vesselRefreshZoom(vessels[j], zoom);
                    }
                    break;
                }
            }
        });

    };

    rotatePoints = function (center, points, yaw) {
        var res = [];
        var angle = yaw * (Math.PI / 180);
        for (var i = 0; i < points.length; i++) {
            var p = points[i];
            var p2 = [p[0] - center[0], p[1] - center[1]];
            var p3 = [Math.cos(angle) * p2[0] - Math.sin(angle) * p2[1], Math.sin(angle) * p2[0] + Math.cos(angle) * p2[1]];
            var p4 = [p3[0] + center[0], p3[1] + center[1]];
            res.push(p4);
        }
        return res;
    };

    addVesselFromMap = function (data) {
        
        var trail = data.trail;        
        var newTrail = [];
                
        for (var i = (trail.length - 1); i >= 0; i--) {
            newTrail.push(trail[i]);
        }
        
        data.trail = newTrail;                

        // Verifica se deve exibir no mapa
        if (data.printMap === undefined || data.printMap === null) {
            return;
        }

        var position = L.latLng(data.printMap.lat, data.printMap.lng); // Coordenadas da embarcação em Latitude e Longitude
        var coords = [];

        // Se o tamanho da embarcação for grande e proporcional ao zoom do mapa
        if (data.printMap.proportionalMap) {
            coords = getCoordsFromVessel(data.printMap, position);
        }

        // Polígono que representa o navio no mapa
        var polygon = L.polygon([coords],
                {color: data.printMap.color, // Cor da borda
                    opacity: 0.9, // Transparência da borda 
                    weight: 1, // Expressura da borda
                    fillColor: data.printMap.color, // Cor de fundo
                    fillOpacity: data.printMap.opacity}); // Transparência da cor de fundo
        setPopupVesselFromLayer(data, polygon);

        // Poliline que representa o rastro da embarcação
        var latLngsBoat = [];
        for (var i = 0; i < data.trail.length; i++) {
            var trail = data.trail[i];
            latLngsBoat.push([trail.lat, trail.lng]);
        }
        var polyline = L.polyline(latLngsBoat, {
            color: 'blue',
            weight: 1,
            opacity: 0.9});
        map.fitBounds(latLngsBoat);

        // Círculo que presenta o ponto de instalação da antena do ais
        var circle = L.circle(position, (data.printMap.proportionalMap ? 5 : 0), {
            color: data.printMap.color, // Cor da borda
            opacity: 0.9, // Transparência da borda
            weight: 1, // Expressura da borda
            fillColor: '#73b6e6', // Cor de fundo
            fillOpacity: 1});
        setPopupVesselFromLayer(data, circle);
        setInfoTooltipVessel(data, circle);

        var tooltipNameVessel = L.tooltip({
            permanent: true,
            direction: "right",
            className: "leaflet_label"}, circle).setLatLng(position);
        tooltipNameVessel.setContent((data.vessel === null ? "" : data.vessel.name));

        var tooltipMoreVessel = L.tooltip({
            permanent: true,
            direction: "right",
            className: "leaflet_label leaflet_label_more_vessel"}, circle).setLatLng(position);
        tooltipMoreVessel.setContent(data.printMap.velocity + " kn" + " / " + data.printMap.direction + " º");

        infoVessels[0].layer.addLayer(tooltipNameVessel);
        infoVessels[1].layer.addLayer(tooltipMoreVessel);

        // Círculo que representa a embarcação quando diminuído o zoom do mapa
        var circle_zoom = L.circleMarker(position, {
            color: data.printMap.color, // Cor da borda
            radius: 5,
            opacity: 0.9, // Transparência da borda
            weight: 1, // Expressura da borda
            fillColor: data.printMap.color, // Cor de fundo
            fillOpacity: 0.5});
        setPopupVesselFromLayer(data, circle_zoom);
        setInfoTooltipVessel(data, circle_zoom);

        var layer = new L.layerGroup([polygon, polyline, circle, circle_zoom, tooltipNameVessel, tooltipMoreVessel]);

        // adiciona o layer com a embarcação
        for (var i = 0; i < typeVessels.length; i++) {
            if (typeVessels[i].type === data.printMap.codType) {
                typeVessels[i].layer.addLayer(layer);
                break;
            }
        }

        var vesselMap = {data: data,
            polygon: polygon,
            polyline: polyline,
            circle: circle,
            circle_zoom: circle_zoom,
            tooltipNameVessel: tooltipNameVessel,
            tooltipMoreVessel: tooltipMoreVessel,
            layer: layer};

        vessels.push(vesselMap);

        vesselRefreshZoom(vesselMap, map.getZoom());

    };

    setPopupVesselFromLayer = function (data, layer) {

        layer.on('click', function (e) {

            if (layer.getPopup() !== undefined) {
                layer.unbindPopup();
            }

            setContentVesselSuccess(layer, data);

        });

    };

    setInfoTooltipVessel = function (data, circle) {

        var text = "<b style='font-size:'18px';'>" + (data.vessel === null ? "NOME INDISPÍVEL" : data.vessel.name) + "</b>";
        if (data.vessel !== null) {
            text += "<br/><b>Imo: </b>" + data.vessel.imo;
        }
        text += "<br/><b>Velocidade: </b>" + data.printMap.velocity + " kn ";
        text += "<b>Curso: </b>" + data.printMap.direction + " º";
        text += "<br/><b>Destino: </b>" + data.printMap.destination;

        if (circle.getTooltip() === null || circle.getTooltip() === undefined) {
            circle.bindTooltip(text).openTooltip();
        } else {
            circle.setTooltipContent(text);
        }

    };

    vesselRefreshZoom = function (vesselMap, zoom) {

        // Se o tamanho do navio não deve ser proporcional ao mapa
        if (!vesselMap.data.printMap.proportionalMap) {
            var position = L.latLng(vesselMap.data.printMap.lat, vesselMap.data.printMap.lng);
            var coords = getCoordsFromVesselStatic(vesselMap.data.printMap, position);
            vesselMap.polygon.setLatLngs(coords);
        }

        // Zoom acima de 14, mostra todos os navios
        if (zoom > 14) {
            vesselMap.layer.addLayer(vesselMap.polygon);
            vesselMap.layer.addLayer(vesselMap.circle);
            vesselMap.layer.addLayer(vesselMap.polyline);
            vesselMap.circle_zoom.remove();
        } else if (zoom > 13 && vesselMap.data.printMap.proportionalMap) { // Zoom acima de 12, mostra os navios com o tamanho grande
            vesselMap.layer.addLayer(vesselMap.polygon);
            vesselMap.layer.addLayer(vesselMap.circle);
            vesselMap.layer.addLayer(vesselMap.polyline);
            vesselMap.circle_zoom.remove();
        } else { // Se o zoom for menor que 12, mostra somente o circulo do navio
            vesselMap.polygon.remove();
            vesselMap.circle.remove();
            vesselMap.layer.addLayer(vesselMap.polyline);
            vesselMap.layer.addLayer(vesselMap.circle_zoom);
        }

        // Para embarcações não proporcional
        if (zoom > 14 && !vesselMap.data.printMap.proportionalMap) {
            vesselMap.circle.setRadius(1);
        } else if (!vesselMap.data.printMap.proportionalMap) {
            vesselMap.circle.setRadius(0);
        }

        // Para embarcações proporcional
        if (zoom > 14 && vesselMap.data.printMap.proportionalMap) {
            vesselMap.circle.setRadius(7);
        } else if (zoom > 12 && vesselMap.data.printMap.proportionalMap) {
            vesselMap.circle.setRadius(6);
        } else if (vesselMap.data.printMap.proportionalMap) {
            vesselMap.circle.setRadius(6);
        }

        // Zoom acima de 13 -> Exibir nome do navio
        if (zoom > 14) {
            if (map.hasLayer(infoVessels[0].layer) && map.hasLayer(getLayerByTypeVessel(vesselMap.data.printMap.codType))) {
                vesselMap.tooltipNameVessel.addTo(map);
            } else {
                vesselMap.tooltipNameVessel.remove();
            }
            if (map.hasLayer(infoVessels[1].layer) && map.hasLayer(getLayerByTypeVessel(vesselMap.data.printMap.codType))) {
                vesselMap.tooltipMoreVessel.addTo(map);
            } else {
                vesselMap.tooltipMoreVessel.remove();
            }

        } else if (zoom > 13 && vesselMap.data.printMap.proportionalMap) {
            if (map.hasLayer(infoVessels[0].layer) && map.hasLayer(getLayerByTypeVessel(vesselMap.data.printMap.codType))) {
                vesselMap.tooltipNameVessel.addTo(map);
            } else {
                vesselMap.tooltipNameVessel.remove();
            }

            if (map.hasLayer(infoVessels[1].layer) && map.hasLayer(getLayerByTypeVessel(vesselMap.data.printMap.codType))) {
                vesselMap.tooltipMoreVessel.addTo(map);
            } else {
                vesselMap.tooltipMoreVessel.remove();
            }

        } else {
            vesselMap.tooltipNameVessel.remove();
            vesselMap.tooltipMoreVessel.remove();

        }

    };

    getCoordsFromVessel = function (printMap, position) {

        var a = printMap.dimension.a;
        var b = printMap.dimension.b;
        var c = printMap.dimension.c;
        var d = printMap.dimension.d;

        var bico = (a * 17) / 100;
        a = a - bico;

        var width = c + d;

        var coord1 = addMetersInCoordinate(position, b, c * -1);
        var coord2 = addMetersInCoordinate(position, b, d);
        var coord3 = addMetersInCoordinate(coord2, (a + b) * -1, 0);
        var coord4 = addMetersInCoordinate(coord3, bico * -1, (width / 2) * -1);
        var coord5 = addMetersInCoordinate(coord4, bico, (width / 2) * -1);

        var coords = [[coord1.lat, coord1.lng],
            [coord2.lat, coord2.lng],
            [coord3.lat, coord3.lng],
            [coord4.lat, coord4.lng],
            [coord5.lat, coord5.lng]];

        return rotatePoints([position.lat, position.lng], coords, 90 + printMap.direction);

    };

    getCoordsFromVesselStatic = function (vessel, position) {

        var a = vessel.dimension.a;
        var b = vessel.dimension.b;
        var c = vessel.dimension.c;
        var d = vessel.dimension.d;

        var bico = a * 0.3;
        a = a - bico;

        var width = c + d;

        var coord1 = addPixelInCoordinate(position, b, c * -1);
        var coord2 = addPixelInCoordinate(position, 0, 0);
        var coord3 = addPixelInCoordinate(position, b, d);
        var coord4 = addPixelInCoordinate(coord3, (a + b) * -1, 0);
        var coord5 = addPixelInCoordinate(coord4, bico * -1, (width / 2) * -1);
        var coord6 = addPixelInCoordinate(coord5, bico, (width / 2) * -1);

        var coords = [[coord1.lat, coord1.lng],
            [coord2.lat, coord2.lng],
            [coord3.lat, coord3.lng],
            [coord4.lat, coord4.lng],
            [coord5.lat, coord5.lng],
            [coord6.lat, coord6.lng]];

        return rotatePoints([position.lat, position.lng], coords, vessel.direction);

    };

    addPixelInCoordinate = function (coordinate, pixelLongitude, pixelLatitude) {
        var point = map.latLngToLayerPoint(coordinate);

        var x = point.x;
        var y = point.y;

        var newy = y + pixelLongitude;
        var newx = x + pixelLatitude;

        return map.layerPointToLatLng(L.point(newx, newy));
    };

    addMetersInCoordinate = function (coordinate, metersLongitude, metersLatitude) {
        var newLng = getNewLongitude(coordinate.lng, coordinate.lat, metersLongitude);
        var newLat = getNewLatitude(coordinate.lat, metersLatitude);
        return L.latLng(newLat, newLng);
    };

    getNewLatitude = function (latitude, meters) {
        var earth = 6378.137,
                pi = Math.PI,
                m = (1 / ((2 * pi / 360) * earth)) / 1000;
        var new_latitude = latitude + (meters * m);
        return new_latitude;
    };

    getNewLongitude = function (longitude, latitude, meters) {
        var earth = 6378.137,
                pi = Math.PI,
                cos = Math.cos,
                m = (1 / ((2 * pi / 360) * earth)) / 1000;
        var new_longitude = longitude + (meters * m) / cos(latitude * (pi / 180));
        return new_longitude;
    };

    removeLayer = function (parentLayer, layer) {
        map.removeLayer(layer);
        layer.remove();
        layer.removeFrom(map);
        parentLayer.removeLayer(layer);
    };

    convertDate = function (datetime) {
        var date = datetime.split(" ")[0];
        var time = datetime.split(" ")[1];

        var day = date.split("/")[0];
        var month = date.split("/")[1];
        var year = date.split("/")[2];
        var hour = time.split(":")[0];
        var minute = time.split(":")[1];

        return year + "-" + month + "-" + day + "T" + hour + ":" + minute;
    };

    this.loadTrackVessel = function () {

        var url = new URL(window.location.href);
        var mmsi = url.searchParams.get("mmsi");
        var start = url.searchParams.get("start");
        var end = url.searchParams.get("end");

        if (mmsi !== null && start !== null && end !== null) {

            var startDateMilliseconds = new Date(convertDate(start)).getTime();
            var endDateMilliseconds = new Date(convertDate(end)).getTime();

            $.ajax({
                type: 'GET',
                async: false,
                url: '/sismar/api/vessel/track',
                data: 'mmsi=' + mmsi + '&startDate=' + startDateMilliseconds + '&endDate=' + endDateMilliseconds + "&printMap=1",
                success: function (data) {
                    addVesselFromMap(data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                },
                dataType: 'json'
            });
        }
    };

    getContentLoading = function () {
        var content = '<div style="width: 400px;" >' +
                '<div class="col-xs-12 info-main-weather not-padding" style="font-weight: bold;">' +
                '<img src="/sismar/faces/javax.faces.resource/img/icone_loader.gif" style="height: 20px; margin-right: 10px;"> Carregando as informações, aguarde ...' +
                '</div>' +
                '</div>';
        return content;
    };

    getContentError = function () {
        var content = '<div style="width: 400px;" >' +
                '<div class="col-xs-12 info-main-weather not-padding" style="font-weight: bold; color:red;">' +
                'Não foi possível buscar as informações! ' +
                '</div>' +
                '</div>';
        return content;
    };

    setContentError = function (e) {
        var content = getContentError();
        e.bindPopup(content, {className: "leaflet_popup_loading"});
        e.openPopup();
    };

    isNullValueJson = function (value) {
        if (value === undefined || value === null || value === "") {
            return true;
        }
        return false;
    };

    getValueFromContent = function (value, unity = "", replaceIfNullOrEmpty = false, valueIfNullOrEmpty = "") {
        if (isNullValueJson(value)) {
            if (!replaceIfNullOrEmpty) {
                return "&nbsp;";
            } else {
                return valueIfNullOrEmpty;
            }
        }
        return value + unity;
    };

    setContentVesselSuccess = function (e, data) {
        var content = "";
        if (!isNullValueJson(data.vessel)) {
            content = getContentVessel(data);
        } else {
            content = getContentVesselUnknown(data);
        }
        e.bindPopup(content, {className: "leaflet_popup_info_vessel"});
        e.openPopup();
    };

    eventClickOpenPeriodTrack = function (mmsi) {
        $("#form-open-period-track input[name='mmsi']").val(mmsi);
    };

    getContentVessel = function (data) {

        var content = '<div class="container-popup-vessel">' +
                '<div class="row">' +
                '<div class="container-popup-title col-xs-12 col-md-12">' +
                '<div class="dropdown" style="float: left;">' +
                '<button class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" style="color: white; padding-left: 0px !important; padding-top: 9px;">' +
                '<i class="glyphicon glyphicon-menu-hamburger"></i></button>' +
                '<ul class="dropdown-menu">' +
                '<li>' +
                "<a class=\"button-open-period-track\" href=\"#\" class=\"ui-commandlink ui-widget\" onclick=\"eventClickOpenPeriodTrack(" + data.lastAisRecord.mmsi + ");PF('open-period-track').show();PrimeFaces.ab({s:'button-open-period-track'});return false;\">" +
                '<i class="glyphicon glyphicon-search"></i>' +
                'Track (Rastro)' +
                '</a>' +
                '</li>' +
                '</ul>' +
                '</div>' +
                '<p class="container-popup-name">' + getValueFromContent(data.vessel.name, "", true, "DESCONHECIDO") + '</p>' +
                '<p class="container-popup-type" style="padding-left: 30px;">' +
                '<b>IMO:</b> ' + getValueFromContent(data.vessel.imo) + ' ' +
                '<b>Categoria:</b> ' + getValueFromContent(data.vessel.type) +
                '</p>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-img col-xs-12 col-md-12">' +
                (isNullValueJson(data.vessel.image) ?
                        '<img id="image-vessel-popup" src="/sismar/faces/javax.faces.resource/img/sem_imagem.png" width="100%" height="200px"></img>' :
                        '<img id="image-vessel-popup" src="' + data.vessel.image + '" width="100%" height="200px"></img>') +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-info col-xs-12 col-md-12">' +
                '<div class="row">' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Dimensão:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(data.vessel.dimension) + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Velocidade:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(data.lastAisRecord.velocity, " kn") + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Curso:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(data.lastAisRecord.direction, "º") + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Calado:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(data.lastAisRecord.draught, "m") + '</p>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-info-card col-xs-6 col-md-6">' +
                '<p class="container-popup-info-card-title">Destino:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(data.lastAisRecord.destination) + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-6 col-md-6">' +
                '<p class="container-popup-info-card-title">Estado:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(data.lastAisRecord.state) + '</p>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-footer col-xs-12 col-md-12">' +
                '<p><b>Recebido:</b> ' + getValueFromContent(data.lastAisRecord.message) + '</p>' +
                '<p><b>Latitude:</b> ' + getValueFromContent(data.lastAisRecord.lat) + ' <b>Longitude:</b> ' + getValueFromContent(data.lastAisRecord.lng) + '</p>' +
                '<p><b>MMSI:</b> ' + getValueFromContent(data.lastAisRecord.mmsi) + '</p>' +
                '</div>' +
                '</div>' +
                '</div>';

        return content;

    };

    getContentVesselUnknown = function (data) {

        var content = '<div class="container-popup-vessel">' +
                '<div class="row">' +
                '<div class="container-popup-title col-xs-12 col-md-12">' +
                '<div class="dropdown" style="float: left;">' +
                '<button class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" style="color: white; padding-left: 0px !important; padding-top: 9px;">' +
                '<i class="glyphicon glyphicon-menu-hamburger"></i></button>' +
                '<ul class="dropdown-menu">' +
                '<li>' +
                "<a class=\"button-open-period-track\" href=\"#\" class=\"ui-commandlink ui-widget\" onclick=\"eventClickOpenPeriodTrack(" + data.lastAisRecord.mmsi + ");PF('open-period-track').show();PrimeFaces.ab({s:'button-open-period-track'});return false;\">" +
                '<i class="glyphicon glyphicon-search"></i>' +
                'Track (Rastro)' +
                '</a>' +
                '</li>' +
                '</ul>' +
                '</div>' +
                '<p class="container-popup-name">DESCONHECIDO</p>' +
                '<p class="container-popup-type" style="padding-left: 30px;">' +
                '<b>IMO:</b> DESCONHECIDO ' +
                '<b>Categoria:</b> DESCONHECIDO' +
                '</p>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-img col-xs-12 col-md-12">' +
                '<img src="/sismar/faces/javax.faces.resource/img/sem_imagem.png" width="100%" height="200px"></img>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-info col-xs-12 col-md-12">' +
                '<div class="row">' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Dimensão:</p>' +
                '<p class="container-popup-info-card-value">&nbsp;</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Velocidade:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(data.lastAisRecord.velocity, " kn") + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Curso:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(data.lastAisRecord.direction, "º") + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Calado:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(data.lastAisRecord.draught, "m") + '</p>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-info-card col-xs-6 col-md-6">' +
                '<p class="container-popup-info-card-title">Destino:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(data.lastAisRecord.destination) + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-6 col-md-6">' +
                '<p class="container-popup-info-card-title">Estado:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(data.lastAisRecord.state) + '</p>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-footer col-xs-12 col-md-12">' +
                '<p><b>Recebido:</b> ' + getValueFromContent(data.lastAisRecord.message) + '</p>' +
                '<p><b>Latitude:</b> ' + getValueFromContent(data.lastAisRecord.lat) + ' <b>Longitude:</b> ' + getValueFromContent(data.lastAisRecord.lng) + '</p>' +
                '<p><b>MMSI:</b> ' + getValueFromContent(data.lastAisRecord.mmsi) + '</p>' +
                '</div>' +
                '</div>' +
                '</div>';

        return content;

    };

}
;
