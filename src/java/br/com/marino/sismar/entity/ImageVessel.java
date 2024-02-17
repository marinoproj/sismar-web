package br.com.marino.sismar.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class ImageVessel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "codNavio")
    private Integer codNavio;

    @Column(name = "imagemUrl")
    private String imageUrl;
    
    //@Lob
    //@Column(name = "imagem")
    //private Byte[] imagem;

    public ImageVessel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageVessel() {
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

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Navio)) {
            return false;
        }
        ImageVessel other = (ImageVessel) object;
        return !((this.codNavio == null && other.codNavio != null)
                || (this.codNavio != null && !this.codNavio.equals(other.codNavio)));
    }

}
