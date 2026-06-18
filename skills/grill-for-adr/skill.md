---
name: grill-for-adr
description: >-
  Sessão conversacional para extrair, validar e registrar Architecture Decision
  Records (ADRs) em docs/adr/. Entrevista uma decisão por vez, confronta com código
  e ADRs existentes, e só grava quando a decisão cristalizar. Use quando o usuário
  quiser documentar uma decisão arquitetural, criar ADR, ou registrar o porquê de
  algo no sistema.
---

<what-to-do>

Conduza uma entrevista focada em **uma decisão arquitetural** por vez até chegarmos a entendimento compartilhado e, se qualificar, a um ADR escrito.

Para cada pergunta, ofereça sua **resposta recomendada** com base no código, nos ADRs existentes e no que o usuário já disse.

Faça **uma pergunta por vez** e espere a resposta antes de continuar.

Se a resposta estiver no código ou na documentação, explore o repositório em vez de perguntar.

Quando a decisão cristalizar e passar nos três critérios, escreva o ADR em `docs/adr/` seguindo [adr-format.md](adr-format.md).

Se a decisão **não** qualificar, diga por qual critério falhou e encerre sem criar arquivo — ou ajude a reformular até qualificar.

</what-to-do>

<supporting-info>

## Escopo

Esta skill é **somente para ADRs**. Não atualiza `context.md` nem gera documentação de arquitetura. Termos de domínio mencionados na conversa podem ser referenciados a `docs/context/context.md`, mas glossário não é escopo desta skill.

## Antes de perguntar

1. Leia todos os arquivos em `docs/adr/` — evite duplicar ou contradizer decisões existentes.
2. Se existir `docs/context/context.md`, use a terminologia canônica ao formular perguntas e o ADR final.
3. Se o usuário descrever comportamento, confira no código. Contradição → surface imediatamente: "O código faz X, mas você disse Y — qual prevalece?"

## Fluxo da conversa

### 1. Capturar a decisão bruta

Abra com o que o usuário trouxe. Reformule em uma frase: "Parece que a decisão é: … — está certo?"

Se o pedido for vago ("documenta nossa arquitetura"), ajude a reduzir a **uma** decisão específica antes de continuar.

### 2. Triagem rápida (três critérios)

Antes de aprofundar, avalie mentalmente:

| Critério | Pergunta guia |
|----------|---------------|
| Difícil de reverter? | Quanto custaria mudar isso daqui a 6 meses? |
| Surpreendente? | Um dev novo olhando o código perguntaria "por quê"? |
| Trade-off real? | O que foi rejeitado e por quê? |

Se falhar cedo, diga qual critério falta e proponha: abandonar, reformular, ou seguir só para esclarecer (sem ADR).

### 3. Aprofundar ramo a ramo

Caminhe a árvore de decisão **uma dependência por vez**. Tópicos típicos (só os relevantes):

- **Contexto** — que problema ou restrição forçou a decisão?
- **Alternativas** — o que mais foi considerado? por que não?
- **Decisão** — o que exatamente ficou decidido? (preciso, testável)
- **Consequências** — o que muda para quem codifica, opera ou integra?
- **Limites** — o que esta decisão **não** cobre?

Invente **cenários concretos** para testar a decisão: "Se amanhã precisarmos de X, essa decisão ainda segura?"

### 4. Confrontar documentação existente

- ADR existente conflitante → apontar e perguntar se este **substitui** (superseded) ou **complementa**.
- ADR existente redundante → dizer que já está coberto e citar qual.
- Código divergente → decisão documenta intenção ou código documenta realidade? Alinhar antes de escrever.

### 5. Escrever o ADR

Somente quando:

- os três critérios estão claros;
- alternativa rejeitada está explícita (mesmo que em uma frase);
- não há conflito não resolvido com ADRs anteriores.

**Ao escrever:**

- Numere conforme [adr-format.md](adr-format.md)
- Título = decisão, não processo ("Deploy via Helm chart", não "Como fazemos deploy")
- Corpo = contexto + decisão + motivo; alternativa rejeitada inline quando couber
- Seções opcionais (Consequences, Considered Options) só se agregarem — não preencher por template
- Idioma: **português**, mesmo tom dos ADRs existentes no repo

Mostre o ADR ao usuário e pergunte se aprova antes de considerar encerrado — a menos que ele peça para gravar direto.

## Estilo de pergunta

Modelo:

> **Pergunta:** {pergunta única e específica}
>
> **Recomendação:** {sua leitura com base em código/docs, ou opção que favorece simplicidade reversível}

Evite listas de 5 perguntas de uma vez. Evite jargão que não esteja no `context.md` sem definir.

## Quando **não** criar ADR

Diga explicitamente e não crie arquivo:

- Decisão óbvia e reversível em um PR
- Detalhe de implementação sem trade-off
- Usuário só quer explicar código existente sem decisão nova ("como funciona" → apontar código, não ADR)
- Falta alternativa rejeitada — ajude o usuário a articular uma antes de gravar

## Estrutura de arquivos

```
docs/adr/
  0001-slug.md
  0002-slug.md
  ...
```

Crie `docs/adr/` lazy — no momento de gravar o primeiro ADR da sessão.

</supporting-info>
