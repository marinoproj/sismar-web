package br.com.marino.sismar.api;

import br.com.marino.sismar.controller.AisController;
import br.com.marino.sismar.controller.BercosController;
import br.com.marino.sismar.controller.LayerController;
import br.com.marino.sismar.controller.NavioController;
import br.com.marino.sismar.controller.NavioUltimaAtualizacaoController;
import br.com.marino.sismar.entity.Ais;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.Layer;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.NavioUltimaAtualizacao;
import br.com.marino.sismar.util.NavioMapAis;
import br.com.marino.sismar.util.Util;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@Path("/ais")
public class AisApi {

    
    public JSONObject getJsonAis(Ais ais) throws Exception {

        JSONObject jsonAis = new JSONObject();
        
        jsonAis.put("date", ais.getDataHora().getTime());

        Navio vessel = ais.getCodNavio();
        NavioMapAis vesselMapAis = new NavioMapAis(vessel);

        if (vessel != null) {

            JSONObject jsonVessel = new JSONObject();
            jsonVessel.put("name", Util.getValueFromJson(vessel.getNomeNavio()));
            jsonVessel.put("imo", Util.getValueFromJson(vessel.getImo()));
            jsonVessel.put("dimension", Util.getValueFromJson((vesselMapAis.getComprimentoReal() + "m X " + vesselMapAis.getLarguraReal() + "m")));
            jsonVessel.put("type", Util.getValueFromJson(vesselMapAis.getCategoriaEmbarcacao()));
            jsonVessel.put("image", Util.getValueFromJson(null));

            jsonAis.put("vessel", jsonVessel);

        } else {
            jsonAis.put("vessel", Util.getValueFromJson(null));

        }

        JSONObject printMapJson = new JSONObject();
        printMapJson.put("lat", ais.getLatitude());
        printMapJson.put("lng", ais.getLongitude());
        printMapJson.put("mmsi", ais.getMmsi());
        printMapJson.put("velocity", ais.getVelocidadeSobreSolo());
        printMapJson.put("direction", ais.getCursoSobreSolo());
        printMapJson.put("destination", (ais.getDestino() == null || ais.getDestino().trim().isEmpty()) ? "" : ais.getDestino());
        printMapJson.put("color", String.format("#%02X%02X%02X", vesselMapAis.getCorEmbarcacaoMapa().getRed(),
                vesselMapAis.getCorEmbarcacaoMapa().getGreen(), vesselMapAis.getCorEmbarcacaoMapa().getBlue()));
        printMapJson.put("opacity", 0.5);
        printMapJson.put("type", vesselMapAis.getCategoriaEmbarcacao());
        printMapJson.put("codType", vesselMapAis.getCodCategoriaEmbarcacao());
        printMapJson.put("state", Util.getValueFromJson(ais.getStatusNavegacao()));
        printMapJson.put("draught", Util.getValueFromJson(ais.getDraught()));
        printMapJson.put("message", Util.getValueFromJson("Recebido " + Util.getStringDateLastUpdate(new Date(), ais.getDataHora())));

        if (vesselMapAis.isDimensaoProporcionalMapa()) {
            JSONObject dimension = new JSONObject();
            dimension.put("a", vesselMapAis.getA());
            dimension.put("b", vesselMapAis.getB());
            dimension.put("c", vesselMapAis.getC());
            dimension.put("d", vesselMapAis.getD());
            printMapJson.put("dimension", dimension);
            printMapJson.put("proportionalMap", true);
        } else {
            int comprimento = vesselMapAis.getComprimentoMapa();
            int largura = vesselMapAis.getLarguraMapa();
            int b = (int) (comprimento * 0.2);
            int a = comprimento - b;
            int c = largura / 2;
            int d = largura - c;
            JSONObject dimension = new JSONObject();
            dimension.put("a", a);
            dimension.put("b", b);
            dimension.put("c", c);
            dimension.put("d", d);
            printMapJson.put("dimension", dimension);
            printMapJson.put("proportionalMap", false);
        }

        jsonAis.put("printMap", printMapJson);

        return jsonAis;
    }

