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
@Table(name = "correntometro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Correntometro.findAll", query = "SELECT c FROM Correntometro c")})
public class Correntometro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "dataHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;

    @Basic(optional = false)
    @Column(name = "intensidade")
    private double intensidade;

    @Basic(optional = false)
    @Column(name = "direcao")
    private double direcao;
    
    @Basic(optional = false)
    @Column(name = "intensidade2")
    private double intensidade2;
    
    @Basic(optional = false)
    @Column(name = "direcao2")
    private double direcao2;
    
    @Basic(optional = false)
    @Column(name = "intensidade3")
    private double intensidade3;
    
    @Basic(optional = false)
    @Column(name = "direcao3")
    private double direcao3;
    
    @Basic(optional = false)
    @Column(name = "intensidade4")
    private double intensidade4;
    
    @Basic(optional = false)
    @Column(name = "direcao4")
    private double direcao4;
    
    @Basic(optional = false)
    @Column(name = "intensidade5")
    private double intensidade5;
    
    @Basic(optional = false)
    @Column(name = "direcao5")
    private double direcao5;
    
    @Basic(optional = false)
    @Column(name = "intensidade6")
    private double intensidade6;
    
    @Basic(optional = false)
    @Column(name = "direcao6")
    private double direcao6;
    
    @Basic(optional = false)
    @Column(name = "intensidade7")
    private double intensidade7;
    
    @Basic(optional = false)
    @Column(name = "direcao7")
    private double direcao7;
    
    @Basic(optional = false)
    @Column(name = "intensidade8")
    private double intensidade8;
    
    @Basic(optional = false)
    @Column(name = "direcao8")
    private double direcao8;
    
    @Basic(optional = false)
    @Column(name = "intensidade9")
    private double intensidade9;
    
    @Basic(optional = false)
    @Column(name = "direcao9")
    private double direcao9;
    
    @Basic(optional = false)
    @Column(name = "intensidade10")
    private double intensidade10;
    
    @Basic(optional = false)
    @Column(name = "direcao10")
    private double direcao10;
    

    public Correntometro() {
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public double getIntensidade() {
        return intensidade;
    }

    public void setIntensidade(double intensidade) {
        this.intensidade = intensidade;
    }

    public double getDirecao() {
        return direcao;
    }

    public void setDirecao(double direcao) {
        this.direcao = direcao;
    }

    public double getIntensidade2() {
        return intensidade2;
    }

    public void setIntensidade2(double intensidade2) {
        this.intensidade2 = intensidade2;
    }

    public double getDirecao2() {
        return direcao2;
    }

    public void setDirecao2(double direcao2) {
        this.direcao2 = direcao2;
    }

    public double getIntensidade3() {
        return intensidade3;
    }

    public void setIntensidade3(double intensidade3) {
        this.intensidade3 = intensidade3;
    }

    public double getDirecao3() {
        return direcao3;
    }

    public void setDirecao3(double direcao3) {
        this.direcao3 = direcao3;
    }

    public double getIntensidade4() {
        return intensidade4;
    }

    public void setIntensidade4(double intensidade4) {
        this.intensidade4 = intensidade4;
    }

    public double getDirecao4() {
        return direcao4;
    }

    public void setDirecao4(double direcao4) {
        this.direcao4 = direcao4;
    }

    public double getIntensidade5() {
        return intensidade5;
    }

    public void setIntensidade5(double intensidade5) {
        this.intensidade5 = intensidade5;
    }

    public double getDirecao5() {
        return direcao5;
    }

    public void setDirecao5(double direcao5) {
        this.direcao5 = direcao5;
    }

    public double getIntensidade6() {
        return intensidade6;
    }

    public void setIntensidade6(double intensidade6) {
        this.intensidade6 = intensidade6;
    }

    public double getDirecao6() {
        return direcao6;
    }

    public void setDirecao6(double direcao6) {
        this.direcao6 = direcao6;
    }

    public double getIntensidade7() {
        return intensidade7;
    }

    public void setIntensidade7(double intensidade7) {
        this.intensidade7 = intensidade7;
    }

    public double getDirecao7() {
        return direcao7;
    }

    public void setDirecao7(double direcao7) {
        this.direcao7 = direcao7;
    }

    public double getIntensidade8() {
        return intensidade8;
    }

    public void setIntensidade8(double intensidade8) {
        this.intensidade8 = intensidade8;
    }

    public double getDirecao8() {
        return direcao8;
    }

    public void setDirecao8(double direcao8) {
        this.direcao8 = direcao8;
    }

    public double getIntensidade9() {
        return intensidade9;
    }

    public void setIntensidade9(double intensidade9) {
        this.intensidade9 = intensidade9;
    }

    public double getDirecao9() {
        return direcao9;
    }

    public void setDirecao9(double direcao9) {
        this.direcao9 = direcao9;
    }

    public double getIntensidade10() {
        return intensidade10;
    }

    public void setIntensidade10(double intensidade10) {
        this.intensidade10 = intensidade10;
    }

    public double getDirecao10() {
        return direcao10;
    }

    public void setDirecao10(double direcao10) {
        this.direcao10 = direcao10;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dataHora != null ? dataHora.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Correntometro)) {
            return false;
        }
        Correntometro other = (Correntometro) object;
        return !((this.dataHora == null && other.dataHora != null) || (this.dataHora != null && !this.dataHora.equals(other.dataHora)));
    }

    @Override
    public String toString() {
        return "Correntometro{" + "dataHora=" + dataHora + 
                ", intensidade=" + intensidade + 
                ", direcao=" + direcao + ", intensidade2=" + 
                intensidade2 + ", direcao2=" + direcao2 + 
                ", intensidade3=" + intensidade3 + ", direcao3=" + 
                direcao3 + ", intensidade4=" + intensidade4 + 
                ", direcao4=" + direcao4 + ", intensidade5=" + 
                intensidade5 + ", direcao5=" + direcao5 + ", intensidade6=" + 
                intensidade6 + ", direcao6=" + direcao6 + ", intensidade7=" +
                intensidade7 + ", direcao7=" + direcao7 + ", intensidade8=" + 
                intensidade8 + ", direcao8=" + direcao8 + ", intensidade9=" + 
                intensidade9 + ", direcao9=" + direcao9 + ", intensidade10=" + 
                intensidade10 + ", direcao10=" + direcao10 + '}';
    }
    
}