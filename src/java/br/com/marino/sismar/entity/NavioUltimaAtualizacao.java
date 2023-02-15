package br.com.marino.sismar.entity;

import br.com.marino.sismar.util.MarineTraffic;
import br.com.marino.sismar.util.Util;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "ais_radio_ultima_atualizacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NavioUltimaAtualizacao.findAll", query = "SELECT n FROM NavioUltimaAtualizacao n")
    , @NamedQuery(name = "NavioUltimaAtualizacao.findByMmsi", query = "SELECT n FROM NavioUltimaAtualizacao n WHERE n.mmsi = :mmsi")
    , @NamedQuery(name = "NavioUltimaAtualizacao.findByCodAisTabela", query = "SELECT n FROM NavioUltimaAtualizacao n WHERE n.codAisTabela = :codAisTabela")
    , @NamedQuery(name = "NavioUltimaAtualizacao.findByDataUltimaAtualizacao", query = "SELECT n FROM NavioUltimaAtualizacao n WHERE n.dataUltimaAtualizacao = :dataUltimaAtualizacao")
    , @NamedQuery(name = "NavioUltimaAtualizacao.findByCodAis", query = "SELECT n FROM NavioUltimaAtualizacao n WHERE n.codAis = :codAis")})
public class NavioUltimaAtualizacao implements Serializable {

