# PRD — Praticagem: setores angulares no mapa AIS realtime

Status: ready-for-agent

## Declaração do problema

O operador que acompanha o tráfego no mapa AIS em tempo real precisa saber, de forma visual, se uma embarcação está dentro de determinados setores angulares a partir de um ponto de referência na região de São Sebastião / Ilhabela. Hoje esses setores existem apenas em desenhos feitos à mão sobre mapas impressos (azimutes 56°–96° e 140°–180° a partir de um ponto ao sul de São Sebastião). Não há como sobrepor essa informação ao mapa do SISMAR, então o operador precisa comparar mentalmente a posição dos navios com o desenho em papel, o que é lento e sujeito a erro.

## Solução

Na página de AIS realtime, o controle de layers do mapa (canto direito) ganha um novo grupo **"Praticagem"**, cujo primeiro item é **"Setores angulares"**. Ao ligar o item:

- dois setores circulares (56°–96° e 140°–180°, azimute a partir do norte verdadeiro, sentido horário) são desenhados a partir de um único ponto de origem;
- na primeira vez, a origem aparece no centro da área visível do mapa, com raio de 25 km;
- o usuário arrasta o ponto de origem para posicioná-lo; os setores acompanham em tempo real;
- o desenho tem tamanho geográfico real (metros), portanto cresce e diminui junto com o zoom;
- clicar no ponto de origem abre um popup para alterar o raio (1 a 100 km) e para trazer os setores ao centro da tela;
- posição e raio ficam salvos no navegador: ao desligar e religar o layer, ou ao recarregar a página, os setores reaparecem onde estavam.

O grupo "Praticagem" foi pensado para crescer: novos recursos de praticagem entram como novos itens do grupo, cada um ligado/desligado independentemente.

## User stories

1. Como operador, quero ver um grupo "Praticagem" no controle de layers do mapa AIS realtime, para que eu encontre os recursos de praticagem em um lugar único.
2. Como operador, quero que o grupo "Praticagem" tenha um item "Setores angulares", para que eu ligue e desligue os setores com um clique.
3. Como operador, quero que o item "Setores angulares" comece desligado ao abrir a página, para que o mapa mantenha o comportamento padrão dos demais layers.
4. Como operador, quero que, na primeira vez que ligo os setores, eles apareçam no centro da área visível do mapa, para que eu os veja imediatamente sem procurar.
5. Como operador, quero ver o setor de 56° a 96°, para que eu identifique embarcações nessa faixa de azimute.
6. Como operador, quero ver o setor de 140° a 180°, para que eu identifique embarcações nessa faixa de azimute.
7. Como operador, quero que os ângulos sejam medidos a partir do norte verdadeiro, no sentido horário, para que o desenho coincida com o mapa e com o rumo AIS das embarcações.
8. Como operador, quero que os dois setores partam do mesmo ponto de origem, para que o desenho corresponda à referência usada pela praticagem.
9. Como operador, quero que o raio padrão dos setores seja 25 km, para que o desenho cubra a região de interesse sem configuração.
10. Como operador, quero que os setores tenham tamanho geográfico real, para que acompanhem o zoom do mapa e continuem sobrepondo a mesma área.
11. Como operador, quero arrastar o ponto de origem no mapa, para que eu posicione os setores no ponto de referência correto.
12. Como operador, quero que os setores acompanhem o ponto de origem enquanto arrasto, para que eu veja o resultado antes de soltar.
13. Como operador, quero que arrastar o ponto de origem não arraste o mapa, para que o posicionamento seja preciso.
14. Como operador, quero que a posição da origem fique salva no meu navegador, para que, ao desligar e religar o layer, os setores voltem onde estavam.
15. Como operador, quero que a posição continue salva após recarregar a página ou reiniciar o navegador, para que eu não precise reposicionar a cada turno.
16. Como operador, quero clicar no ponto de origem e ver um campo "Raio (km)", para que eu ajuste o alcance dos setores.
17. Como operador, quero que os setores sejam redesenhados assim que altero o raio, para que eu confira o efeito imediatamente.
18. Como operador, quero que o raio aceite apenas valores entre 1 e 100 km, para que eu não gere um desenho inválido ou gigantesco por engano.
19. Como operador, quero que o raio escolhido fique salvo no navegador junto com a posição, para que ele seja mantido ao religar o layer ou recarregar a página.
20. Como operador, quero um botão "Centralizar aqui" no popup, para que eu traga os setores para o centro da tela quando a origem estiver fora da área visível.
21. Como operador, quero ver os rótulos 56°, 96°, 140° e 180° na ponta de cada linha, para que eu identifique os limites de cada setor sem consultar documentação.
22. Como operador, quero que os setores tenham borda fina em vermelho escuro e hachurado cinza interno com linhas paralelas diagonais, para que se destaquem no mapa (como no desenho de referência) sem esconder o que está embaixo.
23. Como operador, quero que o ponto de origem tenha tamanho fixo na tela, para que eu consiga clicar e arrastá-lo em qualquer nível de zoom.
24. Como operador, quero clicar em uma embarcação que esteja dentro de um setor e ver suas informações normalmente, para que os setores não atrapalhem o uso do mapa.
25. Como operador, quero que o ponto de origem fique acima das embarcações, para que ele nunca fique escondido.
26. Como operador, quero que os setores funcionem também no modo tela cheia, para que eu use o recurso no monitor de operação.
27. Como operador, quero que os setores funcionem sobre qualquer mapa base (Google, carta náutica, etc.), para que eu não precise trocar o mapa de fundo.
28. Como operador, quero que um armazenamento local indisponível ou corrompido não quebre a página, para que eu ainda possa usar os setores (com posição e raio padrão).
29. Como operador, quero que todas as funções existentes do mapa (embarcações, popups, régua de distância, layers do banco, berços, meteorologia) continuem funcionando, para que o novo recurso não traga regressões.
30. Como desenvolvedor, quero que a praticagem seja um módulo próprio, para que novos recursos de praticagem sejam adicionados sem inchar o módulo principal do mapa.
31. Como desenvolvedor, quero que os ângulos dos setores estejam em uma constante única, para que uma eventual mudança de regra seja feita em uma linha.
32. Como desenvolvedor, quero testes automatizados do módulo de praticagem, para que mudanças futuras não quebrem o comportamento descrito aqui.

