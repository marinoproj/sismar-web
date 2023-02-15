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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * @author Leonardo
 */
@Entity
@Table(name = "usuarios_web_sessao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuariosWebSessao.findAll", query = "SELECT u FROM UsuariosWebSessao u")
    , @NamedQuery(name = "UsuariosWebSessao.findByCodUsuariosWebSessao", query = "SELECT u FROM UsuariosWebSessao u WHERE u.codUsuariosWebSessao = :codUsuariosWebSessao")
    , @NamedQuery(name = "UsuariosWebSessao.findByInicio", query = "SELECT u FROM UsuariosWebSessao u WHERE u.inicio = :inicio")
    , @NamedQuery(name = "UsuariosWebSessao.findByTermino", query = "SELECT u FROM UsuariosWebSessao u WHERE u.termino = :termino")})
public class UsuariosWebSessao implements Serializable {

    @Size(max = 100)
    @Column(name = "ip")
    private String ip;

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "codUsuariosWebSessao")
    private Integer codUsuariosWebSessao;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;
    
    @Column(name = "termino")
    @Temporal(TemporalType.TIMESTAMP)
    private Date termino;
   
    @JoinColumn(name = "codUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private UsuariosWeb codUsuario;

    public UsuariosWebSessao() {
    }

    public UsuariosWebSessao(Integer codUsuariosWebSessao) {
        this.codUsuariosWebSessao = codUsuariosWebSessao;
    }

    public UsuariosWebSessao(Integer codUsuariosWebSessao, Date inicio) {
        this.codUsuariosWebSessao = codUsuariosWebSessao;
        this.inicio = inicio;
    }

    public Integer getCodUsuariosWebSessao() {
        return codUsuariosWebSessao;
    }

    public void setCodUsuariosWebSessao(Integer codUsuariosWebSessao) {
        this.codUsuariosWebSessao = codUsuariosWebSessao;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getTermino() {
        return termino;
    }

    public void setTermino(Date termino) {
        this.termino = termino;
    }

    public UsuariosWeb getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(UsuariosWeb codUsuario) {
        this.codUsuario = codUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codUsuariosWebSessao != null ? codUsuariosWebSessao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuariosWebSessao)) {
            return false;
        }
        UsuariosWebSessao other = (UsuariosWebSessao) object;
        if ((this.codUsuariosWebSessao == null && other.codUsuariosWebSessao != null) || (this.codUsuariosWebSessao != null && !this.codUsuariosWebSessao.equals(other.codUsuariosWebSessao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.UsuariosWebSessao[ codUsuariosWebSessao=" + codUsuariosWebSessao + " ]";
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
}
