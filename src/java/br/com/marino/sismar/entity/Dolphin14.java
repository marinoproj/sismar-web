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
@Table(name = "dolphin14")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dolphin14.findAll", query = "SELECT v FROM Dolphin14 v")
    ,@NamedQuery(name = "Dolphin14.findByPeriod", query = "SELECT v FROM Dolphin14 v WHERE v.dataHora >= :startDate AND v.dataHora <= :endDate")})
public class Dolphin14 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "dataHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;

    @Basic(optional = false)
    @Column(name = "direcaoMediaVento")
    private Double direcaoMediaVento;

    @Basic(optional = false)
    @Column(name = "velocidadeMediaVento")
    private Double velocidadeMediaVento;

    @Basic(optional = false)
    @Column(name = "velocidadeMaximaVento")
    private Double velocidadeMaximaVento;

    public Dolphin14() {
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public Double getDirecaoMediaVento() {
        return direcaoMediaVento;
    }

    public void setDirecaoMediaVento(Double direcaoMediaVento) {
        this.direcaoMediaVento = direcaoMediaVento;
    }

    public Double getVelocidadeMediaVento() {
        return velocidadeMediaVento;
    }

    public void setVelocidadeMediaVento(Double velocidadeMediaVento) {
        this.velocidadeMediaVento = velocidadeMediaVento;
    }

    public Double getVelocidadeMaximaVento() {
        return velocidadeMaximaVento;
    }

    public void setVelocidadeMaximaVento(Double velocidadeMaximaVento) {
        this.velocidadeMaximaVento = velocidadeMaximaVento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dataHora != null ? dataHora.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Dolphin14)) {
            return false;
        }
        Dolphin14 other = (Dolphin14) object;
        return !((this.dataHora == null && other.dataHora != null)
                || (this.dataHora != null && !this.dataHora.equals(other.dataHora)));
    }

    @Override
    public String toString() {
        return "Dolphin14{" + "dataHora=" + dataHora
                + ", direcaoMediaVento=" + direcaoMediaVento
                + ", velocidadeMediaVento=" + velocidadeMediaVento
                + ", velocidadeMaximaVento=" + velocidadeMaximaVento + '}';
    }
    
}