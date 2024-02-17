package br.com.marino.sismar.bean.admin.components;

import br.com.marino.sismar.controller.NavioController;
import br.com.marino.sismar.controller.NavioUltimaAtualizacaoController;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.NavioUltimaAtualizacao;
import br.com.marino.sismar.entity.VesselSearch;
import br.com.marino.sismar.util.NavioMapAis;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

@ManagedBean(name = "SearchVesselsBean")
@ViewScoped
public class SearchVesselsBean implements Serializable {

    private String nameOrImoVessel;
    private List<VesselSearch> vesselsBySearch;
    private VesselSearch vesselSearchSelected;

    @PostConstruct
    public void init() {
        nameOrImoVessel = "";
        vesselsBySearch = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
    }

    public String getNameOrImoVessel() {
        return nameOrImoVessel;
    }

    public void setNameOrImoVessel(String nameOrImoVessel) {
        this.nameOrImoVessel = nameOrImoVessel;
    }

    public String getImageVessel(String image) {
        return Util.getImageVessel(image);
    }

    public void reloadVesselsBySearch(ActionEvent actionEvent) {

        if (nameOrImoVessel == null || nameOrImoVessel.trim().isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Atenção",
                    "Digite o nome ou o imo da embarcação para pesquisar!"));
            return;
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            vesselsBySearch = NavioController.getVesselsBySearch(manager, nameOrImoVessel.trim());

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Não foi possível efetuar a consulta!"));

            vesselsBySearch = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }

    }

    public List<VesselSearch> getVesselsBySearch() {
        return vesselsBySearch;
    }

    public void setVesselsBySearch(List<VesselSearch> vesselsBySearch) {
        this.vesselsBySearch = vesselsBySearch;
    }

    public VesselSearch getVesselSearchSelected() {
        return vesselSearchSelected;
    }

    // utils
    public String getDimension(VesselSearch vesselSearch) {

        if (vesselSearch == null) {
            return " ";
        }

        Navio vessel = new Navio();
        vessel.setCodNavio(vesselSearch.getCodNavio());
        vessel.setDimensao(vesselSearch.getDimensao());
        vessel.setImo(vesselSearch.getImo());
        vessel.setMmsi(vesselSearch.getMmsi());
        vessel.setNomeNavio(vesselSearch.getNomeNavio());
        vessel.setTipo(vesselSearch.getTipo());

        NavioMapAis vesselMap = new NavioMapAis(vessel);

        return vesselMap.getComprimentoReal() + "m X " + vesselMap.getLarguraReal() + "m";

    }

    public String getType(VesselSearch vesselSearch) {

        if (vesselSearch == null) {
            return " ";
        }

        Navio vessel = new Navio();
        vessel.setCodNavio(vesselSearch.getCodNavio());
        vessel.setDimensao(vesselSearch.getDimensao());
        vessel.setImo(vesselSearch.getImo());
        vessel.setMmsi(vesselSearch.getMmsi());
        vessel.setNomeNavio(vesselSearch.getNomeNavio());
        vessel.setTipo(vesselSearch.getTipo());

        NavioMapAis vesselMap = new NavioMapAis(vessel);

        return vesselMap.getCategoriaEmbarcacao();

    }

    public String getVelocity(VesselSearch vesselSearch) {
        if (vesselSearch == null) {
            return " ";
        }

        if (vesselSearch.getNavioUltimaAtualizacao() == null
                || vesselSearch.getNavioUltimaAtualizacao().getVelocidadeSobreSolo() == null) {
            return " ";
        }

        return vesselSearch.getNavioUltimaAtualizacao().getVelocidadeSobreSolo() + " kn";
    }

    public String getDirection(VesselSearch vesselSearch) {
        if (vesselSearch == null) {
            return " ";
        }

        if (vesselSearch.getNavioUltimaAtualizacao() == null
                || vesselSearch.getNavioUltimaAtualizacao().getCursoSobreSolo() == null) {
            return " ";
        }

        return vesselSearch.getNavioUltimaAtualizacao().getCursoSobreSolo() + "º";
    }

    public String getDraught(VesselSearch vesselSearch) {
        if (vesselSearch == null) {
            return " ";
        }

        if (vesselSearch.getNavioUltimaAtualizacao() == null
                || vesselSearch.getNavioUltimaAtualizacao().getDraught() == null) {
            return " ";
        }

        return vesselSearch.getNavioUltimaAtualizacao().getDraught() + "m";
    }

    public String getDestination(VesselSearch vesselSearch) {
        if (vesselSearch == null) {
            return " ";
        }

        if (vesselSearch.getNavioUltimaAtualizacao() == null
                || vesselSearch.getNavioUltimaAtualizacao().getDestino() == null) {
            return " ";
        }

        return vesselSearch.getNavioUltimaAtualizacao().getDestino();
    }

    public String getState(VesselSearch vesselSearch) {
        if (vesselSearch == null) {
            return " ";
        }

        if (vesselSearch.getNavioUltimaAtualizacao() == null
                || vesselSearch.getNavioUltimaAtualizacao().getStatusNavegacao() == null) {
            return " ";
        }

        return vesselSearch.getNavioUltimaAtualizacao().getStatusNavegacao();

    }

    public String getReceived(VesselSearch vesselSearch) {
        if (vesselSearch == null) {
            return " ";
        }

        if (vesselSearch.getNavioUltimaAtualizacao() == null
                || vesselSearch.getNavioUltimaAtualizacao().getDataUltimaAtualizacao() == null) {
            return " ";
        }

        return Util.getStringDateLastUpdate(new Date(), vesselSearch.getNavioUltimaAtualizacao().getDataUltimaAtualizacao());

    }

    public String getLng(VesselSearch vesselSearch) {
        if (vesselSearch == null) {
            return " ";
        }

        if (vesselSearch.getNavioUltimaAtualizacao() == null
                || vesselSearch.getNavioUltimaAtualizacao().getLongitude() == null) {
            return " ";
        }

        return vesselSearch.getNavioUltimaAtualizacao().getLongitude() + "";
    }

    public String getLat(VesselSearch vesselSearch) {
        if (vesselSearch == null) {
            return " ";
        }

        if (vesselSearch.getNavioUltimaAtualizacao() == null
                || vesselSearch.getNavioUltimaAtualizacao().getLatitude() == null) {
            return " ";
        }

        return vesselSearch.getNavioUltimaAtualizacao().getLatitude() + "";
    }

    // fim    
    public void setVesselSearchSelected(VesselSearch vesselSearchSelected) {

        this.vesselSearchSelected = vesselSearchSelected;

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            NavioUltimaAtualizacao navioUltimaAtualizacao = NavioUltimaAtualizacaoController
                    .getRadioLastUpdate(manager, this.vesselSearchSelected.getMmsi());

            this.vesselSearchSelected.setNavioUltimaAtualizacao(navioUltimaAtualizacao);

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Não foi possível efetuar a consulta!"));

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
