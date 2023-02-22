package br.com.marino.sismar.entity;

import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "aispoin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aispoin.findAll", query = "SELECT a FROM Aispoin a")
    , @NamedQuery(name = "Aispoin.findByCodAisPoin", query = "SELECT a FROM Aispoin a WHERE a.codAisPoin = :codAisPoin")
    , @NamedQuery(name = "Aispoin.findByMmsi", query = "SELECT a FROM Aispoin a WHERE a.mmsi = :mmsi")
    , @NamedQuery(name = "Aispoin.findByDataEntrada", query = "SELECT a FROM Aispoin a WHERE a.dataEntrada = :dataEntrada")
    , @NamedQuery(name = "Aispoin.findByDataSaida", query = "SELECT a FROM Aispoin a WHERE a.dataSaida = :dataSaida")
    , @NamedQuery(name = "Aispoin.findByVelocidadeEntrada", query = "SELECT a FROM Aispoin a WHERE a.velocidadeEntrada = :velocidadeEntrada")
    , @NamedQuery(name = "Aispoin.findByVelocidadeSaida", query = "SELECT a FROM Aispoin a WHERE a.velocidadeSaida = :velocidadeSaida")})
public class Aispoin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codAisPoin")
    private Integer codAisPoin;

    @Basic(optional = false)
    @NotNull
    @Column(name = "mmsi")
    private int mmsi;

    @Basic(optional = false)
    @NotNull
    @Column(name = "dataEntrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntrada;

    @Column(name = "dataSaida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSaida;

    @Column(name = "velocidadeEntrada")
    private Double velocidadeEntrada;

    @Column(name = "velocidadeSaida")
    private Double velocidadeSaida;

    @JoinColumn(name = "codPoin", referencedColumnName = "codPoin")
    @ManyToOne(optional = false)
    private Poin codPoin;

    @Transient
    private Ais aisMmsi;

    @Transient
    private Integer tempoPermanencia;

    public Aispoin() {
    }

    public Aispoin(Integer codAisPoin) {
        this.codAisPoin = codAisPoin;
    }

    public Aispoin(Integer codAisPoin, int mmsi, Date dataEntrada) {
        this.codAisPoin = codAisPoin;
        this.mmsi = mmsi;
        this.dataEntrada = dataEntrada;
    }

    public Integer getCodAisPoin() {
        return codAisPoin;
    }

    public void setCodAisPoin(Integer codAisPoin) {
        this.codAisPoin = codAisPoin;
    }

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Double getVelocidadeEntrada() {
        return velocidadeEntrada;
    }

    public void setVelocidadeEntrada(Double velocidadeEntrada) {
        this.velocidadeEntrada = velocidadeEntrada;
    }

    public Double getVelocidadeSaida() {
        return velocidadeSaida;
    }

    public void setVelocidadeSaida(Double velocidadeSaida) {
        this.velocidadeSaida = velocidadeSaida;
    }

    public Poin getCodPoin() {
        return codPoin;
    }

    public void setCodPoin(Poin codPoin) {
        this.codPoin = codPoin;
    }

    public Integer getTempoPermanencia() {
        return tempoPermanencia;
    }

    public void setTempoPermanencia(Integer tempoPermanencia) {
        this.tempoPermanencia = tempoPermanencia;
    }
    
    public long getTempoPermanenciaEmMinutos(){
        return Util.getTempoPermanenciaEmMinutos(dataEntrada, (dataSaida == null ? new Date() : dataSaida));
    }

    public String getTempoPermanenciaByString() {
        int qtdSegundos;
        int horas = 0;
        int minutos = 0;
        int segundos = 0;

        try {
            qtdSegundos = tempoPermanencia;
            horas = qtdSegundos / 3600;
            minutos = qtdSegundos % 3600 / 60;
            segundos = qtdSegundos % 3600 % 60;
        } catch (Exception ex) {
        }

        return (horas < 10 ? "0" + horas : horas) + ":" + (minutos < 10 ? "0" + minutos : minutos)
                + ":" + (segundos < 10 ? "0" + segundos : segundos);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codAisPoin != null ? codAisPoin.hashCode() : 0);
        return hash;
    }

    public Ais getAisMmsi() {
        return aisMmsi;
    }

    public void setAisMmsi(Ais aisMmsi) {
        this.aisMmsi = aisMmsi;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aispoin)) {
            return false;
        }
        Aispoin other = (Aispoin) object;
        if ((this.codAisPoin == null && other.codAisPoin != null) || (this.codAisPoin != null && !this.codAisPoin.equals(other.codAisPoin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Aispoin{" + "codAisPoin=" + codAisPoin + ", mmsi=" + mmsi
                + ", dataEntrada=" + dataEntrada + ", dataSaida=" + dataSaida
                + ", velocidadeEntrada=" + velocidadeEntrada + ", velocidadeSaida="
                + velocidadeSaida + ", codPoin=" + codPoin + ", aisMmsi=" + aisMmsi + '}';
    }

}
