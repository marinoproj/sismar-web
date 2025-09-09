/* AisMap.js - ES Module */
export default class AisMap {
  // ====== CONFIG E ESTADO ======
  API_URL;
  containerId;
  onReady;

  // constantes
  CENTER = L.latLng(-24.05777, -46.362274);
  ZOOM = 10;
  UPDATE_INTERVAL_MS = 30000;
  POSITION_EPSILON_METERS = 5;

  DIRECTION_LINE_COLOR = "blue";
  DIRECTION_LINE_MIN_VELOCITY = 0.7; // kn
  DIRECTION_LINE_LENGTH_METERS = 100;

  ZOOM_TRIANGLE = 12;
  ZOOM_REAL_POLYGON = 14;
  ZOOM_DIRECTION_LINE = 14;
  TRIANGLE_BASE_PX = 15;
  TRIANGLE_HEIGHT_PX = 20;

  NAVIOS_CARGA = 1;
  PETROLEIROS = 2;
  PASSAGEIROS = 3;
  REBOCADOR_EMBARCACOES_ESPECIAIS = 4;
  EMBARCACAO_DESCONHECIDA = 5;
  EMBARCACAO_NAO_ESPECIFICADA = 6;

  // objetos leaflet / estado
  map = null;
  markerClusterGroup = null;
  regionLayerGroup = null;
  vesselsCache = new Map(); // mmsi -> { data, layerRefs, ... }
  updateTimer = null;

  typeMaps = [];
  typeVessels = [];
  vesselNameLayer = L.layerGroup();
  vesselInfoLayer = L.layerGroup();

  iconBerco = new L.Icon({
    iconUrl: "/sismar/faces/javax.faces.resource/img/icon_berco_marker.png",
    iconSize: [20, 27],
  });

  ativeButtonRoute = false;
  activeButtonAisMapProperties = false;
  fontSizeExtraLabel = 10;
  modalVesselInfo;

  // ====== CONSTRUTOR ======
  constructor({ containerId, apiUrl = "/", onReady = () => {} }) {
    this.containerId = containerId;
    this.API_URL = apiUrl;
    this.onReady = onReady;

    this.typeMaps = this.getTypeMaps();
    this.typeVessels = this.getTypeVessels();

    $("#mapproperties").draggable();
    L.DomEvent.disableClickPropagation(
      document.getElementById("mapproperties")
    );
    L.DomEvent.disableScrollPropagation(
      document.getElementById("mapproperties")
    );
  }

  // ====== API PÚBLICA ======
  async init() {
    this.initMap();
    await this.addControlLayers();
    this.addEventsMap();
    this.addControlsMap();
    //this.startAutoRefresh();
    this.onReady?.();
  }

  destroy() {
    this.stopAutoRefresh();
    this.vesselsCache.forEach((_, mmsi) => this.removeVesselFromMap(mmsi));
    if (this.map) {
      this.map.off();
      this.map.remove();
      this.map = null;
    }
  }

  startAutoRefresh() {
    if (this.updateTimer) clearInterval(this.updateTimer);
    this.updateTimer = setInterval(
      () => this.refreshAndProcess(),
      this.UPDATE_INTERVAL_MS
    );
    this.refreshAndProcess();
  }

  stopAutoRefresh() {
    if (this.updateTimer) {
      clearInterval(this.updateTimer);
      this.updateTimer = null;
    }
  }

  async refreshAndProcess(syncType = "default") {
    console.log("refreshAndProcess syncType = " + syncType);
    const all = await this.fetchAllVessels();
    this.processVesselsResponse(all);
  }

  /** Se quiser injetar sua própria lista (ex.: playback) */
  processList(list) {
    this.processVesselsResponse(list);
  }

  /** Para ajustar fonte dos labels via código */
  setFontSizeExtraLabel(value) {
    this.fontSizeExtraLabel = Number(value);
    document.querySelectorAll(".vessel-name-label div").forEach((el) => {
      el.style.setProperty("font-size", value + "px", "important");
    });
    document.querySelectorAll(".vessel-info-label div").forEach((el) => {
      el.style.setProperty("font-size", value + "px", "important");
      el.style.setProperty("margin-top", value - 5 + "px", "important");
    });
  }

  setModalVesselInfo(modal) {
    this.modalVesselInfo = modal;
  }

  addButtonToMap(title, image, onClick) {
    const ControlBtn = L.Control.extend({
      options: { position: "topleft" },
      onAdd: () => {
        const container = L.DomUtil.create(
          "div",
          "leaflet-bar leaflet-control leaflet-control-custom"
        );

        container.type = "button";
        container.title = title;
        container.style.backgroundImage =
          "url(/sismar/faces/javax.faces.resource/img/" + image + ")"; // <-- fechando corretamente
        container.style.backgroundSize = "18px 18px";
        container.style.backgroundRepeat = "no-repeat";
        container.style.backgroundPosition = "center";
        container.style.cursor = "pointer";
        container.style.backgroundColor = "white";
        container.style.width = "35px";
        container.style.height = "35px";

        container.onclick = () => onClick();

        container.onmouseover = () =>
          (container.style.backgroundColor = "#f4f4f4");
        container.onmouseout = () =>
          (container.style.backgroundColor = "white");

        // evita pan/zoom involuntário ao clicar no botão
        L.DomEvent.disableClickPropagation(container);
        L.DomEvent.disableScrollPropagation?.(container);

        return container;
      },
    });

    this.map.addControl(new ControlBtn());
  }

  getMap() {
    return this.map;
  }

  // ====== LAYERS TREE ======
  getBaseLayer() {
    const children = this.typeMaps.map((tm) => ({
      label: tm.name,
      layer: tm.layer,
    }));
    return {
      label: "<span class='leaflet-layerstree-header-title'>Mapa</span>",
      children,
    };
  }

  async addControlLayers() {
    const baseLayer = this.getBaseLayer();
    const dataStartup = await this.getLayersAndBercos();

    const overlayLayer = [
      this.getNauticalChartLayer(),
      this.getInfoVesselsLayer(),
      this.getTypeVesselsLayer(),
      this.getLayersLayer(dataStartup),
      this.getBercosLayers(dataStartup),
    ];

    L.control.layers.tree(baseLayer, overlayLayer).addTo(this.map);
  }

