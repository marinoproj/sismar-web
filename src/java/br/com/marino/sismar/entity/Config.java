package br.com.marino.sismar.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "config")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Config.findAll", query = "SELECT c FROM Config c")
    , @NamedQuery(name = "Config.findByCod", query = "SELECT c FROM Config c WHERE c.cod = :cod")})
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod")
    private Integer cod;
    
    @Column(name = "operacaoTmpGravacaoSeg")
    private Integer operacaoTmpGravacaoSeg;

    @Column(name = "operacaoTmpGravacaoDocadoSeg")
    private Integer operacaoTmpGravacaoDocadoSeg;
    
    @Column(name = "operacaoDistanciaAtracadoCm")
    private Double operacaoDistanciaAtracadoCm;
    
    @Column(name = "operacaoDistanciaDerivaMt")
    private Double operacaoDistanciaDerivaMt;
    
    @Column(name = "operacaoTmpFocoInicioSeg")
    private Integer operacaoTmpFocoInicioSeg;
    
    @Column(name = "operacaoTmpSemFocoEncerrarSeg")
    private Integer operacaoTmpSemFocoEncerrarSeg;
    
    @Column(name = "operacaoIgnorarDistanciaAbaixoDeMt")
    private Double operacaoIgnorarDistanciaAbaixoDeMt;
    
    @Column(name = "operacaoIntegrarComAis")
    private Boolean operacaoIntegrarComAis;

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public Integer getOperacaoTmpGravacaoSeg() {
        return operacaoTmpGravacaoSeg;
    }

    public void setOperacaoTmpGravacaoSeg(Integer operacaoTmpGravacaoSeg) {
        this.operacaoTmpGravacaoSeg = operacaoTmpGravacaoSeg;
    }

    public Integer getOperacaoTmpGravacaoDocadoSeg() {
        return operacaoTmpGravacaoDocadoSeg;
    }

    public void setOperacaoTmpGravacaoDocadoSeg(Integer operacaoTmpGravacaoDocadoSeg) {
        this.operacaoTmpGravacaoDocadoSeg = operacaoTmpGravacaoDocadoSeg;
    }

    public Double getOperacaoDistanciaAtracadoCm() {
        return operacaoDistanciaAtracadoCm;
    }

    public void setOperacaoDistanciaAtracadoCm(Double operacaoDistanciaAtracadoCm) {
        this.operacaoDistanciaAtracadoCm = operacaoDistanciaAtracadoCm;
    }

    public Double getOperacaoDistanciaDerivaMt() {
        return operacaoDistanciaDerivaMt;
    }

    public void setOperacaoDistanciaDerivaMt(Double operacaoDistanciaDerivaMt) {
        this.operacaoDistanciaDerivaMt = operacaoDistanciaDerivaMt;
    }

    public Integer getOperacaoTmpFocoInicioSeg() {
        return operacaoTmpFocoInicioSeg;
    }

    public void setOperacaoTmpFocoInicioSeg(Integer operacaoTmpFocoInicioSeg) {
        this.operacaoTmpFocoInicioSeg = operacaoTmpFocoInicioSeg;
    }

    public Integer getOperacaoTmpSemFocoEncerrarSeg() {
        return operacaoTmpSemFocoEncerrarSeg;
    }

    public void setOperacaoTmpSemFocoEncerrarSeg(Integer operacaoTmpSemFocoEncerrarSeg) {
        this.operacaoTmpSemFocoEncerrarSeg = operacaoTmpSemFocoEncerrarSeg;
    }

    public Double getOperacaoIgnorarDistanciaAbaixoDeMt() {
        return operacaoIgnorarDistanciaAbaixoDeMt;
    }

    public void setOperacaoIgnorarDistanciaAbaixoDeMt(Double operacaoIgnorarDistanciaAbaixoDeMt) {
        this.operacaoIgnorarDistanciaAbaixoDeMt = operacaoIgnorarDistanciaAbaixoDeMt;
    }

    public Boolean getOperacaoIntegrarComAis() {
        return operacaoIntegrarComAis;
    }

    public void setOperacaoIntegrarComAis(Boolean operacaoIntegrarComAis) {
        this.operacaoIntegrarComAis = operacaoIntegrarComAis;
    }    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cod != null ? cod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Config)) {
            return false;
        }
        Config other = (Config) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

}
