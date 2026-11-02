/* sismar.praticagem.js - ES Module
 *
 * Recursos de praticagem exibidos no mapa AIS realtime.
 * Hoje: setores angulares (azimute a partir do norte verdadeiro, sentido horário)
 * saindo de um ponto de origem arrastável, com raio editável e
 * posição/raio salvos no navegador.
 */

// Setores exibidos. Mudou a regra? Altere apenas aqui.
export const SETORES_ANGULARES = [
  { de: 56, ate: 96 },
  { de: 140, ate: 180 },
];

export const STORAGE_KEY = "sismar.ais.setoresAngulares";
export const RAIO_PADRAO_KM = 25;
export const RAIO_MIN_KM = 1;
export const RAIO_MAX_KM = 100;

const COR = "#b22222";
const PASSO_ARCO_GRAUS = 2;
const RAIO_TERRA_M = 6371000; // mesmo raio usado pelo Leaflet (L.CRS.Earth.R)
const PANE_SETORES = "praticagemSetores";

// Hachurado interno dos setores: linhas paralelas "\" com espaçamento fixo na tela.
export const HACHURA_ID = "praticagem-hachura";
const HACHURA_COR = "#808080";
const HACHURA_OPACIDADE = 0.6;
const HACHURA_ESPACO_PX = 10;
const SVG_NS = "http://www.w3.org/2000/svg";

/** Cria (uma única vez por página) o <pattern> SVG usado como preenchimento dos setores. */
function garantirPadraoHachura() {
  if (document.getElementById(HACHURA_ID)) return;

  const svg = document.createElementNS(SVG_NS, "svg");
  svg.setAttribute("width", "0");
  svg.setAttribute("height", "0");
  svg.setAttribute("aria-hidden", "true");
  // não usar display:none — o navegador deixa de renderizar o pattern
  svg.style.position = "absolute";

  const defs = document.createElementNS(SVG_NS, "defs");
  const pattern = document.createElementNS(SVG_NS, "pattern");
  pattern.setAttribute("id", HACHURA_ID);
  pattern.setAttribute("patternUnits", "userSpaceOnUse");
  pattern.setAttribute("width", String(HACHURA_ESPACO_PX));
  pattern.setAttribute("height", String(HACHURA_ESPACO_PX));
  pattern.setAttribute("patternTransform", "rotate(-45)");

  const linha = document.createElementNS(SVG_NS, "line");
  linha.setAttribute("x1", "0");
  linha.setAttribute("y1", "0");
  linha.setAttribute("x2", "0");
  linha.setAttribute("y2", String(HACHURA_ESPACO_PX));
  linha.setAttribute("stroke", HACHURA_COR);
  linha.setAttribute("stroke-opacity", String(HACHURA_OPACIDADE));
  linha.setAttribute("stroke-width", "1");

  pattern.appendChild(linha);
  defs.appendChild(pattern);
  svg.appendChild(defs);
  document.body.appendChild(svg);
}

/** Ponto a `distanciaM` metros de `origem` no `azimuteGraus` informado (fórmula esférica). */
export function destino(origem, azimuteGraus, distanciaM) {
  const rad = Math.PI / 180;
  const lat1 = origem.lat * rad;
  const lng1 = origem.lng * rad;
  const az = azimuteGraus * rad;
  const d = distanciaM / RAIO_TERRA_M;

  const lat2 = Math.asin(
    Math.sin(lat1) * Math.cos(d) + Math.cos(lat1) * Math.sin(d) * Math.cos(az)
  );
  const lng2 =
    lng1 +
    Math.atan2(
      Math.sin(az) * Math.sin(d) * Math.cos(lat1),
      Math.cos(d) - Math.sin(lat1) * Math.sin(lat2)
    );

  const lngGraus = ((((lng2 / rad + 540) % 360) + 360) % 360) - 180;
  return L.latLng(lat2 / rad, lngGraus);
}

function limitarRaio(km) {
  return Math.min(RAIO_MAX_KM, Math.max(RAIO_MIN_KM, km));
}

function storagePadrao() {
  try {
    return globalThis.localStorage || null;
  } catch (e) {
    return null;
  }
}

export default class Praticagem {
  map = null;
  storage = null;

  // layer ligado/desligado pelo controle de layers
  setoresAngulares = L.layerGroup();

  origem = null; // L.LatLng
  raioKm = RAIO_PADRAO_KM;

  marcadorOrigem = null;
  poligonos = []; // um por setor, mesma ordem de SETORES_ANGULARES
  rotulos = []; // { angulo, tooltip }

  constructor({ storage } = {}) {
    this.storage = storage === undefined ? storagePadrao() : storage;

    this.setoresAngulares.on("add", () => this.aoLigarSetores());
    this.setoresAngulares.on("remove", () => this.aoDesligarSetores());
  }

  // ====== API PÚBLICA ======
  attach(map) {
    this.map = map;
    if (!map.getPane(PANE_SETORES)) {
      const pane = map.createPane(PANE_SETORES);
      pane.style.zIndex = 450; // acima das embarcações, abaixo dos marcadores
      pane.style.pointerEvents = "none"; // cliques passam para as camadas de baixo
    }
    garantirPadraoHachura();
  }

  getLayerTree() {
    return {
      label: "<span class='leaflet-layerstree-header-title'>Praticagem</span>",
      children: [{ label: "Setores angulares", layer: this.setoresAngulares }],
    };
  }

  // ====== CICLO DE VIDA DO LAYER ======
  aoLigarSetores() {
    const salvo = this.lerEstado();
    this.origem = salvo ? L.latLng(salvo.lat, salvo.lng) : this.map.getCenter();
    this.raioKm = salvo ? salvo.raioKm : RAIO_PADRAO_KM;
    this.criarDesenho();
  }

