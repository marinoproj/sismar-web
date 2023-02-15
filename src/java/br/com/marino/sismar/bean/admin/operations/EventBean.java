package br.com.marino.sismar.bean.admin.operations;

import br.com.marino.sismar.controller.EventosController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Eventos;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "EventBean")
@ViewScoped
public class EventBean implements Serializable {

    private Eventos event;
    private Integer codEvent;

    @PostConstruct
    public void init() {
        event = new Eventos();
        try {
            String cod = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("evento");
            if (cod != null && !cod.isEmpty()) {
                codEvent = Integer.parseInt(cod);
            }
        } catch (NumberFormatException ex) {
            codEvent = null;
        }
        if (event != null) {
            reloadInit();
        }
    }

    @PreDestroy
    public void destroy() {
        event = null;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            event = EventosController.getByCod(manager, codEvent);

        } catch (Exception ex) {
            event = new Eventos();
            codEvent = null;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public Eventos getEvent() {
        return event;
    }

    public void setEvent(Eventos event) {
        this.event = event;
    }

    public Integer getCodEvent() {
        return codEvent;
    }

    public void setCodEvent(Integer codEvent) {
        this.codEvent = codEvent;
    }

    public boolean isActionNew(){
        return codEvent == null;
    }
    
    public void save() {

        if (event.getNome().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(
                    "form-event",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Preencha o formulário corretamente. O nome do evento é obrigatório"));
            return;
        }

        UsuariosWeb user = SessionContext.getInstance().getUserLoggedIn();
        Clientes cliente = SessionContext.getInstance().getClientLoggedIn();

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            event.setCodUsuario(user);
            event.setCodCliente(cliente);
            event.setStatus(true);

            manager.getTransaction().begin();

            if (codEvent == null) {
                EventosController.insert(manager, event);
                manager.getTransaction().commit();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Evento cadastrado com sucesso!"));
            } else {
                event.setCod(codEvent);
                EventosController.edit(manager, event);
                manager.getTransaction().commit();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Cadastro atualizado com sucesso!"));
            }

            codEvent = event.getCod();

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
        }

    }

    public void delete() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            event.setStatus(false);
            EventosController.edit(manager, event);

            manager.getTransaction().commit();

            
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro removido com sucesso!"));

            FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl() 
                    + "admin/operations/events.xhtml");
            
            
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
        }

    }

}
