package br.com.marino.sismar.bean.admin.ais;

import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "AisBean")
@ViewScoped
public class AisBean implements Serializable {  

    private boolean init;
    private int delay;
    private Date datetimesearch;
    
    @PostConstruct
    public void init() {  
        init = false;
        this.delay = 0;
    }

    @PreDestroy
    public void destroy() {
    }    

    public void updateVessels(EntityManager managerSuper){    
        
        Date datetimesearchnew = new Date();        
        if (datetimesearch != null){            
            if (Util.dateToString(datetimesearch, "dd/MM/yyyy HH:mm:ss")
                    .equalsIgnoreCase(Util.dateToString(datetimesearchnew, "dd/MM/yyyy HH:mm:ss"))){
                return;
            }            
        }                
        
        datetimesearch = datetimesearchnew;        

        UsuariosWeb user = SessionContext.getInstance().getUserLoggedIn();
              
        if (!init){
            PrimeFaces.current().executeScript("ais.updateVessels(false); ais.fitBoundsShowAllVessels(); ais.execByParamsUrl();");               
        } else {
            PrimeFaces.current().executeScript("ais.updateVessels(true);");               
        }                       
        
        init = true; 
        delay = user.getTempoAtualizacao();
        
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }    
        
}
