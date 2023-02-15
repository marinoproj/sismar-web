package br.com.marino.sismar.util;

import br.com.marino.sismar.entity.Navio;
import java.awt.Color;
import java.util.ArrayList;

public class NavioMapAis {

    // expressão regular para separar a dimensão do embarcacao
    private final static String EXPRESSAO_REGULAR = "A = (\\w+) B = (\\w+) C = (\\w+) D = (\\w+)";
    // objeto que representa o embarcacao
    private final Navio embarcacao;
    // limite minimo em metros para o comprimento ser proporcional ao mapa
    private final static int COMPRIMENTO_MINIMO_MAPA = 50;
    // caso o comprimento seja menor que 100 metros, define como padrão 16 pixels de comprimento
    private final static int COMPRIMENTO_PADRAO_PIXEL = 12;
    // caso o comprimento seja menor que 100 metros, define como padrão 8 pixels de largura
    private final static int LARGURA_PADRAO_PIXEL = 5;
    // comprimento da embarcação para o mapa
    private int comprimentoEmbarcacaoMapa;
    // largura da embarcação para o mapa
    private int larguraEmbarcacaoMapa;
    // flag que identifica se a dimensão deve ser proporcional no mapa
    private boolean dimensaoProporcionalMapa;
    // tipo do embarcacao
    private int codCategoriaEmbarcacao;
    // cor do embarcacao
    private Color corEmbarcacaoMapa;
    // definição dos códigos categoria embarcação
    public static final int NAVIOS_CARGA = 1;
    public static final int PETROLEIROS = 2;
    public static final int PASSAGEIROS = 3;
    public static final int REBOCADOR_EMBARCACOES_ESPECIAIS = 4;
    public static final int EMBARCACAO_DESCONHECIDA = 5;
    public static final int EMBARCACAO_NAO_ESPECIFICADA = 6;

    public static final int PORCENTAGEM_DIMENSAO = 15;

    // todos os tipos de embarcações
    public static final ArrayList<Integer> CATEGORIAS_EMBARCACOES;

    static {
        CATEGORIAS_EMBARCACOES = new ArrayList<>();
        CATEGORIAS_EMBARCACOES.add(NAVIOS_CARGA);
        CATEGORIAS_EMBARCACOES.add(PETROLEIROS);
        CATEGORIAS_EMBARCACOES.add(PASSAGEIROS);
        CATEGORIAS_EMBARCACOES.add(REBOCADOR_EMBARCACOES_ESPECIAIS);
        CATEGORIAS_EMBARCACOES.add(EMBARCACAO_DESCONHECIDA);
        CATEGORIAS_EMBARCACOES.add(EMBARCACAO_NAO_ESPECIFICADA);
    }

    public NavioMapAis(Navio embarcacao) {

        this.embarcacao = embarcacao;

        // define a dimensão do navio  
        definirDimensaoNavio();

        // define o tipo
        definirCodCategoriaEmbarcacao();

        // definine a dimensão
        definirDimensaoMapa();

        // define a cor
        definirCorEmbarcacaoMapa();

    }

    private void definirDimensaoNavio() {

        if (embarcacao == null) {
            return;
        }

        if (!embarcacao.getDimensao().matches(EXPRESSAO_REGULAR)) {
            embarcacao.setDimensao("A = 0 B = 0 C = 0 D = 0");
        }

        String as = embarcacao.getDimensao().replaceAll(EXPRESSAO_REGULAR, "$1");
        String bs = embarcacao.getDimensao().replaceAll(EXPRESSAO_REGULAR, "$2");
        String cs = embarcacao.getDimensao().replaceAll(EXPRESSAO_REGULAR, "$3");
        String ds = embarcacao.getDimensao().replaceAll(EXPRESSAO_REGULAR, "$4");

        if (!as.equals("null") && !bs.equals("null") && cs.equals("null") && ds.equals("null")) {

            Integer comprimento = Integer.parseInt(as);
            Integer largura = Integer.parseInt(bs);

            int b = (comprimento * PORCENTAGEM_DIMENSAO) / 100;
            int a = comprimento - b;
            int c = largura / 2;
            int d = largura - c;

            embarcacao.setDimensao("A = " + a + " B = " + b + " C = " + c + " D = " + d);

        } else if (!(!as.equals("null") && !bs.equals("null") && !cs.equals("null") && !ds.equals("null"))) {
            embarcacao.setDimensao("A = 0 B = 0 C = 0 D = 0");
        }

    }

