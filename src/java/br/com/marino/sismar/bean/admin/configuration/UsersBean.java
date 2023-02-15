package br.com.marino.sismar.bean.admin.configuration;

import br.com.marino.sismar.controller.UsuariosWebController;
import br.com.marino.sismar.entity.Clientes;
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

@ManagedBean(name = "UsersBean")
@ViewScoped
public class UsersBean implements Serializable {

    private List<UsuariosWeb> listUsers;
    private UsuariosWeb userSelectedDelete;

    @PostConstruct
    public void init() {
        listUsers = reloadListUsers();
    }

    @PreDestroy
    public void destroy() {
        listUsers = null;
        userSelectedDelete = null;
    }

    public void selectUserDelete(UsuariosWeb user) {
        this.userSelectedDelete = user;
    }

    public List<UsuariosWeb> getListUsers() {
        return listUsers;
    }

    public void deleteUser() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();
            UsuariosWebController.delete(manager, userSelectedDelete.getIdUsuario());
            manager.getTransaction().commit();

            listUsers.remove(userSelectedDelete);

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro excluído com sucesso!"));

        } catch (Exception ex) {

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao excluir o cadastro!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
            userSelectedDelete = null;
        }

    }

    public List<UsuariosWeb> reloadListUsers() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            Clientes client = SessionContext.getInstance().getClientLoggedIn();
            
            UsuariosWeb u = SessionContext.getInstance().getUserLoggedIn();
            
            if (u.getMaster() != null && u.getMaster()){
                return UsuariosWebController.getListUsersByClient(manager, client.getCod(), true);
            }
            
            return UsuariosWebController.getListUsersByClient(manager, client.getCod(), false);
            
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

}
