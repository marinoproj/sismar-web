---
name: to-issues
description: Quebra um plano, spec ou PRD em issues independentes no issue tracker do projeto usando fatias verticais (tracer bullets). Use quando o usuário quiser converter um plano em issues, criar tickets de implementação ou decompor trabalho em issues.
---

# Para Issues

Quebre um plano em issues independentes usando fatias verticais (tracer bullets).

O issue tracker e o vocabulário de labels de triage devem ter sido fornecidos a você.

## Processo

### 1. Reunir contexto

Trabalhe com o que já estiver no contexto da conversa. Se o usuário passar uma referência de issue (número, URL ou path) como argumento, busque-a no issue tracker e leia o corpo completo e os comentários.

### 2. Explorar o codebase (opcional)

Se você ainda não explorou o codebase, faça-o para entender o estado atual do código. Títulos e descrições das issues devem usar o vocabulário do glossário de domínio do projeto e respeitar ADRs na área que você está tocando.

### 3. Rascunhar fatias verticais

Quebre o plano em issues **tracer bullet**. Cada issue é uma fatia vertical fina que atravessa TODAS as camadas de integração de ponta a ponta, NÃO uma fatia horizontal de uma única camada.

As fatias podem ser 'HITL' ou 'AFK'. Fatias HITL exigem interação humana, como uma decisão arquitetural ou revisão de design. Fatias AFK podem ser implementadas e mergeadas sem interação humana. Prefira AFK em vez de HITL quando possível.

<vertical-slice-rules>
- Cada fatia entrega um caminho estreito mas COMPLETO por todas as camadas (schema, API, UI, tests)
- Uma fatia concluída é demonstrável ou verificável por conta própria
- Prefira muitas fatias finas em vez de poucas grossas
</vertical-slice-rules>

### 4. Validar com o usuário

Apresente a decomposição proposta como lista numerada. Para cada fatia, mostre:

- **Title**: nome descritivo curto
- **Type**: HITL / AFK
- **Bloqueado por**: quais outras fatias (se houver) devem ser concluídas primeiro
- **User stories cobertas**: quais user stories esta fatia endereça (se o material de origem tiver)

Pergunte ao usuário:

- A granularidade parece adequada? (muito grossa / muito fina)
- Os relacionamentos de dependência estão corretos?
- Alguma fatia deve ser mergeada ou dividida ainda mais?
- As fatias corretas estão marcadas como HITL e AFK?

Itere até o usuário aprovar a decomposição.

### 5. Publicar as issues no issue tracker

Para cada fatia aprovada, publique uma nova issue no issue tracker. Use o template de corpo de issue abaixo. Essas issues são consideradas prontas para agentes AFK, então publique-as com a label de triage correta, salvo instrução em contrário.

Publique as issues em ordem de dependência (bloqueadores primeiro) para poder referenciar identificadores reais de issue no campo "Bloqueado por".

<issue-template>
## Issue pai

Uma referência à issue pai no issue tracker (se a origem foi uma issue existente, caso contrário omita esta seção).

## O que construir

Uma descrição concisa desta fatia vertical. Descreva o comportamento de ponta a ponta, não a implementação camada por camada.

Evite paths de arquivo ou snippets de código específicos — eles ficam obsoletos rápido. Exceção: se um protótipo produziu um snippet que codifica uma decisão com mais precisão do que a prosa (state machine, reducer, schema, type shape), inclua-o aqui e note brevemente que veio de um protótipo. Recorte para as partes ricas em decisão — não uma demo funcional, apenas o que importa.

## Critérios de aceite

- [ ] Critério 1
- [ ] Critério 2
- [ ] Critério 3

## Bloqueado por

- Uma referência ao ticket bloqueador (se houver)

Ou "Nenhum — pode começar imediatamente" se não houver bloqueadores.

</issue-template>

NÃO feche nem modifique nenhuma issue pai.
