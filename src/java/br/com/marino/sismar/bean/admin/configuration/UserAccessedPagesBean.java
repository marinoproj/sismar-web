package br.com.marino.sismar.bean.admin.configuration;

import br.com.marino.sismar.controller.SessaoPaginaController;
import br.com.marino.sismar.controller.UsuariosWebController;
import br.com.marino.sismar.controller.UsuariosWebSessaoController;
import br.com.marino.sismar.entity.SessaoPagina;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.entity.UsuariosWebSessao;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "UserAccessedPagesBean")
@ViewScoped
public class UserAccessedPagesBean implements Serializable {

    private UsuariosWeb user;
    private UsuariosWebSessao session;
    private List<SessaoPagina> pages;
    
    // parâmetros da url
    private String sessionId;
    

    @PostConstruct
    public void init() {
        try {
            sessionId = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("sessionId");
            initData();
        } catch (NumberFormatException ex) {
            user = null;
        }
    }

    public void initData() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            if (sessionId != null) {                
                session = UsuariosWebSessaoController.getByCodUsuario(manager, Integer.parseInt(sessionId));
                user = session.getCodUsuario();
            }
            
            if (session != null) {
                pages = SessaoPaginaController.getPaginasByCodUsuariosWebSessao(manager, session.getCodUsuariosWebSessao());
            }     

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    @PreDestroy
    public void destroy() {
        user = null;
        session = null;
        pages = null;
        sessionId = null;
    }

    public UsuariosWeb getUser() {
        return user;
    }

    public void setUser(UsuariosWeb user) {
        this.user = user;
    }

    public boolean userExists() {
        return user != null;
    }

    public boolean userHasAccess() {
        if (!userExists()) {
            return false;
        }
        return pages != null && !pages.isEmpty();
    }

    public String getDatetimeToString(Date datetime) {
        if (datetime == null) {
            return "";
        }
        return Util.dateToString(datetime, "dd/MM/yyyy HH:mm:ss");
    }

    public String getTimeDuration(Date start, Date end) {
        return Util.getTimeDuration(start, end == null ? new Date() : end);
    }

    public UsuariosWebSessao getSession() {
        return session;
    }

    public void setSession(UsuariosWebSessao session) {
        this.session = session;
    }

    public List<SessaoPagina> getPages() {
        return pages;
    }

    public void setPages(List<SessaoPagina> pages) {
        this.pages = pages;
    }
    
}
