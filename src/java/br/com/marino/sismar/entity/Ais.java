package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ais.findAll", query = "SELECT a FROM Ais a")
    , @NamedQuery(name = "Ais.findByCodAis", query = "SELECT a FROM Ais a WHERE a.codAis = :codAis")
    , @NamedQuery(name = "Ais.findByDataHora", query = "SELECT a FROM Ais a WHERE a.dataHora = :dataHora")
    , @NamedQuery(name = "Ais.findByMmsi", query = "SELECT a FROM Ais a WHERE a.mmsi = :mmsi")
    , @NamedQuery(name = "Ais.findByCodAisTabela", query = "SELECT a FROM Ais a WHERE a.codAisTabela = :codAisTabela")
    , @NamedQuery(name = "Ais.findByDestino", query = "SELECT a FROM Ais a WHERE a.destino = :destino")
    , @NamedQuery(name = "Ais.findByChegadaPrevista", query = "SELECT a FROM Ais a WHERE a.chegadaPrevista = :chegadaPrevista")
    , @NamedQuery(name = "Ais.findByLatitude", query = "SELECT a FROM Ais a WHERE a.latitude = :latitude")
    , @NamedQuery(name = "Ais.findByLongitude", query = "SELECT a FROM Ais a WHERE a.longitude = :longitude")
    , @NamedQuery(name = "Ais.findByStatusNavegacao", query = "SELECT a FROM Ais a WHERE a.statusNavegacao = :statusNavegacao")
    , @NamedQuery(name = "Ais.findByVelocidadeSobreSolo", query = "SELECT a FROM Ais a WHERE a.velocidadeSobreSolo = :velocidadeSobreSolo")
    , @NamedQuery(name = "Ais.findByCursoSobreSolo", query = "SELECT a FROM Ais a WHERE a.cursoSobreSolo = :cursoSobreSolo")
    , @NamedQuery(name = "Ais.findByVelocidadeVento", query = "SELECT a FROM Ais a WHERE a.velocidadeVento = :velocidadeVento")
    , @NamedQuery(name = "Ais.findByMaximaVento", query = "SELECT a FROM Ais a WHERE a.maximaVento = :maximaVento")
    , @NamedQuery(name = "Ais.findByDirecaoVento", query = "SELECT a FROM Ais a WHERE a.direcaoVento = :direcaoVento")
    , @NamedQuery(name = "Ais.findByVelocidadeCorrente", query = "SELECT a FROM Ais a WHERE a.velocidadeCorrente = :velocidadeCorrente")
    , @NamedQuery(name = "Ais.findByDirecaoCorrente", query = "SELECT a FROM Ais a WHERE a.direcaoCorrente = :direcaoCorrente")
    , @NamedQuery(name = "Ais.findByEpfd", query = "SELECT a FROM Ais a WHERE a.epfd = :epfd")
    , @NamedQuery(name = "Ais.findByDraught", query = "SELECT a FROM Ais a WHERE a.draught = :draught")
    , @NamedQuery(name = "Ais.findByPositionAccurate", query = "SELECT a FROM Ais a WHERE a.positionAccurate = :positionAccurate")
    , @NamedQuery(name = "Ais.findByHeading", query = "SELECT a FROM Ais a WHERE a.heading = :heading")
    , @NamedQuery(name = "Ais.findByRaim", query = "SELECT a FROM Ais a WHERE a.raim = :raim")})
public class Ais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "codAis")
    private Integer codAis;

    @Basic(optional = false)
    @Column(name = "dataHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;

    @Basic(optional = false)
    @Column(name = "mmsi")
    private int mmsi;

    @Basic(optional = false)
    @Column(name = "codAisTabela")
    private int codAisTabela;

    @Column(name = "destino")
    private String destino;

    @Column(name = "chegadaPrevista")
    private String chegadaPrevista;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

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

    @Transient
    private Navio codNavio;    

    public Ais() {
    }

    public Ais(Integer codAis) {
        this.codAis = codAis;
    }

    public Integer getCodAis() {
        return codAis;
    }

    public void setCodAis(Integer codAis) {
        this.codAis = codAis;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
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

    public Navio getCodNavio() {
        return codNavio;
    }

    public void setCodNavio(Navio codNavio) {
        this.codNavio = codNavio;
    }

    public int getCodAisTabela() {
        return codAisTabela;
    }

    public void setCodAisTabela(int codAisTabela) {
        this.codAisTabela = codAisTabela;
    }   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codAis != null ? codAis.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ais)) {
            return false;
        }
        Ais other = (Ais) object;
        if ((this.codAis == null && other.codAis != null) || (this.codAis != null && !this.codAis.equals(other.codAis))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ais{" + "codAis=" + codAis + ", dataHora=" + dataHora + ", mmsi=" + mmsi + ", codAisTabela=" + codAisTabela + ", destino=" + destino + ", chegadaPrevista=" + chegadaPrevista + ", latitude=" + latitude + ", longitude=" + longitude + ", statusNavegacao=" + statusNavegacao + ", velocidadeSobreSolo=" + velocidadeSobreSolo + ", cursoSobreSolo=" + cursoSobreSolo + ", velocidadeVento=" + velocidadeVento + ", maximaVento=" + maximaVento + ", direcaoVento=" + direcaoVento + ", velocidadeCorrente=" + velocidadeCorrente + ", direcaoCorrente=" + direcaoCorrente + ", epfd=" + epfd + ", draught=" + draught + ", positionAccurate=" + positionAccurate + ", heading=" + heading + ", raim=" + raim + ", codNavio=" + codNavio + '}';
    }

}