    @Size(max = 200)
    @Column(name = "destino")
    private String destino;
    @Size(max = 200)
    @Column(name = "chegadaPrevista")
    private String chegadaPrevista;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Size(max = 200)
    @Column(name = "statusNavegacao")
    private String statusNavegacao;
    @Column(name = "velocidadeSobreSolo")
    private Double velocidadeSobreSolo;
    @Column(name = "cursoSobreSolo")
    private Double cursoSobreSolo;
    @Column(name = "velocidadeVento")
    private Double velocidadeVento;
    @Column(name = "maximaVento")
    private Double maximaVento;
    @Column(name = "direcaoVento")
    private Double direcaoVento;
    @Column(name = "velocidadeCorrente")
    private Double velocidadeCorrente;
    @Column(name = "direcaoCorrente")
    private Double direcaoCorrente;
    @Column(name = "epfd")
    private Integer epfd;
    @Column(name = "draught")
    private Double draught;
    @Column(name = "positionAccurate")
    private Short positionAccurate;
    @Column(name = "heading")
    private Integer heading;
    @Column(name = "raim")
    private Short raim;

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "mmsi")
    private Integer mmsi;

    @Column(name = "codAisTabela")
    private Integer codAisTabela;

    @Column(name = "codAis")
    private Integer codAis;

    @Column(name = "dataUltimaAtualizacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltimaAtualizacao;
    
    @Transient
    private boolean dataFromExternal = false;

    public NavioUltimaAtualizacao() {
    }

    public NavioUltimaAtualizacao(MarineTraffic marineTraffic, int mmsi) throws Exception {
        if (marineTraffic == null) {
            return;
        }
        latitude = Util.round(marineTraffic.getLatitude(), 7);
        longitude = Util.round(marineTraffic.getLongitude(), 7);
        statusNavegacao = marineTraffic.getStatus();
        dataUltimaAtualizacao = marineTraffic.getDataHora();
        draught = Util.round(marineTraffic.getCalado(), 1);
        destino = marineTraffic.getDestino();
        chegadaPrevista = marineTraffic.getChegadaPrevista();
        velocidadeSobreSolo = Util.round(marineTraffic.getVelocidade(), 1);
        cursoSobreSolo = Util.round(marineTraffic.getDirecao(), 1);
        this.mmsi = mmsi;
        dataFromExternal = true;
    }

    public NavioUltimaAtualizacao(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public Integer getCodAisTabela() {
        return codAisTabela;
    }

    public void setCodAisTabela(Integer codAisTabela) {
        this.codAisTabela = codAisTabela;
    }

    public Integer getCodAis() {
        return codAis;
    }

    public void setCodAis(Integer codAis) {
        this.codAis = codAis;
    }

    public Date getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mmsi != null ? mmsi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NavioUltimaAtualizacao)) {
            return false;
        }
        NavioUltimaAtualizacao other = (NavioUltimaAtualizacao) object;
        if ((this.mmsi == null && other.mmsi != null) || (this.mmsi != null && !this.mmsi.equals(other.mmsi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NavioUltimaAtualizacao{" + "destino=" + destino + ", chegadaPrevista=" + chegadaPrevista + ", latitude=" + latitude + ", longitude=" + longitude + ", statusNavegacao=" + statusNavegacao + ", velocidadeSobreSolo=" + velocidadeSobreSolo + ", cursoSobreSolo=" + cursoSobreSolo + ", velocidadeVento=" + velocidadeVento + ", maximaVento=" + maximaVento + ", direcaoVento=" + direcaoVento + ", velocidadeCorrente=" + velocidadeCorrente + ", direcaoCorrente=" + direcaoCorrente + ", epfd=" + epfd + ", draught=" + draught + ", positionAccurate=" + positionAccurate + ", heading=" + heading + ", raim=" + raim + ", mmsi=" + mmsi + ", codAisTabela=" + codAisTabela + ", codAis=" + codAis + ", dataUltimaAtualizacao=" + dataUltimaAtualizacao + '}';
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getChegadaPrevista() {
        return chegadaPrevista;
    }

    public void setChegadaPrevista(String chegadaPrevista) {
        this.chegadaPrevista = chegadaPrevista;
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

    public String getStatusNavegacao() {
        return statusNavegacao;
    }

    public void setStatusNavegacao(String statusNavegacao) {
        this.statusNavegacao = statusNavegacao;
    }

    public Double getVelocidadeSobreSolo() {
        return velocidadeSobreSolo;
    }

    public void setVelocidadeSobreSolo(Double velocidadeSobreSolo) {
        this.velocidadeSobreSolo = velocidadeSobreSolo;
    }

    public Double getCursoSobreSolo() {
        return cursoSobreSolo;
    }

    public void setCursoSobreSolo(Double cursoSobreSolo) {
        this.cursoSobreSolo = cursoSobreSolo;
    }

    public Double getVelocidadeVento() {
        return velocidadeVento;
    }

    public void setVelocidadeVento(Double velocidadeVento) {
        this.velocidadeVento = velocidadeVento;
    }

    public Double getMaximaVento() {
        return maximaVento;
    }

    public void setMaximaVento(Double maximaVento) {
        this.maximaVento = maximaVento;
    }

    public Double getDirecaoVento() {
        return direcaoVento;
    }

    public void setDirecaoVento(Double direcaoVento) {
        this.direcaoVento = direcaoVento;
    }

    public Double getVelocidadeCorrente() {
        return velocidadeCorrente;
    }

    public void setVelocidadeCorrente(Double velocidadeCorrente) {
        this.velocidadeCorrente = velocidadeCorrente;
    }

    public Double getDirecaoCorrente() {
        return direcaoCorrente;
    }

    public void setDirecaoCorrente(Double direcaoCorrente) {
        this.direcaoCorrente = direcaoCorrente;
    }

    public Integer getEpfd() {
        return epfd;
    }

    public void setEpfd(Integer epfd) {
        this.epfd = epfd;
    }

    public Double getDraught() {
        return draught;
    }

    public void setDraught(Double draught) {
        this.draught = draught;
    }

    public Short getPositionAccurate() {
        return positionAccurate;
    }

    public void setPositionAccurate(Short positionAccurate) {
        this.positionAccurate = positionAccurate;
    }

    public Integer getHeading() {
        return heading;
    }

    public void setHeading(Integer heading) {
        this.heading = heading;
    }

    public Short getRaim() {
        return raim;
    }

    public void setRaim(Short raim) {
        this.raim = raim;
    }

    public boolean isDataFromExternal() {
        return dataFromExternal;
    }

    public void setDataFromExternal(boolean dataFromExternal) {
        this.dataFromExternal = dataFromExternal;
    }
    
}
