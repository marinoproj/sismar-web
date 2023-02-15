package br.com.marino.sismar.bean.admin.administration;

import br.com.marino.sismar.controller.BercosController;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.util.UAgentInfo;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "BercoBean")
@ViewScoped
public class BercosBean implements Serializable {

    private List<Berco> listBercos;
    private Berco bercoSelectedDelete;
    private Berco bercoSelectedDetails;
    private UAgentInfo uAgentInfo;

    @PostConstruct
    public void init() {
        uAgentInfo = new UAgentInfo((HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest());

        reloadListBercos();
    }

    public boolean isDeviceMobile() {
        return uAgentInfo.detectMobileLong();
    }

    public void selectBercoDelete(Berco berco) {
        this.bercoSelectedDelete = berco;
    }

    public Berco getBercoSelectedDetails() {
        return bercoSelectedDetails;
    }

    public void setBercoSelectedDetails(Berco bercoSelectedDetails) {
        this.bercoSelectedDetails = bercoSelectedDetails;
    }

    public void reloadListBercos() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            listBercos = BercosController.getListBercos(manager);

        } catch (Exception ex) {
            listBercos = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void deleteBerco() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();
            BercosController.delete(manager, bercoSelectedDelete.getCodBerco());
            manager.getTransaction().commit();
            
            Util.removeImageBerco(bercoSelectedDelete.getImagem());
            
            listBercos.remove(bercoSelectedDelete);

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro excluído com sucesso!"));

        } catch (Exception ex) {

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao excluir o cadastro!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
            bercoSelectedDelete = null;
        }

    }

    @PreDestroy
    public void destroy() {
    }

    public List<Berco> getListBercos() {
        return listBercos;
    }

    public void setListBercos(List<Berco> listBercos) {
        this.listBercos = listBercos;
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

    public String getBercoArredondado(Berco berco) {
        if (berco == null || berco.getBercoArredondado() == null) {
            return "";
        }
        return berco.getBercoArredondado() == 1 ? "S" : "N";
    }

    public String getArea(Berco berco) {
        if (berco == null || berco.getCodArea() == null) {
            return "";
        }
        return berco.getCodArea().getNome();
    }

    public String getPoin(Berco berco) {
        if (berco == null || berco.getCodPoin() == null) {
            return "";
        }
        return berco.getCodPoin().getNome();
    }

    public String getLatitude(Berco berco) {
        if (berco == null || berco.getLatitude() == null) {
            return "";
        }
        return berco.getLatitude() + "";
    }

    public String getLongitude(Berco berco) {
        if (berco == null || berco.getLongitude() == null) {
            return "";
        }
        return berco.getLongitude() + "";
    }

    public String getCaladoBaixaMare(Berco berco) {
        if (berco == null || berco.getCaladoBaixaMare() == null) {
            return "";
        }
        return berco.getCaladoBaixaMare() + " m";
    }

    public String getCaladoPreaMare(Berco berco) {
        if (berco == null || berco.getCaladoPreaMare() == null) {
            return "";
        }
        return berco.getCaladoPreaMare() + " m";
    }

    public String getImageBerco(Berco berco) {
        if (berco == null || berco.getImagem() == null) {
            return "img/sem_imagem.png";
        }
        return "img/bercos/" + berco.getImagem();
    }

}
