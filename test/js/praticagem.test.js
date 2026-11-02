import { test, describe, beforeEach, afterEach } from "node:test";
import assert from "node:assert/strict";
import {
  setupLeaflet,
  criarMapa,
  criarStorage,
  azimute,
} from "./helpers/leaflet-dom.js";

setupLeaflet();
const { default: Praticagem, STORAGE_KEY, HACHURA_ID } = await import(
  "../../web/resources/js/sismar.praticagem.js"
);

const ORIGEM_SALVA = { lat: -23.82, lng: -45.41 };

let map;
let storage;
let praticagem;
let setores; // layer "Setores angulares" do controle de layers

function montar(opcoes = { storage }) {
  const p = new Praticagem(opcoes);
  p.attach(map);
  setores = p.getLayerTree().children[0].layer;
  return p;
}

const camadas = (tipo) => setores.getLayers().filter((l) => l instanceof tipo);
const origem = () => camadas(L.Marker)[0];
const poligonos = () => camadas(L.Polygon);
const rotulos = () => camadas(L.Tooltip);
const salvo = () => JSON.parse(storage.getItem(STORAGE_KEY));
const perto = (a, b) => L.latLng(a).equals(L.latLng(b), 1e-9);

function vertices(poligono) {
  const [anel] = poligono.getLatLngs();
  return { centro: anel[0], arco: anel.slice(1) };
}

function raioDesenhadoM() {
  const { centro, arco } = vertices(poligonos()[0]);
  return map.distance(centro, arco[0]);
}

function arrastarOrigem(para) {
  const m = origem();
  m.setLatLng(L.latLng(para));
  m.fire("dragstart");
  m.fire("drag");
  m.fire("dragend");
}

function abrirPopup() {
  origem().openPopup();
  const el = origem().getPopup().getElement();
  return {
    input: el.querySelector(".praticagem-popup input"),
    botao: el.querySelector(".praticagem-popup button"),
  };
}

function digitarRaio(input, valor) {
  input.value = valor;
  input.dispatchEvent(new window.Event("change"));
}

beforeEach(() => {
  map = criarMapa();
  storage = criarStorage();
  praticagem = montar();
});

afterEach(() => {
  map.remove();
});

describe("árvore de layers", () => {
  test("expõe o grupo Praticagem com o item Setores angulares", () => {
    const arvore = praticagem.getLayerTree();
    assert.match(arvore.label, /Praticagem/);
    assert.equal(arvore.children.length, 1);
    assert.equal(arvore.children[0].label, "Setores angulares");
    assert.ok(arvore.children[0].layer instanceof L.Layer);
  });

  test("setores começam desligados", () => {
    assert.equal(map.hasLayer(setores), false);
  });
});