  getNauticalChartLayer() {
    const portoSantosParteSul = new L.ImageOverlay(
      "/sismar/faces/javax.faces.resource/img/porto_santos_parte_sul.png",
      [
        [-23.95, -46.39305556],
        [-24.08416667, -46.2775],
      ]
    );
    const portoSantosParteNorte = new L.ImageOverlay(
      "/sismar/faces/javax.faces.resource/img/porto_santos_parte_norte.png",
      [
        [-23.866452, -46.399899],
        [-23.966843, -46.266389],
      ],
      { opacity: 1.0 }
    );
    const proxPortoSaoSebastiao = new L.ImageOverlay(
      "/sismar/faces/javax.faces.resource/img/proximidades_porto_sao_sebastiao.png",
      [
        [-23.621249, -45.75325],
        [-24.120435, -44.971161],
      ],
      { opacity: 1.0 }
    );

    const nauticalCharts = [
      { name: "Parte Sul - Porto de Santos", layer: portoSantosParteSul },
      { name: "Parte Norte - Porto de Santos", layer: portoSantosParteNorte },
      {
        name: "Proximidades - Porto de São Sebastião",
        layer: proxPortoSaoSebastiao,
      },
    ];

    const children = nauticalCharts.map((nc) => ({
      label: nc.name,
      layer: nc.layer,
    }));
    return {
      label:
        "<span class='leaflet-layerstree-header-title'>Cartas Náuticas</span>",
      children,
    };
  }

  getTypeMaps() {
    const googleStreets = L.tileLayer(
      "http://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}",
      { maxZoom: 20, subdomains: ["mt0", "mt1", "mt2", "mt3"] }
    );
    const googleHybrid = L.tileLayer(
      "http://{s}.google.com/vt/lyrs=s,h&x={x}&y={y}&z={z}",
      { maxZoom: 20, subdomains: ["mt0", "mt1", "mt2", "mt3"] }
    );
    const googleSat = L.tileLayer(
      "http://{s}.google.com/vt/lyrs=s&x={x}&y={y}&z={z}",
      { maxZoom: 20, subdomains: ["mt0", "mt1", "mt2", "mt3"] }
    );
    const googleTerrain = L.tileLayer(
      "http://{s}.google.com/vt/lyrs=p&x={x}&y={y}&z={z}",
      { maxZoom: 20, subdomains: ["mt0", "mt1", "mt2", "mt3"] }
    );
    const grayMapWithLabels = L.tileLayer(
      "https://{s}.basemaps.cartocdn.com/rastertiles/light_all/{z}/{x}/{y}.png",
      { subdomains: "abcd", maxZoom: 19 }
    );
    const darkMapWithLabels = L.tileLayer(
      "https://{s}.basemaps.cartocdn.com/rastertiles/dark_all/{z}/{x}/{y}.png",
      { subdomains: "abcd", maxZoom: 19 }
    );

    const seaMapGroup = new L.layerGroup();
    const seaLayer = L.tileLayer(
      "https://tiles.openseamap.org/seamark/{z}/{x}/{y}.png",
      {}
    );
    const seaMap = L.tileLayer(
      "http://{s}.google.com/vt/lyrs=p&x={x}&y={y}&z={z}",
      { maxZoom: 20, subdomains: ["mt0", "mt1", "mt2", "mt3"] }
    );

    seaMapGroup.addLayer(seaMap);
    seaMapGroup.addLayer(seaLayer);

    return [
      { name: "Streets", primary: false, layer: googleStreets },
      { name: "Hybrid", primary: false, layer: googleHybrid },
      { name: "Satellite", primary: false, layer: googleSat },
      { name: "Terrain", primary: false, layer: googleTerrain },
      { name: "Gray Map", primary: false, layer: grayMapWithLabels },
      { name: "Dark Map", primary: false, layer: darkMapWithLabels },
      { name: "Sea", primary: true, layer: seaMapGroup },
    ];
  }

  getTypeMapPrimary() {
    const primary = this.typeMaps.find((t) => t.primary);
    return primary ? primary.layer : this.typeMaps[0].layer;
  }

  getTypeVessels() {
    return [
      {
        name: "Navios de carga <img src='/sismar/faces/javax.faces.resource/img/icone_navio_carga.png'/>",
        type: this.NAVIOS_CARGA,
        layer: new L.layerGroup(),
      },
      {
        name: "Petroleiros <img src='/sismar/faces/javax.faces.resource/img/icone_navio_petroleiro.png'/>",
        type: this.PETROLEIROS,
        layer: new L.layerGroup(),
      },
      {
        name: "Passageiros <img src='/sismar/faces/javax.faces.resource/img/icone_navio_passageiros.png'/>",
        type: this.PASSAGEIROS,
        layer: new L.layerGroup(),
      },
      {
        name: "Rebocadores e embarcações especiais <img src='/sismar/faces/javax.faces.resource/img/icone_rebocador.png'/>",
        type: this.REBOCADOR_EMBARCACOES_ESPECIAIS,
        layer: new L.layerGroup(),
      },
      {
        name: "Embarcação desconhecida <img src='/sismar/faces/javax.faces.resource/img/icone_navio_desconhecido.png'/>",
        type: this.EMBARCACAO_DESCONHECIDA,
        layer: new L.layerGroup(),
      },
      {
        name: "Embarcação não especificada <img src='/sismar/faces/javax.faces.resource/img/icone_navio_nao_especificada.png'/>",
        type: this.EMBARCACAO_NAO_ESPECIFICADA,
        layer: new L.layerGroup(),
      },
    ];
  }

  getTypeVesselsLayer() {
    const children = this.typeVessels.map((tv) => ({
      label: tv.name,
      layer: tv.layer,
    }));
    return {
      label:
        "<span class='leaflet-layerstree-header-title'>Tipos de Embarcações</span>",
      children,
    };
  }

