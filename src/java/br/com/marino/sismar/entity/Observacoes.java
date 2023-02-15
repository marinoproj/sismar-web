package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "observacoes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Observacoes.findAll", query = "SELECT o FROM Observacoes o")
    , @NamedQuery(name = "Observacoes.findByCod", query = "SELECT o FROM Observacoes o WHERE o.cod = :cod")
    , @NamedQuery(name = "Observacoes.findByDataCriacao", query = "SELECT o FROM Observacoes o WHERE o.dataCriacao = :dataCriacao")
    , @NamedQuery(name = "Observacoes.findByStatus", query = "SELECT o FROM Observacoes o WHERE o.status = :status")})
public class Observacoes implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod")
    private Integer cod;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "observacao")
    private String observacao;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    
    @JoinColumn(name = "codEtapaOperacao", referencedColumnName = "cod")
    @ManyToOne(optional = false)
    private EtapasOperacao codEtapaOperacao;

    public Observacoes() {
    }

    public Observacoes(Integer cod) {
        this.cod = cod;
    }

    public Observacoes(Integer cod, String observacao, Date dataCriacao, boolean status) {
        this.cod = cod;
        this.observacao = observacao;
        this.dataCriacao = dataCriacao;
        this.status = status;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public EtapasOperacao getCodEtapaOperacao() {
        return codEtapaOperacao;
    }

    public void setCodEtapaOperacao(EtapasOperacao codEtapaOperacao) {
        this.codEtapaOperacao = codEtapaOperacao;
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
        if (!(object instanceof Observacoes)) {
            return false;
        }
        Observacoes other = (Observacoes) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Observacoes[ cod=" + cod + " ]";
    }
    
}
