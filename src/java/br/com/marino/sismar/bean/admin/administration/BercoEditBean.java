package br.com.marino.sismar.bean.admin.administration;

import br.com.marino.sismar.controller.AreaController;
import br.com.marino.sismar.controller.BercosController;
import br.com.marino.sismar.controller.CaisController;
import br.com.marino.sismar.controller.EmpresaController;
import br.com.marino.sismar.controller.MercadoriaController;
import br.com.marino.sismar.controller.PoinController;
import br.com.marino.sismar.entity.Area;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.Cais;
import br.com.marino.sismar.entity.Empresa;
import br.com.marino.sismar.entity.Mercadoria;
import br.com.marino.sismar.entity.Poin;
import br.com.marino.sismar.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "BercoEditBean")
@ViewScoped
public class BercoEditBean implements Serializable {

    private int codBerco;

    private String name;
    private String cabecos;
    private Double length;
    private Cais codCais;
    private Double depth;
    private Double lowDraftMare;
    private Double preaDraftMare;
    private String priority;
    private Boolean roundedBerco;
    private Empresa codCompany;
    private Mercadoria codMerchandise;
    private Area codArea;
    private Poin codPoin;
    private Double latitude;
    private Double longitude;
    private String image;

    // listas
    private List<Cais> listCais;
    private List<Empresa> listCompany;
    private List<Mercadoria> listMerchandise;
    private List<Area> listArea;
    private List<Poin> listPoin;

    private Part imageFile;
    private byte[] imageBytes;

    @PostConstruct
    public void init() {

        try {
            codBerco = Integer.parseInt(FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("berco"));
        } catch (NumberFormatException ex) {
            codBerco = 0;
        }

        reload();

    }

    private void reload() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            listCompany = EmpresaController.getListCompany(manager);
            listCais = CaisController.getListCais(manager);
            listMerchandise = MercadoriaController.getListMerchandise(manager);
            listArea = AreaController.getListArea(manager);
            listPoin = PoinController.getListPoins(manager);

            Berco berco = BercosController.getBercoByCodBerco(manager, codBerco);

