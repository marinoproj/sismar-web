package br.com.marino.sismar.bean.admin.components;

import br.com.marino.sismar.controller.BercosController;
import br.com.marino.sismar.entity.Berco;
import static br.com.marino.sismar.entity.Berco_.codBerco;
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
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "SearchBercosBean")
@ViewScoped
public class SearchBercosBean implements Serializable {

    private String nameBerco;
    private List<Berco> bercoBySearch;
    private Berco bercoSearchSelected;

    @PostConstruct
    public void init() {
        nameBerco = "";
        bercoBySearch = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
    }

    public String getNameBerco() {
        return nameBerco;
    }

    public void setNameBerco(String nameBerco) {
        this.nameBerco = nameBerco;
    }

    public Berco getBercoSearchSelected() {
        return bercoSearchSelected;
    }

    public void setBercoSearchSelected(Berco bercoSearchSelected) {
        this.bercoSearchSelected = bercoSearchSelected;
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl() + "admin/ais/berco.xhtml?berco=" + bercoSearchSelected.getCodBerco() + "&historic=7days");
        } catch (IOException ex) {
        }
    }
    
    public void reloadBercosBySearch(ActionEvent actionEvent) {

        if (nameBerco == null || nameBerco.trim().isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Atenção",
                    "Digite o nome do berço para pesquisar!"));
            return;
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            bercoBySearch = BercosController.getBercosBySearch(manager, nameBerco.trim());
            
        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Não foi possível efetuar a consulta!"));

            bercoBySearch = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }

    }
    
    public List<Berco> getBercosBySearch() {
        return bercoBySearch;
    }

    public void setBercosBySearch(List<Berco> bercosBySearch) {
        this.bercoBySearch = bercosBySearch;
    }

    public String getImageBerco(Berco berco) {
        if (berco == null || berco.getImagem() == null) {
            return "img/sem_imagem.png";
        }
        return "img/bercos/" + berco.getImagem();
    }
    
    public String getArea(Berco berco) {
        if (berco == null || berco.getCodArea() == null) {
            return "";
        }
        return berco.getCodArea().getNome();
    }
    
    public String getCabecos(Berco berco) {
        if (berco == null || berco.getCabecos() == null) {
            return "";
        }
        return berco.getCabecos();
    }

    public String getComprimento(Berco berco) {
        if (berco == null || berco.getComprimento() == null) {
            return "";
        }
        return berco.getComprimento() + " m";
    }

    public String getTipoCais(Berco berco) {
        if (berco == null || berco.getCodCais() == null) {
            return "";
        }
        return berco.getCodCais().getNome();
    }

    public String getProfundidade(Berco berco) {
        if (berco == null || berco.getProfundidade() == null) {
            return "";
        }
        return berco.getProfundidade() + " m";
    }

    public String getEmpresa(Berco berco) {
        if (berco == null || berco.getCodEmpresa() == null) {
            return "";
        }
        return berco.getCodEmpresa().getNome();
    }

    public String getMercadoria(Berco berco) {
        if (berco == null || berco.getCodMercadoria() == null) {
            return "";
        }
        return berco.getCodMercadoria().getNome();
    }

}
