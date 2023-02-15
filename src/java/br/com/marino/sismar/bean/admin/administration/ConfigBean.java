package br.com.marino.sismar.bean.admin.administration;

import br.com.marino.sismar.controller.ConfigController;
import br.com.marino.sismar.entity.Config;
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
import org.primefaces.PrimeFaces;

@ManagedBean(name = "ConfigBean")
@ViewScoped
public class ConfigBean implements Serializable {

    private Config config;

    @PostConstruct
    public void init() {
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        config = null;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            config = ConfigController.getConfig(manager);
            if (config == null) {
                config = new Config();
            }

        } catch (Exception ex) {
            config = new Config();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void save() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            ConfigController.save(manager, config);
            
            manager.getTransaction().commit();
            
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Parâmetros atualizado com sucesso!"));           

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
            PrimeFaces.current().executeScript("PF('statusdialog').hide()");
        }

    }

}