    /**
     * Método que retorna o objeto passado por parâmetro do construtor
     *
     * @return
     */
    public Navio getEmbarcacao() {
        return embarcacao;
    }

    /**
     * Retorna código do tipo da embarcação
     *
     * @return
     */
    public int getCodCategoriaEmbarcacao() {
        return codCategoriaEmbarcacao;

    }

    /**
     * Retorna o nome da embarcação para colocar no mapa
     *
     * @return
     */
    public String getNomeEmbarcacaoMapa() {
        if (embarcacao == null
                || embarcacao.getNomeNavio() == null
                || embarcacao.getNomeNavio().trim().isEmpty()) {
            return "";
        }
        return embarcacao.getNomeNavio().trim();
    }

    /**
     * Método que defini a dimensão do navio para ao mapa
     */
    private void definirDimensaoMapa() {

        if (embarcacao == null) {
            comprimentoEmbarcacaoMapa = COMPRIMENTO_PADRAO_PIXEL;
            larguraEmbarcacaoMapa = LARGURA_PADRAO_PIXEL;
            dimensaoProporcionalMapa = false;
            return;
        }

        dimensaoProporcionalMapa = true;
        comprimentoEmbarcacaoMapa = getComprimentoReal();
        larguraEmbarcacaoMapa = getLarguraReal();

        if (comprimentoEmbarcacaoMapa < larguraEmbarcacaoMapa) {
            int comprimentoFlag = comprimentoEmbarcacaoMapa;
            comprimentoEmbarcacaoMapa = larguraEmbarcacaoMapa;
            larguraEmbarcacaoMapa = comprimentoFlag;
        }

        if ((codCategoriaEmbarcacao == 1 || codCategoriaEmbarcacao == 2)
                && comprimentoEmbarcacaoMapa < COMPRIMENTO_MINIMO_MAPA) {
            comprimentoEmbarcacaoMapa = 180;
            if (larguraEmbarcacaoMapa < 20) {
                larguraEmbarcacaoMapa = 40;
            }
        } else if (larguraEmbarcacaoMapa < 20) {
            larguraEmbarcacaoMapa = 40;
        }

        if ((codCategoriaEmbarcacao != 1 && codCategoriaEmbarcacao != 2)) {
            if (comprimentoEmbarcacaoMapa < COMPRIMENTO_MINIMO_MAPA) {
                comprimentoEmbarcacaoMapa = COMPRIMENTO_PADRAO_PIXEL;
                larguraEmbarcacaoMapa = LARGURA_PADRAO_PIXEL;
                dimensaoProporcionalMapa = false;
            } else if (larguraEmbarcacaoMapa < 20) {
                larguraEmbarcacaoMapa = 30;
            }
        }

    }

    /**
     * Retorna a cor da embarcação no mapa
     *
     * @return
     */
    public Color getCorEmbarcacaoMapa() {
        return corEmbarcacaoMapa;
    }

    /**
     * defini a cor da embarcação no mapa
     */
    private void definirCorEmbarcacaoMapa() {
        switch (codCategoriaEmbarcacao) {
            case 1:
                corEmbarcacaoMapa = new Color(184, 166, 75);
                break;
            case 2:
                corEmbarcacaoMapa = new Color(184, 78, 75);
                break;
            case 3:
                corEmbarcacaoMapa = new Color(133, 56, 142);
                break;
            case 4:
                corEmbarcacaoMapa = new Color(61, 157, 67);
                break;
            case 5:
                corEmbarcacaoMapa = new Color(80, 124, 143);
                break;
            default:
                corEmbarcacaoMapa = new Color(137, 137, 137);
                break;
        }
    }

