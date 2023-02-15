package br.com.marino.sismar.entity;

import br.com.marino.sismar.enums.ModoEncerrarAtracacao;
import br.com.marino.sismar.enums.TipoSensoresAproximacao;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clientes.findAll", query = "SELECT c FROM Clientes c")
    , @NamedQuery(name = "Clientes.findByCod", query = "SELECT c FROM Clientes c WHERE c.cod = :cod")
    , @NamedQuery(name = "Clientes.findByStatus", query = "SELECT c FROM Clientes c WHERE c.status = :status")})
public class Clientes implements Serializable {

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
    @Column(name = "logo")
    private String logo;    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    
    @Column(name = "token")
    private String token;
    
    @Column(name = "tipoSensoresAproximacao")
    @Enumerated(EnumType.ORDINAL)
    private TipoSensoresAproximacao tipoSensoresAproximacao;
    
    @Column(name = "modoEncerrarAtracacao")
    @Enumerated(EnumType.ORDINAL)
    private ModoEncerrarAtracacao modoEncerrarAtracacao;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codCliente")
    private List<BercoCliente> bercosCliente;

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }    

    public List<BercoCliente> getBercosCliente() {
        return bercosCliente;
    }

    public TipoSensoresAproximacao getTipoSensoresAproximacao() {
        return tipoSensoresAproximacao;
    }

    public void setTipoSensoresAproximacao(TipoSensoresAproximacao tipoSensoresAproximacao) {
        this.tipoSensoresAproximacao = tipoSensoresAproximacao;
    }
    
    public void setBercosCliente(List<BercoCliente> bercosCliente) {
        this.bercosCliente = bercosCliente;
    }    

    public ModoEncerrarAtracacao getModoEncerrarAtracacao() {
        return modoEncerrarAtracacao;
    }

    public void setModoEncerrarAtracacao(ModoEncerrarAtracacao modoEncerrarAtracacao) {
        this.modoEncerrarAtracacao = modoEncerrarAtracacao;
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
        if (!(object instanceof Clientes)) {
            return false;
        }
        Clientes other = (Clientes) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

}
