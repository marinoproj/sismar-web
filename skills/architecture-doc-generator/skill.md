---
name: architecture-doc-generator
description: >-
  Gera documentação de arquitetura a partir do código, docs/adr/ e opcionalmente
  docs/context/context.md. Use quando o usuário pedir documentação de arquitetura,
  architecture.md, guia para IA codificar, ou visão técnica consolidada do sistema.
---

# Architecture Doc Generator

Gera documentação de arquitetura orientada a codificação — um único arquivo que complementa o código sem repeti-lo.

## Propósito

Produzir **um único arquivo** que permita a uma IA (ou desenvolvedor) saber **onde colocar código** e **como o sistema se comporta** — sem repetir o que já está no código, no glossário ou nos ADRs.

| Fonte | Papel |
|---|---|
| Código-fonte | Comportamento e estrutura real — **apontar**, não transcrever |
| `docs/context/context.md` (se existir) | Linguagem ubíqua e conceitos de domínio — **referenciar**, não duplicar |
| `docs/adr/` (se existir) | Decisões não óbvias no código — **referenciar**, não reescrever |
| `docs/architecture/architecture.md` | **Output desta skill** |

## Princípios

1. **Código é a fonte da verdade** — documente o que o código não deixa claro sozinho.
2. **Onde codificar > inventário de módulos** — priorize guia de pacotes e tipos (bean, api, controller, entity, conversor…) em detrimento de listar todos os arquivos.
3. **Comprimir, não catalogar** — tabelas e ponteiros a arquivos/pacotes; evite inventário exaustivo.
4. **Evidência ou omitir** — afirme só o que encontrou; não inclua seção de lacunas.
5. **Terminologia consistente** — se existir `docs/context/context.md`, use os termos definidos lá.
6. **Uma passagem** — investigue e escreva direto no arquivo final.

## O que **não** incluir no output

- Como rodar (build, deploy, configuração de ambiente)
- Variáveis de ambiente ou credenciais
- Catálogo exaustivo de todas as entidades ou endpoints
- Segurança e observabilidade
- Logs e rastreio
- Testes
- Legado, dívida e riscos
- Lacunas

## Output

```
docs/architecture/architecture.md
```

Criar o diretório se não existir. Sobrescrever o arquivo apenas quando o usuário pedir regeneração completa; caso contrário, atualizar as partes afetadas.

---

## Workflow

Execute as etapas **em sequência**. Escreva o arquivo final somente na última etapa.

### Etapa 1 — Ler documentação existente

1. Se existir `docs/context/context.md`, ler por completo.
2. Se existir `docs/adr/`, ler todos os arquivos (decisão + impacto na codificação).
3. Anotar o que **não** reescrever (glossário, ADRs).

### Etapa 2 — Stack (resumida)

Identificar apenas o necessário para contextualizar o projeto:

| O quê | Onde olhar |
|---|---|
| Linguagem e framework | `build.xml`, `nbproject/project.properties` |
| Persistência | `src/conf/persistence.xml` |
| Deploy | `web/WEB-INF/web.xml`, artefato WAR |
| Dependências principais | `nbproject/project.properties` (libs.*) |

**Extrair:** tabela curta stack (linguagem, framework, servidor, banco) — só o confirmado.

**Não extrair:** comandos de build/run, variáveis de configuração, inventário completo de dependências.

### Etapa 3 — Onde fica cada tipo de código (backend)

Mapear **pacotes e convenções** dentro de `src/java/br/com/marino/sismar/`, não a lista completa de arquivos.

| Tipo | Onde procurar no repo |
|---|---|
| APIs REST (JAX-RS `@Path`) | `api/` |
| Managed Beans JSF (`@ManagedBean @ViewScoped`) | `bean/admin/{módulo}/` |
| JPA DAOs (métodos estáticos, recebem `EntityManager`) | `controller/` |
| Entidades JPA (`@Entity @NamedQueries`) | `entity/` |
| Enums de domínio | `enums/` |
| DTOs | `dto/response/` |
| Conversores JSF | `converters/` |
| Filtros (autenticação e controle de acesso) | `filter/` |
| Gestão de sessão HTTP | `session/` |
| Utilitários transversais | `util/` |
| Relatórios (JasperReports) | `reports/` |
| Gráficos | `chart/` |

