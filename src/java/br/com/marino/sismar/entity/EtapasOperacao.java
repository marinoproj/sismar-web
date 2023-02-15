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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "etapas_operacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtapasOperacao.findAll", query = "SELECT e FROM EtapasOperacao e")
    , @NamedQuery(name = "EtapasOperacao.findByCod", query = "SELECT e FROM EtapasOperacao e WHERE e.cod = :cod")
    , @NamedQuery(name = "EtapasOperacao.findByLatitude", query = "SELECT e FROM EtapasOperacao e WHERE e.latitude = :latitude")
    , @NamedQuery(name = "EtapasOperacao.findByLongitude", query = "SELECT e FROM EtapasOperacao e WHERE e.longitude = :longitude")
    , @NamedQuery(name = "EtapasOperacao.findByEndereco", query = "SELECT e FROM EtapasOperacao e WHERE e.endereco = :endereco")
    , @NamedQuery(name = "EtapasOperacao.findByDataMobilizacao", query = "SELECT e FROM EtapasOperacao e WHERE e.dataMobilizacao = :dataMobilizacao")
    , @NamedQuery(name = "EtapasOperacao.findByDataChegada", query = "SELECT e FROM EtapasOperacao e WHERE e.dataChegada = :dataChegada")
    , @NamedQuery(name = "EtapasOperacao.findByDataInicio", query = "SELECT e FROM EtapasOperacao e WHERE e.dataInicio = :dataInicio")
    , @NamedQuery(name = "EtapasOperacao.findByDataTermino", query = "SELECT e FROM EtapasOperacao e WHERE e.dataTermino = :dataTermino")
    , @NamedQuery(name = "EtapasOperacao.findByStatus", query = "SELECT e FROM EtapasOperacao e WHERE e.status = :status")})
public class EtapasOperacao implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod")
    private Integer cod;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "latitude")
    private Double latitude;
    
    @Column(name = "longitude")
    private Double longitude;
    
    @Size(max = 250)
    @Column(name = "endereco")
    private String endereco;
    
    @Column(name = "dataMobilizacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataMobilizacao;
    
    @Column(name = "dataChegada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataChegada;
    
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
    
    @JoinColumn(name = "codEtapa", referencedColumnName = "cod")
    @ManyToOne(optional = false)
    private Etapas codEtapa;
    
    @JoinColumn(name = "codOperacao", referencedColumnName = "cod")
    @ManyToOne(optional = false)
    private Operacao codOperacao;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codEtapaOperacao")
    private List<Observacoes> observacoesList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codEtapaOperacao")
    private List<Interrupcoes> interrupcoesList;

    public EtapasOperacao() {
    }

    public EtapasOperacao(Integer cod) {
        this.cod = cod;
    }

    public EtapasOperacao(Integer cod, Date dataInicio, boolean status) {
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Date getDataMobilizacao() {
        return dataMobilizacao;
    }

    public void setDataMobilizacao(Date dataMobilizacao) {
        this.dataMobilizacao = dataMobilizacao;
    }

    public Date getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(Date dataChegada) {
        this.dataChegada = dataChegada;
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

    public Etapas getCodEtapa() {
        return codEtapa;
    }

    public void setCodEtapa(Etapas codEtapa) {
        this.codEtapa = codEtapa;
    }

    public Operacao getCodOperacao() {
        return codOperacao;
    }

    public void setCodOperacao(Operacao codOperacao) {
        this.codOperacao = codOperacao;
    }

    @XmlTransient
    public List<Observacoes> getObservacoesList() {
        return observacoesList;
    }

    public void setObservacoesList(List<Observacoes> observacoesList) {
        this.observacoesList = observacoesList;
    }

    @XmlTransient
    public List<Interrupcoes> getInterrupcoesList() {
        return interrupcoesList;
    }

    public void setInterrupcoesList(List<Interrupcoes> interrupcoesList) {
        this.interrupcoesList = interrupcoesList;
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
        if (!(object instanceof EtapasOperacao)) {
            return false;
        }
        EtapasOperacao other = (EtapasOperacao) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }
    
    
    public Interrupcoes getInterrupcaoAtual() {
        for(Interrupcoes obj : getInterrupcoesList()){
            if (obj.getDataTermino() == null){
                return obj;
            }
        }
        return null;        
    }

    public boolean isEmAndamento() {
        if (getDataInicio() == null) {
            return false;
        }
        for(Interrupcoes obj : getInterrupcoesList()){
            if (obj.getDataTermino() == null){
                return false;
            }
        }
        return getDataTermino() == null;

    }

    public boolean isParalizado() {
        for(Interrupcoes obj : getInterrupcoesList()){
            if (obj.getDataTermino() == null){
                return true;
            }
        }
        return false;        
    }

    public boolean isFinalizado() {
        return getDataTermino() != null;

    }

    public int getPctDecorridoTime() {

        int tempoEstimado = getCodEtapa().getTempoEstimadoConclusaoMin() * 60;

        int tempoExecutado = Util.getRangeSeconds(getDataInicio(),
                (getDataTermino() != null ? getDataTermino()
                : Util.getDateUTC()));

        tempoExecutado = tempoExecutado - getDuracaoParalizacao();

        return (tempoExecutado * 100) / tempoEstimado;

    }
    
    public String getDecorridoTime() {

        if (getDataInicio() == null) {
            return "";
        }

        int tempoExecutado = Util.getRangeSeconds(getDataInicio(),
                (getDataTermino() != null ? getDataTermino()
                : Util.getDateUTC()));

        tempoExecutado = tempoExecutado - getDuracaoParalizacao();
        
        return Util.getTimeDuration(tempoExecutado);        

    }
    
    public String getDecorridoTimeParalizacao() {

        if (getDataInicio() == null) {
            return "";
        }

        int tempoExecutado = getDuracaoParalizacao();

        return Util.getTimeDuration(tempoExecutado);        

    }

    public int getDuracaoParalizacao() {

        int duracao = 0;
        
        for(Interrupcoes obj : getInterrupcoesList()){
            duracao += obj.getDuracaoParalizacaoSegundos();
        }
        
        return duracao;

    }

    public String getClassPctDecorridoTime() {
        if (getPctDecorridoTime() > (getCodEtapa().getPctEstadoCritico() + 100)) {
            return "progress-bar-danger";
        } else if (getPctDecorridoTime() > (getCodEtapa().getPctEstadoAtencao() + 100)) {
            return "progress-bar-warning";
        }
        return "progress-bar-success";
    }

    public String getColorDecorridoTime() {
        if (getPctDecorridoTime() > (getCodEtapa().getPctEstadoCritico() + 100)) {
            return "#d9534f";
        } else if (getPctDecorridoTime() > (getCodEtapa().getPctEstadoAtencao() + 100)) {
            return "#f0ad4e";
        }
        return "#5cb85c";
    }

    public String getColorStatus() {
        if (getDataInicio() == null) {
            return "grey";
        }
        for(Interrupcoes obj : getInterrupcoesList()){
            if (obj.getDataTermino() == null){
                return "#ec1d26";
            }
        }
        if (getDataTermino() == null) {
            return "#5cb85c";
        }
        return "#3a86b2";
    }

    public String getStatusString() {
        if (getDataInicio() == null) {
            return "Aguardando";
        }        
        for(Interrupcoes obj : getInterrupcoesList()){
            if (obj.getDataTermino() == null){
                return "Paralizado";
            }
        }
        if (getDataTermino() == null) {
            return "Em andamento";
        }
        return "Finalizado";
    }
    
    
    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.EtapasOperacao[ cod=" + cod + " ]";
    }

}
