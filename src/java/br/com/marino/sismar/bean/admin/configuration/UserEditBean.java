package br.com.marino.sismar.bean.admin.configuration;

import br.com.marino.sismar.controller.UsuariosWebController;
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

@ManagedBean(name = "UserEditBean")
@ViewScoped
public class UserEditBean implements Serializable {

    private UsuariosWeb userEdit;
    private String login;
    private String password;

    @PostConstruct
    public void init() {
        password = null;
        try {
            login = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("user");
            userEdit = reloadUserByLogin();
        } catch (NumberFormatException ex) {
            login = "";
        }
    }

    public List<String> getListTimeZones(){
        return Util.getListTimeZones();
    }
    
    public boolean isCurrentUserMaster(){
        UsuariosWeb user = SessionContext.getInstance().getUserLoggedIn();
        return user.getMaster() != null && user.getMaster();
    }
    
    public UsuariosWeb reloadUserByLogin() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            return UsuariosWebController.getUserByLogin(manager, login);

        } catch (Exception ex) {
            return new UsuariosWeb();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    @PreDestroy
    public void destroy() {
        userEdit = null;
        password = null;
        login = null;
    }

    public UsuariosWeb getUserEdit() {
        return userEdit;
    }

    public void setUserEdit(UsuariosWeb userEdit) {
        this.userEdit = userEdit;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getTelasHabilitadasArray() {
        String telasHabilitadas = userEdit.getTelasHabilitadas();
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
        userEdit.setTelasHabilitadas(telasHabilitadas);
    }

    public void editUser() {

        if (userEdit.getNome().trim().isEmpty() || userEdit.getLogin().trim().isEmpty()
                || userEdit.getTelasHabilitadas().trim().isEmpty() 
                || userEdit.getTempoAtualizacao() == null || userEdit.getTempoAtualizacao() == 0) {

            FacesContext.getCurrentInstance().addMessage(
                    "form-edit-user",
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
            UsuariosWeb exists = UsuariosWebController.getUserByLogin(manager, userEdit.getLogin());
            if (exists != null && exists.getIdUsuario().intValue() != userEdit.getIdUsuario().intValue()) {
                FacesContext.getCurrentInstance().addMessage(
                        "form-edit-user",
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Atenção",
                                "Já existe um usuário com o mesmo login!"));
                return;
            }

            UsuariosWeb user = userEdit.clone();
            if (!password.trim().isEmpty()) {
                user.setSenha(Util.convertStringToMd5(password));
            }

            manager.getTransaction().begin();
            UsuariosWebController.edit(manager, user);
            manager.getTransaction().commit();

            userEdit = user;
            password = null;

            if (SessionContext.getInstance().getUserLoggedIn().getIdUsuario().intValue() == userEdit.getIdUsuario().intValue()) {
                SessionContext.getInstance().setUserLoggedIn(userEdit, SessionContext.getInstance().getClientLoggedIn());
            }

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro atualizado com sucesso!"));

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao atualizar o cadastro!"));

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

}
