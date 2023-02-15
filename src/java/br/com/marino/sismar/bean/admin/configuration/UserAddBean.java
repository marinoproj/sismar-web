package br.com.marino.sismar.bean.admin.configuration;

import br.com.marino.sismar.controller.ClientesUsuariosController;
import br.com.marino.sismar.controller.UsuariosWebController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.ClientesUsuarios;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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

@ManagedBean(name = "UserAddBean")
@ViewScoped
public class UserAddBean implements Serializable {

    private UsuariosWeb userAdd;
    private String password;
    private List<UsuariosWeb> users;
    private UsuariosWeb userAddExist;

    @PostConstruct
    public void init() {
        userAdd = new UsuariosWeb();
        password = null;
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        userAdd = null;
        password = null;
    }

    public UsuariosWeb getUserAdd() {
        return userAdd;
    }

    public void setUserAdd(UsuariosWeb userAdd) {
        this.userAdd = userAdd;
    }
    
    public boolean isCurrentUserMaster(){
        UsuariosWeb user = SessionContext.getInstance().getUserLoggedIn();
        return user.getMaster() != null && user.getMaster();
    }

    public UsuariosWeb getUserAddExist() {
        return userAddExist;
    }

    public void setUserAddExist(UsuariosWeb userAddExist) {
        this.userAddExist = userAddExist;
    }    
    
    public List<String> getListTimeZones(){
        return Util.getListTimeZones();
    }

    public ArrayList<String> getTelasHabilitadasArray() {
        String telasHabilitadas = userAdd.getTelasHabilitadas();
        if (telasHabilitadas == null || telasHabilitadas.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(telasHabilitadas.split(";")));
    }

    public void setTelasHabilitadasArray(ArrayList<String> telasHabilitadasArray) {
        String telasHabilitadas = "";
        for (String l : telasHabilitadasArray) {
            if (telasHabilitadas.isEmpty()) {
                telasHabilitadas = l;
            } else {
                telasHabilitadas += ";" + l;
            }
        }
        userAdd.setTelasHabilitadas(telasHabilitadas);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UsuariosWeb> getUsers() {
        return users;
    }

    public void setUsers(List<UsuariosWeb> users) {
        this.users = users;
    }
    
    public void insertUserExist() {
        
        
        if (userAddExist == null){
            
            FacesContext.getCurrentInstance().addMessage(
                    "form-add-user",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Selecione um usuário para adicionar!"));
            
            return;
            
        }
        
        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            userAdd.setSenha(Util.convertStringToMd5(password));
            userAdd.setMaster(userAdd.getMaster() == null ? false : userAdd.getMaster());

            manager.getTransaction().begin();
            
            // vincula o usuário ao cliente
            ClientesUsuarios clienteUsuario = new ClientesUsuarios();
            clienteUsuario.setCodCliente(SessionContext.getInstance().getClientLoggedIn());
            clienteUsuario.setCodUsuario(userAddExist);
            ClientesUsuariosController.insert(manager, clienteUsuario);            
            
            manager.getTransaction().commit();

            userAddExist = null;            

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Usuário adicionado com sucesso!"));

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao adicionar usuário!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }
        
    }
    
    public void insertUser() {

        if (userAdd.getNome().trim().isEmpty() || userAdd.getLogin().trim().isEmpty()
                || password.trim().isEmpty() || userAdd.getTelasHabilitadas().trim().isEmpty()
                || userAdd.getTempoAtualizacao() == null || userAdd.getTempoAtualizacao() == 0) {

            FacesContext.getCurrentInstance().addMessage(
                    "form-add-user",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Preencha o formulário corretamente. Nome, Login, Senha e Tempo de atualização "
                                    + "são obrigatórios"));
            return;
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            // verifica se já existe um usuário com o mesmo login
            UsuariosWeb exists = UsuariosWebController.getUserByLogin(manager, userAdd.getLogin());
            if (exists != null) {
                FacesContext.getCurrentInstance().addMessage(
                        "form-add-user",
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Atenção",
                                "Já existe um usuário com o mesmo login!"));
                return;
            }

            userAdd.setSenha(Util.convertStringToMd5(password));
            userAdd.setMaster(userAdd.getMaster() == null ? false : userAdd.getMaster());

            manager.getTransaction().begin();
            
            UsuariosWebController.insert(manager, userAdd);
            
            // vincula o usuário ao cliente
            ClientesUsuarios clienteUsuario = new ClientesUsuarios();
            clienteUsuario.setCodCliente(SessionContext.getInstance().getClientLoggedIn());
            clienteUsuario.setCodUsuario(userAdd);
            ClientesUsuariosController.insert(manager, clienteUsuario);            
            
            manager.getTransaction().commit();

            userAdd = new UsuariosWeb();
            password = null;

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Usuário cadastrado com sucesso!"));

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao cadastrar usuário!"));

            password = null;

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

            Clientes client = SessionContext.getInstance().getClientLoggedIn();            
            users = UsuariosWebController.getListUsersByNotClient(manager, client.getCod(), false);                        

        } catch (Exception ex) {
            users = new ArrayList<>();

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
