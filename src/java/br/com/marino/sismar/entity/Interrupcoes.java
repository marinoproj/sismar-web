package br.com.marino.sismar.entity;

import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "interrupcoes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Interrupcoes.findAll", query = "SELECT i FROM Interrupcoes i")
    , @NamedQuery(name = "Interrupcoes.findByCod", query = "SELECT i FROM Interrupcoes i WHERE i.cod = :cod")
    , @NamedQuery(name = "Interrupcoes.findByDataInicio", query = "SELECT i FROM Interrupcoes i WHERE i.dataInicio = :dataInicio")
    , @NamedQuery(name = "Interrupcoes.findByDataTermino", query = "SELECT i FROM Interrupcoes i WHERE i.dataTermino = :dataTermino")
    , @NamedQuery(name = "Interrupcoes.findByPararOperacao", query = "SELECT i FROM Interrupcoes i WHERE i.pararOperacao = :pararOperacao")
    , @NamedQuery(name = "Interrupcoes.findByStatus", query = "SELECT i FROM Interrupcoes i WHERE i.status = :status")})
public class Interrupcoes implements Serializable {

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
    
    @NotNull
    @Column(name = "dataTermino")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTermino;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "pararOperacao")
    private boolean pararOperacao;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    
    @Column(name = "observacao")
    private String observacao;
    
    @JoinColumn(name = "codEtapaOperacao", referencedColumnName = "cod")
    @ManyToOne(optional = false)
    private EtapasOperacao codEtapaOperacao;
    
    @JoinColumn(name = "codEvento", referencedColumnName = "cod")
    @ManyToOne(optional = false)
    private Eventos codEvento;

    public Interrupcoes() {
    }

    public Interrupcoes(Integer cod) {
        this.cod = cod;
    }

    public Interrupcoes(Integer cod, Date dataInicio, Date dataTermino, boolean pararOperacao, boolean status) {
        this.cod = cod;
        this.dataInicio = dataInicio;
        this.dataTermino = dataTermino;
        this.pararOperacao = pararOperacao;
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

    public boolean getPararOperacao() {
        return pararOperacao;
    }

    public void setPararOperacao(boolean pararOperacao) {
        this.pararOperacao = pararOperacao;
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

    public Eventos getCodEvento() {
        return codEvento;
    }

    public void setCodEvento(Eventos codEvento) {
        this.codEvento = codEvento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cod != null ? cod.hashCode() : 0);
        return hash;
    }

    public int getDuracaoParalizacaoSegundos(){        
        return Util.getRangeSeconds(getDataInicio(),
                (getDataTermino() != null ? getDataTermino()
                        : Util.getDateUTC()));        
    }
    
    public String getDecorridoTime() {
        if (getDataInicio() == null) {
            return "";
        }
        int tempoExecutado = getDuracaoParalizacaoSegundos();
        return Util.getTimeDuration(tempoExecutado);        
    }
    
    public boolean isDeletar(){
        return getDataTermino() == null;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Interrupcoes)) {
            return false;
        }
        Interrupcoes other = (Interrupcoes) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Interrupcoes[ cod=" + cod + " ]";
    }
    
}