            name = berco.getNome();
            cabecos = berco.getCabecos();
            length = berco.getComprimento();
            codCais = berco.getCodCais();
            depth = berco.getProfundidade();
            lowDraftMare = berco.getCaladoBaixaMare();
            preaDraftMare = berco.getCaladoPreaMare();
            priority = berco.getPrioridade();
            roundedBerco = (berco.getBercoArredondado() == null ? null : berco.getBercoArredondado() == 1);
            codCompany = berco.getCodEmpresa();
            codMerchandise = berco.getCodMercadoria();
            codArea = berco.getCodArea();
            codPoin = berco.getCodPoin();
            latitude = berco.getLatitude();
            longitude = berco.getLongitude();
            image = berco.getImagem();
            imageBytes = (image == null) ? null : Util.getImageBercoToBytes(image);

        } catch (Exception ex) {
            ex.printStackTrace();
            listCompany = new ArrayList<>();
            listCais = new ArrayList<>();
            listMerchandise = new ArrayList<>();
            listArea = new ArrayList<>();
            listPoin = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void removeImageBerco() {
        imageFile = null;
        imageBytes = null;
    }

    public void uploadImageBerco(AjaxBehaviorEvent event) throws IOException {

        if (imageFile != null) {
            if (!"image/png".equals(imageFile.getContentType())) {

                FacesContext.getCurrentInstance().addMessage(
                        "form-edit-berco",
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Tipo de imagem inválida",
                                "A imagem deve ser do tipo png."));

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Tipo de imagem inválida",
                        "A imagem deve ser do tipo png."));

                imageFile = null;

                return;

            }
        }

        imageBytes = IOUtils.toByteArray(imageFile.getInputStream());
        imageFile = null;

    }

    public String getImageBerco() {
        if (imageBytes == null) {
            return "/sismar/faces/javax.faces.resource/img/sem_imagem.png";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageBytes, false)));
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCabecos() {
        return cabecos;
    }

    public void setCabecos(String cabecos) {
        this.cabecos = cabecos;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Cais getCodCais() {
        return codCais;
    }

    public void setCodCais(Cais codCais) {
        this.codCais = codCais;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public Double getLowDraftMare() {
        return lowDraftMare;
    }

    public void setLowDraftMare(Double lowDraftMare) {
        this.lowDraftMare = lowDraftMare;
    }

    public Double getPreaDraftMare() {
        return preaDraftMare;
    }

    public void setPreaDraftMare(Double preaDraftMare) {
        this.preaDraftMare = preaDraftMare;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Boolean getRoundedBerco() {
        return roundedBerco;
    }

    public void setRoundedBerco(Boolean roundedBerco) {
        this.roundedBerco = roundedBerco;
    }

    public Empresa getCodCompany() {
        return codCompany;
    }

    public void setCodCompany(Empresa codCompany) {
        this.codCompany = codCompany;
    }

    public Mercadoria getCodMerchandise() {
        return codMerchandise;
    }

    public void setCodMerchandise(Mercadoria codMerchandise) {
        this.codMerchandise = codMerchandise;
    }

    public Area getCodArea() {
        return codArea;
    }

    public void setCodArea(Area codArea) {
        this.codArea = codArea;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Part getImageFile() {
        return imageFile;
    }

    public void setImageFile(Part imageFile) {
        this.imageFile = imageFile;
    }

    public List<Cais> getListCais() {
        return listCais;
    }

    public void setListCais(List<Cais> listCais) {
        this.listCais = listCais;
    }

    public List<Empresa> getListCompany() {
        return listCompany;
    }

    public void setListCompany(List<Empresa> listCompany) {
        this.listCompany = listCompany;
    }

    public List<Mercadoria> getListMerchandise() {
        return listMerchandise;
    }

    public void setListMerchandise(List<Mercadoria> listMerchandise) {
        this.listMerchandise = listMerchandise;
    }

    public List<Area> getListArea() {
        return listArea;
    }

    public void setListArea(List<Area> listArea) {
        this.listArea = listArea;
    }

    public List<Poin> getListPoin() {
        return listPoin;
    }

    public void setListPoin(List<Poin> listPoin) {
        this.listPoin = listPoin;
    }

    public Poin getCodPoin() {
        return codPoin;
    }

    public void setCodPoin(Poin codPoin) {
        this.codPoin = codPoin;
    }

    public String getImagem(String image) {
        if (this.image == null) {
            return "img/sem_imagem.png";
        }
        return "img/bercos/" + image;
    }

    @PreDestroy
    public void destroy() {
    }

    private void clear() {
        imageFile = null;
    }

    public void updateBerco() {

        if (name == null || name.isEmpty()) {

            PrimeFaces.current().executeScript("PF('statusdialog').hide()");

            FacesContext.getCurrentInstance().addMessage(
                    "form-edit-berco",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Preencha o formulário corretamente. O Campo nome é obrigatório"));

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Atenção",
                    "Preencha o formulário corretamente. O Campo nome é obrigatório"));

            return;

        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            Berco berco = new Berco();
            berco.setCodBerco(codBerco);
            berco.setNome(name);
            berco.setCabecos(cabecos);
            berco.setComprimento(length);
            berco.setCodCais(codCais);
            berco.setProfundidade(depth);
            berco.setCaladoBaixaMare(lowDraftMare);
            berco.setCaladoPreaMare(preaDraftMare);
            berco.setPrioridade(priority);
            berco.setBercoArredondado(roundedBerco == null ? null : roundedBerco ? (short) 1 : (short) 0);
            berco.setCodEmpresa(codCompany);
            berco.setCodMercadoria(codMerchandise);
            berco.setCodArea(codArea);
            berco.setCodPoin(codPoin);
            berco.setLatitude(latitude == null || latitude == 0 ? null : latitude);
            berco.setLongitude(longitude == null || longitude == 0 ? null : longitude);           
            berco.setImagem(null);

            if (imageBytes != null) {
                berco.setImagem("berco_" + codBerco + "_" + Util.getLongTimeNow() + ".png");
            }

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();
            BercosController.edit(manager, berco);
            manager.getTransaction().commit();

            Util.removeImageBerco(image);

            if (imageBytes != null) {
                Util.saveImageBerco(berco.getImagem(), imageBytes);
            }

            image = berco.getImagem();

            clear();

            PrimeFaces.current().executeScript("PF('statusdialog').hide()");

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro atualizado com sucesso!"));

        } catch (Exception ex) {

            PrimeFaces.current().executeScript("PF('statusdialog').hide()");

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao atualizar o cadastro do berço!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }
    }

}
