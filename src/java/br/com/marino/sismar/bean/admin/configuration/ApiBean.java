package br.com.marino.sismar.bean.admin.configuration;

import br.com.marino.sismar.controller.ClientesController;
import br.com.marino.sismar.controller.UsuariosWebController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
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

@ManagedBean(name = "ApiBean")
@ViewScoped
public class ApiBean implements Serializable {

    private String api;
    private String apiTokenClient;

    @PostConstruct
    public void init() {
        UsuariosWeb user = SessionContext.getInstance().getUserLoggedIn();
        Clientes client = SessionContext.getInstance().getClientLoggedIn();
        api = user.getToken() == null ? "" : user.getToken();
        apiTokenClient = client.getToken() == null ? "" : client.getToken();
    }

    @PreDestroy
    public void destroy() {
        api = null;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getApiTokenClient() {
        return apiTokenClient;
    }

    public void setApiTokenClient(String apiTokenClient) {
        this.apiTokenClient = apiTokenClient;
    }

    public void copyToken() {
        String command = "var copyText = document.getElementById(\"form-token:txtToken\");"
                + "copyText.select();"
                + "document.execCommand(\"copy\");";
        PrimeFaces.current().executeScript(command);
    }

    public void generateToken() {

        UsuariosWeb user = SessionContext.getInstance().getUserLoggedIn();
        String tokenOld = user.getToken();

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            String token = Util.generateToken(user.getIdUsuario(), Util.TYPE_TOKEN_USER);

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            user.setToken(token);

            manager.getTransaction().begin();
            UsuariosWebController.edit(manager, user);
            manager.getTransaction().commit();

            api = token;

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao gerar o token!"));

            user.setToken(tokenOld);

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }

    }
    
    public void generateTokenClient() {

        Clientes client = SessionContext.getInstance().getClientLoggedIn();
        String tokenOld = client.getToken();

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            String token = Util.generateToken(client.getCod(), Util.TYPE_TOKEN_CLIENT);

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            client.setToken(token);

            manager.getTransaction().begin();
            ClientesController.edit(manager, client);
            manager.getTransaction().commit();

            apiTokenClient = token;

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao gerar o token!"));

            client.setToken(tokenOld);

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
