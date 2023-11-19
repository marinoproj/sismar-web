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
@Table(name = "equipamentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Equipamentos.findAll", query = "SELECT e FROM Equipamentos e")
    , @NamedQuery(name = "Equipamentos.findByCodEquipamento", query = "SELECT e FROM Equipamentos e WHERE e.codEquipamento = :codEquipamento")
    , @NamedQuery(name = "Equipamentos.findByNome", query = "SELECT e FROM Equipamentos e WHERE e.nome = :nome")})
public class Equipamentos implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codEquipamento")
    private Integer codEquipamento;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nome")
    private String nome;
    
    @Column(name = "tipo")
    @Basic(optional = true)
    private String tipo;

    public Equipamentos() {
    }

    public Equipamentos(Integer codEquipamento) {
        this.codEquipamento = codEquipamento;
    }

    public Equipamentos(Integer codEquipamento, String nome) {
        this.codEquipamento = codEquipamento;
        this.nome = nome;
    }

    public Integer getCodEquipamento() {
        return codEquipamento;
    }

    public void setCodEquipamento(Integer codEquipamento) {
        this.codEquipamento = codEquipamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codEquipamento != null ? codEquipamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Equipamentos)) {
            return false;
        }
        Equipamentos other = (Equipamentos) object;
        if ((this.codEquipamento == null && other.codEquipamento != null) || (this.codEquipamento != null && !this.codEquipamento.equals(other.codEquipamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Equipamentos[ codEquipamento=" + codEquipamento + " ]";
    }
    
}
