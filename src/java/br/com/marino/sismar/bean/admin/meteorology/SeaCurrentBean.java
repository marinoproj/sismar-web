package br.com.marino.sismar.bean.admin.meteorology;

import br.com.marino.sismar.controller.CorrentometroController;
import br.com.marino.sismar.entity.Correntometro;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "SeaCurrentBean")
@ViewScoped
public class SeaCurrentBean implements Serializable {

    private Correntometro seaCurrent;
    private Exception seaCurrentException;
    private UsuariosWeb userLogged;
    
    @PostConstruct
    public void init() {
        userLogged = SessionContext.getInstance().getUserLoggedIn();
        reloadSeaCurrent();
    }

    @PreDestroy
    public void destroy() {
        seaCurrent = null;
        seaCurrentException = null;
    }    
    
    public void reloadSeaCurrent() {
        
        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            seaCurrent = CorrentometroController.getCorrentometroLast(manager);
            
            if (seaCurrent != null && !Util.isOnlineInfo(seaCurrent.getDataHora(),
                    Util.TMP_SECONDS_ONLINE_SEACURRENT)) {
                throw new Exception("outdated information.");
            }
            
            seaCurrentException = null;

        } catch (Exception ex) {
            seaCurrentException = ex;
            seaCurrent = null;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }
    
     public String getSeaCurrentSpeed() {
        if (seaCurrentException != null) {
            return "-";
        }
        if (seaCurrent == null) {
            return "-";
        }
        return getValueFormatted(Util.getSpeedCorrentometroByLevel(seaCurrent)) + " nós";
    }

    public String getSeaCurrentDir() {
        if (seaCurrentException != null) {
            return "-";
        }
        if (seaCurrent == null) {
            return "-";
        }
        return getValueFormatted(Util.getDirecaoCorrentometroByLevel(seaCurrent)) + " º";
    }

    public String getSeaCurrentMessageStatus() {
        if (seaCurrentException != null) {
            return "offline";
        }
        if (seaCurrent == null) {
            return "offline";
        }
        return "online";
    }    
        
    public String getValueFormatted(Double value) {
        if (value == null) {
            return "";
        }
        return Util.converterValue(value);
    }

    public UsuariosWeb getUserLogged() {
        return userLogged;
    }

    public void setUserLogged(UsuariosWeb userLogged) {
        this.userLogged = userLogged;
    }

}