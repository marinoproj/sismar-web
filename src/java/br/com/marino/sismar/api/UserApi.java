package br.com.marino.sismar.api;

import br.com.marino.sismar.controller.UsuariosWebController;
import br.com.marino.sismar.entity.UsuariosWeb;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

@Path("/user")
public class UserApi {

    
    @GET
    @Path("/functionalities")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getFunctionalitiesByUser(@QueryParam("login") String login) throws Exception {

        EntityManagerFactory factory = null;
        EntityManager manager = null;
        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            UsuariosWeb user = UsuariosWebController.getUserByLogin(manager, login);

            if (user == null) {
                throw new Exception("Nenhum usuário com o login correspondente");
            }

            String telasHabilitadas[] = user.getTelasHabilitadas().split(";");

            json.put("functionalities", telasHabilitadas);
            json.put("login", user.getLogin());
            json.put("nome", user.getNome());

        } catch (Exception ex) {
            json.put("message", "Não foi possível buscar as informações");

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

        return json.toString();

    }

}
