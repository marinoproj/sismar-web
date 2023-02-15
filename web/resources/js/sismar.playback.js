/* global L, moment, PF */

function Sismar() {
}

Sismar.playback = function () {

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
    var layersMap;
    var centerDefault;
    var typeVessels;
    var infoVessels;
    var layers;
    var moduleAtracacao;

    // Controle playback
    var bt_start;
    var bt_end;
    var sld_navegation;
    var lb_date_data;
    var lb_qtd_records;
    var chk_velocity;
    var sleep_frames = 1000;
    var index_data = 0;
    var start_playback = false;
    var refreshIntervalDataId;
    var dataPlayback;
    var panel_controls_playback;


    this.initialize = function (id, atracacao) {

        idMap = id;
        moduleAtracacao = atracacao;
        
        var zoom = 15;

        // Posição default do mapa
        var coordenadasBerco = moduleAtracacao.getCoordenadasBerco();
        if (coordenadasBerco){
            centerDefault = L.latLng(coordenadasBerco[0], coordenadasBerco[1]);    
        }
        
        if (!centerDefault){
            centerDefault = L.latLng(-23.802159, -45.391202);
        }

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
            zoom: zoom,
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

        // Adiciona controles do playback
        addControlPlayback();

        submitFormPeriodPlayback('form-open-period-playback');
        //$(".controls-playback").draggable();

    };

    alertSelectedPeriod = function (contentId, title, message) {
        $("#" + contentId).empty();
        $("#" + contentId).append('<div class="alert alert-danger fade in alert-dismissible">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close" title="close">×</a>' +
                '<strong>' + title + '</strong> ' + message +
                '</div>');
    };

    submitFormPeriodPlayback = function (formId) {
        $('#' + formId).submit(function () {

            var startDate = $('#' + formId + " input[name='start']").val();
            var endDate = $('#' + formId + " input[name='end']").val();

            if (startDate === "" || endDate === "") {
                alertSelectedPeriod('alert-selected-period', "Atenção!", "Informe as datas corretamente.");
                return false;
            }

            var startDateMilliseconds = new Date(startDate).getTime();
            var endDateMilliseconds = new Date(endDate).getTime();

            if (startDateMilliseconds > endDateMilliseconds) {
                alertSelectedPeriod('alert-selected-period', "Atenção!", "A data de inicio não pode ser superior a data de término.");
                return false;
            }

            var durationMinutes = moment.duration(moment(endDateMilliseconds).diff(moment(startDateMilliseconds))).asMinutes();

            if (durationMinutes > 120) {
                alertSelectedPeriod('alert-selected-period', "Atenção!", "O período informado não pode ser superior a 2 horas.");
                return false;
            }

            return true;

        });

    };

    addControlsMap = function () {

        var url = new URL(window.location.href);
        codAtracacao = url.searchParams.get("codAtracacao");

        if (!codAtracacao) {

            var playbackButton = L.Control.extend({

                options: {
                    position: 'topleft'
                },

                onAdd: function () {
                    var container = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom');

                    container.type = "button";
                    container.title = "Playback";
                    container.style.backgroundImage = "url(/sismar/faces/javax.faces.resource/img/play.png)";
                    container.style.backgroundSize = "18px 18px";
                    container.style.backgroundRepeat = "no-repeat";
                    container.style.backgroundPosition = "center";
                    container.style.cursor = "pointer";
                    container.style.backgroundColor = 'white';
                    container.style.width = '35px';
                    container.style.height = '35px';

                    container.onclick = function () {
                        PF('open-period-playback').show();
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
            
            map.addControl(new playbackButton());

        }

        var backButton = L.Control.extend({

            options: {
                position: 'topleft'
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

        map.addControl(new backButton());

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
        var polyline = L.polyline(latLngsBoat, {
            color: 'blue',
            weight: 1,
            opacity: 0.9});

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

            var popup_loading = L.popup({className: "leaflet_popup_loading"}).
                    setLatLng(e.latlng).
                    setContent(getContentLoading()).
                    openOn(map);

            $.ajax({
                type: 'GET',
                url: '/sismar/api/vessel/image',
                data: 'mmsi=' + data.printMap.mmsi,
                success: function (response) {
                    popup_loading.closePopup();
                    removeLayer(map, popup_loading);
                    for (var i = 0; i < vessels.length; i++) {
                        if (vessels[i].data.printMap.mmsi === data.printMap.mmsi) {
                            vessels[i].data.vessel.image = response.image;
                            setContentVesselSuccess(layer, vessels[i].data);
                        }
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    setContentError(layer);
                },
                dataType: 'json'
            });

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
            vesselMap.polyline.remove();
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

        } else if (zoom > 13 && vesselMap.data.proportionalMap) {
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

    getCoordsFromVessel = function (data, position) {

        var a = data.dimension.a;
        var b = data.dimension.b;
        var c = data.dimension.c;
        var d = data.dimension.d;

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

        return rotatePoints([position.lat, position.lng], coords, 90 + data.direction);

    };

    getCoordsFromVesselStatic = function (data, position) {

        var a = data.dimension.a;
        var b = data.dimension.b;
        var c = data.dimension.c;
        var d = data.dimension.d;

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

        return rotatePoints([position.lat, position.lng], coords, data.direction);

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

    update = function (response) {

        // Adiciona ou atualiza
        for (var i = 0; i < response.length; i++) {

            var dataNew = response[i];
            var dataOld = null;
            var dataOldIndex = null;

            for (var j = 0; j < vessels.length; j++) {
                if (vessels[j].data.printMap.mmsi === dataNew.printMap.mmsi) {
                    dataOld = vessels[j];
                    dataOldIndex = j;
                    break;
                }
            }

            // Adiciona novo navio no mapa
            if (dataOld === null) {
                addVesselFromMap(dataNew);
                continue;
            }

            if (JSON.stringify(dataNew) === JSON.stringify(dataOld.data)) {
                continue;
            }

            // Atualiza o navio no mapa
            var position = L.latLng(dataNew.printMap.lat, dataNew.printMap.lng);
            var coords = [];
            if (dataNew.printMap.proportionalMap) {
                coords = getCoordsFromVessel(dataNew.printMap, position);
            } else {
                coords = getCoordsFromVesselStatic(dataNew.printMap, position);
            }

            // atualiza o polígono
            dataOld.polygon.setLatLngs(coords);
            dataOld.polygon.setStyle({color: dataNew.printMap.color,
                fillColor: dataNew.printMap.color,
                fillOpacity: dataNew.printMap.opacity});

            // atualiza o polyline
            var latLngsBoat = [];
            dataOld.polyline.setLatLngs(latLngsBoat);

            // atualiza o círculo
            dataOld.circle.setLatLng(position);
            dataOld.circle.setStyle({color: dataNew.printMap.color});
            setInfoTooltipVessel(dataNew, dataOld.circle);

            // atualiza o círculo de zoom
            dataOld.circle_zoom.setLatLng(position);
            dataOld.circle_zoom.setStyle({color: dataNew.printMap.color,
                fillColor: dataNew.printMap.color});
            setInfoTooltipVessel(dataNew, dataOld.circle_zoom);

            // atualiza info
            dataOld.tooltipNameVessel.setLatLng(position);
            dataOld.tooltipNameVessel.setContent(dataNew.vessel === null ? " " : dataNew.vessel.name);
            dataOld.tooltipMoreVessel.setLatLng(position);
            dataOld.tooltipMoreVessel.setContent(dataNew.printMap.velocity + " kn" + " / " + dataNew.printMap.direction + " º");

            // atualiza o layer
            if (dataOld.data.printMap.codType !== dataNew.printMap.codType) {
                var layerNew = dataOld.layer;
                removeLayer(getParentLayerVesselFromTypeVessel(dataOld.layer), dataOld.layer);
                addLayerVesselFromTypeVessel(dataNew, layerNew);
            }

            // alterar objeto da lista
            dataOld.data = dataNew;
            vesselRefreshZoom(vessels[dataOldIndex], map.getZoom());

        }


        // remove as embarcações
        for (var i = vessels.length - 1; i >= 0; --i) {

            var dataOld = vessels[i];
            var flag = false;

            for (var j = 0; j < response.length; j++) {
                var dataNew = response[j];
                if (dataOld.data.printMap.mmsi === dataNew.printMap.mmsi) {
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                removeLayer(dataOld.layer, dataOld.polygon);
                removeLayer(dataOld.layer, dataOld.polyline);
                removeLayer(dataOld.layer, dataOld.circle);
                removeLayer(dataOld.layer, dataOld.circle_zoom);
                removeLayer(dataOld.layer, dataOld.tooltipNameVessel);
                removeLayer(infoVessels[0].layer, dataOld.tooltipNameVessel);
                removeLayer(dataOld.layer, dataOld.tooltipMoreVessel);
                removeLayer(infoVessels[1].layer, dataOld.tooltipMoreVessel);
                removeLayer(getParentLayerVesselFromTypeVessel(dataOld.layer), dataOld.layer);
                vessels.splice(i, 1);
            }

        }

    };

    getParentLayerVesselFromTypeVessel = function (layer) {
        for (var i = 0; i < typeVessels.length; i++) {
            var layerOld = typeVessels[i].layer.getLayer(layer._leaflet_id);
            if (layerOld === undefined || layerOld === null) {
                continue;
            }
            return typeVessels[i].layer;
        }
        return null;
    };

    addLayerVesselFromTypeVessel = function (data, layer) {
        for (var i = 0; i < typeVessels.length; i++) {
            if (typeVessels[i].type === data.printMap.codType) {
                typeVessels[i].layer.addLayer(layer);
                break;
            }
        }
    };

    removeLayer = function (parentLayer, layer) {
        map.removeLayer(layer);
        layer.remove();
        layer.removeFrom(map);
        parentLayer.removeLayer(layer);
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

    this.loadPlaybackAis = function () {

        var url = new URL(window.location.href);
        var start = url.searchParams.get("start");
        var end = url.searchParams.get("end");

        if (start !== null && end !== null) {

            var startDateMilliseconds = new Date(convertDate(start)).getTime();
            var endDateMilliseconds = new Date(convertDate(end)).getTime();

            $("#status-search-data-playback").text("Buscando as informações, aguarde ...");
            PF('search-data-playback').show();

            $.ajax({
                type: 'GET',
                async: true,
                url: '/sismar/api/ais/playback',
                data: 'startDate=' + startDateMilliseconds + '&endDate=' + endDateMilliseconds,
                success: function (response) {

                    var initial = response.initial;
                    var history = response.history;

                    $("#status-search-data-playback").text("Montando os frames de exibição, aguarde ...");

                    dataPlayback = getListPlayBack(initial, history, startDateMilliseconds, endDateMilliseconds);

                    PF('search-data-playback').hide();

                    if (dataPlayback.length === 0) {
                        lb_qtd_records.text("0 registros");
                        sld_navegation.val(0);
                        sld_navegation.attr('min', 0);
                        sld_navegation.attr('max', 0);
                        setDataPlayback([], "");
                    } else {
                        lb_qtd_records.text(dataPlayback.length + " registros");
                        sld_navegation.attr('min', 1);
                        sld_navegation.attr('max', dataPlayback.length);
                        setDataPlayback(dataPlayback[0].vessels, dataPlayback[0].date);
                        panel_controls_playback.css("display", "block");
                    }

                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    dataPlayback = [];
                    lb_qtd_records.text("0 registros");
                    sld_navegation.val(0);
                    sld_navegation.attr('min', 0);
                    sld_navegation.attr('max', 0);
                    setDataPlayback([], "");

                    PF('search-data-playback').hide();

                },
                dataType: 'json'
            });




        }

    };


    containsMmsi = function (list, mmsi) {
        for (var i = 0; i < list.length; i++) {
            if (list[i].printMap.mmsi === mmsi) {
                return true;
            }
        }
        return false;
    };

    getHistoryPlayBack = function (date, historyOld, historyNew) {

        var historyPlayBack = [];

        for (var i = 0; i < historyNew.length; i++) {
            if (!containsMmsi(historyPlayBack, historyNew[i].printMap.mmsi)) {
                historyPlayBack.push(historyNew[i]);
            }
        }

        for (var i = 0; i < historyOld.length; i++) {
            var a = historyOld[i];

            var end = moment(a.date);
            var now = moment(date);
            var duration = moment.duration(now.diff(end));
            var minutes = duration.asMinutes();

            if (minutes <= 180 && !containsMmsi(historyPlayBack, a.printMap.mmsi)) {
                historyPlayBack.push(a);
            }

        }

        return historyPlayBack;
    };

    getAisPlaybackByDate = function (listPlayBack, date) {
        for (var i = 0; i < listPlayBack.length; i++) {
            if (listPlayBack[i].date === date) {
                return listPlayBack[i];
            }
        }
        return null;
    };

    enriquecer = function (listPlayback, start, end) {

        var list = [];

        while (start <= end) {

            var aisPlayback = getAisPlaybackByDate(listPlayback, start);

            if (aisPlayback === null) {
                if (list.length === 0) {
                    aisPlayback = {"date": start, "vessels": []};
                } else {
                    aisPlayback = {"date": start, "vessels": list[list.length - 1].vessels};
                }
            }

            list.push(aisPlayback);

            start += 1000;

        }

        return list;

    };

    getListPlayBack = function (listInitial, history, start, end) {

        var listPlayBack = [];

        if (history.length === 0) {
            return enriquecer(listPlayBack, start, end);
        }

        var date = null;
        var listAisNew = [];

        for (var i = 0; i < history.length; i++) {

            var ais = history[i];

            if (date === null) {
                listAisNew.push(ais);
                date = ais.date;
                continue;
            }

            if (ais.date === date) {
                listAisNew.push(ais);

                if (i === history.length - 1) {
                    // pronto
                    var historyOld = null;
                    if (listPlayBack.length === 0) {
                        historyOld = listInitial;
                    } else {
                        historyOld = listPlayBack[listPlayBack.length - 1].vessels;
                    }

                    var newHistoryPlayback = getHistoryPlayBack(date,
                            historyOld, listAisNew);
                    var aisPlayBack = {"date": date, "vessels": newHistoryPlayback};
                    listPlayBack.push(aisPlayBack);

                    continue;
                }

                date = ais.date;
                continue;
            }

            if (ais.date > date) {
                // pronto                                     

                var historyOld = null;

                if (listPlayBack.length === 0) {
                    historyOld = listInitial;
                } else {
                    historyOld = listPlayBack[listPlayBack.length - 1].vessels;
                }

                var newHistoryPlayback = getHistoryPlayBack(date,
                        historyOld, listAisNew);

                var aisPlayBack = {"date": date, "vessels": newHistoryPlayback};
                listPlayBack.push(aisPlayBack);

                listAisNew = [];
                listAisNew.push(ais);
                date = ais.date;

            }

        }

        return enriquecer(listPlayBack, start, end);

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

    setContentWeatherSuccess = function (e, response) {
        var content = getContentWeather(response);
        e.bindPopup(content, {className: "leaflet_popup_weather"});
        e.openPopup();
    };

    getContentWeather = function (response) {
        var content = '<div style="width: 400px;" >' +
                '<div class="col-xs-12 info-main-weather not-padding">' +
                '<div class="col-xs-6 col-md-6 icon-weather" style="padding-right: 0px;">' +
                '<img src="/sismar/faces/javax.faces.resource/img/climatempo/' + response.icon + '.jpg">' +
                '</div>' +
                '<div class="col-xs-6 col-md-6" style="padding-left: 0;" >' +
                '<div class="col-xs-12 temperature-weather" >' +
                response.temperature + 'º' +
                '</div>' +
                '<div class="col-xs-12 condition-weather" >' +
                response.condition +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="col-xs-12 info-others-weather not-padding" >' +
                '<div class="col-xs-3 not-padding" >' +
                '<div class="col-xs-12 title-others-weather not-padding" >' +
                'Sensação' +
                '</div>' +
                '<div class="col-xs-12 value-others-weather not-padding" >' +
                response.sensation + ' º' +
                '</div>' +
                '</div>' +
                '<div class="col-xs-3 not-padding" >' +
                '<div class="col-xs-12 title-others-weather not-padding" >' +
                'Umidade' +
                '</div>' +
                '<div class="col-xs-12 value-others-weather not-padding" >' +
                response.humidity + ' %' +
                '</div>' +
                '</div>' +
                '<div class="col-xs-3 not-padding" >' +
                '<div class="col-xs-12 title-others-weather not-padding" >' +
                'Pressão' +
                '</div>' +
                '<div class="col-xs-12 value-others-weather not-padding" >' +
                response.pressure + ' hPA' +
                '</div>' +
                '</div>' +
                '<div class="col-xs-3 not-padding" >' +
                '<div class="col-xs-12 title-others-weather not-padding" >' +
                'Vento' +
                '</div>' +
                '<div class="col-xs-12 value-others-weather not-padding" >' +
                response.wind_velocity + ' Km/h' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="col-xs-12 date-update-weather not-padding" >' +
                'Fonte: Clima tempo' +
                '</div>' +
                '</div>';
        return content;
    };

    setContentVesselSuccess = function (e, response) {
        var content = "";
        if (!isNullValueJson(response.vessel)) {
            content = getContentVessel(response);
        } else {
            content = getContentVesselUnknown(response);
        }
        e.bindPopup(content, {className: "leaflet_popup_info_vessel"});
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

    eventClickOpenPeriodTrack = function (mmsi) {
        $("#form-open-period-track input[name='mmsi']").val(mmsi);
        var m1 = moment();
        var m2 = moment();
        m2.subtract({hours: 24});
        $("#form-open-period-track input[name='start']").val(m2.format('YYYY-MM-DDTH:mm'));
        $("#form-open-period-track input[name='end']").val(m1.format('YYYY-MM-DDTH:mm'));
    };

    getContentVessel = function (response) {

        var content = '<div class="container-popup-vessel">' +
                '<div class="row">' +
                '<div class="container-popup-title col-xs-12 col-md-12">' +
                '<div class="dropdown" style="float: left;">' +
                '<button class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" style="color: white; padding-left: 0px !important; padding-top: 9px;">' +
                '<i class="glyphicon glyphicon-menu-hamburger"></i></button>' +
                '<ul class="dropdown-menu">' +
                '<li>' +
                "<a class=\"button-open-period-track\" href=\"#\" class=\"ui-commandlink ui-widget\" onclick=\"eventClickOpenPeriodTrack(" + response.printMap.mmsi + ");PF('open-period-track').show();PrimeFaces.ab({s:'button-open-period-track'});return false;\">" +
                '<i class="glyphicon glyphicon-search"></i>' +
                'Track (Rastro)' +
                '</a>' +
                '</li>' +
                '</ul>' +
                '</div>' +
                '<p class="container-popup-name">' + getValueFromContent(response.vessel.name, "", true, "DESCONHECIDO") + '</p>' +
                '<p class="container-popup-type" style="padding-left: 30px;">' +
                '<b>IMO:</b> ' + getValueFromContent(response.vessel.imo) + ' ' +
                '<b>Categoria:</b> ' + getValueFromContent(response.vessel.type) +
                '</p>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-img col-xs-12 col-md-12">' +
                (isNullValueJson(response.vessel.image) ?
                        '<img id="image-vessel-popup" src="/sismar/faces/javax.faces.resource/img/sem_imagem.png" width="100%" height="200px"></img>' :
                        '<img id="image-vessel-popup" src="' + response.vessel.image + '" width="100%" height="200px"></img>') +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-info col-xs-12 col-md-12">' +
                '<div class="row">' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Dimensão:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.vessel.dimension) + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Velocidade:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.printMap.velocity, " kn") + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Curso:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.printMap.direction, "º") + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Calado:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.printMap.draught, "m") + '</p>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-info-card col-xs-6 col-md-6">' +
                '<p class="container-popup-info-card-title">Destino:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.printMap.destination) + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-6 col-md-6">' +
                '<p class="container-popup-info-card-title">Estado:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.printMap.state) + '</p>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-footer col-xs-12 col-md-12">' +
                '<p><b>Recebido:</b> ' + getValueFromContent(response.printMap.message) + '</p>' +
                '<p><b>Latitude:</b> ' + getValueFromContent(response.printMap.lat) + ' <b>Longitude:</b> ' + getValueFromContent(response.printMap.lng) + '</p>' +
                '<p><b>MMSI:</b> ' + getValueFromContent(response.printMap.mmsi) + '</p>' +
                '</div>' +
                '</div>' +
                '</div>';

        return content;

    };

    getContentVesselUnknown = function (response) {

        var content = '<div class="container-popup-vessel">' +
                '<div class="row">' +
                '<div class="container-popup-title col-xs-12 col-md-12">' +
                '<div class="dropdown" style="float: left;">' +
                '<button class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" style="color: white; padding-left: 0px !important; padding-top: 9px;">' +
                '<i class="glyphicon glyphicon-menu-hamburger"></i></button>' +
                '<ul class="dropdown-menu">' +
                '<li>' +
                "<a class=\"button-open-period-track\" href=\"#\" class=\"ui-commandlink ui-widget\" onclick=\"eventClickOpenPeriodTrack(" + response.printMap.mmsi + ");PF('open-period-track').show();PrimeFaces.ab({s:'button-open-period-track'});return false;\">" +
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
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.printMap.velocity, " kn") + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Curso:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.printMap.direction, "º") + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Calado:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.printMap.draught, "m") + '</p>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-info-card col-xs-6 col-md-6">' +
                '<p class="container-popup-info-card-title">Destino:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.printMap.destination) + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-6 col-md-6">' +
                '<p class="container-popup-info-card-title">Estado:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.printMap.state) + '</p>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-footer col-xs-12 col-md-12">' +
                '<p><b>Recebido:</b> ' + getValueFromContent(response.printMap.message) + '</p>' +
                '<p><b>Latitude:</b> ' + getValueFromContent(response.printMap.lat) + ' <b>Longitude:</b> ' + getValueFromContent(response.printMap.lng) + '</p>' +
                '<p><b>MMSI:</b> ' + getValueFromContent(response.printMap.mmsi) + '</p>' +
                '</div>' +
                '</div>' +
                '</div>';

        return content;

    };

    setDataPlayback = function (vessels, date) {
        update(vessels);
        lb_date_data.text(moment(date).format("DD/MM/YYYY HH:mm:ss"));
        sld_navegation.val(index_data + 1);
    };

    restartRefreshData = function (sleep_frames) {
        clearInterval(refreshIntervalDataId);
        refreshIntervalDataId = setInterval(function () {

            if (index_data === dataPlayback.length) {
                start_playback = !start_playback;
                bt_start.text("Iniciar");
                clearInterval(refreshIntervalDataId);
            } else {
                setDataPlayback(dataPlayback[index_data].vessels, dataPlayback[index_data].date);
                moduleAtracacao.updateData(dataPlayback[index_data].date);
                index_data = index_data + 1;
                sld_navegation.val(index_data + 1);
            }

        }, sleep_frames);
    };

    addControlPlayback = function () {

        bt_start = $("#bt-start");
        bt_end = $("#bt-end");
        sld_navegation = $("#sld-navegation");
        lb_date_data = $("#lb-date-data");
        lb_qtd_records = $("#lb-qtd-records");
        chk_velocity = $("#chk-velocity");
        panel_controls_playback = $(".controls-playback");

        bt_start.click(function (e) {

            start_playback = !start_playback;
            if (!start_playback) {
                bt_start.text("Iniciar");
                clearInterval(refreshIntervalDataId);
            } else {
                bt_start.text("Parar");
                if (index_data >= dataPlayback.length - 1) {
                    index_data = 0;
                }
                restartRefreshData(sleep_frames);
            }

        });

        bt_end.click(function () {
            start_playback = false;
            bt_start.text("Iniciar");
            clearInterval(refreshIntervalDataId);
            index_data = 0;
            setDataPlayback(dataPlayback[0].vessels, dataPlayback[0].date);
        });

        sld_navegation.change(function () {
            var val = parseInt($(this).val());
            index_data = val - 1;
        });

        chk_velocity.change(function () {
            var val = parseInt($(this).val());
            sleep_frames = 1000 / val;
            if (start_playback) {
                restartRefreshData(sleep_frames);
            }
        });

    }
    ;
}
;
