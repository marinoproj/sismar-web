package br.com.marino.sismar.entity;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "sensores_proximidade_bercos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SensorProximidadeBerco.findAll", query = "SELECT s FROM SensorProximidadeBerco s")
    , @NamedQuery(name = "SensorProximidadeBerco.findByCod", query = "SELECT s FROM SensorProximidadeBerco s WHERE s.cod = :cod")
    , @NamedQuery(name = "SensorProximidadeBerco.findByCodSensorProximidade", query = "SELECT s FROM SensorProximidadeBerco s WHERE s.codSensorProximidade = :cod")})
public class SensorProximidadeBerco implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod")
    private Integer cod;
    
    @JoinColumn(name = "codSensorProximidade", referencedColumnName = "cod")
    @ManyToOne(optional = false)
    private SensorProximidade codSensorProximidade;
    
    @JoinColumn(name = "codBerco", referencedColumnName = "codBerco")
    @ManyToOne(optional = false)
    private Berco codBerco;
        
    @Column(name = "descontoDistancia")
    private Double descontoDistancia;

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public SensorProximidade getCodSensorProximidade() {
        return codSensorProximidade;
    }

    public void setCodSensorProximidade(SensorProximidade codSensorProximidade) {
        this.codSensorProximidade = codSensorProximidade;
    }

    public Berco getCodBerco() {
        return codBerco;
    }

    public void setCodBerco(Berco codBerco) {
        this.codBerco = codBerco;
    }

    public Double getDescontoDistancia() {
        return descontoDistancia;
    }

    public void setDescontoDistancia(Double descontoDistancia) {
        this.descontoDistancia = descontoDistancia;
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
        if (!(object instanceof SensorProximidadeBerco)) {
            return false;
        }
        SensorProximidadeBerco other = (SensorProximidadeBerco) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

}
