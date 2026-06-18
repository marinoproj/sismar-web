---
description: Instruções do repositório — documentação, skills, implementação e issues locais
---

# Instruções do repositório

Briefing para assistentes de IA neste projeto. Mantenha respostas concisas e alinhadas ao que já existe no código e na documentação.

## Documentação

Antes de inferir domínio, arquitetura ou decisões, consulte os arquivos que existirem neste repositório:

| Documento | Caminho típico | Quando consultar |
|-----------|----------------|------------------|
| Contexto / glossário | `docs/context/context.md` | Termos de domínio, linguagem ubíqua, conceitos de negócio |
| Arquitetura | `docs/architecture/architecture.md` | Onde colocar código, fluxos, integrações, estrutura |
| Decisões (ADRs) | `docs/adr/*.md` | Porquês não óbvios no código |
| Agentes / convenções | `docs/agents/` | Issue tracker, labels, workflows de agente |

Se um arquivo não existir, derive do código — não invente documentação paralela sem pedido.

## Skills

Skills ficam em `skills/<nome-da-skill>/` (arquivo `skill.md` ou `SKILL.md`). Cada skill descreve um workflow especializado.

**Regra:** nunca invoque uma skill automaticamente. Se o pedido se encaixar em uma skill, **informe que existe**, explique brevemente o que ela faz e **pergunte se deseja usá-la**. Só siga a skill após confirmação.

| Skill | Quando sugerir | Exemplo de pedido do usuário |
|-------|----------------|------------------------------|
| `grill-with-docs` | Gerar ou evoluir documentação de **contexto** (glossário, domínio) | "Quero documentar o contexto da aplicação" / "Preciso de um glossário do domínio" |
| `architecture-doc-generator` | Gerar ou atualizar documentação de **arquitetura** | "Gere a documentação de arquitetura" / "Preciso de um guia de onde colocar código" |
| `grill-for-adr` | Registrar uma **decisão arquitetural** (ADR) via conversa | "Quero documentar por que escolhemos X" / "Preciso de um ADR sobre …" |
| `grill-me` | Estressar um plano ou design **sem** foco em documentação | "Grill me sobre esse plano" / "Quero refinar essa ideia antes de codar" |
| `to-prd` | Transformar o contexto da conversa em um **PRD** no issue tracker | "Gere um PRD a partir do que discutimos" / "Quero documentar essa feature como PRD" |
| `to-issues` | Quebrar um plano ou PRD em **issues** (vertical slices / tracer bullets) | "Quebre esse PRD em issues" / "Crie tickets de implementação a partir do plano" |
| `translate-skill-pt` | Traduzir uma skill do inglês para português brasileiro | "Traduza essa skill para português" / "Localize o skill.md em PT-BR" |

Outras skills podem existir em `skills/` — leia a `description` no frontmatter de cada uma antes de sugerir.

**Exemplo de como oferecer (não executar direto):**

> Existe a skill `architecture-doc-generator` para gerar `docs/architecture/architecture.md` a partir do código e das docs existentes. Quer que eu use essa skill?

## Gate de implementação (obrigatório)

Vale em **qualquer modo** (Agent, Ask, etc.). Detalhes e exemplos: `docs/agents/implementation-workflow.md`.

### Dois modos

| Modo | Quando | Comportamento |
|------|--------|---------------|
| **PRD/issue acordada** | Pedido explícito para implementar PRD ou issue em `docs/planning/` | Implemente direto — o PRD substitui plano e aprovação na conversa |
| **Pedido aberto** | Qualquer outro pedido de código | Plano + ressalvas → **pare** → aguarde aprovação explícita |

### Quando NÃO pedir plano nem aprovação

Implemente direto (sem plano intermediário) quando **todas** forem verdadeiras:

- O humano pedir explicitamente implementar um **PRD** ou **issue** em `docs/planning/`
- O PRD/issue descrever escopo, critérios de aceite e comportamento esperado
- O pedido **não** adicionar requisitos novos além do PRD/issue

Leia antes de codar: PRD → issue referenciada (se houver) → `context.md` → `architecture.md` → ADRs relevantes.

**Frases-gatilho:** "Implemente o PRD em …", "Implemente a issue …", "Implemente docs/planning/…/issues/01-…"

### Quando pedir plano e aprovação

Para pedidos abertos ou ambíguos — ex.: "cria um endpoint", "adiciona validação", "melhora o fluxo".

1. Leia a documentação existente relevante (`context.md`, `architecture.md`, ADRs, issues em `docs/planning/`).
2. Explore o código só o necessário para montar o plano.
3. Apresente um **Plano de implementação** (template em `implementation-workflow.md`) e **pare**.
4. **Não** crie, edite ou apague arquivos até aprovação explícita (ex.: "aprovado", "pode implementar", "segue").

### Quando AINDA pedir plano (mesmo com PRD)

- Escopo parcial ou ambíguo ("implementa só a parte X", "adapta o PRD")
- Conflito entre PRD/issue e código ou docs atuais
- Falta de critério de aceite ou decisão arquitetural no PRD
- Requisitos novos na mesma mensagem ("implemente o PRD X, mas muda Y")

### Exceções ao gate (sem plano)

- Reverter ou descartar trabalho quando o humano pedir
- Responder dúvidas sem alterar código
- Correções triviais de typo/formato **somente** se o humano disser "pode corrigir direto"

## Ao implementar

1. **Classifique o pedido** conforme o gate acima (PRD/issue acordada vs pedido aberto).

2. **Se pedido aberto e spec insuficiente:**
   - Explore o código para identificar lacunas.
   - **Recomende** a skill `grill-me` — mas **pergunte antes** de usá-la.
   - Se `grill-me` não existir, faça perguntas diretas (uma por vez) até entendimento compartilhado.
   - Apresente plano e aguarde aprovação antes de codar.

3. **Implemente** seguindo convenções do código existente no módulo/pasta tocado. Mudanças mínimas e focadas.

4. **Ao terminar**, se a mudança impactou arquitetura, negócio, premissas ou decisões, **atualize a documentação correspondente** (ou proponha a atualização e peça confirmação):
   - Domínio / termos → `docs/context/context.md`
   - Estrutura / fluxos / onde codificar → `docs/architecture/architecture.md`
   - Decisão difícil de reverter → novo ADR em `docs/adr/` (considere sugerir `grill-for-adr`)
   - Escopo / requisitos → issue ou PRD em `docs/planning/`

Não crie commits, PRs ou pushes sem pedido explícito.

## Issues

Issues e PRDs são **arquivos Markdown locais**, não GitHub Issues.

| Item | Caminho |
|------|---------|
| Feature / epic | `docs/planning/<feature-slug>/` |
| PRD | `docs/planning/<feature-slug>/PRD.md` |
| Issue de implementação | `docs/planning/<feature-slug>/issues/<NN>-<slug>.md` |

Convenções completas: `docs/agents/issue-tracker.md`  
Labels de triagem: `docs/agents/triage-labels.md`

Ao trabalhar em uma feature, leia o PRD e a issue referenciada antes de codar. Ao concluir trabalho ligado a uma issue, atualize o `Status:` no arquivo conforme `triage-labels.md`.
