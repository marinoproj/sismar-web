/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "mercadoria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mercadoria.findAll", query = "SELECT m FROM Mercadoria m")
    , @NamedQuery(name = "Mercadoria.findByCodMercadoria", query = "SELECT m FROM Mercadoria m WHERE m.codMercadoria = :codMercadoria")
    , @NamedQuery(name = "Mercadoria.findByNome", query = "SELECT m FROM Mercadoria m WHERE m.nome = :nome")})
public class Mercadoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codMercadoria")
    private Integer codMercadoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nome")
    private String nome;
    @OneToMany(mappedBy = "codMercadoria")
    private List<Berco> bercoList;

    public Mercadoria() {
    }

    public Mercadoria(Integer codMercadoria) {
        this.codMercadoria = codMercadoria;
    }

    public Mercadoria(Integer codMercadoria, String nome) {
        this.codMercadoria = codMercadoria;
        this.nome = nome;
    }

    public Integer getCodMercadoria() {
        return codMercadoria;
    }

    public void setCodMercadoria(Integer codMercadoria) {
        this.codMercadoria = codMercadoria;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codMercadoria != null ? codMercadoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mercadoria)) {
            return false;
        }
        Mercadoria other = (Mercadoria) object;
        if ((this.codMercadoria == null && other.codMercadoria != null) || (this.codMercadoria != null && !this.codMercadoria.equals(other.codMercadoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Mercadoria[ codMercadoria=" + codMercadoria + " ]";
    }
    
}