  getBercosLayers(dataStartup) {
    const bercosMap = [];
    for (let i = 0; i < dataStartup.bercos.length; i++) {
      const berco = dataStartup.bercos[i];
      if (!berco.lat || !berco.lng) continue;

      const layerGroup = new L.layerGroup();
      const pos = L.latLng(berco.lat, berco.lng);

      let text =
        "<div style='display: flex !important;'> <div><img src='/sismar/faces/javax.faces.resource/" +
        berco.image +
        "' width='210px' height='130px' /></div> <div style='margin-left: 15px;'> <b style='font-size: 16px !important;'>" +
        berco.name +
        "</b>";
      if (berco.cabecos) text += "<br/><b>Cabeços:</b> " + berco.cabecos;
      if (berco.length) text += "<br/><b>Comprimento:</b> " + berco.length;
      if (berco.type) text += "<br/><b>Tipo de cais:</b> " + berco.type;
      if (berco.depth) text += "<br/><b>Profundidade:</b> " + berco.depth;
      if (berco.low_sea_tide)
        text += "<br/><b>Calado (Baixa maré):</b> " + berco.low_sea_tide;
      if (berco.prea_sea_tide)
        text += "<br/><b>Calado (Prea maré):</b> " + berco.prea_sea_tide;
      if (berco.company) text += "<br/><b>Empresa:</b> " + berco.company;
      if (berco.merchandise)
        text += "<br/><b>Mercadoria:</b> " + berco.merchandise;
      text += "</div></div>";

      const marker = L.marker(pos, {
        icon: this.iconBerco,
        draggable: false,
      }).bindTooltip(text, { sticky: true, direction: "right" });
      layerGroup.addLayer(marker);
      bercosMap.push({ name: berco.name, layer: layerGroup });
    }

    const children = bercosMap.map((b) => ({ label: b.name, layer: b.layer }));
    return {
      label:
        "<span class='leaflet-layerstree-header-title'>Berços de atracação</span>",
      children,
    };
  }

  getLayersLayer(dataStartup) {
    const layersMap = [];

    for (let i = 0; i < dataStartup.layers.length; i++) {
      const layerDef = dataStartup.layers[i];
      const layerGroup = new L.layerGroup();

      for (let j = 0; j < layerDef.poins.length; j++) {
        const poin = layerDef.poins[j];
        const coords = poin.coordinates.map((c) => [c.latitude, c.longitude]);
        const new_opacity = poin.opacity - (55.0 * poin.opacity) / 100.0;

        const polygon = L.polygon([coords], {
          color: poin.color,
          opacity: 0.9,
          weight: 1,
          fillColor: poin.color,
          fillOpacity: new_opacity,
        });

        const tooltipNamePoin = L.tooltip(
          { permanent: true, direction: "right", className: "leaflet_label" },
          polygon
        ).setLatLng(coords[0]);
        tooltipNamePoin.setContent(poin.name);

        layerGroup.addLayer(polygon);
        layerGroup.addLayer(tooltipNamePoin);
      }

      layersMap.push({ name: layerDef.name, layer: layerGroup });
    }

    const seaMap = L.tileLayer(
      "https://tiles.openseamap.org/seamark/{z}/{x}/{y}.png",
      {}
    );
    layersMap.push({ name: "Sea", layer: seaMap });

    const children = layersMap.map((l) => ({ label: l.name, layer: l.layer }));
    return {
      label: "<span class='leaflet-layerstree-header-title'>Layers</span>",
      children,
    };
  }

  getLayerGroupByType(codType) {
    const item = this.typeVessels.find((t) => t.type === codType);
    return item ? item.layer : null;
  }

  getInfoVesselsLayer() {
    const infoVessels = [
      { name: "Nome da embarcação", layer: this.vesselNameLayer },
      { name: "Velocidade e direção", layer: this.vesselInfoLayer },
    ];
    const children = infoVessels.map((iv) => ({
      label: iv.name,
      layer: iv.layer,
    }));
    return {
      label:
        "<span class='leaflet-layerstree-header-title'>Exibir Informações</span>",
      children,
    };
  }

  getLayersActive() {
    const layers = [this.getTypeMapPrimary()];
    for (const tv of this.typeVessels) layers.push(tv.layer);
    return layers;
  }

  // ====== GEO HELPERS ======
  metersToLatitudeDegrees(meters) {
    return meters / 111320;
  }
  metersToLongitudeDegrees(meters, lat) {
    return meters / (111320 * Math.cos((lat * Math.PI) / 180));
  }

  distanceMeters(lat1, lon1, lat2, lon2) {
    const R = 6371000,
      toRad = Math.PI / 180;
    const dLat = (lat2 - lat1) * toRad;
    const dLon = (lon2 - lon1) * toRad;
    const a =
      Math.sin(dLat / 2) ** 2 +
      Math.cos(lat1 * toRad) * Math.cos(lat2 * toRad) * Math.sin(dLon / 2) ** 2;
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
  }

  offsetMetersToLatLng(lat, lng, dx_east, dy_north) {
    const dLat = this.metersToLatitudeDegrees(dy_north);
    const dLng = this.metersToLongitudeDegrees(dx_east, lat);
    return [lat + dLat, lng + dLng];
  }

  // ====== DESENHO NAVIO ======
  buildShipPolygonPoints(lat, lng, A, B, C, D, bearingDeg) {
    const θ = (bearingDeg * Math.PI) / 180;
    const forward_e = Math.sin(θ);
    const forward_n = Math.cos(θ);

    const rightθ = ((bearingDeg + 90) * Math.PI) / 180;
    const leftθ = ((bearingDeg - 90) * Math.PI) / 180;
    const right_unit_e = Math.sin(rightθ),
      right_unit_n = Math.cos(rightθ);
    const left_unit_e = Math.sin(leftθ),
      left_unit_n = Math.cos(leftθ);

    const front_dx = A * forward_e,
      front_dy = A * forward_n;
    const back_dx = -B * forward_e,
      back_dy = -B * forward_n;
    const left_dx = C * left_unit_e,
      left_dy = C * left_unit_n;
    const right_dx = D * right_unit_e,
      right_dy = D * right_unit_n;

    const tip = this.offsetMetersToLatLng(lat, lng, front_dx, front_dy);

    const notchFactor = 0.3;
    const notch_dx = A * (1 - notchFactor) * forward_e;
    const notch_dy = A * (1 - notchFactor) * forward_n;

    const fl = this.offsetMetersToLatLng(
      lat,
      lng,
      notch_dx + left_dx,
      notch_dy + left_dy
    );
    const fr = this.offsetMetersToLatLng(
      lat,
      lng,
      notch_dx + right_dx,
      notch_dy + right_dy
    );
    const br = this.offsetMetersToLatLng(
      lat,
      lng,
      back_dx + right_dx,
      back_dy + right_dy
    );
    const bl = this.offsetMetersToLatLng(
      lat,
      lng,
      back_dx + left_dx,
      back_dy + left_dy
    );

    return { polygonPoints: [tip, fr, br, bl, fl], tip };
  }

  buildDirectionLine(lat, lng, bearingDeg, lengthMeters = 50) {
    const θ = (bearingDeg * Math.PI) / 180;
    const dx = Math.sin(θ) * lengthMeters;
    const dy = Math.cos(θ) * lengthMeters;
    const head = this.offsetMetersToLatLng(lat, lng, dx, dy);
    return [[lat, lng], head];
  }

