# Formato de ADR

ADRs ficam em `docs/adr/` com numeração sequencial: `0001-slug.md`, `0002-slug.md`, etc.

Crie `docs/adr/` apenas quando for escrever o primeiro ADR.

## Template

```md
# {Título curto da decisão}

{1–3 frases: qual o contexto, o que decidimos e por quê.}
```

Um ADR pode ser um único parágrafo. O valor está em registrar *que* a decisão foi tomada e *por quê* — não em preencher seções.

## Seções opcionais

Inclua só quando agregarem valor. A maioria dos ADRs não precisa.

- **Status** no frontmatter (`proposed | accepted | deprecated | superseded by ADR-NNNN`) — útil quando a decisão for revisitada
- **Considered Options** — só quando as alternativas rejeitadas valem ser lembradas
- **Consequences** — só quando efeitos não óbvios precisam ser explicitados

## Numeração

Leia `docs/adr/` e use o maior número existente + 1. Slug em minúsculas, palavras separadas por hífen.

## Quando registrar um ADR

Os três critérios devem ser verdadeiros:

1. **Difícil de reverter** — mudar de ideia depois tem custo relevante
2. **Surpreendente sem contexto** — quem ler o código no futuro vai se perguntar "por que fizeram assim?"
3. **Trade-off real** — existiam alternativas genuínas e uma foi escolhida por razões específicas

Se faltar qualquer critério, não crie ADR.

### O que qualifica

- Forma arquitetural (monorepo, camadas, bounded contexts)
- Padrões de integração entre sistemas
- Tecnologias com lock-in (banco, fila, auth, deploy)
- Decisões de fronteira e escopo
- Desvios deliberados do caminho óbvio
- Restrições invisíveis no código (compliance, contrato, SLA)
- Alternativas rejeitadas quando a rejeição não é óbvia

### O que **não** qualifica

- Detalhe de implementação reversível em um PR
- Nomenclatura ou estilo de código
- "Usamos Spring Boot porque já usamos Spring Boot"
- Comportamento já óbvio lendo o código sem ambiguidade
