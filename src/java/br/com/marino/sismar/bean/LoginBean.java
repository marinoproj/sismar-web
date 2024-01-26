package br.com.marino.sismar.bean;

import br.com.marino.sismar.controller.ClientesController;
import br.com.marino.sismar.controller.ClientesUsuariosController;
import br.com.marino.sismar.controller.UsuariosWebController;
import br.com.marino.sismar.controller.UsuariosWebSessaoController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.ClientesUsuarios;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.entity.UsuariosWebSessao;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@ManagedBean(name = "LoginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private String login = "";
    private String password = "";
    private List<Clientes> clientes;
    private UsuariosWeb userAutenticado;

    @PreDestroy
    public void destroy() {
        login = null;
        password = null;
    }

    public boolean containsClientes() {
        return clientes != null;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Clientes> getClientes() {
        return clientes;
    }

    public void setClientes(List<Clientes> clientes) {
        this.clientes = clientes;
    }

    public UsuariosWeb getUserAutenticado() {
        return userAutenticado;
    }

    public void setUserAutenticado(UsuariosWeb userAutenticado) {
        this.userAutenticado = userAutenticado;
    }

    public String getUserLoggedInByJSON() throws JSONException {

        UsuariosWeb user = getUserLoggedIn();
        JSONObject userJson = new JSONObject();
        userJson.put("token", Util.generateTokenLogged(user.getIdUsuario(), getClient().getCod()));        
        userJson.put("functionalities", user.getTelasHabilitadas().split(";"));
        return userJson.toString();

    }

    public void readClientes() {

        if (login.trim().isEmpty() || password.trim().isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Atenção",
                    "Informe o login e a senha para acessar a área administrativa!"));
            return;
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            UsuariosWeb user = UsuariosWebController
                    .getUserByLoginAndPassword(manager, login, Util.convertStringToMd5(password));

            if (user == null) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Atenção",
                        "Login ou senha inválida!"));
                return;
            }

            /*UsuariosWebSessao sessionOpen = user.getOpenSession();

            if (sessionOpen != null) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Atenção",
                        "Não foi possível efetuar o login. Já existe uma sessão aberta em "
                        + "outro dispositivo com as suas credenciais. Encerre a sessão e tente novamente!"));

                return;
            }*/

            clientes = new ArrayList<>();
            userAutenticado = user;

            if (userAutenticado.getMaster() != null && userAutenticado.getMaster()) {
                clientes = ClientesController.getListClientes(manager);
            } else {
                for(ClientesUsuarios cu : ClientesUsuariosController.getListClientesUsuariosByCodUsuario(manager,
                        userAutenticado.getIdUsuario())){
                    clientes.add(cu.getCodCliente());
                }                
            }

            if (clientes.isEmpty()){
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Atenção",
                        "Este usuário não está vinculado a nenhum cliente!"));
                return;
            }
            
            if (clientes.size() == 1){
                signIn(clientes.get(0));
                return;
            }
            
            PrimeFaces.current().ajax().update("dialod-select-client-form");
            PrimeFaces.current().executeScript("PF('dialog-select-client').show();");

        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Atenção",
                    "Não foi possível validar o usuário!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void signIn(Clientes client) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            HttpServletRequest request = (HttpServletRequest) FacesContext.
                    getCurrentInstance().getExternalContext().getRequest();

            String ip = Util.getIpAddr(request);
            
            if (userAutenticado.getOpenSession() == null){
            
                UsuariosWebSessao sessionNew = new UsuariosWebSessao();
                sessionNew.setCodUsuario(userAutenticado);
                sessionNew.setInicio(new Date());
                sessionNew.setIp(ip);
            
                manager.getTransaction().begin();
                UsuariosWebSessaoController.insert(manager, sessionNew);
                manager.getTransaction().commit();
                
                userAutenticado.addSession(sessionNew);
                
            }
            
            SessionContext.getInstance().setUserLoggedIn(userAutenticado, client);

            login = "";
            password = "";

            if (userAutenticado.getPaginaInicial() != null 
                    && !userAutenticado.getPaginaInicial().isEmpty()){
                FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl()
                        + userAutenticado.getPaginaInicial());
            
            } else if (Util.containsFeature(userAutenticado, "ais_realtime")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl()
                        + "admin/ais/realtime.xhtml");
            
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl()
                        + "admin/home.xhtml");
            }

        } catch (Exception ex) {
            
            if (manager != null) {
                manager.getTransaction().rollback();
            }

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Atenção",
                    "Não foi possível efetuar o login!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public String signOut() {
        SessionContext.getInstance().closeSession();
        return "/index.xhtml?faces-redirect=true";
    }

    public UsuariosWeb getUserLoggedIn() {
        return SessionContext.getInstance().getUserLoggedIn();
    }
    
    public Clientes getClient(){
        return SessionContext.getInstance().getClientLoggedIn();
    }
    
    public String getNameSystem() {
        UsuariosWeb user = getUserLoggedIn();
        if (user.getLogin().equalsIgnoreCase("carlos") || user.getLogin().equalsIgnoreCase("leo")){
            return "MARINO";
        }
        return "SISMAR";
    }

    
}
