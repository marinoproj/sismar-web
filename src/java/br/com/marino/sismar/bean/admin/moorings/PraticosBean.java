package br.com.marino.sismar.bean.admin.moorings;

import br.com.marino.sismar.controller.PraticoController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Pratico;
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

@ManagedBean(name = "PraticosBean")
@ViewScoped
public class PraticosBean implements Serializable {

    private List<Pratico> praticos;
    private Pratico praticoSelectedDelete;

    @PostConstruct
    public void init() {
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        praticos = null;
    }

    public List<Pratico> getPraticos() {
        return praticos;
    }

    public void setPraticos(List<Pratico> praticos) {
        this.praticos = praticos;
    }

    public Pratico getPraticoSelectedDelete() {
        return praticoSelectedDelete;
    }

    public void setPraticoSelectedDelete(Pratico praticoSelectedDelete) {
        this.praticoSelectedDelete = praticoSelectedDelete;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            Clientes cliente = SessionContext.getInstance().getClientLoggedIn();
            
            praticos = PraticoController.getListByCodCliente(manager, cliente.getCod());

        } catch (Exception ex) {
            praticos = new ArrayList<>();

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

            praticoSelectedDelete.setStatus(false);
            PraticoController.edit(manager, praticoSelectedDelete);

            manager.getTransaction().commit();
            
            praticos.remove(praticoSelectedDelete);

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
            praticoSelectedDelete = null;
        }

    }

}
