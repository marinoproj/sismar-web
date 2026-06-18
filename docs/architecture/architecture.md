# SISMAR — Guia de Arquitetura

> Guia de arquitetura para codificação.
> Glossário em [context.md](../context/context.md).

## Visão geral

O SISMAR é um sistema web de monitoramento marítimo portuário que combina rastreamento de navios em tempo real via AIS com gestão manual de operações portuárias. É usado por portos, armadores e operadores logísticos, exibindo navios em mapa interativo (Leaflet), histórico de escalas, operações, meteorologia e alertas configuráveis por Poin.

### Stack

| Camada | Tecnologia |
|--------|------------|
| Linguagem | Java 8 |
| Frontend servidor | JSF 2.0 + PrimeFaces 7.0 (Facelets/XHTML) |
| Frontend cliente | JavaScript (jQuery, Leaflet, ES6) |
| API REST | JAX-RS (Java EE 6) — path base `/api` |
| ORM | EclipseLink JPA 2.1 — `persistence-unit: sismarPU` |
| Banco | Microsoft SQL Server |
| Transações | `RESOURCE_LOCAL` — gerenciadas manualmente nos beans |
| Servidor | Apache Tomcat 8.0 |
| Build / Deploy | Ant + NetBeans → WAR `sismar-web-santos.war` |
| Relatórios | JasperReports 5.1.0 |

---

## Estrutura do código (backend)

O backend tem quatro camadas em cascata: **filtros** interceptam toda requisição HTTP e validam autenticação e acesso por feature; **Beans JSF** e **APIs JAX-RS** recebem as interações do usuário (formulários e chamadas REST, respectivamente); os **Controllers (DAOs estáticos)** centralizam o acesso ao banco via `EntityManager`; as **Entidades JPA** mapeiam as tabelas. Não há camada de serviço separada — a lógica de negócio fica nos beans ou nos próprios controllers.

Todo acesso ao banco segue o padrão manual: o bean cria `EntityManagerFactory` → `EntityManager` → `begin()` → operação → `commit()` → `finally close()` em ambos.

### Onde implementar o quê

| Tipo | Onde ir | Notas |
|------|---------|-------|
| API REST (JAX-RS) | `src/.../api/` | `@Path`, `@GET/@POST`, retorna JSON via `JSONObject` |
| Managed Bean JSF | `src/.../bean/admin/{módulo}/` | `@ManagedBean @ViewScoped`, implements `Serializable` |
| JPA DAO | `src/.../controller/` | Métodos **estáticos**, recebem `EntityManager` como parâmetro |
| Entidade JPA | `src/.../entity/` | `@Entity @NamedQueries`, sem herança comum |
| Enum de domínio | `src/.../enums/` | |
| DTO de resposta | `src/.../dto/response/` | POJOs simples, sem anotações |
| Conversor JSF | `src/.../converters/` | Implementa `javax.faces.convert.Converter` |
| Filtro | `src/.../filter/` | `LoginFilter` (autenticação) e `AdminFilter` (acesso por feature) |
| Gestão de sessão | `src/.../session/` | `SessionContext` (Singleton) + `SessionListener` |
| Utilitário transversal | `src/.../util/` | `Util.java` centraliza JWT, MD5, geo, datas, features |
| Relatório | `src/.../reports/` | `.jrxml` + `.jasper` compilado |
| Gráfico | `src/.../chart/` | |

### Convenções de nomenclatura

- Controllers (DAOs): `{Entidade}Controller` — ex.: `NavioController`, `AisController`
- Beans JSF: `{Funcionalidade}Bean` — ex.: `OperacaoBean`, `MooringRealTimeBean`
- APIs REST: `{Recurso}Api` — ex.: `AisApi`, `VesselApi`, `MooringApi`
- Conversores JSF: `{Entidade}Converter` — ex.: `BercoConverter`, `PortConverter`
- Enums: `{Dominio}Enum` — ex.: `StatusOperacaoEnum`, `LadoAtracacaoEnum`
- DTOs: `Response{Dominio}DTO` — ex.: `ResponseDeviceDTO`

---

## Estrutura do frontend

Cada módulo funcional tem três artefatos que trabalham juntos: uma página XHTML que declara os componentes PrimeFaces e referencia o bean server-side; o bean JSF que gerencia estado e ações server-side; e um arquivo JavaScript (`sismar.{módulo}.js`) responsável pela lógica client-side — principalmente o mapa Leaflet e as chamadas REST. Componentes de UI comuns (cabeçalho, menu, rodapé, dialogs reutilizáveis) ficam em `web/admin/components/` como includes XHTML.

