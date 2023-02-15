package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "sensor_proximidade_status")
public class SensorProximidadeStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codSensorProximidade")
    private Integer codSensorProximidade;
    
    @Column(name = "dataHora")
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    
    @Column(name = "statusComunicacaoLaser")
    @Basic(optional = false)
    private boolean statusComunicacaoLaser;

    @Column(name = "ip")
    private String ip;
    
    @Column(name = "ultimaLeitura")
    private String ultimaLeitura;
    
    public Integer getCodSensorProximidade() {
        return codSensorProximidade;
    }

    public void setCodSensorProximidade(Integer codSensorProximidade) {
        this.codSensorProximidade = codSensorProximidade;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public boolean isStatusComunicacaoLaser() {
        return statusComunicacaoLaser;
    }

    public void setStatusComunicacaoLaser(boolean statusComunicacaoLaser) {
        this.statusComunicacaoLaser = statusComunicacaoLaser;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }    

    public String getUltimaLeitura() {
        return ultimaLeitura;
    }

    public void setUltimaLeitura(String ultimaLeitura) {
        this.ultimaLeitura = ultimaLeitura;
    }    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.codSensorProximidade);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SensorProximidadeStatus other = (SensorProximidadeStatus) obj;
        if (!Objects.equals(this.codSensorProximidade, other.codSensorProximidade)) {
            return false;
        }
        return true;
    }
    
}
