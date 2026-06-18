# Formato de context.md

## Estrutura

```md
# {Nome do Contexto}

{Uma ou duas frases descrevendo o que é este contexto e por que existe.}

## Linguagem

**Order**:
{Uma ou duas frases descrevendo o termo}
_Avoid_: Purchase, transaction

**Invoice**:
Um pedido de pagamento enviado ao cliente após a entrega.
_Avoid_: Bill, payment request

**Customer**:
Uma pessoa ou organização que realiza pedidos.
_Avoid_: Client, buyer, account
```

## Regras

- **Seja opinativo.** Quando existirem várias palavras para o mesmo conceito, escolha a melhor e liste as outras em `_Avoid_`.
- **Mantenha definições enxutas.** Uma ou duas frases no máximo. Defina o que É, não o que faz.
- **Inclua apenas termos específicos do contexto deste projeto.** Conceitos gerais de programação (timeouts, tipos de erro, padrões utilitários) não pertencem, mesmo que o projeto os use extensivamente. Antes de adicionar um termo, pergunte: é um conceito único deste contexto ou um conceito geral de programação? Apenas o primeiro pertence.
- **Agrupe termos em subseções** quando surgirem agrupamentos naturais. Se todos os termos pertencem a uma única área coesa, uma lista plana é suficiente.

## Repositório único

**Contexto único (maioria dos repositórios):** Um `context.md` em docs/context/ na raiz do repositório.

A skill infere qual estrutura se aplica:

- Se existir apenas um `context.md` em docs/context/, contexto único
- Se não existir, crie um `context.md` em docs/context/ sob demanda quando o primeiro termo for resolvido

Quando existirem múltiplos contextos, infira a qual o tópico atual se relaciona. Se não estiver claro, pergunte.
