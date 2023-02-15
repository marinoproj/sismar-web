/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "meteorologia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Meteorologia.findAll", query = "SELECT m FROM Meteorologia m")
    , @NamedQuery(name = "Meteorologia.findByCodMeteorologia", query = "SELECT m FROM Meteorologia m WHERE m.codMeteorologia = :codMeteorologia")
    , @NamedQuery(name = "Meteorologia.findByDataHora", query = "SELECT m FROM Meteorologia m WHERE m.dataHora = :dataHora")
    , @NamedQuery(name = "Meteorologia.findByDirecaoMinimaVento", query = "SELECT m FROM Meteorologia m WHERE m.direcaoMinimaVento = :direcaoMinimaVento")
    , @NamedQuery(name = "Meteorologia.findByDirecaoMediaVento", query = "SELECT m FROM Meteorologia m WHERE m.direcaoMediaVento = :direcaoMediaVento")
    , @NamedQuery(name = "Meteorologia.findByDirecaoMaximaVento", query = "SELECT m FROM Meteorologia m WHERE m.direcaoMaximaVento = :direcaoMaximaVento")
    , @NamedQuery(name = "Meteorologia.findByVelocidadeMinimaVento", query = "SELECT m FROM Meteorologia m WHERE m.velocidadeMinimaVento = :velocidadeMinimaVento")
    , @NamedQuery(name = "Meteorologia.findByVelocidadeMediaVento", query = "SELECT m FROM Meteorologia m WHERE m.velocidadeMediaVento = :velocidadeMediaVento")
    , @NamedQuery(name = "Meteorologia.findByVelocidadeMaximaVento", query = "SELECT m FROM Meteorologia m WHERE m.velocidadeMaximaVento = :velocidadeMaximaVento")
    , @NamedQuery(name = "Meteorologia.findByTemperaturaAr", query = "SELECT m FROM Meteorologia m WHERE m.temperaturaAr = :temperaturaAr")
    , @NamedQuery(name = "Meteorologia.findByUmidadeRelativaAr", query = "SELECT m FROM Meteorologia m WHERE m.umidadeRelativaAr = :umidadeRelativaAr")
    , @NamedQuery(name = "Meteorologia.findByPressaoAr", query = "SELECT m FROM Meteorologia m WHERE m.pressaoAr = :pressaoAr")
    , @NamedQuery(name = "Meteorologia.findByChuvaAcumulada", query = "SELECT m FROM Meteorologia m WHERE m.chuvaAcumulada = :chuvaAcumulada")
    , @NamedQuery(name = "Meteorologia.findByDuracaoChuva", query = "SELECT m FROM Meteorologia m WHERE m.duracaoChuva = :duracaoChuva")        
    , @NamedQuery(name = "Meteorologia.findByIntensidadeChuva", query = "SELECT m FROM Meteorologia m WHERE m.intensidadeChuva = :intensidadeChuva")
    , @NamedQuery(name = "Meteorologia.findByGranizoAcumulado", query = "SELECT m FROM Meteorologia m WHERE m.granizoAcumulado = :granizoAcumulado")
    , @NamedQuery(name = "Meteorologia.findByDuracaoGranizo", query = "SELECT m FROM Meteorologia m WHERE m.duracaoGranizo = :duracaoGranizo")
    , @NamedQuery(name = "Meteorologia.findByIntensidadeGranizo", query = "SELECT m FROM Meteorologia m WHERE m.intensidadeGranizo = :intensidadeGranizo")
    , @NamedQuery(name = "Meteorologia.findByIntensidadePicoChuva", query = "SELECT m FROM Meteorologia m WHERE m.intensidadePicoChuva = :intensidadePicoChuva")
    , @NamedQuery(name = "Meteorologia.findByIntensidadePicoGranizo", query = "SELECT m FROM Meteorologia m WHERE m.intensidadePicoGranizo = :intensidadePicoGranizo")
    , @NamedQuery(name = "Meteorologia.findByTemperaturaAquecimento", query = "SELECT m FROM Meteorologia m WHERE m.temperaturaAquecimento = :temperaturaAquecimento")
    , @NamedQuery(name = "Meteorologia.findByTensaoAquecimento", query = "SELECT m FROM Meteorologia m WHERE m.tensaoAquecimento = :tensaoAquecimento")
    , @NamedQuery(name = "Meteorologia.findByTensaoAlimentacao", query = "SELECT m FROM Meteorologia m WHERE m.tensaoAlimentacao = :tensaoAlimentacao")
    , @NamedQuery(name = "Meteorologia.findByTensaoReferencia", query = "SELECT m FROM Meteorologia m WHERE m.tensaoReferencia = :tensaoReferencia")})
