# 04 — Conferência manual de regressão e documentação

Status: ready-for-human
Tipo: HITL

## Issue pai

PRD: `docs/planning/praticagem-setores-angulares/PRD.md`

## O que construir

Validar na aplicação rodando localmente (página AIS realtime) que a feature de setores angulares funciona de ponta a ponta e que nada do que já existia no mapa regrediu. Em seguida, atualizar a documentação do projeto.

Requer interação humana: o usuário precisa descrever o significado operacional dos setores angulares para a praticagem, para registrar no glossário de domínio.

## Critérios de aceite

- [x] Checklist manual da feature na página AIS realtime: ligar/desligar, arrastar, zoom, popup de raio, "Centralizar aqui", recarga da página, tela cheia, troca de mapa base.
- [x] Checklist manual de regressão: embarcações carregam e atualizam; popup de informações da embarcação; régua de distância; layers vindos do banco; berços; carta náutica; nome/velocidade das embarcações; filtro por tipo de embarcação; meteorologia; tela cheia.
- [x] Nenhum erro novo no console do navegador.
- [x] `npm test` passa.
- [x] Documentação de arquitetura atualizada com o novo módulo de praticagem, sua integração com o mapa AIS realtime e como rodar os testes JavaScript.
- [ ] ~~Glossário de domínio atualizado com "Praticagem" e "Setores angulares"~~ — adiado: o significado operacional dos setores ainda não é conhecido; o usuário pediu para não documentar por enquanto.

## Bloqueado por

- `03-popup-raio-e-centralizar.md`

## Comments

- 2026-11-02: usuário testou a feature e o restante do mapa no navegador e confirmou que tudo funciona como esperado. `npm test`: 31 testes passando. `architecture.md` atualizado (stack de testes JS, módulo `sismar.praticagem.js`, mapa de navegação). Glossário adiado a pedido do usuário.
