package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "pratico")
public class Pratico implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "codPratico")
    private Integer codPratico;
    
    @Column(name = "nomePratico")
    @Basic(optional = false)
    private String nomePratico;
    
    @Basic(optional = false)
    @Column(name = "dataCadastro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;    
    
    @JoinColumn(name = "codCliente", referencedColumnName = "cod")
    @ManyToOne(optional = true)
    private Clientes codCliente;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;

    public Integer getCodPratico() {
        return codPratico;
    }

    public void setCodPratico(Integer codPratico) {
        this.codPratico = codPratico;
    }

    public String getNomePratico() {
        return nomePratico;
    }

    public void setNomePratico(String nomePratico) {
        this.nomePratico = nomePratico;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Clientes getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Clientes codCliente) {
        this.codCliente = codCliente;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.codPratico);
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
        final Pratico other = (Pratico) obj;
        if (!Objects.equals(this.codPratico, other.codPratico)) {
            return false;
        }
        return true;
    }

}