  createFixedTriangleMarker(
    lat,
    lng,
    directionDeg,
    color = "#3388ff",
    opacity = 0.8
  ) {
    const widthPx = this.TRiangleBasePx ?? this.TRIANGLE_BASE_PX;
    const heightPx = this.TRIANGLE_HEIGHT_PX;
    const centerX = widthPx / 2;
    const centerY = heightPx / 2;
    const points = `${centerX},0 ${widthPx},${heightPx} 0,${heightPx}`;

    const svg = `
      <svg width="${widthPx}" height="${heightPx}" viewBox="0 0 ${widthPx} ${heightPx}" xmlns="http://www.w3.org/2000/svg"
           style="transform: rotate(${directionDeg}deg);">
        <polygon points="${points}" fill="${color}" fill-opacity="${opacity}" stroke="${color}" stroke-width="1"/>
        <circle cx="${centerX}" cy="${centerY}" r="2" fill="#ffffff" stroke="${color}" stroke-width="1"/>
      </svg>
    `;

    const icon = L.divIcon({
      html: svg,
      className: "triangle-icon",
      iconSize: [widthPx, heightPx],
      iconAnchor: [centerX, centerY],
    });

    return L.marker([lat, lng], { icon });
  }

  addExtraLabels(vesselData) {
    const { lat, lng } = vesselData;
    const typeLayer = this.getLayerGroupByType(vesselData.codType);
    if (!typeLayer || !this.map.hasLayer(typeLayer)) return;

    if (this.map.hasLayer(this.vesselNameLayer)) {
      const name = vesselData.vessel?.name || "Desconhecido";
      const nameLabel = L.marker([lat, lng], {
        mmsi: vesselData.mmsi,
        icon: L.divIcon({
          className: "vessel-name-label",
          html: `<div style="white-space:nowrap; font-size:${this.fontSizeExtraLabel}px; font-weight:bold; color:black; text-shadow:1px 1px 2px white;">${name}</div>`,
          iconAnchor: [-10, 0],
        }),
      });
      this.vesselNameLayer.addLayer(nameLabel);
    }

    if (this.map.hasLayer(this.vesselInfoLayer)) {
      const velocity =
        vesselData.velocity !== undefined ? `${vesselData.velocity} kn` : "-";
      const direction =
        vesselData.direction !== undefined ? `${vesselData.direction} º` : "-";
      const infoLabel = L.marker([lat, lng], {
        mmsi: vesselData.mmsi,
        icon: L.divIcon({
          className: "vessel-info-label",
          html: `<div style="white-space:nowrap; font-size:${this.fontSizeExtraLabel}px; color:#333; text-shadow:1px 1px 2px white;">${velocity} • ${direction}</div>`,
          iconAnchor: [-10, this.fontSizeExtraLabel * -1],
        }),
      });
      this.vesselInfoLayer.addLayer(infoLabel);
    }
  }

  clearExtraLabels(mmsi) {
    this.vesselNameLayer.eachLayer((l) => {
      if (l.options.mmsi === mmsi) this.vesselNameLayer.removeLayer(l);
    });
    this.vesselInfoLayer.eachLayer((l) => {
      if (l.options.mmsi === mmsi) this.vesselInfoLayer.removeLayer(l);
    });
  }

  createVesselGraphics(v) {
    const { lat, lng } = v;
    const color = v.color || "#3388ff";
    const opacity = v.opacity !== undefined ? v.opacity : 0.6;
    const zoom = this.map.getZoom();

    const group = L.layerGroup();
    let shape = null,
      antennaPoint = null,
      directionLine = null;

    if (zoom < this.ZOOM_TRIANGLE)
      return { group, shape, antennaPoint, directionLine };

    if (zoom === 12 || zoom === 13) {
      shape = this.createFixedTriangleMarker(
        lat,
        lng,
        v.direction || 0,
        color,
        opacity
      );
      group.addLayer(shape);
      this.attachVesselInteractions(shape, v, lat, lng);
    }

    if (zoom >= this.ZOOM_REAL_POLYGON) {
      if (v.proportionalMap) {
        const { polygonPoints } = this.buildShipPolygonPoints(
          lat,
          lng,
          v.dimension.a || 0,
          v.dimension.b || 0,
          v.dimension.c || 0,
          v.dimension.d || 0,
          v.direction || 0
        );
        shape = L.polygon(polygonPoints, {
          color,
          fillColor: color,
          fillOpacity: opacity,
          weight: 1,
          pane: "vesselPolygons",
        });
        group.addLayer(shape);
        this.attachVesselInteractions(shape, v, lat, lng);

        antennaPoint = L.circleMarker([lat, lng], {
          radius: 3,
          color,
          weight: 1,
          fillColor: "#ffffff",
          fillOpacity: 1,
          pane: "vesselAntennas",
        });
        group.addLayer(antennaPoint);
        this.attachVesselInteractions(antennaPoint, v, lat, lng);
      } else {
        shape = this.createFixedTriangleMarker(
          lat,
          lng,
          v.direction || 0,
          color,
          opacity
        );
        group.addLayer(shape);
        this.attachVesselInteractions(shape, v, lat, lng);
      }
    }

    if (
      zoom >= this.ZOOM_DIRECTION_LINE &&
      v.velocity > this.DIRECTION_LINE_MIN_VELOCITY
    ) {
      let start = [lat, lng];
      if (v.proportionalMap && shape instanceof L.Polygon) {
        const { tip } = this.buildShipPolygonPoints(
          lat,
          lng,
          v.dimension.a || 0,
          v.dimension.b || 0,
          v.dimension.c || 0,
          v.dimension.d || 0,
          v.direction || 0
        );
        start = tip;
      }
      const dircoords = this.buildDirectionLine(
        start[0],
        start[1],
        v.direction || 0,
        this.DIRECTION_LINE_LENGTH_METERS
      );
      directionLine = L.polyline(dircoords, {
        color: this.DIRECTION_LINE_COLOR,
        weight: 1.5,
        opacity: 0.8,
      });
      group.addLayer(directionLine);
    }

    this.addExtraLabels(v);
    return { group, shape, antennaPoint, directionLine };
  }

