package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "bercos_clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BercoCliente.findAll", query = "SELECT bc FROM BercoCliente bc")
    , @NamedQuery(name = "BercoCliente.findByCodCliente", query = "SELECT bc FROM BercoCliente bc WHERE bc.codCliente.cod = :codCliente")})
public class BercoCliente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id    
    @JoinColumn(name = "codBerco", referencedColumnName = "codBerco")
    @ManyToOne
    private Berco codBerco;
    
    @JoinColumn(name = "codCliente", referencedColumnName = "cod")
    @ManyToOne
    private Clientes codCliente;        
    
    @Column(name = "distanciaEntreSensores")
    private Double distanciaEntreSensores;

    public Berco getCodBerco() {
        return codBerco;
    }

    public void setCodBerco(Berco codBerco) {
        this.codBerco = codBerco;
    }

    public Clientes getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Clientes codCliente) {
        this.codCliente = codCliente;
    }

    public Double getDistanciaEntreSensores() {
        return distanciaEntreSensores;
    }

    public void setDistanciaEntreSensores(Double distanciaEntreSensores) {
        this.distanciaEntreSensores = distanciaEntreSensores;
    }    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.codBerco);
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
        final BercoCliente other = (BercoCliente) obj;
        if (!Objects.equals(this.codBerco, other.codBerco)) {
            return false;
        }
        return true;
    }    
    
}