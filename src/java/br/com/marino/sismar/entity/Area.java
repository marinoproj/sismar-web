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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Leonardo
 */
@Entity
@Table(name = "area")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Area.findAll", query = "SELECT a FROM Area a")
    , @NamedQuery(name = "Area.findByCodArea", query = "SELECT a FROM Area a WHERE a.codArea = :codArea")
    , @NamedQuery(name = "Area.findByNome", query = "SELECT a FROM Area a WHERE a.nome = :nome")})
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codArea")
    private Integer codArea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nome")
    private String nome;
    @OneToMany(mappedBy = "codArea")
    private List<Berco> bercoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codArea")
    private List<MovimentacaoPortoParametros> movimentacaoPortoParametrosList;

    public Area() {
    }

    public Area(Integer codArea) {
        this.codArea = codArea;
    }

    public Area(Integer codArea, String nome) {
        this.codArea = codArea;
        this.nome = nome;
    }

    public Integer getCodArea() {
        return codArea;
    }

    public void setCodArea(Integer codArea) {
        this.codArea = codArea;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @XmlTransient
    public List<Berco> getBercoList() {
        return bercoList;
    }

    public void setBercoList(List<Berco> bercoList) {
        this.bercoList = bercoList;
    }

    @XmlTransient
    public List<MovimentacaoPortoParametros> getMovimentacaoPortoParametrosList() {
        return movimentacaoPortoParametrosList;
    }

    public void setMovimentacaoPortoParametrosList(List<MovimentacaoPortoParametros> movimentacaoPortoParametrosList) {
        this.movimentacaoPortoParametrosList = movimentacaoPortoParametrosList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codArea != null ? codArea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Area)) {
            return false;
        }
        Area other = (Area) object;
        if ((this.codArea == null && other.codArea != null) || (this.codArea != null && !this.codArea.equals(other.codArea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Area[ codArea=" + codArea + " ]";
    }
    
}
