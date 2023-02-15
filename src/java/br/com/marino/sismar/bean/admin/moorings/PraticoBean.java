package br.com.marino.sismar.bean.admin.moorings;

import br.com.marino.sismar.controller.PraticoController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Pratico;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "PraticoBean")
@ViewScoped
public class PraticoBean implements Serializable {

    private Pratico pratico;
    private Integer codPratico;

    @PostConstruct
    public void init() {
        pratico = new Pratico();
        try {
            String cod = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("pratico");
            if (cod != null && !cod.isEmpty()) {
                codPratico = Integer.parseInt(cod);
            }
        } catch (NumberFormatException ex) {
            codPratico = null;
        }
        if (pratico != null) {
            reloadInit();
        }
    }

    @PreDestroy
    public void destroy() {
        pratico = null;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            pratico = PraticoController.getByCod(manager, codPratico);

        } catch (Exception ex) {
            pratico = new Pratico();
            codPratico = null;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public Pratico getPratico() {
        return pratico;
    }

    public void setPratico(Pratico pratico) {
        this.pratico = pratico;
    }

    public Integer getCodPratico() {
        return codPratico;
    }

    public void setCodPratico(Integer codPratico) {
        this.codPratico = codPratico;
    }

    public boolean isActionNew(){
        return codPratico == null;
    }
    
    public void save() {

        if (pratico.getNomePratico().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(
                    "form-obj",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Preencha o formulário corretamente. O nome do prático é obrigatório"));
            return;
        }

        Clientes cliente = SessionContext.getInstance().getClientLoggedIn();

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            pratico.setCodCliente(cliente);
            pratico.setStatus(true);
            pratico.setDataCadastro(pratico.getDataCadastro() == null ? new Date() : pratico.getDataCadastro());

            manager.getTransaction().begin();

            if (codPratico == null) {
                PraticoController.insert(manager, pratico);
                manager.getTransaction().commit();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Prático cadastrado com sucesso!"));
            } else {
                pratico.setCodPratico(codPratico);
                PraticoController.edit(manager, pratico);
                manager.getTransaction().commit();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Cadastro atualizado com sucesso!"));
            }

            codPratico = pratico.getCodPratico();

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

            pratico.setStatus(false);
            PraticoController.edit(manager, pratico);

            manager.getTransaction().commit();

            
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro removido com sucesso!"));

            FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl() 
                    + "admin/moorings/praticos.xhtml");
            
            
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
