/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "sessao_pagina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SessaoPagina.findAll", query = "SELECT s FROM SessaoPagina s")
    , @NamedQuery(name = "SessaoPagina.findByCodSessaoPagina", query = "SELECT s FROM SessaoPagina s WHERE s.codSessaoPagina = :codSessaoPagina")
    , @NamedQuery(name = "SessaoPagina.findByPagina", query = "SELECT s FROM SessaoPagina s WHERE s.pagina = :pagina")
    , @NamedQuery(name = "SessaoPagina.findByAcesso", query = "SELECT s FROM SessaoPagina s WHERE s.acesso = :acesso")
    , @NamedQuery(name = "SessaoPagina.findBySaida", query = "SELECT s FROM SessaoPagina s WHERE s.saida = :saida")})
public class SessaoPagina implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "codSessaoPagina")
    private Integer codSessaoPagina;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pagina")
    private String pagina;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "acesso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date acesso;
    
    @Column(name = "saida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saida;
    
    @Column(name = "codUsuariosWebSessao")
    private int codUsuariosWebSessao;

    public SessaoPagina() {
    }

    public SessaoPagina(Integer codSessaoPagina) {
        this.codSessaoPagina = codSessaoPagina;
    }

    public SessaoPagina(Integer codSessaoPagina, String pagina, Date acesso) {
        this.codSessaoPagina = codSessaoPagina;
        this.pagina = pagina;
        this.acesso = acesso;
    }

    public Integer getCodSessaoPagina() {
        return codSessaoPagina;
    }

    public void setCodSessaoPagina(Integer codSessaoPagina) {
        this.codSessaoPagina = codSessaoPagina;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public Date getAcesso() {
        return acesso;
    }

    public void setAcesso(Date acesso) {
        this.acesso = acesso;
    }

    public Date getSaida() {
        return saida;
    }

    public void setSaida(Date saida) {
        this.saida = saida;
    }

    public int getCodUsuariosWebSessao() {
        return codUsuariosWebSessao;
    }

    public void setCodUsuariosWebSessao(int codUsuariosWebSessao) {
        this.codUsuariosWebSessao = codUsuariosWebSessao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codSessaoPagina != null ? codSessaoPagina.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SessaoPagina)) {
            return false;
        }
        SessaoPagina other = (SessaoPagina) object;
        if ((this.codSessaoPagina == null && other.codSessaoPagina != null) || (this.codSessaoPagina != null && !this.codSessaoPagina.equals(other.codSessaoPagina))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.SessaoPagina[ codSessaoPagina=" + codSessaoPagina + " ]";
    }
    
}
