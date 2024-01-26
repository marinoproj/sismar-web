/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "movimentacao_porto_parametros")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovimentacaoPortoParametros.findAll", query = "SELECT m FROM MovimentacaoPortoParametros m")
    , @NamedQuery(name = "MovimentacaoPortoParametros.findByCodMovimentacaoPortoParametro", query = "SELECT m FROM MovimentacaoPortoParametros m WHERE m.codMovimentacaoPortoParametro = :codMovimentacaoPortoParametro")
    , @NamedQuery(name = "MovimentacaoPortoParametros.findBySetPointTempoPermanenciaBerco", query = "SELECT m FROM MovimentacaoPortoParametros m WHERE m.setPointTempoPermanenciaBerco = :setPointTempoPermanenciaBerco")})
public class MovimentacaoPortoParametros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codMovimentacaoPortoParametro")
    private Integer codMovimentacaoPortoParametro;
    @Column(name = "setPointTempoPermanenciaBerco")
    private Integer setPointTempoPermanenciaBerco;
    @JoinColumn(name = "codPoinChegada", referencedColumnName = "codPoin")
    @ManyToOne(optional = false)
    private Poin codPoinChegada;
    @JoinColumn(name = "codPoinFundeio", referencedColumnName = "codPoin")
    @ManyToOne(optional = false)
    private Poin codPoinFundeio;
    @JoinColumn(name = "codPoinCanal", referencedColumnName = "codPoin")
    @ManyToOne(optional = false)
    private Poin codPoinCanal;
    @JoinColumn(name = "codPorto", referencedColumnName = "codPorto")
    @ManyToOne(optional = false)
    private Porto codPorto;
    @JoinColumn(name = "codArea", referencedColumnName = "codArea")
    @ManyToOne(optional = false)
    private Area codArea;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codMovimentacaoPortoParametro")
    private List<MovimentacaoPorto> movimentacaoPortoList;

    public MovimentacaoPortoParametros() {
    }

    public MovimentacaoPortoParametros(Integer codMovimentacaoPortoParametro) {
        this.codMovimentacaoPortoParametro = codMovimentacaoPortoParametro;
    }

    public Integer getCodMovimentacaoPortoParametro() {
        return codMovimentacaoPortoParametro;
    }

    public void setCodMovimentacaoPortoParametro(Integer codMovimentacaoPortoParametro) {
        this.codMovimentacaoPortoParametro = codMovimentacaoPortoParametro;
    }

    public Integer getSetPointTempoPermanenciaBerco() {
        return setPointTempoPermanenciaBerco;
    }

    public void setSetPointTempoPermanenciaBerco(Integer setPointTempoPermanenciaBerco) {
        this.setPointTempoPermanenciaBerco = setPointTempoPermanenciaBerco;
    }

    public Poin getCodPoinChegada() {
        return codPoinChegada;
    }

    public void setCodPoinChegada(Poin codPoinChegada) {
        this.codPoinChegada = codPoinChegada;
    }

    public Poin getCodPoinFundeio() {
        return codPoinFundeio;
    }

    public void setCodPoinFundeio(Poin codPoinFundeio) {
        this.codPoinFundeio = codPoinFundeio;
    }

    public Poin getCodPoinCanal() {
        return codPoinCanal;
    }

    public void setCodPoinCanal(Poin codPoinCanal) {
        this.codPoinCanal = codPoinCanal;
    }

    public Porto getCodPorto() {
        return codPorto;
    }

    public void setCodPorto(Porto codPorto) {
        this.codPorto = codPorto;
    }

    public Area getCodArea() {
        return codArea;
    }

    public void setCodArea(Area codArea) {
        this.codArea = codArea;
    }
        
    @XmlTransient
    public List<MovimentacaoPorto> getMovimentacaoPortoList() {
        return movimentacaoPortoList;
    }

    public void setMovimentacaoPortoList(List<MovimentacaoPorto> movimentacaoPortoList) {
        this.movimentacaoPortoList = movimentacaoPortoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codMovimentacaoPortoParametro != null ? codMovimentacaoPortoParametro.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimentacaoPortoParametros)) {
            return false;
        }
        MovimentacaoPortoParametros other = (MovimentacaoPortoParametros) object;
        if ((this.codMovimentacaoPortoParametro == null && other.codMovimentacaoPortoParametro != null) || (this.codMovimentacaoPortoParametro != null && !this.codMovimentacaoPortoParametro.equals(other.codMovimentacaoPortoParametro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MovimentacaoPortoParametros{" + "codMovimentacaoPortoParametro=" + codMovimentacaoPortoParametro + ", setPointTempoPermanenciaBerco=" + setPointTempoPermanenciaBerco + ", codPoinChegada=" + codPoinChegada + ", codPoinFundeio=" + codPoinFundeio + ", codPoinCanal=" + codPoinCanal + ", codPorto=" + codPorto + ", codArea=" + codArea + ", movimentacaoPortoList=" + movimentacaoPortoList + '}';
    }
    
}