**Extrair:**
- Tabela **"Onde implementar o quê"** completa (tipo → caminho/pacote + nota breve)
- Convenções de nomenclatura: `{Entidade}Controller`, `{Funcionalidade}Bean`, `{Entidade}Api`, `{Entidade}Converter`
- Padrão de transação: `EntityManagerFactory → EntityManager → begin/commit → finally close`
- Visão resumida das **camadas** (filtro → bean JSF / api JAX-RS → controller DAO → entity JPA) em 1 parágrafo

**Não extrair:** lista de todos os arquivos por pasta, código de métodos.

### Etapa 4 — Estrutura do frontend (XHTML + JavaScript)

Mapear a camada de apresentação em `web/`:

| Tipo | Onde procurar no repo |
|---|---|
| Páginas XHTML por módulo | `web/admin/{módulo}/*.xhtml` |
| Componentes XHTML reutilizáveis | `web/admin/components/` |
| Módulos JavaScript (ES6 / IIFE Singleton) | `web/resources/js/sismar.{módulo}.js` |
| JavaScript auxiliar (sem prefixo `sismar.`) | `web/resources/js/*.js` |
| CSS | `web/resources/css/` |
| Imagens e ícones | `web/resources/img/` |

**Extrair:**
- Tabela de módulos JavaScript: arquivo → responsabilidade → padrão (IIFE Singleton `Sismar.*` vs ES6 Class `export default class`)
- Padrão de organização por módulo: página `web/admin/{módulo}/{página}.xhtml` + Bean `bean/admin/{módulo}/` + módulo JS `sismar.{módulo}.js`
- Padrão de comunicação JS↔Backend — dois mecanismos coexistem:
  1. **REST via `$.ajax` / `fetch`** — JS chama `/api/{recurso}` diretamente e processa o JSON (fluxo principal do mapa Leaflet)
  2. **PrimeFaces AJAX** — `p:commandButton` / `p:ajax` dispara ação no ManagedBean server-side; JS usa callbacks `onsuccess`, `onerror`, e `PF('widgetVar').show()/hide()`

**Não extrair:** código inline dos arquivos JS, lista de todas as variáveis globais.

### Etapa 5 — Comportamento e dados

| O quê | Onde olhar |
|---|---|
| Fluxos ponta a ponta | `api/` → `controller/` → `entity/` |
| Integrações externas | `util/MarineTraffic.java`, `api/WeatherApi.java` |
| Persistência | `entity/` + `src/conf/persistence.xml` |
| Regras de negócio | `bean/`, `util/RuleCondition.java`, `entity/Monitorar.java` |
| Autenticação/acesso | `filter/LoginFilter.java`, `filter/AdminFilter.java`, `util/Util.java` |

**Extrair:**
- **3 a 5 fluxos críticos** — parágrafo + passos numerados
- Tabela de integrações: sistema | direção | onde no código | protocolo | uso
- Tabela de persistência: entidade | tabela | propósito | relacionamentos principais
- Até **10 regras de negócio** com referência a arquivo

**Não extrair:** todos os endpoints, todas as entidades, lista de named queries.

### Etapa 6 — Escrever architecture.md

Montar o documento seguindo o template. Remover seções vazias.

---

## Template de saída

