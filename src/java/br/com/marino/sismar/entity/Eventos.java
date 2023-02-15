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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "eventos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Eventos.findAll", query = "SELECT e FROM Eventos e")
    , @NamedQuery(name = "Eventos.findByCod", query = "SELECT e FROM Eventos e WHERE e.cod = :cod")
    , @NamedQuery(name = "Eventos.findByNome", query = "SELECT e FROM Eventos e WHERE e.nome = :nome")
    , @NamedQuery(name = "Eventos.findByStatus", query = "SELECT e FROM Eventos e WHERE e.status = :status")})
public class Eventos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod")
    private Integer cod;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nome")
    private String nome;

    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codEvento")
    private List<Interrupcoes> interrupcoesList;

    @JoinColumn(name = "codUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private UsuariosWeb codUsuario;

    @JoinColumn(name = "codCliente", referencedColumnName = "cod")
    @ManyToOne(optional = true)
    private Clientes codCliente;

    public Eventos() {
    }

    public Eventos(Integer cod) {
        this.cod = cod;
    }

    public Eventos(Integer cod, String nome, boolean status) {
        this.cod = cod;
        this.nome = nome;
        this.status = status;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UsuariosWeb getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(UsuariosWeb codUsuario) {
        this.codUsuario = codUsuario;
    }

    @XmlTransient
    public List<Interrupcoes> getInterrupcoesList() {
        return interrupcoesList;
    }

    public void setInterrupcoesList(List<Interrupcoes> interrupcoesList) {
        this.interrupcoesList = interrupcoesList;
    }

    public Clientes getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Clientes codCliente) {
        this.codCliente = codCliente;
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
        if (!(object instanceof Eventos)) {
            return false;
        }
        Eventos other = (Eventos) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Eventos[ cod=" + cod + " ]";
    }

}
