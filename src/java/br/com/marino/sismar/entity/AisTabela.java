package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "ais_tabela")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AisTabela.findAll", query = "SELECT a FROM AisTabela a")
    , @NamedQuery(name = "AisTabela.findByCodAisTabela", query = "SELECT a FROM AisTabela a WHERE a.codAisTabela = :codAisTabela")
    , @NamedQuery(name = "AisTabela.findByPeriod", query = "SELECT a FROM AisTabela a WHERE a.abertoEm >= :dateStart AND a.abertoEm <= :dateEnd ORDER BY a.abertoEm ASC")
    , @NamedQuery(name = "AisTabela.findByAbertoEm", query = "SELECT a FROM AisTabela a WHERE a.abertoEm = :abertoEm")})
public class AisTabela implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codAisTabela")
    private Integer codAisTabela;
    @Basic(optional = false)
    @Column(name = "abertoEm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date abertoEm;

    public AisTabela() {
    }

    public AisTabela(Integer codAisTabela) {
        this.codAisTabela = codAisTabela;
    }

    public AisTabela(Integer codAisTabela, Date abertoEm) {
        this.codAisTabela = codAisTabela;
        this.abertoEm = abertoEm;
    }

    public Integer getCodAisTabela() {
        return codAisTabela;
    }

    public void setCodAisTabela(Integer codAisTabela) {
        this.codAisTabela = codAisTabela;
    }

    public Date getAbertoEm() {
        return abertoEm;
    }

    public void setAbertoEm(Date abertoEm) {
        this.abertoEm = abertoEm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codAisTabela != null ? codAisTabela.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AisTabela)) {
            return false;
        }
        AisTabela other = (AisTabela) object;
        if ((this.codAisTabela == null && other.codAisTabela != null) || (this.codAisTabela != null && !this.codAisTabela.equals(other.codAisTabela))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.AisTabela[ codAisTabela=" + codAisTabela + " ]";
    }

}