### Páginas por módulo

| Módulo | Páginas XHTML (`web/admin/`) | Bean JSF principal | Módulo JS |
|--------|------------------------------|-------------------|-----------|
| AIS realtime | `ais/realtime.xhtml` | `AisBean` | `sismar.ais.js` / `sismar.ais.realtime.js` |
| Playback histórico | `ais/playback.xhtml` | `PlaybackBean` | `sismar.playback.js` |
| Track de navio | `ais/track.xhtml` | — | `sismar.track.js` |
| Berço (mini-mapa) | `ais/berco.xhtml` | `BercoDashBean` | `sismar.berco.js` |
| Porto | `ais/port.xhtml` | `PortoBean` | — |
| Meteorologia | `meteorology/*.xhtml` | `WeatherBean`, `WindyBean` | `meteorology.js`, `sismar.aiswindy.js` |
| Amarrações | `moorings/*.xhtml` | `MooringRealTimeBean` | `atracacao.js` |
| Operações | `operations/*.xhtml` | `OperacaoBean`, `EtapasBean` | — |
| Administração | `administration/*.xhtml` | `BercosBean`, `ClientsBean` | — |
| Configuração | `configuration/*.xhtml` | `UserAddBean`, `UserEditBean` | — |

### Módulos JavaScript

| Arquivo | Responsabilidade | Padrão |
|---------|-----------------|--------|
| `sismar.ais.js` | Mapa AIS em tempo real — marcadores, layers, controles Leaflet | IIFE Singleton (`Sismar.ais`) |
| `sismar.ais.realtime.js` | Reimplementação moderna do mapa AIS | ES6 Class (`export default class AisMap`) |
| `sismar.ais.preview.js` | Preview de embarcação com histórico de posições | IIFE Singleton |
| `sismar.playback.js` | Animação de histórico temporal no mapa | IIFE Singleton |
| `sismar.track.js` | Rastreamento de uma única embarcação | XMLHttpRequest legacy |
| `sismar.berco.js` | Mini-mapa de berço | IIFE Singleton |
| `sismar.aiswindy.js` | Integração com Windy API (camada de vento) | IIFE Singleton |
| `sismar.search-vessels.js` | Busca de navios no mapa | IIFE Singleton |
| `meteorology.js` | Camada meteorológica no mapa | Funções globais |
| `vesselinfoais.js` | Painel de informações de navio | Funções globais |
| `atracacao.js` | Operações de amarração em tempo real | Funções globais |
| `chartline.js` | Gráficos (Chart.js) para sensores e meteorologia | Funções globais |

### Comunicação JS ↔ Backend

Dois mecanismos coexistem no projeto:

**1. REST via `$.ajax` / `fetch`** — usado no mapa e nos módulos JavaScript autônomos. O JS chama diretamente `/api/{recurso}` com header `Authorization: Basic ...` ou `Bearer {jwt}`. O backend (`api/`) retorna JSON construído manualmente via `JSONObject`/`JSONArray`. Este é o fluxo principal para atualização do mapa em tempo real.

**2. PrimeFaces AJAX** — usado nos formulários e dialogs JSF. `p:commandButton` e `p:ajax` disparam ações no ManagedBean server-side sem recarregar a página; o componente `update="..."` atualiza partes do DOM. O JS interage via callbacks: `onsuccess`, `onerror`, e `PF('widgetVar').show()/hide()` para controle de dialogs. Usado em cadastros, operações e configurações.

---

## Fluxos críticos

### 1. AIS em tempo real

Polling contínuo que mantém os navios atualizados no mapa.

1. Usuário acessa `ais/realtime.xhtml` → `AdminFilter` valida feature `ais_realtime`
2. Página carrega `sismar.ais.js` (ou `sismar.ais.realtime.js`); JS chama `GET /api/ais/startup` para carregar layers, berços e Poins iniciais
3. JS inicializa mapa Leaflet com `L.map()`, layers de tiles (Google, OpenSeaMap) e controles customizados
4. Polling periódico: JS chama `GET /api/ais/all` → `AisApi` → `AisController.getListVesselActive()` (native SQL com filtro geoespacial)
5. JSON retorna lista de navios com MMSI, posição, velocidade, rumo, tipo
6. JS atualiza marcadores (polígonos rotacionados por rumo + tooltips com nome do navio) via Leaflet

**Componentes:** `web/admin/ais/realtime.xhtml` → `sismar.ais.js` → `GET /api/ais/all` → `AisApi.java` → `AisController.java` → `entity/Ais.java`

