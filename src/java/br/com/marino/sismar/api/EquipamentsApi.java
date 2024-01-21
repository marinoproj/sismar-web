package br.com.marino.sismar.api;

import br.com.marino.sismar.controller.ClientesEquipamentosController;
import br.com.marino.sismar.entity.ClientesEquipamentos;
import br.com.marino.sismar.entity.Equipamentos;
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

@Path("/equipaments")
public class EquipamentsApi {

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getEquipaments(@HeaderParam("authorization") String auth) {

        UserLoggedApi userLogged = Util.getUserLoggedApi(auth);
        
        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            List<ClientesEquipamentos> listClientesEquipamentos = ClientesEquipamentosController.getListClientesEquipamentosByCodCliente(manager, userLogged.getCodClient());
                       
            JSONArray jsonEquipamentos = new JSONArray();
            
            for (ClientesEquipamentos obj : listClientesEquipamentos) {

                if (!obj.isExibirTelaMeteorologia()) {
                    continue;
                }

                Equipamentos equipamento = obj.getCodEquipamento();                

                JSONObject jsonEquipamento = new JSONObject();
                jsonEquipamento.put("cod", equipamento.getCodEquipamento());
                jsonEquipamento.put("nome", equipamento.getNome());
                jsonEquipamento.put("tipo", equipamento.getTipo());
                
                jsonEquipamentos.put(jsonEquipamento);
                
            }                       
            
            json.put("equipamentos", jsonEquipamentos);            

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