describe("ao ligar sem estado salvo", () => {
  beforeEach(() => map.addLayer(setores));

  test("origem aparece no centro do mapa visível", () => {
    assert.ok(perto(origem().getLatLng(), map.getCenter()));
  });

  test("desenha os setores 56°–96° e 140°–180° com raio de 25 km", () => {
    const esperados = [
      { de: 56, ate: 96 },
      { de: 140, ate: 180 },
    ];
    assert.equal(poligonos().length, 2);

    poligonos().forEach((p, i) => {
      const { centro, arco } = vertices(p);
      assert.ok(perto(centro, map.getCenter()));
      for (const v of arco) {
        assert.ok(Math.abs(map.distance(centro, v) - 25000) < 1);
      }
      assert.ok(Math.abs(azimute(centro, arco[0]) - esperados[i].de) < 0.01);
      assert.ok(Math.abs(azimute(centro, arco.at(-1)) - esperados[i].ate) < 0.01);
    });
  });

  test("180° aponta para o sul e 96° levemente abaixo do leste", () => {
    const { centro, arco } = vertices(poligonos()[1]);
    const p180 = arco.at(-1);
    assert.ok(Math.abs(p180.lng - centro.lng) < 1e-9);
    assert.ok(p180.lat < centro.lat);

    const p96 = vertices(poligonos()[0]).arco.at(-1);
    assert.ok(p96.lng > centro.lng);
    assert.ok(p96.lat < centro.lat);
  });

  test("mostra os rótulos 56°, 96°, 140° e 180° na ponta das linhas", () => {
    const textos = rotulos().map((t) => t.getContent()).sort();
    assert.deepEqual(textos, ["140°", "180°", "56°", "96°"]);

    const centro = map.getCenter();
    for (const r of rotulos()) {
      const angulo = parseFloat(r.getContent());
      assert.ok(Math.abs(azimute(centro, r.getLatLng()) - angulo) < 0.01);
      assert.ok(map.distance(centro, r.getLatLng()) > 25000);
    }
  });

  test("setores não capturam cliques; só a origem é interativa", () => {
    for (const p of poligonos()) assert.equal(p.options.interactive, false);
    const pane = map.getPane(poligonos()[0].options.pane);
    assert.equal(pane.style.pointerEvents, "none");
    assert.equal(origem().options.draggable, true);
  });

  test("borda dos setores é fina (1px) e vermelha", () => {
    for (const p of poligonos()) {
      assert.equal(p.options.weight, 1);
      assert.equal(p.options.color, "#b22222");
    }
  });

  test("setores são preenchidos com hachurado cinza de linhas paralelas", () => {
    for (const p of poligonos()) {
      assert.equal(p.options.fillColor, `url(#${HACHURA_ID})`);
      assert.equal(p.getElement().getAttribute("fill"), `url(#${HACHURA_ID})`);
    }

    const padroes = document.querySelectorAll(`pattern#${HACHURA_ID}`);
    assert.equal(padroes.length, 1);
    const padrao = padroes[0];
    assert.equal(padrao.getAttribute("patternUnits"), "userSpaceOnUse"); // espaço fixo na tela
    assert.equal(padrao.getAttribute("patternTransform"), "rotate(-45)"); // diagonal "\"
    const linha = padrao.querySelector("line");
    assert.equal(linha.getAttribute("stroke"), "#808080");
    assert.equal(linha.getAttribute("stroke-width"), "1");
  });

  test("padrão de hachura não é duplicado ao montar de novo", () => {
    montar();
    assert.equal(document.querySelectorAll(`pattern#${HACHURA_ID}`).length, 1);
  });

  test("origem tem tamanho fixo em pixels", () => {
    assert.ok(origem().options.icon instanceof L.DivIcon);
    assert.deepEqual(origem().options.icon.options.iconSize, [14, 14]);
  });

  test("tamanho geográfico não muda com o zoom", () => {
    map.setZoom(13, { animate: false });
    assert.ok(Math.abs(raioDesenhadoM() - 25000) < 1);
  });

  test("desligar remove setores, rótulos e origem do mapa", () => {
    const desenhadas = setores.getLayers();
    assert.ok(desenhadas.length > 0);
    map.removeLayer(setores);
    for (const l of desenhadas) assert.equal(map.hasLayer(l), false);
  });
});

describe("arrastar a origem e lembrar a posição", () => {
  beforeEach(() => map.addLayer(setores));

  test("setores e rótulos acompanham a origem durante o arraste", () => {
    const m = origem();
    m.setLatLng(L.latLng(ORIGEM_SALVA));
    m.fire("drag");
    for (const p of poligonos()) assert.ok(perto(vertices(p).centro, ORIGEM_SALVA));
    const r180 = rotulos().find((t) => t.getContent() === "180°");
    assert.ok(Math.abs(azimute(L.latLng(ORIGEM_SALVA), r180.getLatLng()) - 180) < 0.01);
  });

  test("ao soltar, salva a posição no navegador", () => {
    arrastarOrigem(ORIGEM_SALVA);
    assert.deepEqual(salvo(), { ...ORIGEM_SALVA, raioKm: 25 });
  });

  test("desligar e religar restaura a posição salva", () => {
    arrastarOrigem(ORIGEM_SALVA);
    map.removeLayer(setores);
    map.addLayer(setores);
    assert.ok(perto(origem().getLatLng(), ORIGEM_SALVA));
  });

  test("nova instância (recarga da página) restaura a posição salva", () => {
    arrastarOrigem(ORIGEM_SALVA);
    map.removeLayer(setores);

    praticagem = montar();
    map.addLayer(setores);

    assert.ok(perto(origem().getLatLng(), ORIGEM_SALVA));
  });

  test("estado ligado/desligado não é salvo", () => {
    map.removeLayer(setores);
    assert.equal(storage.getItem(STORAGE_KEY), null);
  });
});

