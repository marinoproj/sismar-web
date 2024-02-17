package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "navio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Navio.findAll", query = "SELECT n FROM Navio n")
    , @NamedQuery(name = "Navio.findByCodNavio", query = "SELECT n FROM Navio n WHERE n.codNavio = :codNavio")
    , @NamedQuery(name = "Navio.findByNomeNavio", query = "SELECT n FROM Navio n WHERE n.nomeNavio = :nomeNavio")
    , @NamedQuery(name = "Navio.findByTipo", query = "SELECT n FROM Navio n WHERE n.tipo = :tipo")
    , @NamedQuery(name = "Navio.findByCorCasco", query = "SELECT n FROM Navio n WHERE n.corCasco = :corCasco")
    , @NamedQuery(name = "Navio.findByNomeComandante", query = "SELECT n FROM Navio n WHERE n.nomeComandante = :nomeComandante")
    , @NamedQuery(name = "Navio.findByImo", query = "SELECT n FROM Navio n WHERE n.imo = :imo")
    , @NamedQuery(name = "Navio.findByDimensao", query = "SELECT n FROM Navio n WHERE n.dimensao = :dimensao")
    , @NamedQuery(name = "Navio.findByDataCadastro", query = "SELECT n FROM Navio n WHERE n.dataCadastro = :dataCadastro")
    , @NamedQuery(name = "Navio.findByIndicativo", query = "SELECT n FROM Navio n WHERE n.indicativo = :indicativo")
    , @NamedQuery(name = "Navio.findByMmsi", query = "SELECT n FROM Navio n WHERE n.mmsi = :mmsi")})
public class Navio implements Serializable {

    //@Lob
    //@Column(name = "imagem")
    //private byte[] imagem;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codNavio")
    private List<Operacao> operacaoList;
    
    @OneToMany(mappedBy = "codNavio")
    private List<MovimentacaoPorto> movimentacaoPortoList;

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "codNavio")
    private Integer codNavio;

    @Basic(optional = false)
    @Column(name = "nomeNavio")
    private String nomeNavio;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "corCasco")
    private String corCasco;

    @Column(name = "nomeComandante")
    private String nomeComandante;

    @Basic(optional = false)
    @Column(name = "imo")
    private int imo;
    
    @Column(name = "dimensao")
    private String dimensao;

    @Column(name = "dataCadastro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;

    @Column(name = "indicativo")
    private String indicativo;

    @Column(name = "mmsi")
    private Integer mmsi;

    @Transient
    private Ais ais;

    //@Transient
    //private Byte[] image;
    
    @Column(name = "imagemUrl")
    private String imageUrl;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codNavio")
    private List<Atracacao> listaOperacoes;

    public Navio() {
    }

    public Navio(Integer codNavio) {
        this.codNavio = codNavio;
    }

    public Navio(VesselSearch vesselSearch) {
        this.codNavio = vesselSearch.getCodNavio();
        this.nomeNavio = vesselSearch.getNomeNavio();
        this.imo = vesselSearch.getImo();
        this.mmsi = vesselSearch.getMmsi();
        this.tipo = vesselSearch.getTipo();
        this.dimensao = vesselSearch.getDimensao();
        this.imageUrl = vesselSearch.getImageUrl();
    }

    public Navio(Integer codNavio, String nomeNavio, int imo) {
        this.codNavio = codNavio;
        this.nomeNavio = nomeNavio;
        this.imo = imo;
    }

    public Integer getCodNavio() {
        return codNavio;
    }

    public void setCodNavio(Integer codNavio) {
        this.codNavio = codNavio;
    }

    public String getNomeNavio() {
        return nomeNavio;
    }

    public void setNomeNavio(String nomeNavio) {
        this.nomeNavio = nomeNavio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCorCasco() {
        return corCasco;
    }

    public void setCorCasco(String corCasco) {
        this.corCasco = corCasco;
    }

    public String getNomeComandante() {
        return nomeComandante;
    }

    public void setNomeComandante(String nomeComandante) {
        this.nomeComandante = nomeComandante;
    }

    public int getImo() {
        return imo;
    }

    public void setImo(int imo) {
        this.imo = imo;
    }

    public String getDimensao() {
        return dimensao;
    }

    public void setDimensao(String dimensao) {
        this.dimensao = dimensao;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getIndicativo() {
        return indicativo;
    }

    public void setIndicativo(String indicativo) {
        this.indicativo = indicativo;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public List<Atracacao> getListaOperacoes() {
        return listaOperacoes;
    }

    public void setListaOperacoes(List<Atracacao> listaOperacoes) {
        this.listaOperacoes = listaOperacoes;
    }

    public Ais getAis() {
        return ais;
    }

    public void setAis(Ais ais) {
        this.ais = ais;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codNavio != null ? codNavio.hashCode() : 0);
        return hash;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Navio)) {
            return false;
        }
        Navio other = (Navio) object;
        return !((this.codNavio == null && other.codNavio != null)
                || (this.codNavio != null && !this.codNavio.equals(other.codNavio)));
    }

    @Override
    public String toString() {
        return "Navio{" + "codNavio=" + codNavio + ", nomeNavio=" + nomeNavio
                + ", tipo=" + tipo + ", corCasco=" + corCasco
                + ", nomeComandante=" + nomeComandante + ", imo="
                + imo + ", dimensao=" + dimensao + ", dataCadastro="
                + dataCadastro + ", indicativo=" + indicativo + ", mmsi=" + mmsi + '}';
    }


    @XmlTransient
    public List<MovimentacaoPorto> getMovimentacaoPortoList() {
        return movimentacaoPortoList;
    }

    public void setMovimentacaoPortoList(List<MovimentacaoPorto> movimentacaoPortoList) {
        this.movimentacaoPortoList = movimentacaoPortoList;
    }

    @XmlTransient
    public List<Operacao> getOperacaoList() {
        return operacaoList;
    }

    public void setOperacaoList(List<Operacao> operacaoList) {
        this.operacaoList = operacaoList;
    }

}