    @GET
    @Path("/playback")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getPlayBack2(@QueryParam("startDate") Long startDateMilliseconds,
            @QueryParam("endDate") Long endDateMilliseconds) throws Exception {

        Date startDate = new Date(startDateMilliseconds);
        Date endDate = new Date(endDateMilliseconds);

        EntityManagerFactory factory = null;
        EntityManager manager = null;
        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            List<Ais> history = AisController.getAisHistory(manager, startDate, endDate);
            
            Date[] period = Util.getPeriodValidForAis(startDate);
            List<Ais> initial = AisController.getListVesselActiveOldVersion(manager, period[1], period[0]);
            
            JSONArray vesselsJsonInitial = new JSONArray();
            for(Ais ais : initial){
                JSONObject aisJson = getJsonAis(ais);
                vesselsJsonInitial.put(aisJson);                
            }
            
            JSONArray vesselsJsonHistory = new JSONArray();
            for(Ais ais : history){
                JSONObject aisJson = getJsonAis(ais);
                vesselsJsonHistory.put(aisJson);                
            }
            
            json.put("initial", vesselsJsonInitial);
            json.put("history", vesselsJsonHistory);
            
        } catch (Exception ex) {
            
            
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
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String all() throws Exception {

        EntityManagerFactory factory = null;
        EntityManager manager = null;
        JSONArray json = new JSONArray();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            Date[] period = Util.getPeriodValidForAis(new Date());

            List<Ais> list = AisController.getListVesselActive(manager, period[1], period[0]);

            
            for (Ais ais : list) {

                List<Ais> boatTrail = new ArrayList<>();

                /*if (ais.getVelocidadeSobreSolo() >= 1) {
                    Calendar current = Calendar.getInstance();
                    current.add(Calendar.MINUTE, -3);
                    if (current.getTime().before(ais.getDataHora())) {
                        boatTrail = AisController.getBoatTrail(manager, period[1],
                                period[0], ais.getMmsi(), 10);
                    }
                }*/

                json.put(Util.getAisByJson(ais, boatTrail));

            }
            
            return json.toString();

        } catch (Exception ex) {
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
    @Path("/vessel/{mmsi}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getDataVesselFromPositionInMap(@PathParam("mmsi") int mmsi,
            @HeaderParam("authorization") String auth) throws Exception {

        EntityManagerFactory factory = null;
        EntityManager manager = null;
        JSONObject json;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            // Verifica se o usuário possui permissão para utilizar a api
            /*if (!Util.isUserAuthenticatedAPI(auth, manager)) {
                json = new JSONObject();
                try {
                    json.put("error", true);
                    json.put("message", "User is not allowed to use api");
                } catch (JSONException ex) {
                }
                return json.toString();
            }*/
            
            Navio vessel = NavioController.getVesselByMmsi(manager, mmsi);
            
            NavioUltimaAtualizacao vesselLastUpdate = NavioUltimaAtualizacaoController
                    .getRadioLastUpdate(manager, mmsi);
            
            json = Util.getAisByJson(mmsi, vessel, vesselLastUpdate, null);            

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
    @Path("/startup")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getDataForStartup(@HeaderParam("authorization") String auth) throws Exception {

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
            
            // pega os layers
            List<Layer> layers = LayerController.getListLayers(manager);
            JSONArray layersJson = new JSONArray();                                 
            for (Layer layer : layers) {
                layersJson.put(layer.toJSONObject());
            }           
            
            // pega os bercos
            List<Berco> bercos = BercosController.getListBercos(manager);
            JSONArray bercosJson = new JSONArray();                                 
            for (Berco berco : bercos) {
                bercosJson.put(berco.toJSONObject());
            }  
            
            json.put("layers", layersJson);
            json.put("bercos", bercosJson);
            

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