describe("armazenamento inválido ou indisponível", () => {
  const casos = {
    "JSON inválido": () => criarStorage({ [STORAGE_KEY]: "{não é json" }),
    "campos ausentes": () => criarStorage({ [STORAGE_KEY]: "{}" }),
    "latitude fora do intervalo": () =>
      criarStorage({
        [STORAGE_KEY]: JSON.stringify({ lat: 120, lng: -45, raioKm: 10 }),
      }),
    "storage que lança exceção": () => ({
      getItem() {
        throw new Error("bloqueado");
      },
      setItem() {
        throw new Error("bloqueado");
      },
    }),
    "sem storage": () => null,
  };

  for (const [nome, criar] of Object.entries(casos)) {
    test(`${nome}: usa centro do mapa e 25 km sem quebrar`, () => {
      montar({ storage: criar() });
      map.addLayer(setores);

      assert.ok(perto(origem().getLatLng(), map.getCenter()));
      assert.ok(Math.abs(raioDesenhadoM() - 25000) < 1);
      assert.doesNotThrow(() => arrastarOrigem(ORIGEM_SALVA));
    });
  }
});

describe("popup de raio e Centralizar aqui", () => {
  beforeEach(() => map.addLayer(setores));

  test("abre com título, o raio atual e largura mínima ampliada", () => {
    const { input, botao } = abrirPopup();
    const el = origem().getPopup().getElement();
    assert.equal(
      el.querySelector(".praticagem-popup-titulo").textContent,
      "Setores angulares"
    );
    assert.equal(input.value, "25");
    assert.equal(botao.textContent, "Centralizar aqui");
    assert.equal(origem().getPopup().options.minWidth, 240);
  });

  test("alterar o raio redesenha e salva", () => {
    digitarRaio(abrirPopup().input, "10");
    assert.ok(Math.abs(raioDesenhadoM() - 10000) < 1);
    assert.equal(salvo().raioKm, 10);
  });

  test("raio é limitado entre 1 e 100 km", () => {
    const { input } = abrirPopup();

    digitarRaio(input, "0");
    assert.equal(salvo().raioKm, 1);
    assert.equal(input.value, "1");

    digitarRaio(input, "500");
    assert.equal(salvo().raioKm, 100);
    assert.equal(input.value, "100");
  });

  test("valor não numérico é ignorado", () => {
    const { input } = abrirPopup();
    digitarRaio(input, "12");
    digitarRaio(input, "");
    assert.equal(salvo().raioKm, 12);
    assert.equal(input.value, "12");
    assert.ok(Math.abs(raioDesenhadoM() - 12000) < 1);
  });

  test("raio salvo é restaurado em nova instância", () => {
    digitarRaio(abrirPopup().input, "40");
    map.removeLayer(setores);

    praticagem = montar();
    map.addLayer(setores);

    assert.ok(Math.abs(raioDesenhadoM() - 40000) < 1);
    assert.equal(abrirPopup().input.value, "40");
  });

  test("Centralizar aqui move a origem para o centro e salva", () => {
    arrastarOrigem(ORIGEM_SALVA);
    map.setView([-23.9, -45.3], 11, { animate: false });

    abrirPopup().botao.click();

    const centro = map.getCenter();
    assert.ok(perto(origem().getLatLng(), centro));
    assert.ok(perto(vertices(poligonos()[0]).centro, centro));
    assert.deepEqual(salvo(), { lat: centro.lat, lng: centro.lng, raioKm: 25 });
  });
});
