/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "movimentacao_porto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovimentacaoPorto.findAll", query = "SELECT m FROM MovimentacaoPorto m")
    , @NamedQuery(name = "MovimentacaoPorto.findByCodMovimentacaoPorto", query = "SELECT m FROM MovimentacaoPorto m WHERE m.codMovimentacaoPorto = :codMovimentacaoPorto")
    , @NamedQuery(name = "MovimentacaoPorto.findByMmsi", query = "SELECT m FROM MovimentacaoPorto m WHERE m.mmsi = :mmsi")
    , @NamedQuery(name = "MovimentacaoPorto.findByImo", query = "SELECT m FROM MovimentacaoPorto m WHERE m.imo = :imo")
    , @NamedQuery(name = "MovimentacaoPorto.findByNomeNavio", query = "SELECT m FROM MovimentacaoPorto m WHERE m.nomeNavio = :nomeNavio")
    , @NamedQuery(name = "MovimentacaoPorto.findByTipo", query = "SELECT m FROM MovimentacaoPorto m WHERE m.tipo = :tipo")
    , @NamedQuery(name = "MovimentacaoPorto.findByCalado", query = "SELECT m FROM MovimentacaoPorto m WHERE m.calado = :calado")
    , @NamedQuery(name = "MovimentacaoPorto.findByCarga", query = "SELECT m FROM MovimentacaoPorto m WHERE m.carga = :carga")
    , @NamedQuery(name = "MovimentacaoPorto.findByProcedencia", query = "SELECT m FROM MovimentacaoPorto m WHERE m.procedencia = :procedencia")
    , @NamedQuery(name = "MovimentacaoPorto.findByDestino", query = "SELECT m FROM MovimentacaoPorto m WHERE m.destino = :destino")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataChegada", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataChegada = :dataChegada")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataFundeio", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataFundeio = :dataFundeio")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataSaidaFundeio", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataSaidaFundeio = :dataSaidaFundeio")
    , @NamedQuery(name = "MovimentacaoPorto.findByTempoFundeioSegundos", query = "SELECT m FROM MovimentacaoPorto m WHERE m.tempoFundeioSegundos = :tempoFundeioSegundos")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataEntradaCanal", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataEntradaCanal = :dataEntradaCanal")
    , @NamedQuery(name = "MovimentacaoPorto.findByCodPoinAtual", query = "SELECT m FROM MovimentacaoPorto m WHERE m.codPoinAtual = :codPoinAtual")
    , @NamedQuery(name = "MovimentacaoPorto.findByMaximaVelocidade", query = "SELECT m FROM MovimentacaoPorto m WHERE m.maximaVelocidade = :maximaVelocidade")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataEntradaPoinAtracacao1", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataEntradaPoinAtracacao1 = :dataEntradaPoinAtracacao1")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataSaidaPoinAtracacao1", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataSaidaPoinAtracacao1 = :dataSaidaPoinAtracacao1")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataEntradaPoinAtracacao2", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataEntradaPoinAtracacao2 = :dataEntradaPoinAtracacao2")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataSaidaPoinAtracacao2", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataSaidaPoinAtracacao2 = :dataSaidaPoinAtracacao2")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataEntradaPoinAtracacao3", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataEntradaPoinAtracacao3 = :dataEntradaPoinAtracacao3")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataSaidaPoinAtracacao3", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataSaidaPoinAtracacao3 = :dataSaidaPoinAtracacao3")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataEntradaPoinAtracacao4", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataEntradaPoinAtracacao4 = :dataEntradaPoinAtracacao4")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataSaidaPoinAtracacao4", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataSaidaPoinAtracacao4 = :dataSaidaPoinAtracacao4")
    , @NamedQuery(name = "MovimentacaoPorto.findByDataSaidaCanal", query = "SELECT m FROM MovimentacaoPorto m WHERE m.dataSaidaCanal = :dataSaidaCanal")})