  updateVesselGraphics(graphics, v) {
    if (!graphics) return;
    const { lat, lng } = v;
    const color = v.color || "#3388ff";
    const opacity = v.opacity !== undefined ? v.opacity : 0.6;
    const zoom = this.map.getZoom();

    if (graphics.directionLine) {
      try {
        graphics.group.removeLayer(graphics.directionLine);
      } catch {}
      graphics.directionLine = null;      
    }

    if (zoom < this.ZOOM_TRIANGLE) {
      graphics.group.clearLayers();
      this.clearExtraLabels(v.mmsi);
      graphics.shape = null;
      graphics.antennaPoint = null;
      return;
    }

    graphics.group.clearLayers();

    if (zoom === 12 || zoom === 13) {
      graphics.shape = this.createFixedTriangleMarker(
        lat,
        lng,
        v.direction || 0,
        color,
        opacity
      );
      graphics.group.addLayer(graphics.shape);
      this.attachVesselInteractions(graphics.shape, v, lat, lng);
      graphics.antennaPoint = null;
    }

    if (zoom >= this.ZOOM_REAL_POLYGON) {
      if (v.proportionalMap) {
        const { polygonPoints } = this.buildShipPolygonPoints(
          lat,
          lng,
          v.dimension.a || 0,
          v.dimension.b || 0,
          v.dimension.c || 0,
          v.dimension.d || 0,
          v.direction || 0
        );

        graphics.shape = L.polygon(polygonPoints, {
          color,
          fillColor: color,
          fillOpacity: opacity,
          weight: 1,
          pane: "vesselPolygons",
        });
        graphics.antennaPoint = L.circleMarker([lat, lng], {
          radius: 3,
          color,
          weight: 1,
          fillColor: "#ffffff",
          fillOpacity: 1,
          pane: "vesselAntennas",
        });

        graphics.group.addLayer(graphics.shape);
        graphics.group.addLayer(graphics.antennaPoint);

        this.attachVesselInteractions(graphics.shape, v, lat, lng);
        this.attachVesselInteractions(graphics.antennaPoint, v, lat, lng);
      } else {
        graphics.shape = this.createFixedTriangleMarker(
          lat,
          lng,
          v.direction || 0,
          color,
          opacity
        );
        graphics.group.addLayer(graphics.shape);
        graphics.antennaPoint = null;
      }
    }

    if (
      zoom >= this.ZOOM_DIRECTION_LINE &&
      v.velocity > this.DIRECTION_LINE_MIN_VELOCITY
    ) {
      let start = [lat, lng];
      if (v.proportionalMap && graphics.shape instanceof L.Polygon) {
        const { tip } = this.buildShipPolygonPoints(
          lat,
          lng,
          v.dimension.a || 0,
          v.dimension.b || 0,
          v.dimension.c || 0,
          v.dimension.d || 0,
          v.direction || 0
        );
        start = tip;
      }
      const dircoords = this.buildDirectionLine(
        start[0],
        start[1],
        v.direction || 0,
        this.DIRECTION_LINE_LENGTH_METERS
      );
      graphics.directionLine = L.polyline(dircoords, {
        color: this.DIRECTION_LINE_COLOR,
        weight: 1.5,
        opacity: 0.8,
      });
      graphics.group.addLayer(graphics.directionLine);
    }

    this.clearExtraLabels(v.mmsi);
    if (this.map.getZoom() >= this.ZOOM_TRIANGLE) this.addExtraLabels(v);
  }

  attachVesselInteractions(layer, vesselData, lat, lng) {
    if (!layer) return;
    layer.off("click");
    layer.off("mouseover");
    layer.off("mouseout");
    layer.bindTooltip(this.buildTooltipText(vesselData), { sticky: true });

    layer.on("click", (e) => {
      this.modalVesselInfo.showInfo(vesselData, layer);
    });
    layer.on("mouseover", () => layer.openTooltip());
    layer.on("mouseout", () => {
      try {
        layer.closeTooltip();
      } catch {}
    });
  }

  buildTooltipText(v) {
    const name = v.vessel?.name ? v.vessel.name : "NOME INDISPONÍVEL";
    const imo = v.vessel?.imo || "-";
    const mmsi = v.mmsi || "-";
    const velocity = v.velocity !== undefined ? `${v.velocity} kn` : "-";
    const direction = v.direction !== undefined ? `${v.direction} º` : "-";
    const destination = v.destination || "-";
    return `<b>${this.escapeHtml(
      name
    )}</b><br>IMO: ${imo} • MMSI: ${mmsi}<br>Vel: ${velocity} • Dir: ${direction}<br>Destino: ${this.escapeHtml(
      destination
    )}`;
  }

  buildPopupContent(v) {
    const base = this.buildTooltipText(v);
    const a = v.dimension?.a ? v.dimension.a : 0;
    const b = v.dimension?.b ? v.dimension.b : 0;
    const c = v.dimension?.c ? v.dimension.c : 0;
    const d = v.dimension?.d ? v.dimension.d : 0;
    const comprimento = a + b;
    const largura = c + d;
    return `${base}<hr><b>Comprimento:</b> ${comprimento} m<br><b>Largura:</b> ${largura} m`;
  }

  escapeHtml(s) {
    if (!s) return "";
    return String(s)
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;");
  }

  vesselMovedSignificantly(
    oldLat,
    oldLng,
    newLat,
    newLng,
    thresholdMeters = this.POSITION_EPSILON_METERS
  ) {
    if (oldLat === undefined || oldLng === undefined) return true;
    const d = this.distanceMeters(oldLat, oldLng, newLat, newLng);
    return d >= thresholdMeters;
  }

  // ====== PIPELINE DE RENDER ======
  processVesselsResponse(list) {
    if (!this.map) return;
    const bounds = this.map.getBounds();
    const zoom = this.map.getZoom();

    const visibleVessels = list.filter((v) => {
      if (typeof v.lat !== "number" || typeof v.lng !== "number") return false;
      if (v.proportionalMap && v.dimension) {
        const { polygonPoints } = this.buildShipPolygonPoints(
          v.lat,
          v.lng,
          v.dimension.a || 0,
          v.dimension.b || 0,
          v.dimension.c || 0,
          v.dimension.d || 0,
          v.direction || 0
        );
        const poly = L.polygon(polygonPoints);
        return bounds.intersects(poly.getBounds());
      } else {
        const approx = 25;
        const corner1 = this.offsetMetersToLatLng(
          v.lat,
          v.lng,
          -approx,
          -approx
        );
        const corner2 = this.offsetMetersToLatLng(v.lat, v.lng, approx, approx);
        const box = L.latLngBounds(corner1, corner2);
        return bounds.intersects(box);
      }
    });

    if (zoom < this.ZOOM_TRIANGLE) {
      for (const [mmsi] of this.vesselsCache.entries()) {
        this.removeVesselFromMap(mmsi);
        this.clearExtraLabels(mmsi);
      }
      if (!this.map.hasLayer(this.markerClusterGroup))
        this.map.addLayer(this.markerClusterGroup);
      this.showClustersOrHeat(visibleVessels, zoom);
    } else {
      if (this.map.hasLayer(this.markerClusterGroup))
        this.map.removeLayer(this.markerClusterGroup);
      this.showDetailedVessels(visibleVessels, zoom);
    }
  }

