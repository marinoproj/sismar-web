# 05 — Ajustes visuais dos setores angulares

Status: ready-for-agent
Tipo: AFK

## Issue pai

PRD: `docs/planning/praticagem-setores-angulares/PRD.md`

## O que construir

Ajustes pedidos pelo usuário após testar as issues 01–03 na página AIS realtime:

1. Borda dos setores mais fina (1px).
2. Hachurado interno com linhas paralelas diagonais ("\"), como no desenho de referência, em cinza claro para não carregar o mapa. Espaçamento fixo na tela, mesma densidade em qualquer zoom. Sem preenchimento colorido. Setores continuam sem capturar cliques.
3. Popup da origem maior e mais legível: título "Setores angulares", largura mínima maior, textos, campo e botão maiores.

## Critérios de aceite

- [x] Borda dos setores com 1px, em vermelho escuro.
- [x] Setores preenchidos por hachurado cinza (`#808080`, ~60% de opacidade) de linhas paralelas diagonais "\", espaçamento ~10px na tela.
- [x] O padrão de hachura é criado uma única vez na página.
- [x] Popup com título "Setores angulares", largura mínima ~240px, textos de 14–16px, campo e botão com mais espaço interno.
- [x] Testes automatizados cobrindo espessura da borda, uso do hachurado e suas propriedades, não duplicação do padrão e título/largura do popup.
- [x] `npm test` passa (31 testes).
- [x] Conferência visual no navegador pelo usuário.

## Bloqueado por

- `03-popup-raio-e-centralizar.md`

## Comments

- 2026-11-02: implementado. Aguardando conferência visual do usuário.
- 2026-11-02: usuário conferiu no navegador e aprovou.
