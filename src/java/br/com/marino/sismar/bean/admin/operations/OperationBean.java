package br.com.marino.sismar.bean.admin.operations;

import br.com.marino.sismar.controller.BercosController;
import br.com.marino.sismar.controller.NavioController;
import br.com.marino.sismar.controller.OperacaoController;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.Operacao;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "OperationBean")
@ViewScoped
public class OperationBean implements Serializable {

    private Operacao operation;
    private Integer codOperation;
    private List<Berco> bercos;

    @PostConstruct
    public void init() {
        operation = new Operacao();
        
        try {
            String cod = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("operacao");
            if (cod != null && !cod.isEmpty()) {
                codOperation = Integer.parseInt(cod);
            }
        } catch (NumberFormatException ex) {
            codOperation = null;
        }

        reloadInit();

    }

    @PreDestroy
    public void destroy() {
        operation = null;
    }

    public List<Navio> completeNavios(String query) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            return NavioController.getVesselsBySearch(manager, query, 10).stream().map(obj -> {

                Navio n = new Navio();

                n.setCodNavio(obj.getCodNavio());
                n.setNomeNavio(obj.getNomeNavio());
                n.setImo(obj.getImo());

                return n;

            }).collect(Collectors.toList());

        } catch (Exception ex) {
            return new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            if (codOperation != null) {
                operation = OperacaoController.getByCod(manager, codOperation);
            }
            
            bercos = BercosController.getListBercos(manager);

        } catch (Exception ex) {
            
            ex.printStackTrace();
            
            operation = new Operacao();
            codOperation = null;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public Operacao getOperation() {
        return operation;
    }

    public void setOperation(Operacao operation) {
        this.operation = operation;
    }

    public Integer getCodOperation() {
        return codOperation;
    }

    public void setCodOperation(Integer codOperation) {
        this.codOperation = codOperation;
    }

    public List<Berco> getBercos() {
        return bercos;
    }

    public void setBercos(List<Berco> bercos) {
        this.bercos = bercos;
    }

    public boolean isActionNew() {
        return codOperation == null;
    }

    public void save() {

        if (operation.getCodBerco() == null
                || operation.getCodNavio() == null) {
            FacesContext.getCurrentInstance().addMessage(
                    "form-operation",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Preencha o formulário corretamente. É obrigatório "
                            + "informar o navio e o berço de atracação"));
            return;
        }

        UsuariosWeb user = SessionContext.getInstance().getUserLoggedIn();
        Clientes cliente = SessionContext.getInstance().getClientLoggedIn();

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            operation.setCodUsuario(user);
            operation.setCodCliente(cliente);
            operation.setStatus(true);

            manager.getTransaction().begin();

            if (codOperation == null) {
                
                operation.setDataInicio(Util.getDateUTC());
                OperacaoController.insert(manager, operation);
                manager.getTransaction().commit();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Operação cadastrada com sucesso!"));
            } else {
                
                operation.setCod(codOperation);
                OperacaoController.edit(manager, operation);
                manager.getTransaction().commit();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Operação atualizada com sucesso!"));
            }

            codOperation = operation.getCod();

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

            operation.setStatus(false);
            OperacaoController.edit(manager, operation);

            manager.getTransaction().commit();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Operação removida com sucesso!"));

            FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl()
                    + "admin/operations/operations.xhtml");

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