  showClustersOrHeat(vessels) {
    if (this.regionLayerGroup) this.regionLayerGroup.clearLayers();

    if (this.markerClusterGroup) {
      this.markerClusterGroup.clearLayers();
    } else {
      this.markerClusterGroup = L.markerClusterGroup({
        chunkedLoading: true,
        spiderfyOnMaxZoom: false,
      });
      this.map.addLayer(this.markerClusterGroup);
    }

    vessels.forEach((v) => {
      const m = L.circleMarker([v.lat, v.lng], {
        radius: 3,
        color: v.color || "#3388ff",
        fillColor: v.color || "#3388ff",
        fillOpacity: v.opacity !== undefined ? v.opacity : 0.6,
      });
      m.bindTooltip(this.buildTooltipText(v));
      this.markerClusterGroup.addLayer(m);
    });

    this.vesselsCache.clear();
  }

  removeVesselFromMap(mmsi) {
    const entry = this.vesselsCache.get(mmsi);
    if (!entry) return;
    const { group } = entry.layerRefs || {};
    const layerGroup = this.getLayerGroupByType(entry.codType);

    if (group && layerGroup && layerGroup.hasLayer(group)) {
      layerGroup.removeLayer(group);
    }
    this.clearExtraLabels(mmsi);
    this.vesselsCache.delete(mmsi);
  }

  showDetailedVessels(vessels, zoom) {
    if (this.regionLayerGroup) this.regionLayerGroup.clearLayers();

    if (!this.markerClusterGroup) {
      this.markerClusterGroup = L.markerClusterGroup({
        chunkedLoading: true,
        spiderfyOnMaxZoom: false,
      });
    }
    if (zoom >= this.ZOOM_TRIANGLE) {
      if (this.map.hasLayer(this.markerClusterGroup))
        this.map.removeLayer(this.markerClusterGroup);
    } else if (!this.map.hasLayer(this.markerClusterGroup)) {
      this.map.addLayer(this.markerClusterGroup);
    }

    const present = new Set();

    vessels.forEach((v) => {
      present.add(v.mmsi);
      const cached = this.vesselsCache.get(v.mmsi);
      const layerGroup = this.getLayerGroupByType(v.codType);
      if (!layerGroup) return;

      if (!cached) {
        const graphics = this.createVesselGraphics(v);
        if (!graphics || !graphics.group || !graphics.shape) return;

        if (zoom >= this.ZOOM_TRIANGLE) graphics.group.addTo(layerGroup);

        this.vesselsCache.set(v.mmsi, {
          data: v,
          layerRefs: graphics,
          lastLat: v.lat,
          lastLng: v.lng,
          lastUpdated: Date.now(),
          lastZoom: zoom,
          codType: v.codType,
        });
      } else {
        const zoomChanged =
          (cached.lastZoom < this.ZOOM_REAL_POLYGON &&
            zoom >= this.ZOOM_REAL_POLYGON) ||
          (cached.lastZoom >= this.ZOOM_REAL_POLYGON &&
            zoom < this.ZOOM_REAL_POLYGON) ||
          (cached.lastZoom < this.ZOOM_TRIANGLE &&
            zoom >= this.ZOOM_TRIANGLE) ||
          (cached.lastZoom >= this.ZOOM_TRIANGLE && zoom < this.ZOOM_TRIANGLE);

        const moved = this.vesselMovedSignificantly(
          cached.lastLat,
          cached.lastLng,
          v.lat,
          v.lng
        );
        const dirChanged = cached.data.direction !== v.direction;
        const dimChanged =
          JSON.stringify(cached.data.dimension || {}) !==
          JSON.stringify(v.dimension || {});
        const colorChanged =
          cached.data.color !== v.color || cached.data.opacity !== v.opacity;
        const nameChanged =
          (cached.data.vessel?.name || "") !== (v.vessel?.name || "");
        const imoChanged =
          (cached.data.vessel?.imo || "") !== (v.vessel?.imo || "");
        const destChanged =
          (cached.data.destination || "") !== (v.destination || "");
        const velocityChanged = cached.data.velocity !== v.velocity;

        if (
          moved ||
          dirChanged ||
          dimChanged ||
          colorChanged ||
          zoomChanged ||
          nameChanged ||
          imoChanged ||
          destChanged ||
          velocityChanged
        ) {
          cached.data = v;
          this.updateVesselGraphics(cached.layerRefs, v);
          cached.lastLat = v.lat;
          cached.lastLng = v.lng;
          cached.lastUpdated = Date.now();
          cached.lastZoom = zoom;
        } else {
          cached.lastUpdated = Date.now();
        }
      }
    });

    for (const [mmsi, entry] of this.vesselsCache.entries()) {
      if (!present.has(mmsi)) this.removeVesselFromMap(mmsi);
    }
  }

  // ====== FETCH ======
  async fetchAllVessels() {
    try {
      const headers = new Headers();
      headers.append("Accept", "*/*");
      const token = JSON.parse(
        localStorage.getItem("user_logged_in") || "{}"
      ).token;
      if (token && token.length)
        headers.append("Authorization", "Bearer " + token);

      const resp = await fetch(this.API_URL + "/api/ais/all", {
        method: "GET",
        headers,
      });
      if (!resp.ok) {
        console.warn("Failed to fetch AIS data", resp.status);
        return [];
      }
      return await resp.json();
    } catch (err) {
      console.error("Error fetching AIS data", err);
      return [];
    }
  }

  async getLayersAndBercos() {
    try {
      const headers = new Headers();
      headers.append("Accept", "*/*");
      headers.append("Authorization", "Basic bGVvOjEyMw==");
      const resp = await fetch(this.API_URL + "/api/ais/startup", {
        method: "GET",
        headers,
      });
      if (!resp.ok) {
        console.warn("Failed to fetch startup data", resp.status);
        return { layers: [], bercos: [] };
      }
      return await resp.json();
    } catch (err) {
      console.error("Error fetching startup data", err);
      return { layers: [], bercos: [] };
    }
  }

