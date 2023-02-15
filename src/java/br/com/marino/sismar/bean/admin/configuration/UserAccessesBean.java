package br.com.marino.sismar.bean.admin.configuration;

import br.com.marino.sismar.controller.UsuariosWebController;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.entity.UsuariosWebSessao;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
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

@ManagedBean(name = "UserAccessesBean")
@ViewScoped
public class UserAccessesBean implements Serializable {

    private UsuariosWeb user;
    private String login;

    @PostConstruct
    public void init() {
        try {
            login = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("user");
            user = getUserByLogin();
        } catch (NumberFormatException ex) {
            login = null;
            user = null;
        }
    }

    public UsuariosWeb getUserByLogin() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            return UsuariosWebController.getUserByLogin(manager, login);

        } catch (Exception ex) {
            return null;

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
        login = null;
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
        return user.getUsuariosWebSessaoList() != null && !user.getUsuariosWebSessaoList().isEmpty();
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

    public List<UsuariosWebSessao> getSessionHistory(){
        List<UsuariosWebSessao> sessions = new ArrayList<>();
        
        if (user.getUsuariosWebSessaoList() == null || user.getUsuariosWebSessaoList().isEmpty()){
            return sessions;
        }
        
        for (int i = user.getUsuariosWebSessaoList().size()-1; i >= 0; i--) {
            if (user.getUsuariosWebSessaoList().get(i).getTermino() == null){
                continue;
            }
            sessions.add(user.getUsuariosWebSessaoList().get(i));
        }
        
        return sessions;
    }    
    
}