  aoDesligarSetores() {
    this.setoresAngulares.clearLayers();
    this.marcadorOrigem = null;
    this.poligonos = [];
    this.rotulos = [];
  }

  // ====== AÇÕES ======
  definirRaio(km) {
    const valor = Number(km);
    if (km === "" || km === null || !Number.isFinite(valor)) return;
    this.raioKm = limitarRaio(valor);
    this.atualizarDesenho();
    this.salvarEstado();
  }

  centralizar() {
    this.origem = this.map.getCenter();
    this.marcadorOrigem?.setLatLng(this.origem);
    this.atualizarDesenho();
    this.salvarEstado();
  }

  // ====== DESENHO ======
  criarDesenho() {
    this.poligonos = SETORES_ANGULARES.map(() =>
      L.polygon([], {
        pane: PANE_SETORES,
        interactive: false,
        color: COR,
        weight: 1,
        opacity: 0.9,
        fillColor: `url(#${HACHURA_ID})`,
        fillOpacity: 1,
      })
    );

    const angulos = [
      ...new Set(SETORES_ANGULARES.flatMap((s) => [s.de, s.ate])),
    ];
    this.rotulos = angulos.map((angulo) => ({
      angulo,
      tooltip: L.tooltip({
        permanent: true,
        direction: "center",
        interactive: false,
        className: "leaflet_label praticagem-rotulo",
      }).setContent(`${angulo}°`),
    }));

    this.marcadorOrigem = L.marker(this.origem, {
      draggable: true,
      autoPan: true,
      zIndexOffset: 1000,
      title: "Origem dos setores angulares",
      icon: L.divIcon({ className: "praticagem-origem", iconSize: [14, 14] }),
    });
    this.marcadorOrigem.on("drag", () => {
      this.origem = this.marcadorOrigem.getLatLng();
      this.atualizarDesenho();
    });
    this.marcadorOrigem.on("dragend", () => {
      this.origem = this.marcadorOrigem.getLatLng();
      this.atualizarDesenho();
      this.salvarEstado();
    });
    this.marcadorOrigem.bindPopup(() => this.criarConteudoPopup(), {
      minWidth: 240,
    });

    this.atualizarDesenho();

    this.poligonos.forEach((p) => this.setoresAngulares.addLayer(p));
    this.rotulos.forEach((r) => this.setoresAngulares.addLayer(r.tooltip));
    this.setoresAngulares.addLayer(this.marcadorOrigem);
  }

  atualizarDesenho() {
    if (!this.origem) return;
    const raioM = this.raioKm * 1000;

    SETORES_ANGULARES.forEach((setor, i) => {
      const arco = [];
      for (let az = setor.de; az < setor.ate; az += PASSO_ARCO_GRAUS) {
        arco.push(destino(this.origem, az, raioM));
      }
      arco.push(destino(this.origem, setor.ate, raioM));
      this.poligonos[i]?.setLatLngs([this.origem, ...arco]);
    });

    // rótulo um pouco além da ponta da linha
    this.rotulos.forEach((r) =>
      r.tooltip.setLatLng(destino(this.origem, r.angulo, raioM * 1.04))
    );
  }

  criarConteudoPopup() {
    const div = L.DomUtil.create("div", "praticagem-popup");

    const titulo = L.DomUtil.create("div", "praticagem-popup-titulo", div);
    titulo.textContent = "Setores angulares";

    const label = L.DomUtil.create("label", "", div);
    label.textContent = "Raio (km)";

    const input = L.DomUtil.create("input", "", label);
    input.type = "number";
    input.min = String(RAIO_MIN_KM);
    input.max = String(RAIO_MAX_KM);
    input.step = "1";
    input.value = String(this.raioKm);
    input.addEventListener("input", () => this.definirRaio(input.value));
    input.addEventListener("change", () => {
      this.definirRaio(input.value);
      input.value = String(this.raioKm);
    });
    // evita que setas/+/- digitados no campo movam ou deem zoom no mapa
    L.DomEvent.on(input, "keydown keypress keyup", L.DomEvent.stopPropagation);

    const botao = L.DomUtil.create("button", "praticagem-centralizar", div);
    botao.type = "button";
    botao.textContent = "Centralizar aqui";
    botao.addEventListener("click", () => this.centralizar());

    L.DomEvent.disableClickPropagation(div);
    return div;
  }

  // ====== PERSISTÊNCIA ======
  lerEstado() {
    try {
      const texto = this.storage?.getItem(STORAGE_KEY);
      if (!texto) return null;
      const dados = JSON.parse(texto);
      const lat = Number(dados?.lat);
      const lng = Number(dados?.lng);
      if (
        !Number.isFinite(lat) ||
        !Number.isFinite(lng) ||
        Math.abs(lat) > 90 ||
        Math.abs(lng) > 180
      )
        return null;
      const raio = Number(dados.raioKm);
      return {
        lat,
        lng,
        raioKm: Number.isFinite(raio) ? limitarRaio(raio) : RAIO_PADRAO_KM,
      };
    } catch (e) {
      return null;
    }
  }

  salvarEstado() {
    try {
      this.storage?.setItem(
        STORAGE_KEY,
        JSON.stringify({
          lat: this.origem.lat,
          lng: this.origem.lng,
          raioKm: this.raioKm,
        })
      );
    } catch (e) {
      // armazenamento indisponível (modo privado, cota, bloqueio): segue sem salvar
    }
  }
}
