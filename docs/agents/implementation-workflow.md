# Workflow de implementação

Como o agente decide entre **implementar direto** ou **apresentar plano e aguardar aprovação**.

Regra resumida em `.cursor/rules/repository-instructions.mdc` (sync com `.github/copilot-instructions.md`).

## Dois modos

### Modo A — PRD ou issue acordada (implementação direta)

Use quando o humano pede explicitamente implementar um documento em `docs/planning/`.

**O PRD/issue já é a spec aprovada** — não repita plano nem peça nova aprovação na conversa.

Antes de codar, leia:

1. `docs/planning/<feature-slug>/PRD.md`
2. Issue referenciada (se o pedido citar uma)
3. `docs/context/context.md`
4. `docs/architecture/architecture.md`
5. ADRs relevantes em `docs/adr/`

Depois: implemente, marque critérios de aceite na issue e atualize `Status:` conforme `triage-labels.md`.

**Exemplos de pedido válido:**

- "Implemente o PRD em `docs/planning/onboarding-v2/PRD.md`"
- "Implemente `docs/planning/onboarding-v2/issues/03-validacao-cpf.md`"

### Modo B — Pedido aberto (plano + aprovação)

Use para qualquer pedido de código **sem** PRD/issue explícita como escopo fechado.

**Fluxo:**

1. Ler docs e explorar código (somente leitura).
2. Apresentar **Plano de implementação** (template abaixo).
3. Encerrar com: **"Aguardo sua aprovação para implementar."**
4. **Não** editar arquivos até aprovação explícita.

**Exemplos de pedido que exigem plano:**

- "Cria um endpoint que soma dois números"
- "Adiciona validação de CPF no candidato"
- "Melhora o fluxo de entuba de lote"

## Template — Plano de implementação

```markdown
## Plano de implementação

### Objetivo
O que será entregue.

### Escopo
- Incluído: …
- Fora do escopo: …

### Abordagem
Camadas, módulos e arquivos previstos.

### Ressalvas e riscos
Impactos, dependências, breaking changes, segurança, performance.

### Premissas
O que foi assumido por falta de spec.

### Alternativas
Trade-offs relevantes (se houver).

### Testes / validação
Como verificar o resultado.

### Perguntas em aberto
Decisões que precisam do humano antes de codar.

---
Aguardo sua aprovação para implementar.
```

## Quando voltar ao Modo B mesmo citando PRD

| Situação | Comportamento |
|----------|---------------|
| "Implemente o PRD X" (escopo fechado) | Modo A — direto |
| "Implemente o PRD X, mas muda Y" | Modo B — plano (escopo alterado) |
| "Implementa só a parte de validação do PRD X" | Modo B — escopo parcial |
| PRD conflita com código ou docs atuais | Modo B — plano com ressalvas do conflito |
| PRD sem critérios de aceite claros | Modo B — plano + perguntas |

## Aprovação explícita (Modo B)

Frases que autorizam implementação:

- "aprovado"
- "pode implementar"
- "segue"
- "implementa"

Ambíguas (não contam como aprovação sozinhas): "ok", "beleza", "manda ver" — peça confirmação se o escopo for relevante.

Após aprovação, implemente conforme o plano apresentado. Se o escopo mudar depois, apresente novo plano.

## Exceções (sem plano, sem PRD)

- Human pede descartar/reverter alterações
- Responder perguntas sem alterar código
- Typo/formato trivial **com** pedido explícito: "pode corrigir direto"

## O que conta como "implementação"

Exige gate Modo B (ou PRD/issue no Modo A):

- Criar, editar ou apagar código, configs, migrations, testes
- Criar commits, PRs ou pushes (commits/PRs também exigem pedido explícito separado)
- Alterar docs de produto (`docs/planning/`, `context.md`, `architecture.md`, ADRs) por iniciativa do agente

Não exige gate:

- Ler arquivos e explorar o repositório
- Apresentar plano, explicação ou review
- Alterar documentação **quando o humano pediu explicitamente** (ex.: "aplique essas mudanças na doc")
