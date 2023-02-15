package br.com.marino.sismar.entity;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author Leonardo
 */
@Entity
@Table(name = "layer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Layer.findAll", query = "SELECT l FROM Layer l")
    , @NamedQuery(name = "Layer.findByCodLayer", query = "SELECT l FROM Layer l WHERE l.codLayer = :codLayer")
    , @NamedQuery(name = "Layer.findByCodigo", query = "SELECT l FROM Layer l WHERE l.codigo = :codigo")
    , @NamedQuery(name = "Layer.findByNome", query = "SELECT l FROM Layer l WHERE l.nome = :nome")
    , @NamedQuery(name = "Layer.findByPermitirAtualizar", query = "SELECT l FROM Layer l WHERE l.permitirAtualizar = :permitirAtualizar")})
public class Layer implements Serializable {

    public static final int LAYER_AREA_FUNDEIO = 1;
    public static final int LAYER_AREA_PORTUARIA = 2;
    public static final int LAYER_CANAL_NAVEGACAO = 3;

    @JoinTable(name = "layer_poin", joinColumns = {
        @JoinColumn(name = "codLayer", referencedColumnName = "codLayer")}, inverseJoinColumns = {
        @JoinColumn(name = "codPoin", referencedColumnName = "codPoin")})
    @ManyToMany
    private List<Poin> poinList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codLayer")
    private Integer codLayer;
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nome")
    private String nome;
    @Column(name = "permitirAtualizar")
    private Short permitirAtualizar;

    public Layer() {
    }

    public Layer(Integer codLayer) {
        this.codLayer = codLayer;
    }

    public Layer(Integer codLayer, String nome) {
        this.codLayer = codLayer;
        this.nome = nome;
    }

    public Integer getCodLayer() {
        return codLayer;
    }

    public void setCodLayer(Integer codLayer) {
        this.codLayer = codLayer;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Short getPermitirAtualizar() {
        return permitirAtualizar;
    }

    public void setPermitirAtualizar(Short permitirAtualizar) {
        this.permitirAtualizar = permitirAtualizar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codLayer != null ? codLayer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Layer)) {
            return false;
        }
        Layer other = (Layer) object;
        if ((this.codLayer == null && other.codLayer != null) || (this.codLayer != null && !this.codLayer.equals(other.codLayer))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.marino.sismar.entity.Layer[ codLayer=" + codLayer + " ]";
    }

    @XmlTransient
    public List<Poin> getPoinList() {
        return poinList;
    }

    public void setPoinList(List<Poin> poinList) {
        this.poinList = poinList;
    }

    public JSONObject toJSONObject() throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("cod", codigo);
        obj.put("name", nome);

        JSONArray poinsJson = new JSONArray();
        for (Poin poin : getPoinList()) {
            JSONObject poinJson = new JSONObject();
            poinJson.put("id", poin.getCodPoin());
            poinJson.put("name", poin.getNome());
            Color color = new Color(poin.getCor());
            poinJson.put("color", String.format("#%02X%02X%02X", color.getRed(),
                    color.getGreen(), color.getBlue()));
            poinJson.put("opacity", poin.getTransparencia() / 100.0);
            poinJson.put("name", poin.getNome());
            poinJson.put("coordinates", new JSONArray(poin.getCoordenadas()));
            poinsJson.put(poinJson);

        }
        obj.put("poins", poinsJson);

        return obj;

    }

}
