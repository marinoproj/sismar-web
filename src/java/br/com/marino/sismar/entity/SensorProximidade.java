package br.com.marino.sismar.entity;

import br.com.marino.sismar.enums.LadoSensorProximidade;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "sensores_proximidade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SensorProximidade.findAll", query = "SELECT s FROM SensorProximidade s")
    , @NamedQuery(name = "SensorProximidade.findByCod", query = "SELECT s FROM SensorProximidade s WHERE s.cod = :cod")
    , @NamedQuery(name = "SensorProximidade.findByStatus", query = "SELECT s FROM SensorProximidade s WHERE s.status = :status")})
public class SensorProximidade implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod")
    private Integer cod;
    
    @Column(name = "serial")
    @Basic(optional = false)
    private String serial;
    
    @Column(name = "descricao")
    @Basic(optional = false)
    private String descricao;
    
    @JoinColumn(name = "codCliente", referencedColumnName = "cod")
    @ManyToOne(optional = false)
    private Clientes codCliente;
    
    @JoinColumn(name = "codBerco", referencedColumnName = "codBerco")
    @ManyToOne
    private Berco codBerco;
    
    @Column(name = "lado")
    @Basic(optional = false)
    @Enumerated(EnumType.ORDINAL)
    private LadoSensorProximidade lado;
    
    @Column(name = "status")
    @Basic(optional = false)
    private boolean status;

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Clientes getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Clientes codCliente) {
        this.codCliente = codCliente;
    }

    public Berco getCodBerco() {
        return codBerco;
    }

    public void setCodBerco(Berco codBerco) {
        this.codBerco = codBerco;
    }

    public LadoSensorProximidade getLado() {
        return lado;
    }

    public void setLado(LadoSensorProximidade lado) {
        this.lado = lado;
    }   

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
        if (!(object instanceof SensorProximidade)) {
            return false;
        }
        SensorProximidade other = (SensorProximidade) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

}
