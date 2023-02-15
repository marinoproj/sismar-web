/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Leonardo
 */
@Entity
@Table(name = "poin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Poin.findAll", query = "SELECT p FROM Poin p")
    , @NamedQuery(name = "Poin.findByCodPoin", query = "SELECT p FROM Poin p WHERE p.codPoin = :codPoin")
    , @NamedQuery(name = "Poin.findByNome", query = "SELECT p FROM Poin p WHERE p.nome = :nome")
    , @NamedQuery(name = "Poin.findByCor", query = "SELECT p FROM Poin p WHERE p.cor = :cor")
    , @NamedQuery(name = "Poin.findByTransparencia", query = "SELECT p FROM Poin p WHERE p.transparencia = :transparencia")
    , @NamedQuery(name = "Poin.findByAtivo", query = "SELECT p FROM Poin p WHERE p.ativo = :ativo")
    , @NamedQuery(name = "Poin.findByUtilizarCalculoViagem", query = "SELECT p FROM Poin p WHERE p.utilizarCalculoViagem = :utilizarCalculoViagem")})
public class Poin implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPoinChegada")
    private List<MovimentacaoPortoParametros> movimentacaoPortoParametrosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPoinFundeio")
    private List<MovimentacaoPortoParametros> movimentacaoPortoParametrosList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPoinCanal")
    private List<MovimentacaoPortoParametros> movimentacaoPortoParametrosList2;
    @OneToMany(mappedBy = "codPoinAtracacao1")
    private List<MovimentacaoPorto> movimentacaoPortoList;
    @OneToMany(mappedBy = "codPoinAtracacao2")
    private List<MovimentacaoPorto> movimentacaoPortoList1;
    @OneToMany(mappedBy = "codPoinAtracacao3")
    private List<MovimentacaoPorto> movimentacaoPortoList2;
    @OneToMany(mappedBy = "codPoinAtracacao4")
    private List<MovimentacaoPorto> movimentacaoPortoList3;

    @ManyToMany(mappedBy = "poinList")
    private List<Monitorar> monitorarList;

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codPoin")
    private Integer codPoin;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nome")
    private String nome;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "coordenadas")
    private String coordenadas;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cor")
    private int cor;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "transparencia")
    private int transparencia;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ativo")
    private short ativo;
    
    @Column(name = "utilizarCalculoViagem")
    private Short utilizarCalculoViagem;
    
    @OneToMany(mappedBy = "codPoin")
    private List<Berco> bercoList;

    public Poin() {
    }

    public Poin(Integer codPoin) {
        this.codPoin = codPoin;
    }

    public Poin(Integer codPoin, String nome, String coordenadas, int cor, int transparencia, short ativo) {
        this.codPoin = codPoin;
        this.nome = nome;
        this.coordenadas = coordenadas;
        this.cor = cor;
        this.transparencia = transparencia;
        this.ativo = ativo;
    }

    public Integer getCodPoin() {
        return codPoin;
    }

    public void setCodPoin(Integer codPoin) {
        this.codPoin = codPoin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }

    public int getTransparencia() {
        return transparencia;
    }

    public void setTransparencia(int transparencia) {
        this.transparencia = transparencia;
    }

    public short getAtivo() {
        return ativo;
    }

    public void setAtivo(short ativo) {
        this.ativo = ativo;
    }

    public Short getUtilizarCalculoViagem() {
        return utilizarCalculoViagem;
    }

    public void setUtilizarCalculoViagem(Short utilizarCalculoViagem) {
        this.utilizarCalculoViagem = utilizarCalculoViagem;
    }

    @XmlTransient
    public List<Berco> getBercoList() {
        return bercoList;
    }

    public void setBercoList(List<Berco> bercoList) {
        this.bercoList = bercoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codPoin != null ? codPoin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Poin)) {
            return false;
        }
        Poin other = (Poin) object;
        if ((this.codPoin == null && other.codPoin != null) || (this.codPoin != null && !this.codPoin.equals(other.codPoin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Poin[ codPoin=" + codPoin + " ]";
    }

    @XmlTransient
    public List<Monitorar> getMonitorarList() {
        return monitorarList;
    }

    public void setMonitorarList(List<Monitorar> monitorarList) {
        this.monitorarList = monitorarList;
    }

    @XmlTransient
    public List<MovimentacaoPortoParametros> getMovimentacaoPortoParametrosList() {
        return movimentacaoPortoParametrosList;
    }

    public void setMovimentacaoPortoParametrosList(List<MovimentacaoPortoParametros> movimentacaoPortoParametrosList) {
        this.movimentacaoPortoParametrosList = movimentacaoPortoParametrosList;
    }

    @XmlTransient
    public List<MovimentacaoPortoParametros> getMovimentacaoPortoParametrosList1() {
        return movimentacaoPortoParametrosList1;
    }

    public void setMovimentacaoPortoParametrosList1(List<MovimentacaoPortoParametros> movimentacaoPortoParametrosList1) {
        this.movimentacaoPortoParametrosList1 = movimentacaoPortoParametrosList1;
    }

    @XmlTransient
    public List<MovimentacaoPortoParametros> getMovimentacaoPortoParametrosList2() {
        return movimentacaoPortoParametrosList2;
    }

    public void setMovimentacaoPortoParametrosList2(List<MovimentacaoPortoParametros> movimentacaoPortoParametrosList2) {
        this.movimentacaoPortoParametrosList2 = movimentacaoPortoParametrosList2;
    }

    @XmlTransient
    public List<MovimentacaoPorto> getMovimentacaoPortoList() {
        return movimentacaoPortoList;
    }

    public void setMovimentacaoPortoList(List<MovimentacaoPorto> movimentacaoPortoList) {
        this.movimentacaoPortoList = movimentacaoPortoList;
    }

    @XmlTransient
    public List<MovimentacaoPorto> getMovimentacaoPortoList1() {
        return movimentacaoPortoList1;
    }

    public void setMovimentacaoPortoList1(List<MovimentacaoPorto> movimentacaoPortoList1) {
        this.movimentacaoPortoList1 = movimentacaoPortoList1;
    }

    @XmlTransient
    public List<MovimentacaoPorto> getMovimentacaoPortoList2() {
        return movimentacaoPortoList2;
    }

    public void setMovimentacaoPortoList2(List<MovimentacaoPorto> movimentacaoPortoList2) {
        this.movimentacaoPortoList2 = movimentacaoPortoList2;
    }

    @XmlTransient
    public List<MovimentacaoPorto> getMovimentacaoPortoList3() {
        return movimentacaoPortoList3;
    }

    public void setMovimentacaoPortoList3(List<MovimentacaoPorto> movimentacaoPortoList3) {
        this.movimentacaoPortoList3 = movimentacaoPortoList3;
    }
    
}
