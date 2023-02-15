package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author Leonardo
 */
@Entity
@Table(name = "berco")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Berco.findAll", query = "SELECT b FROM Berco b")
    , @NamedQuery(name = "Berco.findByCodBerco", query = "SELECT b FROM Berco b WHERE b.codBerco = :codBerco")
    , @NamedQuery(name = "Berco.findByNome", query = "SELECT b FROM Berco b WHERE b.nome = :nome")
    , @NamedQuery(name = "Berco.findByCabecos", query = "SELECT b FROM Berco b WHERE b.cabecos = :cabecos")
    , @NamedQuery(name = "Berco.findByComprimento", query = "SELECT b FROM Berco b WHERE b.comprimento = :comprimento")
    , @NamedQuery(name = "Berco.findByProfundidade", query = "SELECT b FROM Berco b WHERE b.profundidade = :profundidade")
    , @NamedQuery(name = "Berco.findByCaladoBaixaMare", query = "SELECT b FROM Berco b WHERE b.caladoBaixaMare = :caladoBaixaMare")
    , @NamedQuery(name = "Berco.findByCaladoPreaMare", query = "SELECT b FROM Berco b WHERE b.caladoPreaMare = :caladoPreaMare")
    , @NamedQuery(name = "Berco.findByPrioridade", query = "SELECT b FROM Berco b WHERE b.prioridade = :prioridade")
    , @NamedQuery(name = "Berco.findByBercoArredondado", query = "SELECT b FROM Berco b WHERE b.bercoArredondado = :bercoArredondado")
    , @NamedQuery(name = "Berco.findByLatitude", query = "SELECT b FROM Berco b WHERE b.latitude = :latitude")
    , @NamedQuery(name = "Berco.findByLongitude", query = "SELECT b FROM Berco b WHERE b.longitude = :longitude")
    , @NamedQuery(name = "Berco.findByImagem", query = "SELECT b FROM Berco b WHERE b.imagem = :imagem")})
