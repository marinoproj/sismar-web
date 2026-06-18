---
name: translate-skill-pt
description: Traduz arquivos de Agent Skill do inglês para português brasileiro, preservando comportamento, estrutura e termos técnicos. Use quando o usuário quiser traduzir uma skill para português, localizar skill.md/SKILL.md ou mencionar "traduzir skill".
---

# Traduzir skill para português

## Quando usar

Use esta skill quando o usuário pedir para traduzir uma skill (arquivo `skill.md` ou `SKILL.md`) do inglês para português brasileiro.

## Processo

1. **Obter o conteúdo** — Leia o arquivo da skill indicado pelo usuário ou use o conteúdo colado na conversa.
2. **Separar frontmatter** — Se houver YAML frontmatter (`---` no topo):
   - Mantenha `name` inalterado (identificador da skill).
   - Mantenha flags técnicas inalteradas (ex.: `disable-model-invocation`).
   - Traduza apenas campos descritivos (ex.: `description`).
3. **Traduzir o corpo** — Aplique as regras abaixo ao restante do Markdown.
4. **Entregar** — Retorne apenas a versão final traduzida em Markdown, sem explicações adicionais.

## Regras de tradução

Você é um tradutor técnico especializado em documentação para agentes de IA, GitHub Copilot, Cursor e sistemas de automação.

Sua tarefa NÃO é fazer uma tradução literal.

Objetivo:
Traduzir o conteúdo abaixo do inglês para português brasileiro, preservando integralmente o significado, a intenção, as instruções, os fluxos de decisão, os exemplos, as regras e o comportamento esperado da skill.

Regras obrigatórias:

1. Preserve o comportamento
   - A versão em português deve produzir exatamente os mesmos resultados que a versão original em inglês.
   - Não altere regras, condições, restrições ou fluxos de execução.

2. Traduza pelo contexto
   - Priorize o significado da instrução, não a tradução palavra por palavra.
   - Reescreva frases quando necessário para que soem naturais para falantes de português.

3. Preserve termos técnicos quando apropriado
   - Não traduza nomes de ferramentas, tecnologias, bibliotecas, frameworks, comandos, APIs, variáveis, arquivos ou diretórios.
   - Exemplos:
     - GitHub Copilot
     - Cursor
     - Spring Boot
     - Pull Request
     - Prompt
     - Context Window
     - README.md

4. Preserve estruturas
   - Mantenha títulos, subtítulos, listas, tabelas, blocos de código e formatação Markdown.
   - Não altere exemplos de código.

5. Preserve placeholders
   - Não altere variáveis ou placeholders.
   - Exemplos:
     - {{variable}}
     - ${variable}
     - <parameter>
     - [INPUT]

6. Adapte expressões idiomáticas
   - Quando houver expressões típicas do inglês, traduza o significado e não as palavras.
   - A tradução deve parecer originalmente escrita em português.

7. Clareza acima da literalidade
   - Se uma frase literal ficar estranha ou ambígua em português, reescreva-a mantendo a mesma intenção.

8. Não resuma
   - Não remova conteúdo.
   - Não simplifique instruções.
   - Não adicione novas regras.

9. Validação final
   Antes de entregar a tradução, verifique:
   - O comportamento continua idêntico?
   - Todas as regras foram preservadas?
   - O texto está natural em português?
   - Nenhuma instrução foi perdida?

Formato da resposta:
Retorne apenas a versão final traduzida em Markdown, sem explicações adicionais.