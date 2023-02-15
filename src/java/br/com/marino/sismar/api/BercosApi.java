package br.com.marino.sismar.api;

import br.com.marino.sismar.controller.AtracacaoController;
import br.com.marino.sismar.controller.ClientesController;
import br.com.marino.sismar.entity.Atracacao;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.BercoCliente;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.util.UserLoggedApi;
import br.com.marino.sismar.util.Util;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@Path("/bercos")
public class BercosApi {

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getBercos(@HeaderParam("authorization") String auth) {

        UserLoggedApi userLogged = Util.getUserLoggedApi(auth);
        
        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            Clientes client = ClientesController.getByCod(manager, userLogged.getCodClient());

            JSONArray jsonBercos = new JSONArray();

            for (BercoCliente bc : client.getBercosCliente()) {
                
                Berco berco = bc.getCodBerco();
                
                JSONObject jsonBerco = new JSONObject();
                jsonBerco.put("nome", berco.getNome());
                jsonBerco.put("cod", berco.getCodBerco());
                jsonBerco.put("latitude", berco.getLatitude());
                jsonBerco.put("longitude", berco.getLongitude());
                
                jsonBercos.put(jsonBerco);
                
            }
            
            json.put("bercos", jsonBercos);            

        } catch (Exception ex) {
            json = new JSONObject();
            try {
                json.put("error", true);
                json.put("message", "Could not query");
            } catch (Exception ex2) {
            }

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
    
    @GET
    @Path("/operando")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getBercosOperando(@HeaderParam("authorization") String auth) {

        UserLoggedApi userLogged = Util.getUserLoggedApi(auth);
        
        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            List<Atracacao> atracoes = AtracacaoController
                    .getAtracacaoAtivaByCodCliente(manager, userLogged.getCodClient());
            
            JSONArray jsonBercos = new JSONArray();

            for (Atracacao a : atracoes) {                                
                
                JSONObject jsonBerco = new JSONObject();
                jsonBerco.put("cod", a.getCodBerco().getCodBerco());
                jsonBerco.put("nome", a.getCodBerco().getNome());
                jsonBerco.put("latitude", a.getCodBerco().getLatitude());
                jsonBerco.put("longitude", a.getCodBerco().getLongitude());
                
                jsonBercos.put(jsonBerco);
                
            }
            
            json.put("bercos", jsonBercos);            

        } catch (Exception ex) {
            json = new JSONObject();
            try {
                json.put("error", true);
                json.put("message", "Could not query");
            } catch (Exception ex2) {
            }

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
