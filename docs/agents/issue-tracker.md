# Issue tracker: Markdown local

Issues e PRDs deste repositório ficam como arquivos Markdown em `docs/planning/`.

## Convenções

- Uma feature por diretório: `docs/planning/<feature-slug>/`
- O PRD é `docs/planning/<feature-slug>/PRD.md`
- Issues de implementação são `docs/planning/<feature-slug>/issues/<NN>-<slug>.md`, numeradas a partir de `01`
- O estado de triage é registrado como linha `Status:` perto do topo de cada arquivo de issue (veja `triage-labels.md` para as strings de papel)
- Comentários e histórico de conversa são acrescentados ao final do arquivo sob o heading `## Comments`

## Quando uma skill disser para publicar no issue tracker

Crie um novo arquivo em `docs/planning/<feature-slug>/` (criando o diretório se necessário).

## Quando uma skill disser para buscar o ticket relevante

Leia o arquivo no path referenciado. O usuário normalmente passará o path ou o número da issue diretamente.
