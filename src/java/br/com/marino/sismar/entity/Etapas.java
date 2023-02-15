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
@Table(name = "etapas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Etapas.findAll", query = "SELECT e FROM Etapas e")
    , @NamedQuery(name = "Etapas.findAllByStatusTrue", query = "SELECT e FROM Etapas e WHERE e.status = true")
    , @NamedQuery(name = "Etapas.findByCod", query = "SELECT e FROM Etapas e WHERE e.cod = :cod")
    , @NamedQuery(name = "Etapas.findByNome", query = "SELECT e FROM Etapas e WHERE e.nome = :nome")
    , @NamedQuery(name = "Etapas.findByTempoEstimadoConclusaoMin", query = "SELECT e FROM Etapas e WHERE e.tempoEstimadoConclusaoMin = :tempoEstimadoConclusaoMin")
    , @NamedQuery(name = "Etapas.findByPctEstadoAtencao", query = "SELECT e FROM Etapas e WHERE e.pctEstadoAtencao = :pctEstadoAtencao")
    , @NamedQuery(name = "Etapas.findByPctEstadoCritico", query = "SELECT e FROM Etapas e WHERE e.pctEstadoCritico = :pctEstadoCritico")
    , @NamedQuery(name = "Etapas.findByStatus", query = "SELECT e FROM Etapas e WHERE e.status = :status")})
public class Etapas implements Serializable {

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
    
    @Column(name = "tempoEstimadoConclusaoMin")
    private Integer tempoEstimadoConclusaoMin;
    
    @Column(name = "pctEstadoAtencao")
    private Integer pctEstadoAtencao;
    
    @Column(name = "pctEstadoCritico")
    private Integer pctEstadoCritico;
    
    @JoinColumn(name = "codCliente", referencedColumnName = "cod")
    @ManyToOne(optional = true)
    private Clientes codCliente;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codEtapa")
    private List<EtapasOperacao> etapasOperacaoList;
    
    @JoinColumn(name = "codUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private UsuariosWeb codUsuario;

    public Etapas() {
    }

    public Etapas(Integer cod) {
        this.cod = cod;
    }

    public Etapas(Integer cod, String nome, boolean status) {
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

    public Integer getTempoEstimadoConclusaoMin() {
        return tempoEstimadoConclusaoMin;
    }

    public void setTempoEstimadoConclusaoMin(Integer tempoEstimadoConclusaoMin) {
        this.tempoEstimadoConclusaoMin = tempoEstimadoConclusaoMin;
    }

    public Integer getPctEstadoAtencao() {
        return pctEstadoAtencao;
    }

    public void setPctEstadoAtencao(Integer pctEstadoAtencao) {
        this.pctEstadoAtencao = pctEstadoAtencao;
    }

    public Integer getPctEstadoCritico() {
        return pctEstadoCritico;
    }

    public void setPctEstadoCritico(Integer pctEstadoCritico) {
        this.pctEstadoCritico = pctEstadoCritico;
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
        if (!(object instanceof Etapas)) {
            return false;
        }
        Etapas other = (Etapas) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Etapas[ cod=" + cod + " ]";
    }

}