```markdown
# {Nome do Sistema} — Guia de Arquitetura

> Guia de arquitetura para codificação.
> {Se existir: Glossário em [context.md](../context/context.md).}
> {Se existir: Decisões em [docs/adr/](../adr/).}

## Visão geral

{2–3 frases: o que o sistema faz, quem usa, canais principais}

### Stack

| Camada | Tecnologia |
|--------|------------|

## Estrutura do código (backend)

{Parágrafo: camadas e fluxo geral request → bean/api → controller DAO → entity JPA}

### Onde implementar o quê

| Tipo | Onde ir | Notas |
|------|---------|-------|
| API REST (JAX-RS) | `api/` | |
| Managed Bean JSF | `bean/admin/{módulo}/` | `@ManagedBean @ViewScoped` |
| JPA DAO | `controller/` | Métodos estáticos, recebem `EntityManager` |
| Entidade JPA | `entity/` | `@Entity @NamedQueries` |
| Enum | `enums/` | |
| DTO | `dto/response/` | |
| Conversor JSF | `converters/` | |
| Filtro | `filter/` | Login (autenticação) e Admin (acesso por feature) |
| Utilitário | `util/` | |

### Convenções de nomenclatura

{Bullets: `{Entidade}Controller`, `{Funcionalidade}Bean`, `{Entidade}Api`, `{Entidade}Converter`, etc.}

## Estrutura do frontend

{Parágrafo: como XHTML, Beans JSF e JavaScript coexistem; papel do Leaflet e do PrimeFaces}

### Páginas por módulo

| Módulo | Páginas XHTML | Bean JSF | Módulo JS |
|--------|---------------|----------|-----------|

### Módulos JavaScript

| Arquivo | Responsabilidade | Padrão |
|---------|-----------------|--------|

### Comunicação JS ↔ Backend

{Parágrafo descrevendo os dois mecanismos: REST via $.ajax/fetch (mapa) e PrimeFaces AJAX (formulários/dialogs)}

## Fluxos críticos

### {Nome do fluxo}

{Parágrafo curto}

1. {Passo}
2. {Passo}

**Componentes envolvidos:** `{arquivo}` → `{arquivo}` → ...

{Repetir para 3–5 fluxos}

## Integrações externas

| Sistema | Direção | Onde no código | Protocolo | Notas |
|---------|---------|----------------|-----------|-------|

## Persistência

| Entidade | Tabela | Propósito | Relacionamentos principais |
|----------|--------|-----------|---------------------------|

### Relacionamentos principais

{Texto ou lista — entidades centrais e vínculos}

## Regras de negócio no código

{Bullets com referência a arquivo}

## Decisões arquiteturais

{Somente se existir docs/adr/}

| ADR | Resumo | Impacto na codificação |
|-----|--------|------------------------|

## Mapa de navegação

| Preciso... | Começar em |
|------------|------------|
| {tarefa} | `{arquivo}` |
```

---

## Regras de escrita

- Português
- Markdown puro — sem HTML, sem mermaid
- Fluxos com passos numerados
- Tabelas preferidas a parágrafos longos
- Referências: `` `caminho/relativo/Classe.java` ``
- Não copiar trechos grandes de código
- Linkar `context.md` e ADRs — não reescrever

---

## Critérios de encerramento

- [ ] Documentação existente lida (quando presente)
- [ ] Tabela "Onde implementar o quê" cobre APIs REST, Beans JSF, JPA DAOs, entidades, filtros e utilitários
- [ ] Tabela "Onde implementar o quê" não menciona Spring, Gradle, listeners de mensageria ou gateways
- [ ] Seção frontend cobre organização XHTML, módulos JS e padrão de comunicação com o backend
- [ ] 3–5 fluxos críticos com passos numerados
- [ ] Integrações e entidades centrais tabuladas
- [ ] Nenhuma seção proibida presente no documento
- [ ] ~150–350 linhas (comprimir se exceder)

---

## Quando regenerar vs. atualizar

| Situação | Ação |
|----------|------|
| Primeira execução | Gerar arquivo completo |
| Mudança de estrutura, integração ou fluxo | Atualizar partes afetadas |
| Novo ADR ou context.md | Atualizar links e referências |

---

## Escopo

Esta skill **não**:

- Substitui glossário (`context.md`) nem ADRs
- Documenta como rodar, deploy operacional ou configuração de ambiente
- Cataloga todos os arquivos de cada pasta
- Audita segurança, observabilidade, testes ou dívida técnica