public class MovimentacaoPorto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codMovimentacaoPorto")
    private Integer codMovimentacaoPorto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mmsi")
    private int mmsi;
    @Column(name = "imo")
    private Integer imo;
    @Size(max = 250)
    @Column(name = "nomeNavio")
    private String nomeNavio;
    @Size(max = 100)
    @Column(name = "tipo")
    private String tipo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "calado")
    private Double calado;
    @Column(name = "carga")
    private Double carga;
    @Size(max = 100)
    @Column(name = "procedencia")
    private String procedencia;
    @Size(max = 200)
    @Column(name = "destino")
    private String destino;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataChegada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataChegada;
    @Column(name = "dataFundeio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFundeio;
    @Column(name = "dataSaidaFundeio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSaidaFundeio;
    @Column(name = "tempoFundeioSegundos")
    private Integer tempoFundeioSegundos;
    @Column(name = "dataEntradaCanal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntradaCanal;
    @Size(max = 200)
    @Column(name = "codPoinAtual")
    private String codPoinAtual;
    @Column(name = "maximaVelocidade")
    private Double maximaVelocidade;
    @Column(name = "dataEntradaPoinAtracacao1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntradaPoinAtracacao1;
    @Column(name = "dataSaidaPoinAtracacao1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSaidaPoinAtracacao1;
    @Column(name = "dataEntradaPoinAtracacao2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntradaPoinAtracacao2;
    @Column(name = "dataSaidaPoinAtracacao2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSaidaPoinAtracacao2;
    @Column(name = "dataEntradaPoinAtracacao3")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntradaPoinAtracacao3;
    @Column(name = "dataSaidaPoinAtracacao3")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSaidaPoinAtracacao3;
    @Column(name = "dataEntradaPoinAtracacao4")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntradaPoinAtracacao4;
    @Column(name = "dataSaidaPoinAtracacao4")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSaidaPoinAtracacao4;
    @Column(name = "dataSaidaCanal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSaidaCanal;
    @JoinColumn(name = "codMovimentacaoPortoParametro", referencedColumnName = "codMovimentacaoPortoParametro")
    @ManyToOne(optional = false)
    private MovimentacaoPortoParametros codMovimentacaoPortoParametro;
    @JoinColumn(name = "codNavio", referencedColumnName = "codNavio")
    @ManyToOne
    private Navio codNavio;
    @JoinColumn(name = "codPoinAtracacao1", referencedColumnName = "codPoin")
    @ManyToOne
    private Poin codPoinAtracacao1;
    @JoinColumn(name = "codPoinAtracacao2", referencedColumnName = "codPoin")
    @ManyToOne
    private Poin codPoinAtracacao2;
    @JoinColumn(name = "codPoinAtracacao3", referencedColumnName = "codPoin")
    @ManyToOne
    private Poin codPoinAtracacao3;
    @JoinColumn(name = "codPoinAtracacao4", referencedColumnName = "codPoin")
    @ManyToOne
    private Poin codPoinAtracacao4;

    public MovimentacaoPorto() {
    }

    public MovimentacaoPorto(Integer codMovimentacaoPorto) {
        this.codMovimentacaoPorto = codMovimentacaoPorto;
    }

    public MovimentacaoPorto(Integer codMovimentacaoPorto, int mmsi, Date dataChegada) {
        this.codMovimentacaoPorto = codMovimentacaoPorto;
        this.mmsi = mmsi;
        this.dataChegada = dataChegada;
    }

    public Integer getCodMovimentacaoPorto() {
        return codMovimentacaoPorto;
    }

    public void setCodMovimentacaoPorto(Integer codMovimentacaoPorto) {
        this.codMovimentacaoPorto = codMovimentacaoPorto;
    }

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
    }

    public Integer getImo() {
        return imo;
    }

    public void setImo(Integer imo) {
        this.imo = imo;
    }

    public String getNomeNavio() {
        return nomeNavio;
    }

    public void setNomeNavio(String nomeNavio) {
        this.nomeNavio = nomeNavio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getCalado() {
        return calado;
    }

    public void setCalado(Double calado) {
        this.calado = calado;
    }

    public Double getCarga() {
        return carga;
    }

    public void setCarga(Double carga) {
        this.carga = carga;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Date getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(Date dataChegada) {
        this.dataChegada = dataChegada;
    }

    public Date getDataFundeio() {
        return dataFundeio;
    }

    public void setDataFundeio(Date dataFundeio) {
        this.dataFundeio = dataFundeio;
    }

    public Date getDataSaidaFundeio() {
        return dataSaidaFundeio;
    }

    public void setDataSaidaFundeio(Date dataSaidaFundeio) {
        this.dataSaidaFundeio = dataSaidaFundeio;
    }

    public Integer getTempoFundeioSegundos() {
        return tempoFundeioSegundos;
    }

    public void setTempoFundeioSegundos(Integer tempoFundeioSegundos) {
        this.tempoFundeioSegundos = tempoFundeioSegundos;
    }

    public Date getDataEntradaCanal() {
        return dataEntradaCanal;
    }

    public void setDataEntradaCanal(Date dataEntradaCanal) {
        this.dataEntradaCanal = dataEntradaCanal;
    }

    public String getCodPoinAtual() {
        return codPoinAtual;
    }

    public void setCodPoinAtual(String codPoinAtual) {
        this.codPoinAtual = codPoinAtual;
    }

    public Double getMaximaVelocidade() {
        return maximaVelocidade;
    }

    public void setMaximaVelocidade(Double maximaVelocidade) {
        this.maximaVelocidade = maximaVelocidade;
    }

    public Date getDataEntradaPoinAtracacao1() {
        return dataEntradaPoinAtracacao1;
    }

    public void setDataEntradaPoinAtracacao1(Date dataEntradaPoinAtracacao1) {
        this.dataEntradaPoinAtracacao1 = dataEntradaPoinAtracacao1;
    }

    public Date getDataSaidaPoinAtracacao1() {
        return dataSaidaPoinAtracacao1;
    }

    public void setDataSaidaPoinAtracacao1(Date dataSaidaPoinAtracacao1) {
        this.dataSaidaPoinAtracacao1 = dataSaidaPoinAtracacao1;
    }

    public Date getDataEntradaPoinAtracacao2() {
        return dataEntradaPoinAtracacao2;
    }

    public void setDataEntradaPoinAtracacao2(Date dataEntradaPoinAtracacao2) {
        this.dataEntradaPoinAtracacao2 = dataEntradaPoinAtracacao2;
    }

    public Date getDataSaidaPoinAtracacao2() {
        return dataSaidaPoinAtracacao2;
    }

    public void setDataSaidaPoinAtracacao2(Date dataSaidaPoinAtracacao2) {
        this.dataSaidaPoinAtracacao2 = dataSaidaPoinAtracacao2;
    }

    public Date getDataEntradaPoinAtracacao3() {
        return dataEntradaPoinAtracacao3;
    }

    public void setDataEntradaPoinAtracacao3(Date dataEntradaPoinAtracacao3) {
        this.dataEntradaPoinAtracacao3 = dataEntradaPoinAtracacao3;
    }

    public Date getDataSaidaPoinAtracacao3() {
        return dataSaidaPoinAtracacao3;
    }

    public void setDataSaidaPoinAtracacao3(Date dataSaidaPoinAtracacao3) {
        this.dataSaidaPoinAtracacao3 = dataSaidaPoinAtracacao3;
    }

    public Date getDataEntradaPoinAtracacao4() {
        return dataEntradaPoinAtracacao4;
    }

    public void setDataEntradaPoinAtracacao4(Date dataEntradaPoinAtracacao4) {
        this.dataEntradaPoinAtracacao4 = dataEntradaPoinAtracacao4;
    }

    public Date getDataSaidaPoinAtracacao4() {
        return dataSaidaPoinAtracacao4;
    }

    public void setDataSaidaPoinAtracacao4(Date dataSaidaPoinAtracacao4) {
        this.dataSaidaPoinAtracacao4 = dataSaidaPoinAtracacao4;
    }

    public Date getDataSaidaCanal() {
        return dataSaidaCanal;
    }

    public void setDataSaidaCanal(Date dataSaidaCanal) {
        this.dataSaidaCanal = dataSaidaCanal;
    }

    public MovimentacaoPortoParametros getCodMovimentacaoPortoParametro() {
        return codMovimentacaoPortoParametro;
    }

    public void setCodMovimentacaoPortoParametro(MovimentacaoPortoParametros codMovimentacaoPortoParametro) {
        this.codMovimentacaoPortoParametro = codMovimentacaoPortoParametro;
    }

    public Navio getCodNavio() {
        return codNavio;
    }

    public void setCodNavio(Navio codNavio) {
        this.codNavio = codNavio;
    }

    public Poin getCodPoinAtracacao1() {
        return codPoinAtracacao1;
    }

    public void setCodPoinAtracacao1(Poin codPoinAtracacao1) {
        this.codPoinAtracacao1 = codPoinAtracacao1;
    }

    public Poin getCodPoinAtracacao2() {
        return codPoinAtracacao2;
    }

    public void setCodPoinAtracacao2(Poin codPoinAtracacao2) {
        this.codPoinAtracacao2 = codPoinAtracacao2;
    }

    public Poin getCodPoinAtracacao3() {
        return codPoinAtracacao3;
    }

    public void setCodPoinAtracacao3(Poin codPoinAtracacao3) {
        this.codPoinAtracacao3 = codPoinAtracacao3;
    }

    public Poin getCodPoinAtracacao4() {
        return codPoinAtracacao4;
    }

    public void setCodPoinAtracacao4(Poin codPoinAtracacao4) {
        this.codPoinAtracacao4 = codPoinAtracacao4;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codMovimentacaoPorto != null ? codMovimentacaoPorto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimentacaoPorto)) {
            return false;
        }
        MovimentacaoPorto other = (MovimentacaoPorto) object;
        if ((this.codMovimentacaoPorto == null && other.codMovimentacaoPorto != null) || (this.codMovimentacaoPorto != null && !this.codMovimentacaoPorto.equals(other.codMovimentacaoPorto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MovimentacaoPorto{" + "codMovimentacaoPorto=" + codMovimentacaoPorto + ", nomeNavio=" + nomeNavio + ", dataChegada=" + dataChegada + ", dataFundeio=" + dataFundeio + ", dataSaidaFundeio=" + dataSaidaFundeio + ", dataEntradaCanal=" + dataEntradaCanal + ", dataEntradaPoinAtracacao1=" + dataEntradaPoinAtracacao1 + ", dataSaidaPoinAtracacao1=" + dataSaidaPoinAtracacao1 + ", dataEntradaPoinAtracacao2=" + dataEntradaPoinAtracacao2 + ", dataSaidaPoinAtracacao2=" + dataSaidaPoinAtracacao2 + ", dataEntradaPoinAtracacao3=" + dataEntradaPoinAtracacao3 + ", dataSaidaPoinAtracacao3=" + dataSaidaPoinAtracacao3 + ", dataEntradaPoinAtracacao4=" + dataEntradaPoinAtracacao4 + ", dataSaidaPoinAtracacao4=" + dataSaidaPoinAtracacao4 + ", dataSaidaCanal=" + dataSaidaCanal + ", codMovimentacaoPortoParametro=" + codMovimentacaoPortoParametro + ", codNavio=" + codNavio + ", codPoinAtracacao1=" + codPoinAtracacao1 + ", codPoinAtracacao2=" + codPoinAtracacao2 + ", codPoinAtracacao3=" + codPoinAtracacao3 + ", codPoinAtracacao4=" + codPoinAtracacao4 + '}';
    }

    
    
}