  // ====== UTILS ======
  getDistancePositions(lat1, lon1, lat2, lon2) {
    const R = 6371;
    const dLat = ((lat2 - lat1) * Math.PI) / 180;
    const dLon = ((lon2 - lon1) * Math.PI) / 180;
    const a =
      0.5 -
      Math.cos(dLat) / 2 +
      (Math.cos((lat1 * Math.PI) / 180) *
        Math.cos((lat2 * Math.PI) / 180) *
        (1 - Math.cos(dLon))) /
        2;
    return R * 2 * Math.asin(Math.sqrt(a));
  }

  getValueDistanceToText(distanceKm) {
    let val = distanceKm,
      medida = "km";
    if (val < 1) {
      val = val * 1000;
      medida = "m";
    }
    return val.toFixed(2) + " " + medida;
  }

  destroyLayer(layer) {
    if (!layer) return;
    layer.off?.();
    layer.unbindPopup?.();
    layer.unbindTooltip?.();
    this.map.removeLayer(layer);
    layer.remove?.();
  }

  converterDecimalParaDMS(coordenada, isLatitude) {
    const abs = Math.abs(coordenada);
    const graus = Math.floor(abs);
    const minutosDecimais = (abs - graus) * 60;
    const minutos = Math.floor(minutosDecimais);
    const segundos = (minutosDecimais - minutos) * 60;
    const direcao = isLatitude
      ? coordenada >= 0
        ? "N"
        : "S"
      : coordenada >= 0
      ? "E"
      : "W";
    return `${direcao}${graus}° ${minutos}' ${segundos.toFixed(2)}"`;
  }

  getNewLongitude(longitude, latitude, meters) {
    const earth = 6378.137,
      pi = Math.PI,
      cos = Math.cos,
      m = 1 / (((2 * pi) / 360) * earth) / 1000;
    return longitude + (meters * m) / cos(latitude * (pi / 180));
  }

  getNewLatitude(latitude, meters) {
    const earth = 6378.137,
      pi = Math.PI,
      m = 1 / (((2 * pi) / 360) * earth) / 1000;
    return latitude + meters * m;
  }

  addMetersInCoordinate(coordinate, metersLongitude, metersLatitude) {
    const newLng = this.getNewLongitude(
      coordinate.lng,
      coordinate.lat,
      metersLongitude
    );
    const newLat = this.getNewLatitude(coordinate.lat, metersLatitude);
    return L.latLng(newLat, newLng);
  }

  debounce(func, wait) {
    let timeout;
    return (...args) => {
      clearTimeout(timeout);
      timeout = setTimeout(() => func.apply(this, args), wait);
    };
  }

  // ====== MAPA ======
  initMap() {
    this.map = L.map(this.containerId, {
      center: this.CENTER,
      zoom: this.ZOOM,
      fullscreenControl: true,
      layers: this.getLayersActive(),
    });

    this.map.createPane("vesselPolygons");
    this.map.getPane("vesselPolygons").style.zIndex = 400;

    this.map.createPane("vesselAntennas");
    this.map.getPane("vesselAntennas").style.zIndex = 500;

    this.markerClusterGroup = L.markerClusterGroup({
      chunkedLoading: true,
      spiderfyOnMaxZoom: false,
    });
    this.map.addLayer(this.markerClusterGroup);
  }

  addEventsMap() {
    const debouncedRefresh = this.debounce(() => {
      this.refreshAndProcess();
    }, 500);
    this.map.on("moveend", debouncedRefresh);

    this.map.on("zoomend", () => {
      this.refreshAndProcess();
    });

    this.map.on("overlayadd", (e) => {
      const type = this.typeVessels.find((t) => t.layer === e.layer);
      if (type) {
        this.vesselsCache.forEach((entry) => {
          if (entry.codType === type.type) {
            this.clearExtraLabels(entry.data.mmsi);
            if (
              this.map.hasLayer(this.vesselNameLayer) ||
              this.map.hasLayer(this.vesselInfoLayer)
            ) {
              this.clearExtraLabels(entry.data.mmsi);
              this.addExtraLabels(entry.data);
            }
          }
        });
      }
      if (
        e.layer === this.vesselNameLayer ||
        e.layer === this.vesselInfoLayer
      ) {
        this.vesselsCache.forEach((entry) => {
          this.clearExtraLabels(entry.data.mmsi);
          this.addExtraLabels(entry.data);
        });
      }
    });

    this.map.on("overlayremove", (e) => {
      const type = this.typeVessels.find((t) => t.layer === e.layer);
      if (type) {
        this.vesselsCache.forEach((entry) => {
          if (entry.codType === type.type)
            this.clearExtraLabels(entry.data.mmsi);
        });
      }
      if (e.layer === this.vesselNameLayer) this.vesselNameLayer.clearLayers();
      if (e.layer === this.vesselInfoLayer) this.vesselInfoLayer.clearLayers();
    });
  }

