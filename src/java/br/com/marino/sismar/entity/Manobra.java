package br.com.marino.sismar.entity;

import br.com.marino.sismar.enums.StatusOperacaoEnum;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Manobra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod")
    private Long cod;

    @Basic(optional = false)
    @Column(name = "dataHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;

    @Column(name = "distanciaDireita")
    private Float distanciaDireita;

    @Column(name = "velocidadeDireita")
    private Float velocidadeDireita;

    @Column(name = "distanciaEsquerda")
    private Float distanciaEsquerda;

    @Column(name = "velocidadeEsquerda")
    private Float velocidadeEsquerda;

    @Column(name = "angulo")
    private Float angulo;

    @Column(name = "gravadoDistanciaDireita")
    private boolean gravadoDistanciaDireita;

    @Column(name = "gravadoDistanciaEsquerda")
    private boolean gravadoDistanciaEsquerda;

    @Enumerated(EnumType.ORDINAL)
    private StatusOperacaoEnum status;

    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public Float getDistanciaDireita() {
        return distanciaDireita;
    }

    public void setDistanciaDireita(Float distanciaDireita) {
        this.distanciaDireita = distanciaDireita;
    }

    public Float getVelocidadeDireita() {
        return velocidadeDireita;
    }

    public void setVelocidadeDireita(Float velocidadeDireita) {
        this.velocidadeDireita = velocidadeDireita;
    }

    public Float getDistanciaEsquerda() {
        return distanciaEsquerda;
    }

    public void setDistanciaEsquerda(Float distanciaEsquerda) {
        this.distanciaEsquerda = distanciaEsquerda;
    }

    public Float getVelocidadeEsquerda() {
        return velocidadeEsquerda;
    }

    public void setVelocidadeEsquerda(Float velocidadeEsquerda) {
        this.velocidadeEsquerda = velocidadeEsquerda;
    }

    public Float getAngulo() {
        return angulo;
    }

    public void setAngulo(Float angulo) {
        this.angulo = angulo;
    }

    public boolean isGravadoDistanciaDireita() {
        return gravadoDistanciaDireita;
    }

    public void setGravadoDistanciaDireita(boolean gravadoDistanciaDireita) {
        this.gravadoDistanciaDireita = gravadoDistanciaDireita;
    }

    public boolean isGravadoDistanciaEsquerda() {
        return gravadoDistanciaEsquerda;
    }

    public void setGravadoDistanciaEsquerda(boolean gravadoDistanciaEsquerda) {
        this.gravadoDistanciaEsquerda = gravadoDistanciaEsquerda;
    }

    public StatusOperacaoEnum getStatus() {
        return status;
    }

    public void setStatus(StatusOperacaoEnum status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.cod);
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
        final Manobra other = (Manobra) obj;
        if (!Objects.equals(this.cod, other.cod)) {
            return false;
        }
        return true;
    }
    
}
