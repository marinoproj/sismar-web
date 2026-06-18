# Formato de ADR

ADRs ficam em `docs/adr/` e usam numeração sequencial: `0001-slug.md`, `0002-slug.md`, etc.

Crie o diretório `docs/adr/` sob demanda — apenas quando o primeiro ADR for necessário.

## Template

```md
# {Título curto da decisão}

{1-3 frases: qual é o contexto, o que decidimos e por quê.}
```

É isso. Um ADR pode ser um único parágrafo. O valor está em registrar *que* uma decisão foi tomada e *por quê* — não em preencher seções.

## Seções opcionais

Inclua apenas quando agregarem valor genuíno. A maioria dos ADRs não precisará delas.

- **Status** frontmatter (`proposed | accepted | deprecated | superseded by ADR-NNNN`) — útil quando decisões forem revisitadas
- **Considered Options** — apenas quando as alternativas rejeitadas valem a pena lembrar
- **Consequences** — apenas quando efeitos downstream não óbvios precisarem ser destacados

## Numeração

Varra `docs/adr/` em busca do maior número existente e incremente em um.

## Quando oferecer um ADR

Todas estas três condições devem ser verdadeiras:

1. **Difícil de reverter** — o custo de mudar de ideia depois é significativo
2. **Surpreendente sem contexto** — um leitor futuro olhará o código e se perguntará "por que diabos fizeram assim?"
3. **Resultado de trade-off real** — havia alternativas genuínas e você escolheu uma por razões específicas

Se uma decisão é fácil de reverter, pule — você simplesmente reverterá. Se não é surpreendente, ninguém vai se perguntar por quê. Se não havia alternativa real, não há nada a registrar além de "fizemos o óbvio".

### O que qualifica

- **Forma arquitetural.** "Estamos usando monorepo." "O write model é event-sourced, o read model é projetado no Postgres."
- **Padrões de integração entre contextos.** "Ordering e Billing se comunicam via domain events, não HTTP síncrono."
- **Escolhas de tecnologia com lock-in.** Banco de dados, message bus, provedor de auth, alvo de deploy. Não toda biblioteca — apenas as que levariam um trimestre para trocar.
- **Decisões de limite e escopo.** "Dados de Customer pertencem ao contexto Customer; outros contextos referenciam apenas por ID." Os não explícitos valem tanto quanto os sim.
- **Desvios deliberados do caminho óbvio.** "Estamos usando SQL manual em vez de ORM porque X." Qualquer coisa em que um leitor razoável assumiria o oposto. Isso impede o próximo engenheiro de "corrigir" algo que foi deliberado.
- **Restrições não visíveis no código.** "Não podemos usar AWS por requisitos de compliance." "Tempos de resposta devem ficar abaixo de 200ms por contrato da API do parceiro."
- **Alternativas rejeitadas quando a rejeição não é óbvia.** Se você considerou GraphQL e escolheu REST por razões sutis, registre — senão alguém sugerirá GraphQL de novo em seis meses.
