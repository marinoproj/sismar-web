package br.com.marino.sismar.bean.admin.administration;

import br.com.marino.sismar.controller.BercoClienteController;
import br.com.marino.sismar.controller.ClientesController;
import br.com.marino.sismar.entity.BercoCliente;
import br.com.marino.sismar.entity.Clientes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "ClientsBean")
@ViewScoped
public class ClientsBean implements Serializable {

    private List<Clientes> clients;
    private Clientes clientSelectedDelete;

    @PostConstruct
    public void init() {
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        clients = null;
    }

    public List<Clientes> getClients() {
        return clients;
    }

    public void setClients(List<Clientes> clients) {
        this.clients = clients;
    }

    public Clientes getClientSelectedDelete() {
        return clientSelectedDelete;
    }

    public void setClientSelectedDelete(Clientes clientSelectedDelete) {
        this.clientSelectedDelete = clientSelectedDelete;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();            
            clients = ClientesController.getListClientes(manager);

        } catch (Exception ex) {
            clients = new ArrayList<>();

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

            clientSelectedDelete.setStatus(false);
            ClientesController.edit(manager, clientSelectedDelete);

            List<BercoCliente> bercosClienteSelecionados = BercoClienteController.getList(manager, clientSelectedDelete.getCod());
            for (BercoCliente bc : bercosClienteSelecionados) {
                try {
                    BercoClienteController.delete(manager, bc);
                } catch (Exception ex) {
                }
            }
            
            manager.getTransaction().commit();
            
            clients.remove(clientSelectedDelete);

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro removido com sucesso!"));

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
            clientSelectedDelete = null;
        }

    }

}
