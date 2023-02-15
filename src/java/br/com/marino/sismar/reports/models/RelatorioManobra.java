package br.com.marino.sismar.reports.models;

import br.com.marino.sismar.entity.Manobra;
import br.com.marino.sismar.util.Util;
import java.text.DecimalFormat;

public class RelatorioManobra {

    private String dataHora;
    private String angulo;
    private String velEsq;
    private String velDir;
    private String distEsq;
    private String distDir;
    private String status;

    public RelatorioManobra(Manobra manobra){
        
        dataHora = Util.getDateTimeClient(manobra.getDataHora(), "dd/MM/yyyy HH:mm:ss");
        
        angulo = getValue(manobra.getAngulo(), "");        
        velEsq = getVelocidade(manobra.getVelocidadeEsquerda(), "");
        velDir = getVelocidade(manobra.getVelocidadeDireita(), "");
        
        if (manobra.isGravadoDistanciaDireita()){        
            distDir = manobra.getDistanciaDireita() == null ? "Sem foco" : getValue(manobra.getDistanciaDireita(), "");
        }else{
            distDir = getValue(manobra.getDistanciaDireita(), "");
        }
        
        if (manobra.isGravadoDistanciaEsquerda()){        
            distEsq = manobra.getDistanciaEsquerda() == null ? "Sem foco" : getValue(manobra.getDistanciaEsquerda(), "");
        }else{
            distEsq = getValue(manobra.getDistanciaEsquerda(), "");
        }
        
        status = manobra.getStatus().getValue();
        
    }
    
    private String getValue(Float value, String tipo) {
        if (value == null) {
            return "";
        }
        return new DecimalFormat("###,##0.00").format(Util.round(value, 2)) + " " + tipo;
    }
    
    private String getVelocidade(Float value, String tipo) {
        if (value == null) {
            return "";
        }
        return getValue(value * 100, tipo);
    }
    
    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getAngulo() {
        return angulo;
    }

    public void setAngulo(String angulo) {
        this.angulo = angulo;
    }

    public String getVelEsq() {
        return velEsq;
    }

    public void setVelEsq(String velEsq) {
        this.velEsq = velEsq;
    }

    public String getVelDir() {
        return velDir;
    }

    public void setVelDir(String velDir) {
        this.velDir = velDir;
    }

    public String getDistEsq() {
        return distEsq;
    }

    public void setDistEsq(String distEsq) {
        this.distEsq = distEsq;
    }

    public String getDistDir() {
        return distDir;
    }

    public void setDistDir(String distDir) {
        this.distDir = distDir;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}