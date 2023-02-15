package br.com.marino.sismar.bean.admin.moorings;

import br.com.marino.sismar.controller.GiaontController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Giaont;
import br.com.marino.sismar.session.SessionContext;
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

@ManagedBean(name = "GiaontsBean")
@ViewScoped
public class GiaontsBean implements Serializable {

    private List<Giaont> giaonts;
    private Giaont giaontSelectedDelete;

    @PostConstruct
    public void init() {
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        giaonts = null;
    }

    public List<Giaont> getGiaonts() {
        return giaonts;
    }

    public void setGiaonts(List<Giaont> giaonts) {
        this.giaonts = giaonts;
    }

    public Giaont getGiaontSelectedDelete() {
        return giaontSelectedDelete;
    }

    public void setGiaontSelectedDelete(Giaont giaontSelectedDelete) {
        this.giaontSelectedDelete = giaontSelectedDelete;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            Clientes cliente = SessionContext.getInstance().getClientLoggedIn();
            
            giaonts = GiaontController.getListByCodCliente(manager, cliente.getCod());

        } catch (Exception ex) {
            giaonts = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void delete() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            giaontSelectedDelete.setStatus(false);
            GiaontController.edit(manager, giaontSelectedDelete);

            manager.getTransaction().commit();
            
            giaonts.remove(giaontSelectedDelete);

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro removido com sucesso!"));

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao efetuar ação!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
            giaontSelectedDelete = null;
        }

    }

}
