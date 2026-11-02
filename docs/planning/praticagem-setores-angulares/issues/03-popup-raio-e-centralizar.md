# 03 — Popup de raio e "Centralizar aqui"

Status: ready-for-agent
Tipo: AFK

## Issue pai

PRD: `docs/planning/praticagem-setores-angulares/PRD.md`

## O que construir

Ao clicar no ponto de origem dos setores angulares, abre um popup com:

- campo numérico **"Raio (km)"**, com o raio atual preenchido. Ao alterar, os setores e rótulos são redesenhados imediatamente e o raio é salvo no navegador junto com a posição. Valores fora de 1–100 km são limitados ao intervalo; valores não numéricos são ignorados (mantém o raio atual).
- botão **"Centralizar aqui"**, que move a origem para o centro da área visível do mapa, redesenha e salva a nova posição.

O raio salvo é restaurado ao religar o layer e após recarregar a página. O popup funciona também no modo tela cheia.

## Critérios de aceite

- [x] Clicar na origem abre o popup com o raio atual (25 km por padrão).
- [x] Alterar o raio redesenha setores e rótulos imediatamente.
- [x] Raio abaixo de 1 vira 1; acima de 100 vira 100; valor não numérico é ignorado.
- [x] O raio é salvo e restaurado ao religar o layer e após recarregar a página.
- [x] "Centralizar aqui" move a origem para o centro do mapa visível e salva a posição.
- [x] Popup funciona em modo tela cheia.
- [x] Testes automatizados cobrindo: alteração de raio redesenha e persiste; limites 1–100; valor inválido ignorado; restauração do raio em nova instância; "Centralizar aqui" move e persiste.
- [x] `npm test` passa.

## Bloqueado por

- `02-arrastar-origem-e-lembrar-posicao.md`

## Comments

- 2026-11-02: implementado junto com as issues 01–03. Critérios automatizáveis cobertos por `npm test` (28 testes). Itens ainda desmarcados exigem verificação no navegador e serão conferidos na issue 04.
- 2026-11-02: usuário conferiu no navegador os itens restantes; tudo funcionando.
