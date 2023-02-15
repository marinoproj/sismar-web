/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Leonardo
 */
@Entity
@Table(name = "monitorar_regra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MonitorarRegra.findAll", query = "SELECT m FROM MonitorarRegra m")
    , @NamedQuery(name = "MonitorarRegra.findByCodMonitorarRegra", query = "SELECT m FROM MonitorarRegra m WHERE m.codMonitorarRegra = :codMonitorarRegra")
    , @NamedQuery(name = "MonitorarRegra.findByCor", query = "SELECT m FROM MonitorarRegra m WHERE m.cor = :cor")
    , @NamedQuery(name = "MonitorarRegra.findByCondicao", query = "SELECT m FROM MonitorarRegra m WHERE m.condicao = :condicao")
    , @NamedQuery(name = "MonitorarRegra.findByValor", query = "SELECT m FROM MonitorarRegra m WHERE m.valor = :valor")})
public class MonitorarRegra implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ordem")
    private int ordem;

    public static final String BIGGER = "maior";
    public static final String BIGGER_OR_EQUAL = "maior ou igual";
    public static final String SMALLER = "menor";
    public static final String LESS_OR_EQUAL = "menor ou igual";
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nome")
    private String nome;

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "codMonitorarRegra")    
    private Integer codMonitorarRegra;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "cor")
    private String cor;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "condicao")   
    private String condicao;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")    
    private double valor;
    
    @JoinColumn(name = "codMonitorar", referencedColumnName = "codMonitorar")
    @ManyToOne(optional = false)    
    private Monitorar codMonitorar;

    public MonitorarRegra() {
    }

    public MonitorarRegra(Integer codMonitorarRegra) {
        this.codMonitorarRegra = codMonitorarRegra;
    }

    public MonitorarRegra(Integer codMonitorarRegra, String cor, String condicao, double valor) {
        this.codMonitorarRegra = codMonitorarRegra;
        this.cor = cor;
        this.condicao = condicao;
        this.valor = valor;
    }

    public Integer getCodMonitorarRegra() {
        return codMonitorarRegra;
    }

    public void setCodMonitorarRegra(Integer codMonitorarRegra) {
        this.codMonitorarRegra = codMonitorarRegra;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Monitorar getCodMonitorar() {
        return codMonitorar;
    }

    public void setCodMonitorar(Monitorar codMonitorar) {
        this.codMonitorar = codMonitorar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codMonitorarRegra != null ? codMonitorarRegra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MonitorarRegra)) {
            return false;
        }
        MonitorarRegra other = (MonitorarRegra) object;
        if ((this.codMonitorarRegra == null && other.codMonitorarRegra != null) || (this.codMonitorarRegra != null && !this.codMonitorarRegra.equals(other.codMonitorarRegra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MonitorarRegra{" + "nome=" + nome + ", codMonitorarRegra=" + codMonitorarRegra + ", cor=" + cor + ", condicao=" + condicao + ", valor=" + valor + ", codMonitorar=" + codMonitorar + '}';
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }
    
}
