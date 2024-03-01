package br.com.marino.sismar.api;

import br.com.marino.sismar.controller.LayerController;
import br.com.marino.sismar.entity.Layer;
import br.com.marino.sismar.entity.Poin;
import br.com.marino.sismar.util.Util;
import java.awt.Color;
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
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@Path("/layer")
public class LayerApi {
    
    
    @GET
    @Path("/all_sys")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getListLayers(@HeaderParam("authorization") String auth) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            // Verifica se o usuário possui permissão para utilizar a api
            /*if (!Util.isUserAuthenticatedAPI(auth, manager)) {
                try {
                    json.put("error", true);
                    json.put("message", "User is not allowed to use api");
                } catch (JSONException ex) {
                }
                return json.toString();
            }*/

            List<Layer> layers = LayerController.getListLayers(manager);

            JSONArray layersJson = new JSONArray();                      
            
            for (Layer layer : layers) {

                JSONObject layerJson = new JSONObject();
                layerJson.put("cod", layer.getCodigo());
                layerJson.put("name", layer.getNome());

                JSONArray poinsJson = new JSONArray();
                for (Poin poin : layer.getPoinList()) {
                    JSONObject poinJson = new JSONObject();
                    poinJson.put("id", poin.getCodPoin());
                    poinJson.put("name", poin.getNome());                    
                    Color color = new Color(poin.getCor());                    
                    poinJson.put("color", String.format("#%02X%02X%02X", color.getRed(),
                            color.getGreen(), color.getBlue()));                    
                    poinJson.put("opacity", poin.getTransparencia() / 100.0);
                    poinJson.put("name", poin.getNome());
                    poinJson.put("coordinates", new JSONArray(poin.getCoordenadas()));
                    poinsJson.put(poinJson);

                }
                
                layerJson.put("poins", poinsJson);
                layersJson.put(layerJson);

            }           
            
            json.put("results", layers.size());  
            json.put("layers", layersJson);

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
