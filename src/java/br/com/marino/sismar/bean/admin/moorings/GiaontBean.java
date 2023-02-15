package br.com.marino.sismar.bean.admin.moorings;

import br.com.marino.sismar.controller.GiaontController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Giaont;
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

@ManagedBean(name = "GiaontBean")
@ViewScoped
public class GiaontBean implements Serializable {

    private Giaont giaont;
    private Integer codGiaont;

    @PostConstruct
    public void init() {
        giaont = new Giaont();
        try {
            String cod = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("giaont");
            if (cod != null && !cod.isEmpty()) {
                codGiaont = Integer.parseInt(cod);
            }
        } catch (NumberFormatException ex) {
            codGiaont = null;
        }
        if (giaont != null) {
            reloadInit();
        }
    }

    @PreDestroy
    public void destroy() {
        giaont = null;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            giaont = GiaontController.getByCod(manager, codGiaont);

        } catch (Exception ex) {
            giaont = new Giaont();
            codGiaont = null;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public Giaont getGiaont() {
        return giaont;
    }

    public void setGiaont(Giaont giaont) {
        this.giaont = giaont;
    }

    public Integer getCodGiaont() {
        return codGiaont;
    }

    public void setCodGiaont(Integer codGiaont) {
        this.codGiaont = codGiaont;
    }

    public boolean isActionNew(){
        return codGiaont == null;
    }
    
    public void save() {

        if (giaont.getNomeGiaont().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(
                    "form-obj",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Preencha o formulário corretamente. O nome do giaont é obrigatório"));
            return;
        }

        Clientes cliente = SessionContext.getInstance().getClientLoggedIn();

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            giaont.setCodCliente(cliente);
            giaont.setStatus(true);
            giaont.setDataCadastro(giaont.getDataCadastro() == null ? new Date() : giaont.getDataCadastro());

            manager.getTransaction().begin();

            if (codGiaont == null) {
                GiaontController.insert(manager, giaont);
                manager.getTransaction().commit();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Giaont cadastrado com sucesso!"));
            } else {
                giaont.setCodGiaont(codGiaont);
                GiaontController.edit(manager, giaont);
                manager.getTransaction().commit();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Cadastro atualizado com sucesso!"));
            }

            codGiaont = giaont.getCodGiaont();

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

            giaont.setStatus(false);
            GiaontController.edit(manager, giaont);

            manager.getTransaction().commit();

            
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro removido com sucesso!"));

            FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl() 
                    + "admin/moorings/giaonts.xhtml");
            
            
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
