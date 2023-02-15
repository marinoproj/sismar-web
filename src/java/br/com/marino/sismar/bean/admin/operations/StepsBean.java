package br.com.marino.sismar.bean.admin.operations;

import br.com.marino.sismar.controller.EtapasController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Etapas;
import br.com.marino.sismar.entity.UsuariosWeb;
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

@ManagedBean(name = "StepsBean")
@ViewScoped
public class StepsBean implements Serializable {

    private List<Etapas> steps;
    private Etapas stepSelectedDelete;

    @PostConstruct
    public void init() {
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        steps = null;
    }
    
    public Etapas getStepSelectedDelete() {
        return stepSelectedDelete;
    }

    public void setStepSelectedDelete(Etapas stepSelectedDelete) {
        this.stepSelectedDelete = stepSelectedDelete;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            Clientes cliente = SessionContext.getInstance().getClientLoggedIn();           
            steps = EtapasController.getListEtapasByCodCliente(manager, cliente.getCod());

        } catch (Exception ex) {
            steps = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public List<Etapas> getSteps() {
        return steps;
    }

    public void setSteps(List<Etapas> steps) {
        this.steps = steps;
    }

    public void delete() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            stepSelectedDelete.setStatus(false);
            EtapasController.edit(manager, stepSelectedDelete);

            manager.getTransaction().commit();
            
            steps.remove(stepSelectedDelete);

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
            stepSelectedDelete = null;
        }

    }

}
