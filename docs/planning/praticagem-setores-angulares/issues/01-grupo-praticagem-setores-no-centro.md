# 01 — Grupo Praticagem com setores angulares no centro da tela

Status: ready-for-agent
Tipo: AFK

## Issue pai

PRD: `docs/planning/praticagem-setores-angulares/PRD.md`

## O que construir

Primeira fatia de ponta a ponta: o operador abre o mapa AIS realtime, vê o grupo **"Praticagem"** no controle de layers com o item **"Setores angulares"** (desligado) e, ao ligá-lo, vê os dois setores (56°–96° e 140°–180°, norte verdadeiro, sentido horário) saindo de um ponto de origem no centro da área visível, com raio de 25 km, rótulos de ângulo na ponta de cada linha e estilo vermelho escuro semitransparente. Os setores não capturam cliques: embarcações embaixo deles continuam clicáveis. Desligar o item remove tudo do mapa.

Nesta fatia a origem ainda não é arrastável e nada é persistido.

Inclui a estrutura de testes JavaScript (runner nativo do Node + jsdom + Leaflet 1.9.4 como dependências de desenvolvimento, fora do build/WAR, `node_modules` ignorado no versionamento) e os primeiros testes do módulo.

A integração com a classe do mapa é aditiva: opção `praticagem` opcional; sem ela, a árvore de layers fica idêntica à atual.

## Critérios de aceite

- [x] Grupo "Praticagem" aparece na árvore de layers após os grupos existentes, com o item "Setores angulares".
- [x] O item começa desligado ao abrir a página.
- [x] Ao ligar, a origem aparece no centro do mapa visível e os dois setores são desenhados com raio de 25 km.
- [x] O setor de 180° aponta para o sul e o de 96° levemente abaixo do leste (norte verdadeiro, sentido horário).
- [x] Rótulos 56°, 96°, 140° e 180° aparecem na ponta das respectivas linhas.
- [x] O desenho acompanha o zoom (tamanho geográfico real); a origem tem tamanho fixo na tela.
- [x] Setores não interativos: clicar em uma embarcação dentro de um setor abre o popup da embarcação.
- [x] Desligar o item remove setores, rótulos e origem.
- [x] Ângulos definidos em uma única constante.
- [x] Testes automatizados (via `npm test`) cobrindo: nó da árvore, desligado por padrão, origem no centro ao ligar, vértices nos azimutes/distância esperados, setores não interativos, remoção ao desligar.
- [x] Teste de regressão: montagem da árvore de layers sem a opção `praticagem` inalterada.
- [x] `npm test` passa; build do projeto no NetBeans continua funcionando e o WAR não inclui arquivos de teste nem `node_modules`.

## Bloqueado por

Nenhum — pode começar imediatamente.

## Comments

- 2026-11-02: implementado junto com as issues 01–03. Critérios automatizáveis cobertos por `npm test` (28 testes). Itens ainda desmarcados exigem verificação no navegador e serão conferidos na issue 04.
- 2026-11-02: usuário conferiu no navegador os itens restantes; tudo funcionando.
