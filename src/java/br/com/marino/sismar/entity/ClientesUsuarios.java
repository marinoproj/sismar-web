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
@Table(name = "clientes_usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClientesUsuarios.findAll", query = "SELECT cu FROM ClientesUsuarios cu")
    , @NamedQuery(name = "ClientesUsuarios.findByCodUsuario", query = "SELECT cu FROM ClientesUsuarios cu WHERE cu.codUsuario = :codUsuario")
    , @NamedQuery(name = "ClientesUsuarios.findByCodCliente", query = "SELECT cu FROM ClientesUsuarios cu WHERE cu.codCliente = :codCliente")})
public class ClientesUsuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod")
    private Integer cod;
    
    @JoinColumn(name = "codUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private UsuariosWeb codUsuario;

    @JoinColumn(name = "codCliente", referencedColumnName = "cod")
    @ManyToOne(optional = false)
    private Clientes codCliente;
    
    @Basic(optional = true)    
    @Column(name = "token")
    private String token;
    
    public ClientesUsuarios() {
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public UsuariosWeb getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(UsuariosWeb codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Clientes getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Clientes codCliente) {
        this.codCliente = codCliente;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
        if (!(object instanceof ClientesUsuarios)) {
            return false;
        }
        ClientesUsuarios other = (ClientesUsuarios) object;
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