  addControlsMap() {
    const functionalities =
      JSON.parse(localStorage.getItem("user_logged_in") || "{}")
        .functionalities || [];

    // Botão de pesquisa (usa PF global)
    if (functionalities.indexOf("ais_search") > -1) {
      const AisSearchButton = L.Control.extend({
        options: { position: "topleft" },
        onAdd: () => {
          const c = L.DomUtil.create(
            "div",
            "leaflet-bar leaflet-control leaflet-control-custom"
          );
          c.title = "Pesquisar por embarcações";
          Object.assign(
            c.style,
            this.btnStyle("/sismar/faces/javax.faces.resource/img/search.png")
          );
          c.onclick = () => PF?.("open-search-vessels")?.show();
          this.hoverEffect(c);
          return c;
        },
      });
      this.map.addControl(new AisSearchButton());
    }

    // Botão Playback
    if (functionalities.indexOf("ais_playback") > -1) {
      const PlaybackButton = L.Control.extend({
        options: { position: "topleft" },
        onAdd: () => {
          const c = L.DomUtil.create(
            "div",
            "leaflet-bar leaflet-control leaflet-control-custom"
          );
          c.title = "Playback";
          Object.assign(
            c.style,
            this.btnStyle("/sismar/faces/javax.faces.resource/img/play.png")
          );
          c.onclick = () => PF?.("open-period-playback")?.show();
          this.hoverEffect(c);
          return c;
        },
      });
      this.map.addControl(new PlaybackButton());
    }

    // Botão Rota/Trajetória
    const RouteButton = L.Control.extend({
      options: { position: "topleft" },
      onAdd: () => {
        const container = L.DomUtil.create(
          "div",
          "leaflet-bar leaflet-control leaflet-control-custom"
        );
        container.title = "Rota/Trajetória";
        Object.assign(
          container.style,
          this.btnStyle("/sismar/faces/javax.faces.resource/img/route.png")
        );

        let routeLayer = null;
        const changeTooltipContent = (
          positionStart,
          positionEnd,
          marker,
          start,
          end
        ) => {
          const distanceKm = this.getDistancePositions(
            positionStart.lat,
            positionStart.lng,
            positionEnd.lat,
            positionEnd.lng
          );
          const distanceKmText = this.getValueDistanceToText(distanceKm);
          let distanceMilhasText = (distanceKm * 0.539957).toFixed(2);

          const latInicialDms = this.converterDecimalParaDMS(
            positionStart.lat,
            true
          );
          const lonInicialDms = this.converterDecimalParaDMS(
            positionStart.lng,
            false
          );
          const latFinalDms = this.converterDecimalParaDMS(
            positionEnd.lat,
            true
          );
          const lonFinalDms = this.converterDecimalParaDMS(
            positionEnd.lng,
            false
          );

          let text = `<b>Posição inicial:</b> ${start}<br><u>Lat:</u> ${latInicialDms} ~ ${positionStart.lat.toFixed(
            6
          )} <br> <u>Lng:</u> ${lonInicialDms} ~ ${positionStart.lng.toFixed(
            6
          )}`;
          text += `<br><br><b>Posição final:</b> ${end}<br><u>Lat:</u> ${latFinalDms} ~ ${positionEnd.lat.toFixed(
            6
          )} <br> <u>Lng:</u> ${lonFinalDms} ~ ${positionEnd.lng.toFixed(6)}`;
          text += `<br><br><b>Distância:</b> ${distanceKmText} ~ ${distanceMilhasText} nmi`;

          if (!marker.getTooltip()) marker.bindTooltip(text).openTooltip();
          else marker.setTooltipContent(text);
        };

        container.onclick = () => {
          if (!this.ativeButtonRoute) {
            const markerPositionA = this.map.getCenter();
            const markerPositionB = this.addMetersInCoordinate(
              markerPositionA,
              700,
              0
            );

            const blueIcon = new L.Icon({
              iconUrl:
                "/sismar/faces/javax.faces.resource/img/marker-icon-2x-blue.png",
              shadowUrl:
                "/sismar/faces/javax.faces.resource/img/marker-shadow.png",
              iconSize: [25, 41],
              iconAnchor: [12, 41],
              popupAnchor: [1, -34],
              shadowSize: [41, 41],
            });
            const redIcon = new L.Icon({
              iconUrl:
                "/sismar/faces/javax.faces.resource/img/marker-icon-2x-red.png",
              shadowUrl:
                "/sismar/faces/javax.faces.resource/img/marker-shadow.png",
              iconSize: [25, 41],
              iconAnchor: [12, 41],
              popupAnchor: [1, -34],
              shadowSize: [41, 41],
            });

            const markerA = L.marker(markerPositionA, {
              icon: blueIcon,
              draggable: true,
            });
            const markerB = L.marker(markerPositionB, {
              icon: redIcon,
              draggable: true,
            });

            const polyline = L.polyline([markerPositionA, markerPositionB], {
              color: "blue",
              weight: 3,
              opacity: 0.9,
            });

            markerA.on("drag", (e) => {
              const positionA = e.target.getLatLng();
              const positionB = markerB.getLatLng();
              polyline.setLatLngs([positionA, positionB]);
              changeTooltipContent(positionA, positionB, markerA, "A", "B");
            });
            markerB.on("drag", (e) => {
              const positionB = e.target.getLatLng();
              const positionA = markerA.getLatLng();
              polyline.setLatLngs([positionA, positionB]);
              changeTooltipContent(positionA, positionB, markerB, "A", "B");
            });
            markerA.on("mouseover", () =>
              changeTooltipContent(
                markerA.getLatLng(),
                markerB.getLatLng(),
                markerA,
                "A",
                "B"
              )
            );
            markerB.on("mouseover", () =>
              changeTooltipContent(
                markerB.getLatLng(),
                markerA.getLatLng(),
                markerB,
                "B",
                "A"
              )
            );

            routeLayer = new L.layerGroup([polyline, markerA, markerB]);
            this.map.addLayer(routeLayer);
            this.ativeButtonRoute = true;
          } else {
            this.destroyLayer(routeLayer);
            this.ativeButtonRoute = false;
          }
        };

        this.hoverEffect(container);
        return container;
      },
    });
    this.map.addControl(new RouteButton());

    // Botão de propriedades (tamanho da fonte dos labels)
    const AisMapPropertiesButton = L.Control.extend({
      options: { position: "topleft" },
      onAdd: () => {
        const container = L.DomUtil.create(
          "div",
          "leaflet-bar leaflet-control leaflet-control-custom"
        );
        container.title = "Propriedades";
        Object.assign(
          container.style,
          this.btnStyle("/sismar/faces/javax.faces.resource/img/icon_tools.png")
        );

        const rangeInput = document.getElementById("fontSizeRange");
        const output = document.getElementById("fontSizeValue");
        if (rangeInput && output) {
          rangeInput.value = String(this.fontSizeExtraLabel);
          output.textContent = String(this.fontSizeExtraLabel);
          rangeInput.addEventListener("input", () => {
            output.textContent = rangeInput.value;
            this.setFontSizeExtraLabel(rangeInput.value);
          });
        }

        container.onclick = () => {
          const panel = document.getElementById("mapproperties");
          if (!panel) return;
          this.activeButtonAisMapProperties =
            !this.activeButtonAisMapProperties;
          panel.style.display = this.activeButtonAisMapProperties
            ? "block"
            : "none";
        };

        this.hoverEffect(container);
        return container;
      },
    });
    this.map.addControl(new AisMapPropertiesButton());
  }

  btnStyle(bgUrl) {
    return {
      backgroundImage: `url(${bgUrl})`,
      backgroundSize: "18px 18px",
      backgroundRepeat: "no-repeat",
      backgroundPosition: "center",
      cursor: "pointer",
      backgroundColor: "white",
      width: "35px",
      height: "35px",
    };
  }
  hoverEffect(el) {
    el.onmouseover = () => (el.style.backgroundColor = "#f4f4f4");
    el.onmouseout = () => (el.style.backgroundColor = "white");
  }
}
