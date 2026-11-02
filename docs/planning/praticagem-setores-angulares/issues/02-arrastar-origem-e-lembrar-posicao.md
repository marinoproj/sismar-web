# 02 — Arrastar a origem e lembrar a posição

Status: ready-for-agent
Tipo: AFK

## Issue pai

PRD: `docs/planning/praticagem-setores-angulares/PRD.md`

## O que construir

O operador arrasta o ponto de origem dos setores angulares pelo mapa; os setores e rótulos acompanham em tempo real, sem que o mapa seja arrastado junto. Ao soltar, a posição é salva no navegador (chave `sismar.ais.setoresAngulares`, JSON `{ lat, lng, raioKm }`). Ao desligar e religar o layer, ou ao recarregar a página, os setores reaparecem na posição salva. Sem posição salva, continua aparecendo no centro do mapa visível.

Leitura e escrita no armazenamento são tolerantes a falhas: armazenamento indisponível, que lança exceção ou com conteúdo inválido é tratado como "sem valor salvo", sem quebrar a página.

## Critérios de aceite

- [x] Ponto de origem arrastável; setores e rótulos acompanham durante o arraste.
- [x] Arrastar a origem não move o mapa.
- [x] Ao soltar, a posição é salva no navegador.
- [x] Desligar e religar o layer restaura a posição salva.
- [x] Recarregar a página e ligar o layer restaura a posição salva.
- [x] Sem estado salvo, a origem aparece no centro do mapa visível.
- [x] Armazenamento com JSON inválido, campos ausentes/fora do formato ou que lança exceção não quebra o módulo (usa os padrões).
- [x] O estado ligado/desligado do layer não é persistido.
- [x] Testes automatizados cobrindo: arraste redesenha e persiste; desligar/religar restaura; nova instância do módulo (simulando recarga) restaura; armazenamento corrompido e armazenamento que lança exceção.
- [x] `npm test` passa.

## Bloqueado por

- `01-grupo-praticagem-setores-no-centro.md`

## Comments

- 2026-11-02: implementado junto com as issues 01–03. Critérios automatizáveis cobertos por `npm test` (28 testes). Itens ainda desmarcados exigem verificação no navegador e serão conferidos na issue 04.
- 2026-11-02: usuário conferiu no navegador os itens restantes; tudo funcionando.
