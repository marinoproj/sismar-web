---
name: to-prd
description: Transforma o contexto da conversa atual em um PRD e publica no issue tracker do projeto. Use quando o usuário quiser criar um PRD a partir do contexto atual.
---

Esta skill pega o contexto da conversa atual e o entendimento do codebase e produz um PRD. NÃO entreviste o usuário — apenas sintetize o que você já sabe.

O issue tracker e o vocabulário de labels de triage devem ter sido fornecidos a você.

## Processo

1. Explore o repositório para entender o estado atual do codebase, se ainda não o fez. Use o vocabulário do glossário de domínio do projeto em todo o PRD e respeite ADRs na área que você está tocando.

2. Esboce as seams em que você vai testar a feature. Seams existentes devem ser preferidas a novas. Use a seam mais alta possível. Se novas seams forem necessárias, proponha-as no ponto mais alto que puder.

Confira com o usuário se essas seams correspondem às expectativas dele.

3. Escreva o PRD usando o template abaixo e publique-o no issue tracker do projeto. Aplique a label de triage `ready-for-agent` — sem necessidade de triage adicional.

<prd-template>

## Declaração do problema

O problema que o usuário enfrenta, da perspectiva do usuário.

## Solução

A solução para o problema, da perspectiva do usuário.

## User stories

Uma lista LONGA e numerada de user stories. Cada user story deve estar no formato:

1. Como <ator>, quero <feature>, para que <benefício>

<user-story-example>
1. Como cliente de banco mobile, quero ver o saldo das minhas contas, para que possa tomar decisões mais informadas sobre meus gastos
</user-story-example>

Esta lista de user stories deve ser extremamente extensa e cobrir todos os aspectos da feature.

## Decisões de implementação

Uma lista de decisões de implementação que foram tomadas. Pode incluir:

- Os módulos que serão construídos/modificados
- As interfaces desses módulos que serão modificadas
- Esclarecimentos técnicos do desenvolvedor
- Decisões arquiteturais
- Mudanças de schema
- Contratos de API
- Interações específicas

NÃO inclua paths de arquivo ou snippets de código específicos. Eles podem ficar obsoletos muito rápido.

Exceção: se um protótipo produziu um snippet que codifica uma decisão com mais precisão do que a prosa (state machine, reducer, schema, type shape), inclua-o na decisão relevante e note brevemente que veio de um protótipo. Recorte para as partes ricas em decisão — não uma demo funcional, apenas o que importa.

## Decisões de teste

Uma lista de decisões de teste que foram tomadas. Inclua:

- Uma descrição do que faz um bom teste (testar apenas comportamento externo, não detalhes de implementação)
- Quais módulos serão testados
- Prior art para os testes (ou seja, tipos similares de testes no codebase)

## Fora de escopo

Uma descrição do que está fora de escopo para este PRD.

## Notas adicionais

Quaisquer notas adicionais sobre a feature.

</prd-template>