## Decisões de implementação

- **Módulo novo de Praticagem**: classe ES6 exportada por padrão, no mesmo padrão dos módulos de meteorologia e informações da embarcação. Interface pública:
  - `attach(map)` — recebe o mapa Leaflet, cria os panes/layers necessários;
  - `getLayerTree()` — devolve o nó da árvore do controle de layers: `{ label: "Praticagem", children: [{ label: "Setores angulares", layer }] }`.
  - Opções de construtor para injetar armazenamento (padrão: `localStorage`) — útil para testes e para tolerar ambientes sem `localStorage`.
- **Integração com o mapa AIS realtime**: a classe do mapa passa a aceitar uma opção opcional `praticagem`. Ao montar o controle de layers, se a opção existir, chama `attach(map)` e acrescenta `getLayerTree()` aos overlays, após os grupos já existentes. Sem a opção, a árvore permanece idêntica à atual (mudança aditiva, sem breaking change).
- **Página AIS realtime**: importa o módulo de praticagem como os demais módulos ES6 e o passa ao mapa.
- **Ângulos**: constante única `[{ de: 56, ate: 96 }, { de: 140, ate: 180 }]`, azimute a partir do norte verdadeiro, sentido horário. Sem correção de declinação magnética. Não editáveis pela UI.
- **Geometria**: cada setor é um polígono `origem → pontos do arco (passo de ~2°) → origem`, calculados por fórmula esférica de destino (azimute + distância em metros). Precisão suficiente para dezenas de km.
- **Raio**: padrão 25 km; valores fora de 1–100 km são limitados ao intervalo; valores não numéricos são ignorados.
- **Ponto de origem**: marcador Leaflet com ícone `divIcon` (círculo de tamanho fixo em pixels), `draggable: true`. Redesenha os setores no evento `drag`; persiste no `dragend`.
- **Interatividade**: setores com `interactive: false`, em pane próprio; cliques passam para as camadas de baixo. Apenas o ponto de origem é interativo.
- **Estilo**: borda sólida fina (1px) em `#b22222`; preenchimento hachurado com linhas paralelas diagonais ("\") em cinza (`#808080`, ~60% de opacidade, 1px), via padrão SVG com espaçamento fixo de ~10px na tela (mesma densidade em qualquer zoom), sem preenchimento colorido; rótulos permanentes com o ângulo na ponta de cada raio, reaproveitando a classe de rótulo já usada no mapa. *(Revisado após teste do usuário — ver issue 05.)*
- **Popup da origem**: título "Setores angulares", campo numérico "Raio (km)" e botão "Centralizar aqui" (move a origem para o centro do mapa e persiste). Largura mínima de ~240px e textos de 14–16px para leitura confortável.
- **Persistência**: chave `sismar.ais.setoresAngulares` no `localStorage`, valor JSON `{ lat, lng, raioKm }`. Escopo por navegador (não por usuário). Leitura e escrita protegidas contra exceção; JSON inválido ou campos fora do formato são tratados como "sem valor salvo". O estado ligado/desligado do layer **não** é persistido.
- **Ciclo de vida do layer**: ao ser adicionado ao mapa, usa a posição/raio salvos ou, se não houver, o centro atual do mapa e 25 km; ao ser removido, remove setores, rótulos e origem.

## Decisões de teste

- **O que é um bom teste aqui**: exercita apenas o comportamento observável pela interface pública do módulo de praticagem e pelo mapa Leaflet real (layers presentes no mapa, coordenadas dos polígonos, posição do marcador, conteúdo do armazenamento). Não testa funções internas, nomes de variáveis ou ordem de chamadas.
- **Ferramentas**: runner nativo do Node (`node --test`) com jsdom e Leaflet 1.9.4 (mesma versão da página) como dependências de desenvolvimento. O `package.json` existe apenas para os testes; não participa do build Ant/NetBeans nem do WAR. `node_modules` fica fora do controle de versão.
- **Módulo testado**: Praticagem (montado sobre um mapa Leaflet real em jsdom).
- **Cenários cobertos**:
  - nó da árvore "Praticagem" → "Setores angulares";
  - layer desligado por padrão; ao ligar sem estado salvo, origem = centro do mapa e raio = 25 km;
  - vértices dos polígonos nos azimutes e distância esperados (tolerância pequena);
  - setores não interativos;
  - arraste (simulado via mudança de posição + eventos de drag) redesenha e persiste;
  - desligar/religar e nova instância (simulando recarga) restauram posição e raio;
  - raio limitado a 1–100 km e persistido; "Centralizar aqui" move a origem para o centro;
  - armazenamento corrompido ou que lança exceção não quebra o módulo.
- **Regressão**: teste garantindo que a montagem da árvore de layers sem a opção `praticagem` não muda; checklist manual na página rodando localmente (embarcações, popups, régua, layers do banco, berços, meteorologia, tela cheia).
- **Prior art**: o projeto não tem testes de JavaScript. Os testes existentes são JUnit que acessam o banco real e não devem ser usados como modelo para esta feature, nem executados contra o banco externo.

## Fora de escopo

- Edição dos ângulos ou criação/remoção de setores pela UI.
- Persistir se o layer estava ligado ou desligado.
- Persistência por usuário ou no servidor (banco/API).
- Alertas quando uma embarcação entra em um setor.
- Outros recursos de praticagem além dos setores angulares.
- Aplicar o recurso em outras páginas de mapa (playback, preview, legado).

## Notas adicionais

- Os ângulos vieram de um desenho manual sobre o mapa da região de São Sebastião / Ilhabela, com origem ao sul de São Sebastião.
- O significado operacional dos setores para a praticagem ainda precisa ser descrito pelo usuário para o glossário de domínio (tratado na última issue).
- Após o deploy, pode ser necessário forçar recarga sem cache no navegador para carregar os scripts novos.
