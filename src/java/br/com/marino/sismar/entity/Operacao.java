package br.com.marino.sismar.entity;

import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "operacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Operacao.findAll", query = "SELECT o FROM Operacao o")
    , @NamedQuery(name = "Operacao.findByCod", query = "SELECT o FROM Operacao o WHERE o.cod = :cod")
    , @NamedQuery(name = "Operacao.findByDataInicio", query = "SELECT o FROM Operacao o WHERE o.dataInicio = :dataInicio")
    , @NamedQuery(name = "Operacao.findByDataTermino", query = "SELECT o FROM Operacao o WHERE o.dataTermino = :dataTermino")
    , @NamedQuery(name = "Operacao.findByStatus", query = "SELECT o FROM Operacao o WHERE o.status = :status")})
public class Operacao implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod")
    private Integer cod;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataInicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;
    
    @Column(name = "dataTermino")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTermino;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codOperacao")
    private List<EtapasOperacao> etapasOperacaoList;
    
    @JoinColumn(name = "codBerco", referencedColumnName = "codBerco")
    @ManyToOne(optional = false)
    private Berco codBerco;
    
    @JoinColumn(name = "codNavio", referencedColumnName = "codNavio")
    @ManyToOne(optional = false)
    private Navio codNavio;
    
    @JoinColumn(name = "codCliente", referencedColumnName = "cod")
    @ManyToOne(optional = true)
    private Clientes codCliente;
    
    @JoinColumn(name = "codUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private UsuariosWeb codUsuario;

    public Operacao() {
    }

    public Operacao(Integer cod) {
        this.cod = cod;
    }

    public Operacao(Integer cod, Date dataInicio, boolean status) {
        this.cod = cod;
        this.dataInicio = dataInicio;
        this.status = status;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @XmlTransient
    public List<EtapasOperacao> getEtapasOperacaoList() {
        return etapasOperacaoList;
    }

    public void setEtapasOperacaoList(List<EtapasOperacao> etapasOperacaoList) {
        this.etapasOperacaoList = etapasOperacaoList;
    }

    public Berco getCodBerco() {
        return codBerco;
    }

    public void setCodBerco(Berco codBerco) {
        this.codBerco = codBerco;
    }

    public Navio getCodNavio() {
        return codNavio;
    }

    public void setCodNavio(Navio codNavio) {
        this.codNavio = codNavio;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cod != null ? cod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Operacao)) {
            return false;
        }
        Operacao other = (Operacao) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

    public String getColorStatus() {
        if (getDataInicio() == null) {
            return "grey";
        } else if (getDataTermino() == null) {
            return "#5cb85c";
        }
        return "#3a86b2";
    }

    public String getStatusString() {
        if (getDataInicio() == null) {
            return "";
        } else if (getDataTermino() == null) {
            return "Em andamento";
        }
        return "Finalizado";
    }

    public boolean isEmAndamento() {
        return getDataTermino() == null;
    }

    public String getDecorridoTimeParalizacao() {

        if (getDataInicio() == null) {
            return "";
        }

        int duracao = 0;

        for (EtapasOperacao etapasOperacao : getEtapasOperacaoList()) {
            duracao += etapasOperacao.getDuracaoParalizacao();
        }

        return Util.getTimeDuration(duracao);

    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Operacao[ cod=" + cod + " ]";
    }

}
