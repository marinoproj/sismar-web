// Regressão: a árvore de layers do mapa AIS realtime não muda sem a Praticagem.
import { test, describe } from "node:test";
import assert from "node:assert/strict";
import { setupLeaflet, criarMapa, criarStorage } from "./helpers/leaflet-dom.js";

setupLeaflet();

// dependências globais da página usadas pelo construtor do AisMap
globalThis.$ = () => ({ draggable() {} });
const props = document.createElement("div");
props.id = "mapproperties";
document.body.appendChild(props);

const { default: AisMap } = await import(
  "../../web/resources/js/sismar.ais.realtime.js"
);
const { default: Praticagem } = await import(
  "../../web/resources/js/sismar.praticagem.js"
);

const dataStartup = {
  layers: [
    {
      name: "Área de fundeio",
      poins: [
        {
          name: "Fundeio 1",
          color: "#00ff00",
          opacity: 50,
          coordinates: [
            { latitude: -23.8, longitude: -45.4 },
            { latitude: -23.81, longitude: -45.4 },
            { latitude: -23.81, longitude: -45.41 },
          ],
        },
      ],
    },
  ],
  bercos: [
    { name: "Berço 101", lat: -23.81, lng: -45.39, image: "img/x.png" },
    { name: "Sem posição", lat: null, lng: null },
  ],
};

const titulo = (no) => no.label.replace(/<[^>]+>/g, "");
const itens = (no) => no.children.map((c) => c.label);

describe("AisMap.getOverlayTree", () => {
  test("sem praticagem: mantém exatamente os grupos atuais", () => {
    const arvore = new AisMap({ containerId: "map" }).getOverlayTree(dataStartup);

    assert.deepEqual(arvore.map(titulo), [
      "Cartas Náuticas",
      "Exibir Informações",
      "Tipos de Embarcações",
      "Layers",
      "Berços de atracação",
    ]);
    assert.deepEqual(itens(arvore[0]), [
      "Parte Sul - Porto de Santos",
      "Parte Norte - Porto de Santos",
      "Proximidades - Porto de São Sebastião",
    ]);
    assert.deepEqual(itens(arvore[1]), [
      "Nome da embarcação",
      "Velocidade e direção",
    ]);
    assert.equal(arvore[2].children.length, 6);
    assert.deepEqual(itens(arvore[3]), ["Área de fundeio", "Sea"]);
    assert.deepEqual(itens(arvore[4]), ["Berço 101"]);
    for (const grupo of arvore) {
      for (const item of grupo.children) assert.ok(item.layer instanceof L.Layer);
    }
  });

  test("com praticagem: acrescenta o grupo Praticagem por último", () => {
    const map = criarMapa();
    const ais = new AisMap({
      containerId: "map",
      praticagem: new Praticagem({ storage: criarStorage() }),
    });
    ais.map = map;

    const semPraticagem = new AisMap({ containerId: "map" }).getOverlayTree(
      dataStartup
    );
    const arvore = ais.getOverlayTree(dataStartup);

    assert.equal(arvore.length, semPraticagem.length + 1);
    assert.deepEqual(arvore.slice(0, -1).map(titulo), semPraticagem.map(titulo));
    assert.equal(titulo(arvore.at(-1)), "Praticagem");
    assert.deepEqual(itens(arvore.at(-1)), ["Setores angulares"]);
    map.remove();
  });
});