### 2. Clique em navio → popup de informações

1. Usuário clica em marcador Leaflet → `sismar.ais.js` captura evento `click` na layer
2. JS chama `GET /api/vessel?mmsi={mmsi}` → `VesselApi` → `NavioController` + `AisController`
3. JSON retorna: nome, tipo, calado, destino, ETA, última posição AIS, imagem em Base64
4. JS constrói HTML do popup e exibe via `layer.bindPopup(html).openPopup()`
5. Popup contém links para "Track" e "Playback" que abrem `p:dialog` PrimeFaces via `PF('open-period-track').show()`

**Componentes:** `sismar.ais.js` → `GET /api/vessel` → `VesselApi.java` → `NavioController.java`, `AisController.java`

### 3. Login e controle de acesso por feature

1. Usuário acessa `/faces/index.xhtml?auth=base64(login:senha)`
2. `LoginFilter` decodifica Base64, busca usuário no banco, valida senha MD5
3. Cria/atualiza `UsuariosWebSessao` com IP e timestamp; armazena `UsuariosWeb` na sessão HTTP
4. Redireciona para `realtime.xhtml` (se tem feature `ais_realtime`) ou `home.xhtml`
5. Para cada página admin seguinte, `AdminFilter` verifica feature mapeada e redireciona para `notavailable.xhtml` se o usuário não tiver acesso

**Componentes:** `filter/LoginFilter.java` → `filter/AdminFilter.java` → `session/SessionContext.java` → `util/Util.java` (lista de features)

### 4. Registro de Operação portuária

1. Operador acessa `operations/*.xhtml`; `OperacaoBean` (`@PostConstruct`) carrega navios e berços via controllers estáticos
2. Usuário preenche navio, berço, cliente e data de início; `p:commandButton` dispara `OperacaoBean.insert()` via PrimeFaces AJAX
3. Bean cria `EntityManager` → `begin()` → `persist(Operacao)` → `commit()` → `finally close()`
4. Dentro da operação, `EtapasBean` gerencia adição de Etapas (instâncias de `EtapasOperacao`), cada uma podendo ter Eventos, Interrupções e Observações
5. Operação permanece "Em andamento" enquanto `dataTermino == null`; finalização seta `dataTermino`

**Componentes:** `web/admin/operations/` → `OperacaoBean.java`, `EtapasBean.java` → `controller/AtracacaoController.java` → `entity/Operacao.java`, `EtapasOperacao.java`

### 5. Playback histórico

1. Usuário seleciona período em `p:dialog` aberto a partir do popup de navio
2. `PlaybackBean.openPlayback()` redireciona para `playback.xhtml?mmsi=...&start=...&end=...`
3. `sismar.playback.js` lê parâmetros da URL e chama `GET /api/ais/playback`
4. `AisApi` retorna lista ordenada de registros AIS no período
5. JS anima os marcadores no mapa em sequência temporal; controles de slider permitem navegar pelo histórico

**Componentes:** `PlaybackBean.java` → `web/admin/ais/playback.xhtml` → `sismar.playback.js` → `GET /api/ais/playback` → `AisApi.java` → `AisController.java`

---

## Integrações externas

| Sistema | Direção | Onde no código | Protocolo | Notas |
|---------|---------|----------------|-----------|-------|
| Marine Traffic | Entrada (pull) | `util/MarineTraffic.java` | HTTP + HTML scraping (JSoup) | Extrai posição, tipo, dimensões de navios por MMSI; frágil a mudanças de layout |
| ClimaTempoAdvisor | Entrada (pull) | `api/WeatherApi.java` | REST/JSON via Unirest | Dados meteorológicos por código de cidade; token em `Util.TOKEN` |
| Windy API | Entrada (embed) | `sismar.aiswindy.js` | iFrame / JS SDK | Camada de vento e ondas no mapa; configurada no frontend |

---

## Persistência

