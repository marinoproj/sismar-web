package br.com.marino.sismar.bean.admin.components;

import br.com.marino.sismar.controller.ClientesController;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.BercoCliente;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.session.SessionContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "FiltroBercoDialogBean")
@ViewScoped
public class FiltroBercoDialogBean implements Serializable {

    private List<Berco> listBercos;

    @PostConstruct
    public void init() {
        listBercos = new ArrayList<>();        
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        listBercos = null;
    }

    public List<Berco> getListBercos() {
        return listBercos;
    }

    public void setListBercos(List<Berco> listBercos) {
        this.listBercos = listBercos;
    }
    
    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();            
            
            Clientes client = ClientesController.getByCod(manager,
                    SessionContext.getInstance().getClientLoggedIn().getCod());   
            
            for (BercoCliente bc : client.getBercosCliente()) {                                                
                listBercos.add(bc.getCodBerco());                
            }                          
            
        } catch (Exception ex) {
            listBercos = new ArrayList<>();

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
