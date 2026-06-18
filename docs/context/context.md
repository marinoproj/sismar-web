# SISMAR

Sistema de monitoramento marítimo portuário usado por portos, armadores e operadores logísticos. Combina rastreamento de navios em tempo real via AIS com gestão de operações portuárias.

## Linguagem

### Navios e Identificação

**Navio**:
Embarcação monitorada pelo sistema, identificada por MMSI e IMO.
_Avoid_: Embarcação, vessel

**MMSI**:
Código numérico único que identifica um navio no sistema AIS. É o identificador primário usado para cruzar dados entre tabelas.
_Avoid_: Código AIS, ID do navio

**IMO**:
Número de registro internacional permanente do navio, emitido pela International Maritime Organization. Diferente do MMSI: o IMO não muda ao longo da vida do navio.
_Avoid_: Número IMO

**AIS** (Automatic Identification System):
Sistema de rastreamento que navios transmitem automaticamente contendo posição, velocidade, rumo, calado e destino. É a fonte de dados de posicionamento em tempo real do SISMAR.

### Geografia Portuária

**Porto**:
O porto onde ocorrem as operações. Agrupa Cais.

**Cais**:
Estrutura física dentro do porto que agrupa Berços.

**Berço**:
Posição específica de atracação dentro de um Cais. Um navio realiza suas operações portuárias num Berço. Cada Berço é demarcado geograficamente por um Poin.

**Poin** (Polígono de Interesse):
Região geográfica delimitada por um polígono, marcada no mapa. Usada para detectar entrada/saída de navios em áreas específicas (berços, canais, fundeadouros) e para configurar alertas.
_Avoid_: Área, perímetro (são sinônimos aceitos informalmente, mas Poin é o termo técnico canônico)

### Movimentação Portuária

**Escala**:
Visita completa de um navio ao porto, desde a chegada até a saída definitiva. Registrada automaticamente via AIS. Inclui as fases: chegada, fundeio, entrada no canal, atracação e saída. Uma nova visita do mesmo navio gera uma nova Escala.
_Avoid_: Movimentação, visita

**Fundeio**:
Período em que o navio ancora em área designada enquanto aguarda autorização para entrar no canal ou atracar num Berço.

### Operações Portuárias

**Operação**:
Trabalho portuário realizado por um navio num Berço (ex: carga, descarga). Registrada manualmente pelos operadores, com data de início e término. Composta por Etapas.

**Etapa**:
Fase de uma Operação (ex: descarregamento, lavagem). O tipo/template é definido em `Etapas`; a instância vinculada a uma Operação é `EtapasOperacao`. Uma Etapa pode ter Eventos, Interrupções e Observações.

**Interrupção**:
Parada não planejada dentro de uma Etapa que suspende o andamento da operação.

**Evento**:
Ocorrência registrada dentro de uma Etapa.

**Observação**:
Nota livre registrada em uma Etapa.

### Alertas e Monitoramento

**Monitoramento**:
Regra configurada por um usuário que dispara uma mensagem quando um navio entra, sai, permanece por tempo excessivo ou navega com velocidade acima do limite num conjunto de Poins. Composto por um conjunto de Regras avaliadas em sequência.
_Avoid_: Alerta, regra (use Monitoramento para o conjunto e Regra para cada condição individual)

**Regra**:
Condição individual dentro de um Monitoramento (ex: "velocidade > 5 kn"). Regras são avaliadas em ordem.