| Entidade | Tabela | Propósito | Relacionamentos principais |
|----------|--------|-----------|---------------------------|
| `Navio` | `navio` | Registro de embarcação (MMSI, IMO, nome, tipo) | → N `Ais`, → N `Operacao`, → N `MovimentacaoPorto` |
| `Ais` | `ais` | Registro AIS pontual (posição, velocidade, rumo, tempo) | MMSI referencia `Navio` (sem FK mapeada) |
| `MovimentacaoPorto` | `movimentacao_porto` | Escala completa de um navio no porto | → `Navio`; referencia até 4 `Poin` de atracação |
| `Poin` | `poin` | Polígono de interesse (coordenadas GeoJSON) | → N `Berco`; M:N `Monitorar`; referenciado em `MovimentacaoPorto` |
| `Porto` | `porto` | Porto | → N `Cais` |
| `Cais` | `cais` | Estrutura física do porto | → N `Berco` |
| `Berco` | `berco` | Posição de atracação | → `Cais`, → `Poin`, → `Area`, → `Empresa`, → `Mercadoria` |
| `Operacao` | `operacao` | Operação portuária num Berço | → `Navio`, → `Berco`, → `Clientes`, → `UsuariosWeb`; → N `EtapasOperacao` |
| `EtapasOperacao` | `etapas_operacao` | Instância de Etapa vinculada a uma Operação | → `Operacao`; → N `Eventos`, `Interrupcoes`, `Observacoes` |
| `Monitorar` | `monitorar` | Regra de alerta configurada por usuário | → N `MonitorarRegra`; M:N `Poin` |
| `MonitorarRegra` | `monitorar_regra` | Condição individual de um Monitoramento | → `Monitorar` |
| `UsuariosWeb` | `usuarios_web` | Usuário do sistema com features e timezone | → N `UsuariosWebSessao` |
| `Meteorologia` | `meteorologia` | Leitura de sensores meteorológicos do porto | — |

### Relacionamentos principais

`Navio` é a entidade central: conecta `Ais` (posições), `Operacao` (trabalho portuário) e `MovimentacaoPorto` (escala). `Poin` é o elo geográfico: define o contorno de um `Berco` e é associado a regras de `Monitorar`. Uma `Operacao` ocorre num `Berco` e é composta por `EtapasOperacao`, que agrega `Eventos`, `Interrupcoes` e `Observacoes`.

---

## Regras de negócio no código

- **Controle de acesso por feature**: cada página admin tem uma feature mapeada em `filter/AdminFilter.java`; usuário sem a feature é redirecionado para `notavailable.xhtml`. Features definidas como constantes em `util/Util.java`.
- **Validade AIS**: registro AIS considerado "online" por 180 minutos (`TMP_MINUTES_ONLINE_AIS` em `Util.java`); navios fora desse janela são exibidos como inativos.
- **Status de Operação**: `Operacao.isEmAndamento()` retorna `true` quando `dataTermino == null`; o campo `status` (boolean) controla visibilidade (`entity/Operacao.java:208`).
- **Variáveis de Monitoramento**: `Monitorar` aceita quatro tipos — chegada (1), saída (2), tempo de permanência em horas (3), velocidade de navegação em nós (4) — definidos como constantes em `entity/Monitorar.java:41`.
- **Tempo de paralisação de Operação**: calculado somando `getDuracaoParalizacao()` de cada `EtapasOperacao` (`entity/Operacao.java:212`).
- **Autenticação REST**: header `Authorization` validado em `Util.isUserAuthenticatedAPI()` — requer feature `config_api` ativa para o usuário.
- **Poin e cálculo de viagem**: campo `utilizarCalculoViagem` em `Poin` indica se o polígono entra no cálculo de tempo de Escala (`entity/Poin.java:98`).
- **Detecção de posição em Poin**: `util/GeoPosition.java` — point-in-polygon para determinar se navio está dentro de um Poin.

---

## Mapa de navegação

| Preciso... | Começar em |
|------------|------------|
| Adicionar endpoint REST | `src/.../api/` — criar `{Recurso}Api.java` com `@Path` |
| Adicionar página JSF com form | `src/.../bean/admin/{módulo}/{Funcionalidade}Bean.java` + `web/admin/{módulo}/{pagina}.xhtml` |
| Adicionar consulta ao banco | `src/.../controller/{Entidade}Controller.java` — método estático recebendo `EntityManager` |
| Adicionar entidade | `src/.../entity/{Entidade}.java` + registrar em `src/conf/persistence.xml` |
| Adicionar módulo JS no mapa | `web/resources/js/sismar.{módulo}.js` — seguir padrão ES6 Class de `sismar.ais.realtime.js` |
| Adicionar feature de acesso | `util/Util.java` (constante) + `filter/AdminFilter.java` (mapeamento página → feature) |
| Entender um fluxo do mapa | `web/resources/js/sismar.ais.js` (legado) ou `sismar.ais.realtime.js` (ES6) |
| Entender autenticação | `filter/LoginFilter.java` → `filter/AdminFilter.java` → `session/SessionContext.java` |
