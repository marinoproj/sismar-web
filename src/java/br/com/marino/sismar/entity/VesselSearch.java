package br.com.marino.sismar.entity;

import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class VesselSearch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "codNavio")
    private Integer codNavio;

    @Basic(optional = false)
    @Column(name = "nomeNavio")
    private String nomeNavio;
    
    @Basic(optional = false)
    @Column(name = "imo")
    private int imo;
    
    @Column(name = "mmsi")
    private Integer mmsi;
    
    @Column(name = "tipo")
    private String tipo;
    
    @Column(name = "dimensao")
    private String dimensao;
    
    @Column(name = "imagemUrl")
    private String imageUrl;
    
    //@Lob
    //@Column(name = "imagem")
    //private Byte[] imagem;

    @Transient
    private NavioUltimaAtualizacao navioUltimaAtualizacao;
    
    public VesselSearch(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public VesselSearch() {
    }
    

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getCodNavio() {
        return codNavio;
    }

    public void setCodNavio(Integer codNavio) {
        this.codNavio = codNavio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codNavio != null ? codNavio.hashCode() : 0);
        return hash;
    }

    public String getNomeNavio() {
        return nomeNavio;
    }

    public void setNomeNavio(String nomeNavio) {
        this.nomeNavio = nomeNavio;
    }

    public int getImo() {
        return imo;
    }

    public void setImo(int imo) {
        this.imo = imo;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public NavioUltimaAtualizacao getNavioUltimaAtualizacao() {
        return navioUltimaAtualizacao;
    }

    public void setNavioUltimaAtualizacao(NavioUltimaAtualizacao navioUltimaAtualizacao) {
        this.navioUltimaAtualizacao = navioUltimaAtualizacao;
    }    

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDimensao() {
        return dimensao;
    }

    public void setDimensao(String dimensao) {
        this.dimensao = dimensao;
    }    
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Navio)) {
            return false;
        }
        VesselSearch other = (VesselSearch) object;
        return !((this.codNavio == null && other.codNavio != null)
                || (this.codNavio != null && !this.codNavio.equals(other.codNavio)));
    }

    public String toJSONMap() throws Exception{
        return Util.getAisByJson(this, null).toString();
    }   

}
