---
name: grill-with-docs
description: Sessão de questionamento que desafia o plano contra o modelo de domínio existente, refina a terminologia e atualiza a documentação (context.md, ADRs) inline conforme as decisões se cristalizam. Use quando o usuário quiser estressar um plano contra a linguagem do projeto e as decisões documentadas.
---

<what-to-do>

Entreviste-me de forma incansável sobre cada aspecto deste plano até alcançarmos um entendimento compartilhado. Percorra cada ramo da árvore de design, resolvendo as dependências entre decisões uma a uma. Para cada pergunta, forneça sua resposta recomendada.

Faça as perguntas uma de cada vez, aguardando feedback em cada pergunta antes de continuar.

Se uma pergunta puder ser respondida explorando o codebase, explore o codebase em vez de perguntar.

</what-to-do>

<supporting-info>

## Consciência de domínio

Durante a exploração do codebase, procure também documentação existente:

### Estrutura de arquivos

O repositório tem um único contexto:

```
/
...
├── docs/context/context.md
├── docs/adr/...
...
```

Crie arquivos sob demanda — apenas quando tiver algo para escrever. Se não existir `context.md`, crie um quando o primeiro termo for resolvido. Se não existir `docs/adr/`, crie quando o primeiro ADR for necessário.

## Durante a sessão

### Desafiar contra o glossário

Quando o usuário usar um termo que conflite com a linguagem existente em `context.md`, aponte isso imediatamente. "Seu glossário define 'cancellation' como X, mas você parece querer dizer Y — qual é?"

### Afiar linguagem imprecisa

Quando o usuário usar termos vagos ou sobrecarregados, proponha um termo canônico preciso. "Você está dizendo 'account' — quer dizer Customer ou User? São coisas diferentes."

### Discutir cenários concretos

Quando relacionamentos de domínio estiverem sendo discutidos, estresse-os com cenários específicos. Invente cenários que explorem casos de borda e forcem o usuário a ser preciso sobre os limites entre conceitos.

### Cruzar com o código

Quando o usuário afirmar como algo funciona, verifique se o código concorda. Se encontrar uma contradição, exponha: "Seu código cancela Orders inteiras, mas você acabou de dizer que cancelamento parcial é possível — qual está certo?"

### Atualizar context.md inline

Quando um termo for resolvido, atualize `context.md` na hora. Não acumule — capture conforme acontece. Use o formato em [context-format.md](context-format.md).

`context.md` deve estar totalmente livre de detalhes de implementação. Não trate `context.md` como spec, rascunho ou repositório de decisões de implementação. É um glossário e nada mais.

### Oferecer ADRs com moderação

Ofereça criar um ADR apenas quando as três condições forem verdadeiras:

1. **Difícil de reverter** — o custo de mudar de ideia depois é significativo
2. **Surpreendente sem contexto** — um leitor futuro vai se perguntar "por que fizeram assim?"
3. **Resultado de trade-off real** — havia alternativas genuínas e você escolheu uma por razões específicas

Se qualquer uma das três faltar, pule o ADR. Use o formato em [adr-format](adr-format.md).

</supporting-info>
