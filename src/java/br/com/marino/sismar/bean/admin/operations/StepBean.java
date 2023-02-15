package br.com.marino.sismar.bean.admin.operations;

import br.com.marino.sismar.controller.EtapasController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Etapas;
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

@ManagedBean(name = "StepBean")
@ViewScoped
public class StepBean implements Serializable {

    private Etapas step;
    private Integer codEtapa;

    @PostConstruct
    public void init() {
        step = new Etapas();
        try {
            String cod = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("etapa");
            if (cod != null && !cod.isEmpty()) {
                codEtapa = Integer.parseInt(cod);
            }
        } catch (NumberFormatException ex) {
            codEtapa = null;
        }
        if (step != null) {
            reloadInit();
        }
    }

    @PreDestroy
    public void destroy() {
        step = null;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            step = EtapasController.getByCod(manager, codEtapa);

        } catch (Exception ex) {
            step = new Etapas();
            codEtapa = null;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public Etapas getStep() {
        return step;
    }

    public void setStep(Etapas step) {
        this.step = step;
    }

    public Integer getCodEtapa() {
        return codEtapa;
    }

    public void setCodEtapa(Integer codEtapa) {
        this.codEtapa = codEtapa;
    }

    public boolean isActionNew(){
        return codEtapa == null;
    }
    
    public void save() {

        if (step.getNome().trim().isEmpty() ||
                step.getTempoEstimadoConclusaoMin() == null 
                || step.getPctEstadoAtencao() == null 
                || step.getPctEstadoCritico() == null) {
            FacesContext.getCurrentInstance().addMessage(
                    "form-step",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Preencha o formulário corretamente. O nome da etapa, "
                                    + "tempo estimado de conclusão, estado atenção e crítico é obrigatório!"));
            return;
        }

        UsuariosWeb user = SessionContext.getInstance().getUserLoggedIn();
        Clientes cliente = SessionContext.getInstance().getClientLoggedIn();

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            step.setCodUsuario(user);
            step.setCodCliente(cliente);
            step.setStatus(true);

            manager.getTransaction().begin();

            if (codEtapa == null) {
                EtapasController.insert(manager, step);
                manager.getTransaction().commit();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Etapa cadastrada com sucesso!"));
            } else {
                step.setCod(codEtapa);
                EtapasController.edit(manager, step);
                manager.getTransaction().commit();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Cadastro atualizado com sucesso!"));
            }

            codEtapa = step.getCod();

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

            step.setStatus(false);
            EtapasController.edit(manager, step);

            manager.getTransaction().commit();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro removido com sucesso!"));

            FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl() 
                    + "admin/operations/steps.xhtml");
            
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
