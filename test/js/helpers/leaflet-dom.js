// Ambiente de navegador (jsdom) com o Leaflet real, para testar módulos do mapa no Node.
import { JSDOM } from "jsdom";
import { createRequire } from "node:module";

const require = createRequire(import.meta.url);

function definirGlobal(nome, valor) {
  Object.defineProperty(globalThis, nome, {
    value: valor,
    configurable: true,
    writable: true,
  });
}

export function setupLeaflet() {
  if (globalThis.L) return globalThis.L;

  const { window } = new JSDOM("<!doctype html><html><body></body></html>", {
    url: "http://localhost/",
    pretendToBeVisual: true,
  });

  // jsdom não implementa layout nem createSVGRect: o Leaflet precisa de ambos.
  window.SVGSVGElement.prototype.createSVGRect = () => ({});
  Object.defineProperty(window.HTMLElement.prototype, "clientWidth", {
    get: () => 800,
    configurable: true,
  });
  Object.defineProperty(window.HTMLElement.prototype, "clientHeight", {
    get: () => 600,
    configurable: true,
  });

  definirGlobal("window", window);
  definirGlobal("document", window.document);
  definirGlobal("navigator", window.navigator);
  definirGlobal("Element", window.Element);
  definirGlobal("HTMLElement", window.HTMLElement);

  const L = require("leaflet");
  definirGlobal("L", L);
  return L;
}

export function criarMapa(center = [-23.83, -45.42], zoom = 10) {
  const div = document.createElement("div");
  document.body.appendChild(div);
  return L.map(div, { center, zoom });
}

export function criarStorage(inicial = {}) {
  const dados = new Map(Object.entries(inicial));
  return {
    getItem: (k) => (dados.has(k) ? dados.get(k) : null),
    setItem: (k, v) => dados.set(k, String(v)),
    removeItem: (k) => dados.delete(k),
  };
}

/** Azimute inicial (graus, 0 = norte, sentido horário) de `a` para `b`. */
export function azimute(a, b) {
  const rad = Math.PI / 180;
  const dLng = (b.lng - a.lng) * rad;
  const y = Math.sin(dLng) * Math.cos(b.lat * rad);
  const x =
    Math.cos(a.lat * rad) * Math.sin(b.lat * rad) -
    Math.sin(a.lat * rad) * Math.cos(b.lat * rad) * Math.cos(dLng);
  return (Math.atan2(y, x) / rad + 360) % 360;
}
