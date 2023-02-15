package br.com.marino.sismar.reports.models;

import br.com.marino.sismar.entity.Atracacao;
import br.com.marino.sismar.util.Util;
import java.text.DecimalFormat;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

public class RelatorioManobraResumo {

    private int codAtracacao;
    private String berco;
    private String navio;

    private String dataInicio;
    private String dataDocagem;
    private String dataPartida;
    private String dataFinal;

    private String tmpManobra;
    private String tmpDocado;

    private String comandante;
    private String pratico;

    private String velMaxDir;
    private String velMaxEsq;

    private String velToqDir;
    private String velToqEsq;

    private String angMax;
    private String anguloToque;

    public RelatorioManobraResumo(Atracacao atracacao) {

        codAtracacao = atracacao.getCodAtracacao();
        berco = atracacao.getCodBerco().getNome();
        navio = (atracacao.getCodNavio() == null ? "Não informado" : atracacao.getCodNavio().getNomeNavio());

        dataInicio = Util.getDateTimeClient(atracacao.getDataInicio(), "dd/MM/yyyy HH:mm:ss");
        dataDocagem = Util.getDateTimeClient(atracacao.getDataDocagem(), "dd/MM/yyyy HH:mm:ss");
        dataPartida = Util.getDateTimeClient(atracacao.getDataPartida(), "dd/MM/yyyy HH:mm:ss");
        dataFinal = Util.getDateTimeClient(atracacao.getDataEncerrado(), "dd/MM/yyyy HH:mm:ss");

        if (atracacao.getDataDocagem() != null) {

            DateTime dataAnt = new DateTime(atracacao.getDataInicio());
            DateTime dataDep = new DateTime(atracacao.getDataDocagem());

            int qtdSegundos;
            int horas = 0;
            int minutos = 0;
            int segundos = 0;

            try {
                qtdSegundos = Seconds.secondsBetween(dataAnt, dataDep).getSeconds();
                horas = qtdSegundos / 3600;
                minutos = qtdSegundos % 3600 / 60;
                segundos = qtdSegundos % 3600 % 60;
            } catch (Exception ex) {
            }

            tmpManobra = "Hrs: " + horas + " Min: " + minutos + " Seg: " + segundos;

        } else {
            tmpManobra = "";
        }

        if (atracacao.getDataDocagem() != null && atracacao.getDataPartida() != null) {

            DateTime dataAnt = new DateTime(atracacao.getDataDocagem());
            DateTime dataDep = new DateTime(atracacao.getDataPartida());

            int qtdSegundos;
            int horas = 0;
            int minutos = 0;
            int segundos = 0;

            try {
                qtdSegundos = Seconds.secondsBetween(dataAnt, dataDep).getSeconds();
                horas = qtdSegundos / 3600;
                minutos = qtdSegundos % 3600 / 60;
                segundos = qtdSegundos % 3600 % 60;
            } catch (Exception ex) {
            }

            tmpDocado = "Hrs: " + horas + " Min: " + minutos
                    + " Seg: " + segundos;

        } else {

            tmpDocado = "";
        }

        comandante = (atracacao.getCodNavio() == null ? "Não informado" : atracacao.getCodNavio().getNomeComandante() == null ? "Não informado" : atracacao.getCodNavio().getNomeComandante());
        pratico = (atracacao.getCodPratico() == null ? "Não informado" : atracacao.getCodPratico().getNomePratico());

        velMaxDir = getVelocidade(atracacao.getVelocidadeMaximaDireita(), "cm/s");
        velMaxEsq = getVelocidade(atracacao.getVelocidadeMaximaEsquerda(), "cm/s");
        velToqDir = getVelocidade(atracacao.getVelocidadeToqueDireita(), "cm/s");
        velToqEsq = getVelocidade(atracacao.getVelocidadeToqueEsquerda(), "cm/s");
        angMax = getValue(atracacao.getAnguloMaximo(), "º");
        anguloToque = getValue(atracacao.getAnguloToque(), "º");

    }

    public RelatorioManobraResumo() {
    }
    
    private String getValue(Double value, String tipo) {
        if (value == null) {
            return "";
        }
        return new DecimalFormat("###,##0.00").format(Util.round(value, 2)) + " " + tipo;
    }
    
    private String getVelocidade(Double value, String tipo) {
        if (value == null) {
            return "";
        }
        return getValue(value * 100, tipo);
    }

    public int getCodAtracacao() {
        return codAtracacao;
    }

    public void setCodAtracacao(int codAtracacao) {
        this.codAtracacao = codAtracacao;
    }

    public String getBerco() {
        return berco;
    }

    public void setBerco(String berco) {
        this.berco = berco;
    }

    public String getNavio() {
        return navio;
    }

    public void setNavio(String navio) {
        this.navio = navio;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataDocagem() {
        return dataDocagem;
    }

    public void setDataDocagem(String dataDocagem) {
        this.dataDocagem = dataDocagem;
    }

    public String getDataPartida() {
        return dataPartida;
    }

    public void setDataPartida(String dataPartida) {
        this.dataPartida = dataPartida;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getTmpManobra() {
        return tmpManobra;
    }

    public void setTmpManobra(String tmpManobra) {
        this.tmpManobra = tmpManobra;
    }

    public String getTmpDocado() {
        return tmpDocado;
    }

    public void setTmpDocado(String tmpDocado) {
        this.tmpDocado = tmpDocado;
    }

    public String getComandante() {
        return comandante;
    }

    public void setComandante(String comandante) {
        this.comandante = comandante;
    }

    public String getPratico() {
        return pratico;
    }

    public void setPratico(String pratico) {
        this.pratico = pratico;
    }

    public String getVelMaxDir() {
        return velMaxDir;
    }

    public void setVelMaxDir(String velMaxDir) {
        this.velMaxDir = velMaxDir;
    }

    public String getVelMaxEsq() {
        return velMaxEsq;
    }

    public void setVelMaxEsq(String velMaxEsq) {
        this.velMaxEsq = velMaxEsq;
    }

    public String getVelToqDir() {
        return velToqDir;
    }

    public void setVelToqDir(String velToqDir) {
        this.velToqDir = velToqDir;
    }

    public String getVelToqEsq() {
        return velToqEsq;
    }

    public void setVelToqEsq(String velToqEsq) {
        this.velToqEsq = velToqEsq;
    }

    public String getAngMax() {
        return angMax;
    }

    public void setAngMax(String angMax) {
        this.angMax = angMax;
    }

    public String getAnguloToque() {
        return anguloToque;
    }

    public void setAnguloToque(String anguloToque) {
        this.anguloToque = anguloToque;
    }

}