public class Berco implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codBerco")
    private List<Operacao> operacaoList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "codBerco")
    private Integer codBerco;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nome")
    private String nome;
    @Size(max = 200)
    @Column(name = "cabecos")
    private String cabecos;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation

    @Column(name = "comprimento")
    private Double comprimento;

    @Column(name = "profundidade")
    private Double profundidade;

    @Column(name = "caladoBaixaMare")
    private Double caladoBaixaMare;

    @Column(name = "caladoPreaMare")
    private Double caladoPreaMare;

    @Size(max = 50)
    @Column(name = "prioridade")
    private String prioridade;

    @Column(name = "bercoArredondado")
    private Short bercoArredondado;

    // novo
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Size(min = 1, max = 250)
    @Column(name = "imagem")
    private String imagem;

    @JoinColumn(name = "codArea", referencedColumnName = "codArea")
    @ManyToOne
    private Area codArea;
    @JoinColumn(name = "codCais", referencedColumnName = "codCais")
    @ManyToOne
    private Cais codCais;
    @JoinColumn(name = "codEmpresa", referencedColumnName = "codEmpresa")
    @ManyToOne
    private Empresa codEmpresa;
    @JoinColumn(name = "codMercadoria", referencedColumnName = "codMercadoria")
    @ManyToOne
    private Mercadoria codMercadoria;
    @JoinColumn(name = "codPoin", referencedColumnName = "codPoin")
    @ManyToOne
    private Poin codPoin;

    public Berco() {
    }

    public Berco(Integer codBerco) {
        this.codBerco = codBerco;
    }

    public Berco(Integer codBerco, String nome) {
        this.codBerco = codBerco;
        this.nome = nome;
    }

    public Integer getCodBerco() {
        return codBerco;
    }

    public void setCodBerco(Integer codBerco) {
        this.codBerco = codBerco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCabecos() {
        return cabecos;
    }

    public void setCabecos(String cabecos) {
        this.cabecos = cabecos;
    }

    public Double getComprimento() {
        return comprimento;
    }

    public void setComprimento(Double comprimento) {
        this.comprimento = comprimento;
    }

    public Double getProfundidade() {
        return profundidade;
    }

    public void setProfundidade(Double profundidade) {
        this.profundidade = profundidade;
    }

    public Double getCaladoBaixaMare() {
        return caladoBaixaMare;
    }

    public void setCaladoBaixaMare(Double caladoBaixaMare) {
        this.caladoBaixaMare = caladoBaixaMare;
    }

    public Double getCaladoPreaMare() {
        return caladoPreaMare;
    }

    public void setCaladoPreaMare(Double caladoPreaMare) {
        this.caladoPreaMare = caladoPreaMare;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public Short getBercoArredondado() {
        return bercoArredondado;
    }

    public void setBercoArredondado(Short bercoArredondado) {
        this.bercoArredondado = bercoArredondado;
    }

    public Area getCodArea() {
        return codArea;
    }

    public void setCodArea(Area codArea) {
        this.codArea = codArea;
    }

    public Cais getCodCais() {
        return codCais;
    }

    public void setCodCais(Cais codCais) {
        this.codCais = codCais;
    }

    public Empresa getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(Empresa codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public Mercadoria getCodMercadoria() {
        return codMercadoria;
    }

    public void setCodMercadoria(Mercadoria codMercadoria) {
        this.codMercadoria = codMercadoria;
    }

    public Poin getCodPoin() {
        return codPoin;
    }

    public void setCodPoin(Poin codPoin) {
        this.codPoin = codPoin;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codBerco != null ? codBerco.hashCode() : 0);
        return hash;
    }

    public JSONObject toJSONObject() throws JSONException{
        
        JSONObject obj = new JSONObject();
        
        obj.put("name", nome);
        obj.put("cabecos", cabecos);
        obj.put("length", (comprimento == null || comprimento == 0) ? JSONObject.NULL : comprimento + " m" );
        obj.put("type", (codCais == null) ? JSONObject.NULL : codCais.getNome());
        obj.put("depth", (profundidade == null || profundidade == 0) ? JSONObject.NULL : profundidade + " m");
        obj.put("low_sea_tide", (caladoBaixaMare == null || caladoBaixaMare == 0) ? JSONObject.NULL : caladoBaixaMare + " m");
        obj.put("prea_sea_tide", (caladoPreaMare == null || caladoPreaMare == 0) ? JSONObject.NULL : caladoPreaMare + " m");        
        obj.put("rounded_crib", (bercoArredondado == null) ? JSONObject.NULL : bercoArredondado == 1 ? "S" : "N");        
        obj.put("company", (codEmpresa == null) ? JSONObject.NULL : codEmpresa.getNome());
        obj.put("merchandise", (codMercadoria == null) ? JSONObject.NULL : codMercadoria.getNome());
        obj.put("lat", (latitude == null || latitude == 0) ? JSONObject.NULL : latitude);
        obj.put("lng", (longitude == null || longitude == 0) ? JSONObject.NULL : longitude);
        obj.put("image", (imagem == null) ? "img/sem_imagem.png" : "img/bercos/" + imagem);                        
        
        return obj;
        
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Berco)) {
            return false;
        }
        Berco other = (Berco) object;
        if ((this.codBerco == null && other.codBerco != null) || (this.codBerco != null && !this.codBerco.equals(other.codBerco))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Berco{" + "codBerco=" + codBerco + ", nome=" + nome + ", cabecos=" + cabecos + ", comprimento=" + comprimento + ", profundidade=" + profundidade + ", caladoBaixaMare=" + caladoBaixaMare + ", caladoPreaMare=" + caladoPreaMare + ", prioridade=" + prioridade + ", bercoArredondado=" + bercoArredondado + ", latitude=" + latitude + ", longitude=" + longitude + ", imagem=" + imagem + ", codArea=" + codArea + ", codCais=" + codCais + ", codEmpresa=" + codEmpresa + ", codMercadoria=" + codMercadoria + ", codPoin=" + codPoin + '}';
    }

    @XmlTransient
    public List<Operacao> getOperacaoList() {
        return operacaoList;
    }

    public void setOperacaoList(List<Operacao> operacaoList) {
        this.operacaoList = operacaoList;
    }

}
