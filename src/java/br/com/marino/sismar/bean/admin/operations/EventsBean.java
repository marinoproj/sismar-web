package br.com.marino.sismar.bean.admin.operations;

import br.com.marino.sismar.controller.EventosController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Eventos;
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

@ManagedBean(name = "EventsStepsBean")
@ViewScoped
public class EventsBean implements Serializable {

    private List<Eventos> events;
    private Eventos eventSelectedDelete;

    @PostConstruct
    public void init() {
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        events = null;
    }

    public List<Eventos> getEvents() {
        return events;
    }

    public void setEvents(List<Eventos> events) {
        this.events = events;
    }

    public Eventos getEventSelectedDelete() {
        return eventSelectedDelete;
    }

    public void setEventSelectedDelete(Eventos eventSelectedDelete) {
        this.eventSelectedDelete = eventSelectedDelete;
    }   

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            Clientes cliente = SessionContext.getInstance().getClientLoggedIn();
            
            events = EventosController.getListEventosByCodCliente(manager, cliente.getCod());

        } catch (Exception ex) {
            events = new ArrayList<>();

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

            eventSelectedDelete.setStatus(false);
            EventosController.edit(manager, eventSelectedDelete);

            manager.getTransaction().commit();
            
            events.remove(eventSelectedDelete);

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
            eventSelectedDelete = null;
        }

    }

}
