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
 * @author leona
 */
@Entity
@Table(name = "porto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Porto.findAll", query = "SELECT p FROM Porto p")
    , @NamedQuery(name = "Porto.findByCodPorto", query = "SELECT p FROM Porto p WHERE p.codPorto = :codPorto")
    , @NamedQuery(name = "Porto.findByNome", query = "SELECT p FROM Porto p WHERE p.nome = :nome")
    , @NamedQuery(name = "Porto.findByDescricao", query = "SELECT p FROM Porto p WHERE p.descricao = :descricao")})
public class Porto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codPorto")
    private Integer codPorto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descricao")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPorto")
    private List<MovimentacaoPortoParametros> movimentacaoPortoParametrosList;

    public Porto() {
    }

    public Porto(Integer codPorto) {
        this.codPorto = codPorto;
    }

    public Porto(Integer codPorto, String nome, String descricao) {
        this.codPorto = codPorto;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Integer getCodPorto() {
        return codPorto;
    }

    public void setCodPorto(Integer codPorto) {
        this.codPorto = codPorto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        hash += (codPorto != null ? codPorto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Porto)) {
            return false;
        }
        Porto other = (Porto) object;
        if ((this.codPorto == null && other.codPorto != null) || (this.codPorto != null && !this.codPorto.equals(other.codPorto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Porto[ codPorto=" + codPorto + " ]";
    }
    
}
