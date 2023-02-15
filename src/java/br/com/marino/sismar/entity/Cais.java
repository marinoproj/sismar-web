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
@Table(name = "cais")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cais.findAll", query = "SELECT c FROM Cais c")
    , @NamedQuery(name = "Cais.findByCodCais", query = "SELECT c FROM Cais c WHERE c.codCais = :codCais")
    , @NamedQuery(name = "Cais.findByNome", query = "SELECT c FROM Cais c WHERE c.nome = :nome")})
public class Cais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codCais")
    private Integer codCais;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nome")
    private String nome;
    @OneToMany(mappedBy = "codCais")
    private List<Berco> bercoList;

    public Cais() {
    }

    public Cais(Integer codCais) {
        this.codCais = codCais;
    }

    public Cais(Integer codCais, String nome) {
        this.codCais = codCais;
        this.nome = nome;
    }

    public Integer getCodCais() {
        return codCais;
    }

    public void setCodCais(Integer codCais) {
        this.codCais = codCais;
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
        hash += (codCais != null ? codCais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cais)) {
            return false;
        }
        Cais other = (Cais) object;
        if ((this.codCais == null && other.codCais != null) || (this.codCais != null && !this.codCais.equals(other.codCais))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Cais[ codCais=" + codCais + " ]";
    }
    
}