public class Meteorologia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codMeteorologia")
    private Integer codMeteorologia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "direcaoMinimaVento")
    private Double direcaoMinimaVento;
    @Column(name = "direcaoMediaVento")
    private Double direcaoMediaVento;
    @Column(name = "direcaoMaximaVento")
    private Double direcaoMaximaVento;
    @Column(name = "velocidadeMinimaVento")
    private Double velocidadeMinimaVento;
    @Column(name = "velocidadeMediaVento")
    private Double velocidadeMediaVento;
    @Column(name = "velocidadeMaximaVento")
    private Double velocidadeMaximaVento;
    @Column(name = "temperaturaAr")
    private Double temperaturaAr;
    @Column(name = "umidadeRelativaAr")
    private Double umidadeRelativaAr;
    @Column(name = "pressaoAr")
    private Double pressaoAr;
    @Column(name = "chuvaAcumulada")
    private Double chuvaAcumulada;
    @Column(name = "duracaoChuva")
    private Double duracaoChuva;
    @Column(name = "intensidadeChuva")
    private Double intensidadeChuva;
    @Column(name = "granizoAcumulado")
    private Double granizoAcumulado;
    @Column(name = "duracaoGranizo")
    private Double duracaoGranizo;
    @Column(name = "intensidadeGranizo")
    private Double intensidadeGranizo;
    @Column(name = "intensidadePicoChuva")
    private Double intensidadePicoChuva;
    @Column(name = "intensidadePicoGranizo")
    private Double intensidadePicoGranizo;
    @Column(name = "temperaturaAquecimento")
    private Double temperaturaAquecimento;
    @Column(name = "tensaoAquecimento")
    private Double tensaoAquecimento;
    @Column(name = "tensaoAlimentacao")
    private Double tensaoAlimentacao;
    @Column(name = "tensaoReferencia")
    private Double tensaoReferencia;
    private Integer codEquipamento;

    public Meteorologia() {
    }

    
    
    public Meteorologia(Integer codMeteorologia) {
        this.codMeteorologia = codMeteorologia;
    }

    public Meteorologia(Integer codMeteorologia, Date dataHora) {
        this.codMeteorologia = codMeteorologia;
        this.dataHora = dataHora;
    }

    public Integer getCodMeteorologia() {
        return codMeteorologia;
    }

    public void setCodMeteorologia(Integer codMeteorologia) {
        this.codMeteorologia = codMeteorologia;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public Double getDirecaoMinimaVento() {
        return direcaoMinimaVento;
    }

    public void setDirecaoMinimaVento(Double direcaoMinimaVento) {
        this.direcaoMinimaVento = direcaoMinimaVento;
    }

    public Double getDirecaoMediaVento() {
        return direcaoMediaVento;
    }

    public void setDirecaoMediaVento(Double direcaoMediaVento) {
        this.direcaoMediaVento = direcaoMediaVento;
    }

    public Double getDirecaoMaximaVento() {
        return direcaoMaximaVento;
    }

    public void setDirecaoMaximaVento(Double direcaoMaximaVento) {
        this.direcaoMaximaVento = direcaoMaximaVento;
    }

    public Double getVelocidadeMinimaVento() {
        return velocidadeMinimaVento;
    }

    public void setVelocidadeMinimaVento(Double velocidadeMinimaVento) {
        this.velocidadeMinimaVento = velocidadeMinimaVento;
    }

    public Double getVelocidadeMediaVento() {
        return velocidadeMediaVento;
    }

    public void setVelocidadeMediaVento(Double velocidadeMediaVento) {
        this.velocidadeMediaVento = velocidadeMediaVento;
    }

    public Double getVelocidadeMaximaVento() {
        return velocidadeMaximaVento;
    }

    public void setVelocidadeMaximaVento(Double velocidadeMaximaVento) {
        this.velocidadeMaximaVento = velocidadeMaximaVento;
    }

    public Double getTemperaturaAr() {
        return temperaturaAr;
    }

    public void setTemperaturaAr(Double temperaturaAr) {
        this.temperaturaAr = temperaturaAr;
    }

    public Double getUmidadeRelativaAr() {
        return umidadeRelativaAr;
    }

    public void setUmidadeRelativaAr(Double umidadeRelativaAr) {
        this.umidadeRelativaAr = umidadeRelativaAr;
    }

    public Double getPressaoAr() {
        return pressaoAr;
    }

    public void setPressaoAr(Double pressaoAr) {
        this.pressaoAr = pressaoAr;
    }

    public Double getChuvaAcumulada() {
        return chuvaAcumulada;
    }

    public void setChuvaAcumulada(Double chuvaAcumulada) {
        this.chuvaAcumulada = chuvaAcumulada;
    }

    public Double getDuracaoChuva() {
        return duracaoChuva;
    }

    public void setDuracaoChuva(Double duracaoChuva) {
        this.duracaoChuva = duracaoChuva;
    }

    public Double getIntensidadeChuva() {
        return intensidadeChuva;
    }

    public void setIntensidadeChuva(Double intensidadeChuva) {
        this.intensidadeChuva = intensidadeChuva;
    }

    public Double getGranizoAcumulado() {
        return granizoAcumulado;
    }

    public void setGranizoAcumulado(Double granizoAcumulado) {
        this.granizoAcumulado = granizoAcumulado;
    }

    public Double getDuracaoGranizo() {
        return duracaoGranizo;
    }

    public void setDuracaoGranizo(Double duracaoGranizo) {
        this.duracaoGranizo = duracaoGranizo;
    }

    public Double getIntensidadeGranizo() {
        return intensidadeGranizo;
    }

    public void setIntensidadeGranizo(Double intensidadeGranizo) {
        this.intensidadeGranizo = intensidadeGranizo;
    }

    public Double getIntensidadePicoChuva() {
        return intensidadePicoChuva;
    }

    public void setIntensidadePicoChuva(Double intensidadePicoChuva) {
        this.intensidadePicoChuva = intensidadePicoChuva;
    }

    public Double getIntensidadePicoGranizo() {
        return intensidadePicoGranizo;
    }

    public void setIntensidadePicoGranizo(Double intensidadePicoGranizo) {
        this.intensidadePicoGranizo = intensidadePicoGranizo;
    }

    public Double getTemperaturaAquecimento() {
        return temperaturaAquecimento;
    }

    public void setTemperaturaAquecimento(Double temperaturaAquecimento) {
        this.temperaturaAquecimento = temperaturaAquecimento;
    }

    public Double getTensaoAquecimento() {
        return tensaoAquecimento;
    }

    public void setTensaoAquecimento(Double tensaoAquecimento) {
        this.tensaoAquecimento = tensaoAquecimento;
    }

    public Double getTensaoAlimentacao() {
        return tensaoAlimentacao;
    }

    public void setTensaoAlimentacao(Double tensaoAlimentacao) {
        this.tensaoAlimentacao = tensaoAlimentacao;
    }

    public Double getTensaoReferencia() {
        return tensaoReferencia;
    }

    public void setTensaoReferencia(Double tensaoReferencia) {
        this.tensaoReferencia = tensaoReferencia;
    }

    public Integer getCodEquipamento() {
        return codEquipamento;
    }

    public void setCodEquipamento(Integer codEquipamento) {
        this.codEquipamento = codEquipamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codMeteorologia != null ? codMeteorologia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meteorologia)) {
            return false;
        }
        Meteorologia other = (Meteorologia) object;
        if ((this.codMeteorologia == null && other.codMeteorologia != null) || (this.codMeteorologia != null && !this.codMeteorologia.equals(other.codMeteorologia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Meteorologia[ codMeteorologia=" + codMeteorologia + " ]";
    }
    
}