    public boolean isDimensaoProporcionalMapa() {
        return dimensaoProporcionalMapa;
    }

    public int getAlphaCorMapa() {
        return 75;
    }

    public int getComprimentoMapa() {
        return comprimentoEmbarcacaoMapa;
    }

    public int getLarguraMapa() {
        return larguraEmbarcacaoMapa;
    }

    public Integer getA() {
        if (!embarcacao.getDimensao().matches(EXPRESSAO_REGULAR)) {
            return 0;
        }
        return Integer.parseInt(embarcacao.getDimensao().replaceAll(EXPRESSAO_REGULAR, "$1"));
    }

    public Integer getB() {
        if (!embarcacao.getDimensao().matches(EXPRESSAO_REGULAR)) {
            return 0;
        }
        return Integer.parseInt(embarcacao.getDimensao().replaceAll(EXPRESSAO_REGULAR, "$2"));
    }

    public Integer getC() {
        if (!embarcacao.getDimensao().matches(EXPRESSAO_REGULAR)) {
            return 0;
        }
        return Integer.parseInt(embarcacao.getDimensao().replaceAll(EXPRESSAO_REGULAR, "$3"));
    }

    public Integer getD() {
        if (!embarcacao.getDimensao().matches(EXPRESSAO_REGULAR)) {
            return 0;
        }
        return Integer.parseInt(embarcacao.getDimensao().replaceAll(EXPRESSAO_REGULAR, "$4"));
    }

    public int getComprimentoReal() {
        if (embarcacao == null) {
            return 0;
        }

        return getA() + getB();

    }

    public int getLarguraReal() {
        if (embarcacao == null) {
            return 0;
        }

        return getC() + getD();

    }

    public String getCategoriaEmbarcacao() {
        switch (codCategoriaEmbarcacao) {
            case NAVIOS_CARGA:
                return "Navios de carga";
            case PETROLEIROS:
                return "Petroleiros";
            case PASSAGEIROS:
                return "Navios de passageiros";
            case REBOCADOR_EMBARCACOES_ESPECIAIS:
                return "Rebocador e embarcações especiais";
            case EMBARCACAO_DESCONHECIDA:
                return "Embarcação desconhecida";
            case EMBARCACAO_NAO_ESPECIFICADA:
                return "Embarcação não especificada";
            default:
                return "";
        }
    }

    private void definirCodCategoriaEmbarcacao() {
        if (embarcacao == null) {
            codCategoriaEmbarcacao = EMBARCACAO_NAO_ESPECIFICADA;
            return;
        }
        switch (embarcacao.getTipo()) {
            case "Cargo":
            case "CargoHazardousA":
            case "CargoNoAdditionalInfo":
            case "CargoHazardousB":
            case "CargoHazardousD":
                codCategoriaEmbarcacao = NAVIOS_CARGA;
                break;
            case "Tanker":
            case "TankerHazardousC":
            case "TankerNoAdditionalInfo":
            case "TankerFuture3":
            case "PETROLEIRO":
                codCategoriaEmbarcacao = PETROLEIROS;
                break;
            case "Passenger":
            case "PassengerNoAdditionalInfo":
                codCategoriaEmbarcacao = PASSAGEIROS;
                break;
            case "Tug":
            case "OtherHazardousB":
            case "Sailing":
            case "MilitaryOps":
            case "Towing":
            case "PilotVessel":
            case "SearchAndRescueVessel":
            case "DredgingOrUnderwaterOps":
            case "PortTender":
                codCategoriaEmbarcacao = REBOCADOR_EMBARCACOES_ESPECIAIS;
                break;
            case "NotAvailable":
                codCategoriaEmbarcacao = EMBARCACAO_NAO_ESPECIFICADA;
                break;
            default:
                codCategoriaEmbarcacao = EMBARCACAO_DESCONHECIDA;
                break;
        }
    }

}
