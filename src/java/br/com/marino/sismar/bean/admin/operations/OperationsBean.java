package br.com.marino.sismar.bean.admin.operations;

import br.com.marino.sismar.controller.OperacaoController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Operacao;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.session.SessionContext;
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

@ManagedBean(name = "OperationsBean")
@ViewScoped
public class OperationsBean implements Serializable {

    private List<Operacao> operations;
    private Operacao operationSelectedDelete;

    @PostConstruct
    public void init() {        
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        operations = null;
    }

    public List<Operacao> getOperations() {
        return operations;
    }

    public void setOperations(List<Operacao> operations) {
        this.operations = operations;
    }

    public Operacao getOperationSelectedDelete() {
        return operationSelectedDelete;
    }

    public void setOperationSelectedDelete(Operacao operationSelectedDelete) {        
        this.operationSelectedDelete = operationSelectedDelete;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            Clientes cliente = SessionContext.getInstance().getClientLoggedIn();           
            operations = OperacaoController.getListOperacaoByCodCliente(manager, cliente.getCod());

        } catch (Exception ex) {
            operations = new ArrayList<>();

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

            operationSelectedDelete.setStatus(false);
            OperacaoController.edit(manager, operationSelectedDelete);

            manager.getTransaction().commit();
            
            operations.remove(operationSelectedDelete);

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
            operationSelectedDelete = null;
        }

    }

}
