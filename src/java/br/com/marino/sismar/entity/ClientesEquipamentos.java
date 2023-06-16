package br.com.marino.sismar.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "clientes_equipamentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClientesEquipamentos.findAll", query = "SELECT ce FROM ClientesEquipamentos ce")
    , @NamedQuery(name = "ClientesEquipamentos.findByCodEquipamento", query = "SELECT ce FROM ClientesEquipamentos ce WHERE ce.codEquipamento = :codEquipamento")
    , @NamedQuery(name = "ClientesEquipamentos.findByCodCliente", query = "SELECT ce FROM ClientesEquipamentos ce WHERE ce.codCliente = :codCliente")})
public class ClientesEquipamentos implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod")
    private Integer cod;
    
    @JoinColumn(name = "codEquipamento", referencedColumnName = "codEquipamento")
    @ManyToOne(optional = false)
    private Equipamentos codEquipamento;

    @JoinColumn(name = "codCliente", referencedColumnName = "cod")
    @ManyToOne(optional = false)
    private Clientes codCliente;
    
    @Basic(optional = true)    
    @Column(name = "exibirTelaMeteorologia")
    private boolean exibirTelaMeteorologia;
    
    public ClientesEquipamentos() {
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public Equipamentos getCodEquipamento() {
        return codEquipamento;
    }

    public void setCodEquipamento(Equipamentos codEquipamento) {
        this.codEquipamento = codEquipamento;
    }

    public Clientes getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Clientes codCliente) {
        this.codCliente = codCliente;
    }

    public boolean isExibirTelaMeteorologia() {
        return exibirTelaMeteorologia;
    }

    public void setExibirTelaMeteorologia(boolean exibirTelaMeteorologia) {
        this.exibirTelaMeteorologia = exibirTelaMeteorologia;
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
        if (!(object instanceof ClientesEquipamentos)) {
            return false;
        }
        ClientesEquipamentos other = (ClientesEquipamentos) object;
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
