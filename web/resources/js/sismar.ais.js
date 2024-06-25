/* global L, moment, PF */

function Sismar() {
}

Sismar.ais = function () {

    // Constantes
    // Tipos de navios
    const NAVIOS_CARGA = 1;
    const PETROLEIROS = 2;
    const PASSAGEIROS = 3;
    const REBOCADOR_EMBARCACOES_ESPECIAIS = 4;
    const EMBARCACAO_DESCONHECIDA = 5;
    const EMBARCACAO_NAO_ESPECIFICADA = 6;

    const iconBerco1 = new L.Icon({
        iconUrl: '/sismar/faces/javax.faces.resource/img/icon_berco_marker.png',
        iconSize: [20, 27]
    });
    const iconBerco2 = new L.Icon({
        iconUrl: '/sismar/faces/javax.faces.resource/img/icon_berco_marker.png',
        iconSize: [16, 22]
    });
    const iconBerco3 = new L.Icon({
        iconUrl: '/sismar/faces/javax.faces.resource/img/icon_berco_marker.png',
        iconSize: [12, 17]
    });

    // Variáveis
    var idMap;
    var vessels = [];
    var map;
    var typeMaps;
    var nauticalCharts;
    var centerDefault;
    var typeVessels;
    var infoVessels;
    var ativeButtonRoute = false;
    var activeButtonAisInfo = false;
    var routeLayer;

    var layersMap;
    var bercosMap;
    var dataStartup;

    this.initialize = function (id) {

        idMap = id;

        // Coleta os dados iniciais
        getDataForStartup(false);

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
        loadLayersMap();

        // Berços
        loadBercosMap();

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

        addClickButtonCloseInfoAis();
        addClickButtonRefreshFila();
        addClickOpenMoreInfoVessel();

    };

    addClickButtonCloseInfoAis = function () {
        $("#ais-information-header-close").click(function () {
            if (!activeButtonAisInfo) {
                $("#map").removeClass("map-ais-information-invisible");
                $("#ais-information").removeClass("ais-information-invisible");
                $("#map").addClass("map-ais-information-visible");
                $("#ais-information").addClass("ais-information-visible");
                activeButtonAisInfo = true;
            } else {
                $("#map").removeClass("map-ais-information-visible");
                $("#ais-information").removeClass("ais-information-visible");
                $("#map").addClass("map-ais-information-invisible");
                $("#ais-information").addClass("ais-information-invisible");
                activeButtonAisInfo = false;
            }
        });
    };

    addClickButtonRefreshFila = function () {
        $("#ais-information-header-refresh").click(function () {
            loadFilaVessels();
        });
    };

    addControlsMap = function () {

        var functionalities = JSON.parse(localStorage.getItem("user_logged_in")).functionalities;

        if (functionalities.indexOf("ais_search") > -1) {

            // pesquisar
            var aisSearchButton = L.Control.extend({

                options: {
                    position: 'topleft'
                            //control position - allowed: 'topleft', 'topright', 'bottomleft', 'bottomright'
                },

                onAdd: function () {
                    var container = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom');

                    container.type = "button";
                    container.title = "Pesquisar por embarcações";
                    container.style.backgroundImage = "url(/sismar/faces/javax.faces.resource/img/search.png)";
                    container.style.backgroundSize = "18px 18px";
                    container.style.backgroundRepeat = "no-repeat";
                    container.style.backgroundPosition = "center";
                    container.style.cursor = "pointer";
                    container.style.backgroundColor = 'white';
                    container.style.width = '35px';
                    container.style.height = '35px';

                    container.onclick = function () {
                        PF('open-search-vessels').show();
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

            map.addControl(new aisSearchButton());

        }

        if (functionalities.indexOf("ais_playback") > -1) {

            var playbackButton = L.Control.extend({

                options: {
                    position: 'topleft'
                            //control position - allowed: 'topleft', 'topright', 'bottomleft', 'bottomright'
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

        var routeButton = L.Control.extend({

            options: {
                position: 'topleft'
                        //control position - allowed: 'topleft', 'topright', 'bottomleft', 'bottomright'
            },

            onAdd: function () {
                var container = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom');

                container.type = "button";
                container.title = "Rota/Trajetória";
                container.style.backgroundImage = "url(/sismar/faces/javax.faces.resource/img/route.png)";
                container.style.backgroundSize = "18px 18px";
                container.style.backgroundRepeat = "no-repeat";
                container.style.backgroundPosition = "center";
                container.style.cursor = "pointer";
                container.style.backgroundColor = 'white';
                container.style.width = '35px';
                container.style.height = '35px';

                container.onclick = function () {

                    if (!ativeButtonRoute) {

                        var markerPositionA = map.getCenter();
                        var markerPositionB = addMetersInCoordinate(markerPositionA, 700, 00);

                        var blueIcon = new L.Icon({
                            iconUrl: '/sismar/faces/javax.faces.resource/img/marker-icon-2x-blue.png',
                            shadowUrl: '/sismar/faces/javax.faces.resource/img/marker-shadow.png',
                            iconSize: [25, 41],
                            iconAnchor: [12, 41],
                            popupAnchor: [1, -34],
                            shadowSize: [41, 41]
                        });

                        var redIcon = new L.Icon({
                            iconUrl: '/sismar/faces/javax.faces.resource/img/marker-icon-2x-red.png',
                            shadowUrl: '/sismar/faces/javax.faces.resource/img/marker-shadow.png',
                            iconSize: [25, 41],
                            iconAnchor: [12, 41],
                            popupAnchor: [1, -34],
                            shadowSize: [41, 41]
                        });

                        var markerA = L.marker(markerPositionA, {icon: blueIcon, draggable: 'true'});
                        var markerB = L.marker(markerPositionB, {icon: redIcon, draggable: 'true'});

                        var latLngsRoute = [markerPositionA, markerPositionB];
                        var polyline = L.polyline(latLngsRoute, {
                            color: 'blue',
                            weight: 3,
                            opacity: 0.9});

                        markerA.on('drag', function (event) {
                            var marker = event.target;
                            var positionA = marker.getLatLng();
                            var positionB = markerB.getLatLng();
                            polyline.setLatLngs([positionA, markerB.getLatLng()]);
                            changeTooltipContent(positionA, positionB, marker, "A", "B");

                        });

                        markerB.on('drag', function (event) {
                            var marker = event.target;
                            var positionB = marker.getLatLng();
                            var positionA = markerA.getLatLng();
                            polyline.setLatLngs([positionB, markerA.getLatLng()]);
                            changeTooltipContent(positionB, positionA, marker, "B", "A");
                        });

                        markerA.on('mouseover', function (ev) {
                            var marker = ev.target;
                            var positionA = marker.getLatLng();
                            var positionB = markerB.getLatLng();
                            changeTooltipContent(positionA, positionB, marker, "A", "B");
                        });

                        markerB.on('mouseover', function (ev) {
                            var marker = ev.target;
                            var positionB = marker.getLatLng();
                            var positionA = markerA.getLatLng();
                            changeTooltipContent(positionB, positionA, marker, "B", "A");
                        });

                        routeLayer = new L.layerGroup([polyline, markerA, markerB]);
                        map.addLayer(routeLayer);

                        ativeButtonRoute = true;

                    } else {
                        removeLayer(map, routeLayer);
                        ativeButtonRoute = false;
                    }

                };

                changeTooltipContent = function (positionStart, positionEnd, marker, start, end) {


                    var distanceKm = getDistancePositions(positionStart.lat, positionStart.lng,
                            positionEnd.lat, positionEnd.lng);

                    var distanceKmText = getValueDistanceToText(distanceKm);

                    var distanceMilhasText = distanceKm * 0.539957;
                    distanceMilhasText = distanceMilhasText.toFixed(2);

                    var latInicialDms = converterDecimalParaDMS(positionStart.lat, true);
                    var lonInicialDms = converterDecimalParaDMS(positionStart.lng, false);
                    
                    var latFinalDms = converterDecimalParaDMS(positionEnd.lat, true);
                    var lonFinalDms = converterDecimalParaDMS(positionEnd.lng, false);

                    var text = "<b>Posição inicial:</b> " + start + "<br><u>Lat:</u> " + latInicialDms + " ~ " + positionStart.lat.toFixed(6) + " <br> <u>Lng:</u> " + lonInicialDms + " ~ " + positionStart.lng.toFixed(6);
                    text += "<br><br><b>Posição final:</b> " + end + "<br><u>Lat:</u> " + latFinalDms + " ~ " + positionEnd.lat.toFixed(6) + " <br> <u>Lng:</u> " + lonFinalDms + " ~ " + positionEnd.lng.toFixed(6);
                    text += "<br><br><b>Distância:</b> " + distanceKmText + " ~ " + distanceMilhasText + " nmi";

                    if (marker.getTooltip() === null || marker.getTooltip() === undefined) {
                        marker.bindTooltip(text).openTooltip();
                    } else {
                        marker.setTooltipContent(text);
                    }

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

        map.addControl(new routeButton());


        var aisInfoFilaButton = L.Control.extend({

            options: {
                position: 'topleft'
                        //control position - allowed: 'topleft', 'topright', 'bottomleft', 'bottomright'
            },

            onAdd: function () {
                var container = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom');

                container.type = "button";
                container.title = "Fila - Programação";
                container.style.backgroundImage = "url(/sismar/faces/javax.faces.resource/img/ais_info.png)";
                container.style.backgroundSize = "18px 18px";
                container.style.backgroundRepeat = "no-repeat";
                container.style.backgroundPosition = "center";
                container.style.cursor = "pointer";
                container.style.backgroundColor = 'white';
                container.style.width = '35px';
                container.style.height = '35px';

                container.onclick = function () {
                    if (!activeButtonAisInfo) {
                        $("#map").removeClass("map-ais-information-invisible");
                        $("#ais-information").removeClass("ais-information-invisible");
                        $("#map").addClass("map-ais-information-visible");
                        $("#ais-information").addClass("ais-information-visible");
                        activeButtonAisInfo = true;
                        loadFilaVessels();
                    } else {
                        $("#map").removeClass("map-ais-information-visible");
                        $("#ais-information").removeClass("ais-information-visible");
                        $("#map").addClass("map-ais-information-invisible");
                        $("#ais-information").addClass("ais-information-invisible");
                        activeButtonAisInfo = false;
                    }
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

        map.addControl(new aisInfoFilaButton());

    };

    this.addButtonToMap = function (title, image, func) {

        var aisButtonMap = L.Control.extend({

            options: {
                position: 'topleft'
            },

            onAdd: function () {
                var container = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom');

                container.type = "button";
                container.title = title;
                container.style.backgroundImage = "url(/sismar/faces/javax.faces.resource/img/" + image;
                container.style.backgroundSize = "18px 18px";
                container.style.backgroundRepeat = "no-repeat";
                container.style.backgroundPosition = "center";
                container.style.cursor = "pointer";
                container.style.backgroundColor = 'white';
                container.style.width = '35px';
                container.style.height = '35px';

                container.onclick = function () {
                    func();
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

        map.addControl(new aisButtonMap());

    };

    loadFilaVessels = function () {
        var table_fila = $('#table-fila').DataTable();
        table_fila.clear().draw();
        $.ajax({
            type: 'GET',
            url: '/sismar/api/fila/vessels',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Basic bGVvOjEyMw==");
                $("#ais-information-content-loader").removeClass("ais-information-content-loader-invisible");
                $("#ais-information-content-loader").addClass("ais-information-content-loader-visible");
            },
            success: function (response) {
                var table_fila = $('#table-fila').DataTable();
                for (var i = 0; i < response.fila.length; i++) {
                    var vessel = response.fila[i];
                    table_fila.row.add({
                        "id": vessel.order,
                        "ordem": vessel.order,
                        "chegada": vessel.arrival_date,
                        "navio": "<span class='table-fila-link-vessel' onclick='openInfoVessel(" + vessel.mmsi + ");'>" + (vessel.name_vessel === null ? "" : vessel.name_vessel) + "</span><br><b>IMO: </b>" + (vessel.imo_vessel === null ? "" : vessel.imo_vessel),
                        "permanencia": vessel.permanence,
                        "external": vessel.external}).draw();
                }
                $("#ais-information-content-loader").removeClass("ais-information-content-loader-visible");
                $("#ais-information-content-loader").addClass("ais-information-content-loader-invisible");
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $("#ais-information-content-loader").removeClass("ais-information-content-loader-visible");
                $("#ais-information-content-loader").addClass("ais-information-content-loader-invisible");
            },
            dataType: 'json'
        });
    };

    addClickOpenMoreInfoVessel = function () {
        var table = $('#table-fila').DataTable();
        $('#table-fila tbody').on('click', 'td.details-control', function () {
            var tr = $(this).closest('tr');
            var row = table.row(tr);
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
            } else {
                row.child(contentMoreInfoVessel(row.data())).show();
                tr.addClass('shown');
            }
        });

    };

    contentMoreInfoVessel = function (d) {
        if (d.external === null) {
            return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' +
                    '<tr>' +
                    '<td>Nenhuma informação adicional</td>' +
                    '</tr>' +
                    '</table>';
        }
        return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' +
                '<tr>' +
                '<td><b>Terminal:</b></td>' +
                '<td>' + d.external.terminal + '</td>' +
                '</tr>' +
                '<tr>' +
                '<td><b>Local:</b></td>' +
                '<td>' + (d.external.local === null ? '' : d.external.local) + '</td>' +
                '</tr>' +
                '<tr>' +
                '<td><b>Prioridade:</b></td>' +
                '<td>' + d.external.priority + '</td>' +
                '</tr>' +
                '<tr>' +
                '<td><b>Carga:</b></td>' +
                '<td>' + (d.external.charge === null ? '' : d.external.charge) + '</td>' +
                '</tr>' +
                '<tr>' +
                '<td><b>Evento:</b></td>' +
                '<td>' + (d.external.event === null ? '' : d.external.event) + '</td>' +
                '</tr>' +
                '<tr>' +
                '<td><b>Previsão:</b></td>' +
                '<td>' + (d.external.programming_date === null ? '' : d.external.programming_date) + '</td>' +
                '</tr>' +
                '</table>';
    };

    mobilecheck = function () {
        var check = false;
        (function (a) {
            if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4)))
                check = true;
        })(navigator.userAgent || navigator.vendor || window.opera);
        return check;
    };

    openInfoVesselBySearch = function (mmsi, obj) {

        var vessel = JSON.parse(obj);

        if (vessel.dataFromExternal) {
            update([vessel]);
        }

        for (var i = 0; i < vessels.length; i++) {

            vessel = vessels[i];

            if (vessel.data.mmsi !== mmsi) {
                continue;
            }

            var latlng, layer;

            if (map.hasLayer(vessel.circle)) {
                latlng = vessel.circle.getLatLng();
            } else {
                latlng = vessel.circle_zoom.getLatLng();
            }

            map.fitBounds([latlng]);
            map.setZoom(12);

            if (map.hasLayer(vessel.circle)) {
                layer = vessel.circle;
            } else {
                layer = vessel.circle_zoom;
            }

            layer.fireEvent('click', {
                latlng: latlng,
                layerPoint: map.latLngToLayerPoint(latlng),
                containerPoint: map.latLngToContainerPoint(latlng)
            });

            var toolTip = layer.getTooltip();
            if (toolTip) {
                map.closeTooltip(toolTip);
            }

            break;
        }

    };

    this.setLatLngAndZoom = function (lat, lng, zoom) {
        map.fitBounds([L.latLng(lat, lng)]);
        map.setZoom(zoom);
        console.log("change latlng and zoom from map");
    };

    this.openInfoVesselBySearch = function (mmsi, obj) {
        openInfoVesselBySearch(mmsi, obj);
    };


    openInfoVessel = function (mmsi) {

        for (var i = 0; i < vessels.length; i++) {

            var vessel = vessels[i];

            if (vessel.data.mmsi !== mmsi) {
                continue;
            }

            if (mobilecheck()) {
                $("#map").removeClass("map-ais-information-visible");
                $("#ais-information").removeClass("ais-information-visible");
                $("#map").addClass("map-ais-information-invisible");
                $("#ais-information").addClass("ais-information-invisible");
                activeButtonAisInfo = false;
            }

            var latlng, layer;

            if (map.hasLayer(vessel.circle)) {
                latlng = vessel.circle.getLatLng();
            } else {
                latlng = vessel.circle_zoom.getLatLng();
            }

            map.fitBounds([latlng]);
            map.setZoom(12);

            if (map.hasLayer(vessel.circle)) {
                layer = vessel.circle;
            } else {
                layer = vessel.circle_zoom;
            }

            layer.fireEvent('click', {
                latlng: latlng,
                layerPoint: map.latLngToLayerPoint(latlng),
                containerPoint: map.latLngToContainerPoint(latlng)
            });

            var toolTip = layer.getTooltip();
            if (toolTip) {
                map.closeTooltip(toolTip);
            }

            break;
        }
    };

    getDistancePositions = function (lat1, lon1, lat2, lon2) {
        var R = 6371; // Radius of the earth in km
        var dLat = (lat2 - lat1) * Math.PI / 180;  // deg2rad below
        var dLon = (lon2 - lon1) * Math.PI / 180;
        var a =
                0.5 - Math.cos(dLat) / 2 +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                (1 - Math.cos(dLon)) / 2;

        return R * 2 * Math.asin(Math.sqrt(a));
    };


    getValueDistanceToText = function (distance) {
        var newDistance = distance; // em km
        var medida = "km";
        if (newDistance < 1) {
            newDistance = newDistance * 1000; // em metro
            medida = "m";
        }
        return newDistance.toFixed(2) + " " + medida;
    };

    function converterDecimalParaDMS(coordenada, isLatitude) {
        
        const absCoordenada = Math.abs(coordenada);
        const graus = Math.floor(absCoordenada);
        const minutosDecimais = (absCoordenada - graus) * 60;
        const minutos = Math.floor(minutosDecimais);
        const segundos = (minutosDecimais - minutos) * 60;

        let direcao;
        if (isLatitude) {
            direcao = coordenada >= 0 ? 'N' : 'S';
        } else {
            direcao = coordenada >= 0 ? 'E' : 'W';
        }

        return `${direcao}${graus}° ${minutos}' ${segundos.toFixed(2)}"`;
    }

    getLayersActive = function () {
        var layers = [getTypeMapPrimary()];
        for (var i = 0; i < typeVessels.length; i++) {
            var typeVessel = typeVessels[i];
            layers.push(typeVessel.layer);
        }
        for (var i = 0; i < bercosMap.length; i++) {
            layers.push(bercosMap[i].layer);
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
        var portoSantosParteSul = new L.ImageOverlay('/sismar/faces/javax.faces.resource/img/porto_santos_parte_sul.png',
                [[-23.95000000, -46.39305556], [-24.08416667, -46.27750000]]);

        var portoSantosParteNorte = new L.ImageOverlay('/sismar/faces/javax.faces.resource/img/porto_santos_parte_norte.png',
                [[-23.866452, -46.399899], [-23.966843, -46.266389]], {opacity: 1.0});

        var proxPortoSaoSebastiao = new L.ImageOverlay('/sismar/faces/javax.faces.resource/img/proximidades_porto_sao_sebastiao.png',
                [[-23.621249, -45.753250], [-24.120435, -44.971161]], {opacity: 1.0});

        nauticalCharts = [{name: "Parte Sul - Porto de Santos", layer: portoSantosParteSul},
            {name: "Parte Norte - Porto de Santos", layer: portoSantosParteNorte},
            {name: "Proximidades - Porto de São Sebastião", layer: proxPortoSaoSebastiao}];
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
        for (var i = 0; i < dataStartup.layers.length; i++) {

            var layer = dataStartup.layers[i];
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

    loadBercosMap = function () {
        bercosMap = [];
        for (var i = 0; i < dataStartup.bercos.length; i++) {

            var berco = dataStartup.bercos[i];

            if (!berco.lat || !berco.lng) {
                continue;
            }

            var layerGroup = new L.layerGroup();

            var pos = L.latLng(berco.lat, berco.lng);

            var text = "<div style='display: flex !important;'> <div><img src='/sismar/faces/javax.faces.resource/" + berco.image + "' width='210px' height='130px' /></div> <div style='margin-left: 15px;'> <b style='font-size: 16px !important;'>" + berco.name + "</b>";

            if (berco.cabecos) {
                text += "<br/>" + "<b>Cabeços:</b> " + berco.cabecos;
            }
            if (berco.length) {
                text += "<br/>" + "<b>Comprimento:</b> " + berco.length;
            }
            if (berco.type) {
                text += "<br/>" + "<b>Tipo de cais:</b> " + berco.type;
            }
            if (berco.depth) {
                text += "<br/>" + "<b>Profundidade:</b> " + berco.depth;
            }
            if (berco.low_sea_tide) {
                text += "<br/>" + "<b>Calado (Baixa maré):</b> " + berco.low_sea_tide;
            }
            if (berco.prea_sea_tide) {
                text += "<br/>" + "<b>Calado (Prea maré):</b> " + berco.prea_sea_tide;
            }
            if (berco.company) {
                text += "<br/>" + "<b>Empresa:</b> " + berco.company;
            }
            if (berco.merchandise) {
                text += "<br/>" + "<b>Mercadoria:</b> " + berco.merchandise;
            }

            text += "</div></div>";

            var marker = L.marker(pos, {icon: iconBerco1, draggable: 'false'}).
                    bindTooltip(text, {sticky: true, direction: 'right'});

            layerGroup.addLayer(marker);

            bercosMap.push({name: berco.name, layer: layerGroup});

        }

    };

    getWeatherLayer = function () {

        var children = [];
        children.push({label: "São sebastião", layer: getMarkerWeatherLayer([-23.806215, -45.396158], "São Sebastião", 3482)});
        children.push({label: "Santos", layer: getMarkerWeatherLayer([-23.973289, -46.337644], "Santos", 3675)});
        children.push({label: "Guarujá", layer: getMarkerWeatherLayer([-23.995327, -46.245677], "Guarujá", 4234)});
        children.push({label: "Bertioga", layer: getMarkerWeatherLayer([-23.838026, -46.119304], "Bertioga", 4374)});
        children.push({label: "Praia Grande", layer: getMarkerWeatherLayer([-24.016530, -46.423408], "Praia Grande", 3589)});
        children.push({label: "São Vicente", layer: getMarkerWeatherLayer([-23.963265, -46.416964], "São Vicente", 3589)});

        var layer = {
            label: "<span class='leaflet-layerstree-header-title'>Meteorologia (Clima tempo)</span>",
            children: children
        };
        return layer;

    };

    isRefreshWeatherData = function (cityCod) {
        var value = $.cookie("meteorologia_" + cityCod);
        if (value === undefined || value === "null") {
            return true;
        }
        value = jQuery.parseJSON(value);
        var startDate = moment(value.date);
        var endDate = moment();
        var duration = moment.duration(endDate.diff(startDate)).minutes();
        if (duration > 10) {
            return true;
        }
        return false;
    };

    getMarkerWeatherLayer = function (position, cityName, cityCod) {

        var iconWeather = L.icon({
            iconUrl: '/sismar/faces/javax.faces.resource/img/icone_weather.png',
            iconSize: [25, 25]
        });

        var markerWeather = L.marker(position, {icon: iconWeather});

        markerWeather.addTo(map);

        markerWeather.bindTooltip("Meteorologia<br/>" + cityName, {
            permanent: true,
            direction: "right",
            className: "leaflet_label_weather"}).openTooltip();

        markerWeather.on('click', function (e) {

            if (markerWeather.getPopup() !== undefined) {
                markerWeather.unbindPopup();
            }

            var popup_loading = L.popup({className: "leaflet_popup_loading"}).
                    setLatLng(e.latlng).
                    setContent(getContentLoading()).
                    openOn(map);

            $.ajax({
                type: 'GET',
                url: '/sismar/api/weather/' + cityCod,
                data: '',
                success: function (data) {
                    popup_loading.closePopup();
                    removeLayer(map, popup_loading);
                    if (data.hasOwnProperty('error') && data.error) {
                        setContentError(markerWeather);
                    } else {
                        setContentWeatherSuccess(markerWeather, data);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    setContentError(markerWeather);
                },
                dataType: 'json'
            });

        });

        return markerWeather;

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

    getBercosLayer = function () {
        var children = [];
        for (var i = 0; i < bercosMap.length; i++) {
            var berco = bercosMap[i];
            children.push({label: berco.name, layer: berco.layer});
        }
        var berco = {
            label: "<span class='leaflet-layerstree-header-title'>Berços de atracação</span>",
            children: children
        };
        return berco;
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
        var overlayLayer = [getNauticalChartLayer(), getInfoVesselsLayer(),
            getTypeVesselsLayer(), getWeatherLayer(), getLayersFromLayer(), getBercosLayer()];
        return overlayLayer;
    };

    bercosRefreshZoom = function (zoom) {
        for (var i = 0; i < bercosMap.length; i++) {
            var layer = bercosMap[i].layer;
            layer.eachLayer(function (obj) {
                if (zoom < 13) {
                    obj.remove();
                } else {
                    layer.addLayer(obj);
                    if (zoom < 14) {
                        obj.setIcon(iconBerco3);
                    } else if (zoom < 16) {
                        obj.setIcon(iconBerco2);
                    } else {
                        obj.setIcon(iconBerco1);
                    }
                }
            });
        }
    };

    setControlsMap = function () {

        map.on('zoom', function () {
            var zoom = map.getZoom();
            bercosRefreshZoom(zoom);
            for (var i = 0; i < vessels.length; i++) {
                vesselRefreshZoom(vessels[i], zoom);
            }
        });

        map.on('overlayadd', function (e) {
            for (var i = 0; i < typeVessels.length; i++) {
                if (e.layer === typeVessels[i].layer) {
                    var zoom = map.getZoom();
                    for (var j = 0; j < vessels.length; j++) {
                        if (vessels[j].data.codType === typeVessels[i].type) {
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

        var position = L.latLng(data.lat, data.lng); // Coordenadas da embarcação em Latitude e Longitude
        var coords = [];

        // Se o tamanho da embarcação for grande e proporcional ao zoom do mapa
        if (data.proportionalMap) {
            coords = getCoordsFromVessel(data, position);
        }

        // Polígono que representa o navio no mapa
        var polygon = L.polygon([coords],
                {color: data.color, // Cor da borda
                    opacity: 0.9, // Transparência da borda 
                    weight: 1, // Expressura da borda
                    fillColor: data.color, // Cor de fundo
                    fillOpacity: data.opacity}); // Transparência da cor de fundo
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

        // Círculo que presenta o ponto de instalação da antena do ais
        var circle = L.circle(position, (data.proportionalMap ? 5 : 0), {
            color: data.color, // Cor da borda
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
        tooltipNameVessel.setContent((data.vessel === undefined ? "" : data.vessel.name));

        var tooltipMoreVessel = L.tooltip({
            permanent: true,
            direction: "right",
            className: "leaflet_label leaflet_label_more_vessel"}, circle).setLatLng(position);
        tooltipMoreVessel.setContent(data.velocity + " kn" + " / " + data.direction + " º");

        infoVessels[0].layer.addLayer(tooltipNameVessel);
        infoVessels[1].layer.addLayer(tooltipMoreVessel);

        // Círculo que representa a embarcação quando diminuído o zoom do mapa
        var circle_zoom = L.circleMarker(position, {
            color: data.color, // Cor da borda
            radius: 5,
            opacity: 0.9, // Transparência da borda
            weight: 1, // Expressura da borda
            fillColor: data.color, // Cor de fundo
            fillOpacity: 0.5});
        setPopupVesselFromLayer(data, circle_zoom);
        setInfoTooltipVessel(data, circle_zoom);

        var layer = new L.layerGroup([polygon, polyline, circle, circle_zoom, tooltipNameVessel, tooltipMoreVessel]);

        // adiciona o layer com a embarcação
        for (var i = 0; i < typeVessels.length; i++) {
            if (typeVessels[i].type === data.codType) {
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


    rewriteUrlParams = function (urlDefault, paramsUrl, redirect) {

        var host = window.location.protocol + "//" +
                window.location.hostname + ":" +
                window.location.port + "/faces";

        var url = new URL((urlDefault ? host + urlDefault : window.location.href));

        var search = url.search;
        var params = new URLSearchParams(search);

        // deleta todos os parâmetros
        var keys = [...params.keys()];
        for (var i in keys) {
            params.delete(keys[i]);
        }

        // adiciona os novos parâmetros
        for (var i = 0; i < paramsUrl.length; i++) {
            var paramNew = paramsUrl[i];
            params.append(paramNew.key, paramNew.value);
        }

        url.search = params.toString();

        var newUrl = url.toString();

        if (redirect) {
            window.location.href = newUrl;
        } else {
            window.history.pushState('', '', newUrl);
        }

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
                url: '/sismar/api/vessel',
                data: 'mmsi=' + data.mmsi,
                success: function (response) {
                    popup_loading.closePopup();
                    removeLayer(map, popup_loading);
                    setContentVesselSuccess(layer, response);
                    rewriteUrlParams(null, [{"key": "mmsi", "value": data.mmsi}], false);
                    layer.bindPopup().on("popupclose", function (e) {
                        rewriteUrlParams(null, [], false);
                    });
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    setContentError(layer);
                    layer.bindPopup().on("popupclose", function (e) {
                        rewriteUrlParams(null, [], false);
                    });
                },
                dataType: 'json'
            });

        });

    };

    setInfoTooltipVessel = function (data, circle) {

        var text = "<b style='font-size:'18px';'>" + (data.vessel === undefined ? "NOME INDISPÍVEL" : data.vessel.name) + "</b>";
        if (data.vessel !== undefined) {
            text += "<br/><b>Imo: </b>" + data.vessel.imo;
        }
        text += "<br/><b>Velocidade: </b>" + data.velocity + " kn ";
        text += "<b>Curso: </b>" + data.direction + " º";
        text += "<br/><b>Destino: </b>" + data.destination;

        if (circle.getTooltip() === null || circle.getTooltip() === undefined) {
            circle.bindTooltip(text).openTooltip();
        } else {
            circle.setTooltipContent(text);
        }

    };

    vesselRefreshZoom = function (vesselMap, zoom) {

        // Se o tamanho do navio não deve ser proporcional ao mapa
        if (!vesselMap.data.proportionalMap) {
            var position = L.latLng(vesselMap.data.lat, vesselMap.data.lng);
            var coords = getCoordsFromVesselStatic(vesselMap.data, position);
            vesselMap.polygon.setLatLngs(coords);
        }

        // Zoom acima de 14, mostra todos os navios
        if (zoom > 14) {
            vesselMap.layer.addLayer(vesselMap.polygon);
            vesselMap.layer.addLayer(vesselMap.circle);
            vesselMap.layer.addLayer(vesselMap.polyline);
            vesselMap.circle_zoom.remove();
        } else if (zoom > 13 && vesselMap.data.proportionalMap) { // Zoom acima de 12, mostra os navios com o tamanho grande
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
        if (zoom > 14 && !vesselMap.data.proportionalMap) {
            vesselMap.circle.setRadius(1);
        } else if (!vesselMap.data.proportionalMap) {
            vesselMap.circle.setRadius(0);
        }

        // Para embarcações proporcional
        if (zoom > 14 && vesselMap.data.proportionalMap) {
            vesselMap.circle.setRadius(7);
        } else if (zoom > 12 && vesselMap.data.proportionalMap) {
            vesselMap.circle.setRadius(6);
        } else if (vesselMap.data.proportionalMap) {
            vesselMap.circle.setRadius(6);
        }

        // Zoom acima de 13 -> Exibir nome do navio
        if (zoom > 14) {
            if (map.hasLayer(infoVessels[0].layer) && map.hasLayer(getLayerByTypeVessel(vesselMap.data.codType))) {
                vesselMap.tooltipNameVessel.addTo(map);
            } else {
                vesselMap.tooltipNameVessel.remove();
            }
            if (map.hasLayer(infoVessels[1].layer) && map.hasLayer(getLayerByTypeVessel(vesselMap.data.codType))) {
                vesselMap.tooltipMoreVessel.addTo(map);
            } else {
                vesselMap.tooltipMoreVessel.remove();
            }

        } else if (zoom > 13 && vesselMap.data.proportionalMap) {
            if (map.hasLayer(infoVessels[0].layer) && map.hasLayer(getLayerByTypeVessel(vesselMap.data.codType))) {
                vesselMap.tooltipNameVessel.addTo(map);
            } else {
                vesselMap.tooltipNameVessel.remove();
            }

            if (map.hasLayer(infoVessels[1].layer) && map.hasLayer(getLayerByTypeVessel(vesselMap.data.codType))) {
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
                if (vessels[j].data.mmsi === dataNew.mmsi) {
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
            var position = L.latLng(dataNew.lat, dataNew.lng);
            var coords = [];
            if (dataNew.proportionalMap) {
                coords = getCoordsFromVessel(dataNew, position);
            } else {
                coords = getCoordsFromVesselStatic(dataNew, position);
            }

            // atualiza o polígono
            dataOld.polygon.setLatLngs(coords);
            dataOld.polygon.setStyle({color: dataNew.color,
                fillColor: dataNew.color,
                fillOpacity: dataNew.opacity});

            // atualiza o polyline
            var latLngsBoat = [];
            for (var i = 0; i < dataNew.trail.length; i++) {
                var trail = dataNew.trail[i];
                latLngsBoat.push([trail.lat, trail.lng]);
            }
            dataOld.polyline.setLatLngs(latLngsBoat);

            // atualiza o círculo
            dataOld.circle.setLatLng(position);
            dataOld.circle.setStyle({color: dataNew.color});
            setInfoTooltipVessel(dataNew, dataOld.circle);

            // atualiza o círculo de zoom
            dataOld.circle_zoom.setLatLng(position);
            dataOld.circle_zoom.setStyle({color: dataNew.color,
                fillColor: dataNew.color});
            setInfoTooltipVessel(dataNew, dataOld.circle_zoom);

            // atualiza info
            dataOld.tooltipNameVessel.setLatLng(position);
            dataOld.tooltipNameVessel.setContent(dataNew.vessel === undefined ? " " : dataNew.vessel.name);
            dataOld.tooltipMoreVessel.setLatLng(position);
            dataOld.tooltipMoreVessel.setContent(dataNew.velocity + " kn" + " / " + dataNew.direction + " º");

            // atualiza o layer
            if (dataOld.data.codType !== dataNew.codType) {
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

            if (dataOld.data.dataFromExternal) {
                continue;
            }

            var flag = false;

            for (var j = 0; j < response.length; j++) {
                var dataNew = response[j];
                if (dataOld.data.mmsi === dataNew.mmsi) {
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

    addLayerVesselFromTypeVessel = function (vessel, layer) {
        for (var i = 0; i < typeVessels.length; i++) {
            if (typeVessels[i].type === vessel.codType) {
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

    updateListVessels = function (async) {

        var xmlreq = getResquestAjax();
        xmlreq.open("GET", "/sismar/api/ais/all", async);

        xmlreq.onreadystatechange = function () {
            if (xmlreq.readyState === 4 && xmlreq.status === 200) {
                var json = jQuery.parseJSON(xmlreq.responseText);
                update(json);
            }
        };

        xmlreq.send(null);

    };

    getDataForStartup = function (async) {
        var xmlreq = getResquestAjax();
        xmlreq.open("GET", "/sismar/api/ais/startup", async);
        xmlreq.setRequestHeader("Authorization", "Basic bGVvOjEyMw==");

        xmlreq.onreadystatechange = function () {
            if (xmlreq.readyState === 4 && xmlreq.status === 200) {
                var json = jQuery.parseJSON(xmlreq.responseText);
                if (json.error) {
                    dataStartup = {"layers": [], "bercos": []};
                } else {
                    dataStartup = json;
                }
            } else {
                dataStartup = {"layers": [], "bercos": []};
            }
        };

        xmlreq.send(null);
    };

    this.fitBoundsShowAllVessels = function () {
        if (vessels !== null && vessels.length > 0) {
            var latLngsBoat = [];
            for (var i = 0; i < vessels.length; i++) {
                var data = vessels[i].data;
                latLngsBoat.push([data.lat, data.lng]);
            }
            map.fitBounds(latLngsBoat);
        }
    };

    this.updateVessels = function (async) {
        updateListVessels(async);
//        updateListVessels(false);
//        fitBoundsShowAllVessels();
//        setInterval(function () {
//            updateListVessels(true);
//        }, 2000);

    };

    this.execByParamsUrl = function () {
        var url = new URL(window.location.href);

        var search = url.search;
        var params = new URLSearchParams(search);

        if (params.has('mmsi')) {

            var mmsi = parseInt(params.get('mmsi'));

            $.ajax({
                type: 'GET',
                url: '/sismar/api/ais/vessel/' + mmsi,
                async: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", "Basic bGVvOjEyMw==");
                },
                success: function (response) {
                    openInfoVesselBySearch(mmsi, JSON.stringify(response));
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

    getContentVessel = function (response) {

        var functionalities = JSON.parse(localStorage.getItem("user_logged_in")).functionalities;

        var content = '<div class="container-popup-vessel">' +
                '<div class="row" style="padding: 0px !important;">' +
                '<div class="container-popup-title col-xs-12 col-md-12">' +
                '<div class="dropdown" style="float: left;">' +
                '<button class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" style="color: white; padding-left: 0px !important; padding-top: 9px;">' +
                '<i class="glyphicon glyphicon-menu-hamburger"></i></button>' +
                '<ul class="dropdown-menu">';

        if (functionalities.indexOf("ais_track") > -1) {

            content += '<li>' +
                    "<a class=\"button-open-period-track\" href=\"#\" class=\"ui-commandlink ui-widget\" onclick=\"$('#form-select-period-track input[name=mmsi]').val(" + response.last_ais_record.mmsi + "); PF('dialog-select-period-track').show(); return false;\">" +
                    '<i class="glyphicon glyphicon-search"></i>' +
                    'Track (Rastro)' +
                    '</a>' +
                    '</li>';

        }

        content += '</ul>' +
                '</div>' +
                '<p class="container-popup-name">' + getValueFromContent(response.vessel.name, "", true, "DESCONHECIDO") + '</p>' +
                '<p class="container-popup-type" style="padding-left: 30px;">' +
                '<b>IMO:</b> ' + getValueFromContent(response.vessel.imo) + ' ' +
                '<b>Categoria:</b> ' + getValueFromContent(response.vessel.type) +
                '</p>' +
                '</div>' +
                '</div>' +
                '<div class="row" style="padding: 0px !important;">' +
                '<div class="container-popup-img col-xs-12 col-md-12">' +
                (isNullValueJson(response.vessel.image) ?
                        '<img id="image-vessel-popup" src="/sismar/faces/javax.faces.resource/img/sem_imagem.png" width="100%" height="200px"></img>' :
                        '<img id="image-vessel-popup" src="' + response.vessel.image + '" width="100%" height="200px"></img>') +
                '</div>' +
                '</div>' +
                '<div class="row" style="padding: 0px !important;">' +
                '<div class="container-popup-info col-xs-12 col-md-12" style="padding: 0px !important;">' +
                '<div class="row">' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Dimensão:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.vessel.dimension) + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Velocidade:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.last_ais_record.velocity, " kn") + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Curso:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.last_ais_record.direction, "º") + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Calado:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.last_ais_record.draught, "m") + '</p>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-info-card col-xs-5 col-md-5">' +
                '<p class="container-popup-info-card-title">Destino:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.last_ais_record.destination) + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-4 col-md-4">' +
                '<p class="container-popup-info-card-title">Estado:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.last_ais_record.state) + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Call Sign:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.vessel.call_sign) + '</p>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="row" style="padding: 0px !important;">' +
                '<div class="container-popup-footer col-xs-12 col-md-12">' +
                '<p><b>Recebido:</b> ' + getValueFromContent(response.last_ais_record.message) + '</p>' +
                '<p style="display: flex;"><b>Latitude:</b><span style="line-height: 1.5;margin-top: -8px;padding-left: 5px;padding-right: 10px;"> ' + getValueFromContent(response.last_ais_record.lat_graus) + ' <br>' + getValueFromContent(response.last_ais_record.lat) + '</span><b>Longitude:</b><span style="line-height: 1.5;margin-top: -8px;padding-left: 5px;padding-right: 10px;"> ' + getValueFromContent(response.last_ais_record.lng_graus) + '<br>' + getValueFromContent(response.last_ais_record.lng) + '</span></p>' +
                '<p><b>MMSI:</b> ' + getValueFromContent(response.last_ais_record.mmsi) + '</p>' +
                '</div>' +
                '</div>' +
                '</div>';

        return content;

    };

    getContentVesselUnknown = function (response) {

        var functionalities = JSON.parse(localStorage.getItem("user_logged_in")).functionalities;

        var content = '<div class="container-popup-vessel">' +
                '<div class="row">' +
                '<div class="container-popup-title col-xs-12 col-md-12">' +
                '<div class="dropdown" style="float: left;">' +
                '<button class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" style="color: white; padding-left: 0px !important; padding-top: 9px;">' +
                '<i class="glyphicon glyphicon-menu-hamburger"></i></button>' +
                '<ul class="dropdown-menu">';

        if (functionalities.indexOf("ais_track") > -1) {
            content += '<li>' +
                    "<a class=\"button-open-period-track\" href=\"#\" class=\"ui-commandlink ui-widget\" onclick=\"$('#form-select-period-track input[name=mmsi]').val(" + response.last_ais_record.mmsi + "); PF('dialog-select-period-track').show(); return false;\">" +
                    '<i class="glyphicon glyphicon-search"></i>' +
                    'Track (Rastro)' +
                    '</a>' +
                    '</li>';
        }

        content += '</ul>' +
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
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.last_ais_record.velocity, " kn") + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Curso:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.last_ais_record.direction, "º") + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Calado:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.last_ais_record.draught, "m") + '</p>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-info-card col-xs-5 col-md-5">' +
                '<p class="container-popup-info-card-title">Destino:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.last_ais_record.destination) + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-4 col-md-4">' +
                '<p class="container-popup-info-card-title">Estado:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.last_ais_record.state) + '</p>' +
                '</div>' +
                '<div class="container-popup-info-card col-xs-3 col-md-3">' +
                '<p class="container-popup-info-card-title">Call Sign:</p>' +
                '<p class="container-popup-info-card-value">' + getValueFromContent(response.vessel.call_sign) + '</p>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="row">' +
                '<div class="container-popup-footer col-xs-12 col-md-12">' +
                '<p><b>Recebido:</b> ' + getValueFromContent(response.last_ais_record.message) + '</p>' +
                '<p style="display: flex;"><b>Latitude:</b><span style="line-height: 1.5;margin-top: -8px;padding-left: 5px;padding-right: 10px;"> ' + getValueFromContent(response.last_ais_record.lat_graus) + ' <br>' + getValueFromContent(response.last_ais_record.lat) + '</span><b>Longitude:</b><span style="line-height: 1.5;margin-top: -8px;padding-left: 5px;padding-right: 10px;"> ' + getValueFromContent(response.last_ais_record.lng_graus) + '<br>' + getValueFromContent(response.last_ais_record.lng) + '</span></p>' +
                '<p><b>MMSI:</b> ' + getValueFromContent(response.last_ais_record.mmsi) + '</p>' +
                '</div>' +
                '</div>' +
                '</div>';

        return content;

    };

}
;
