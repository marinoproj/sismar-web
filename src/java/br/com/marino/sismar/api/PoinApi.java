package br.com.marino.sismar.api;

import br.com.marino.sismar.controller.AisController;
import br.com.marino.sismar.controller.AispoinController;
import br.com.marino.sismar.controller.NavioController;
import br.com.marino.sismar.controller.NavioUltimaAtualizacaoController;
import br.com.marino.sismar.controller.PoinController;
import br.com.marino.sismar.entity.Ais;
import br.com.marino.sismar.entity.Aispoin;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.NavioUltimaAtualizacao;
import br.com.marino.sismar.entity.Poin;
import br.com.marino.sismar.util.NavioMapAis;
import br.com.marino.sismar.util.Util;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@Path("/poin")
public class PoinApi {

    
    @GET
    @Path("/all")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getListPoins(@HeaderParam("authorization") String auth) {

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

            List<Poin> list = PoinController.getListPoins(manager);

            json.put("results", list.size());

            JSONArray poins = new JSONArray();

            for (Poin poin : list) {

                JSONObject poinJson = new JSONObject();
                poinJson.put("id", poin.getCodPoin());
                poinJson.put("name", poin.getNome());
                poinJson.put("coordinates", new JSONArray(poin.getCoordenadas()));
                poins.put(poinJson);

            }

            json.put("poins", poins);

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
    @Path("/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getPoinById(@PathParam("id") int id,
            @HeaderParam("authorization") String auth) {

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

            Poin poin = PoinController.getPoinById(manager, id);

            if (poin == null) {
                json.put("found", false);
                json.put("poin", Util.getValueFromJson(null));

            } else {
                json.put("found", true);

                JSONObject poinJson = new JSONObject();
                poinJson.put("id", poin.getCodPoin());
                poinJson.put("name", poin.getNome());
                poinJson.put("coordinates", new JSONArray(poin.getCoordenadas()));

                json.put("poin", poinJson);

            }

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
    @Path("/{id}/vessels")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getListVesselsByIdPoin(@PathParam("id") int id,
            @HeaderParam("authorization") String auth) {

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

            Poin poin = PoinController.getPoinById(manager, id);

            // Poin não cadastrado
            if (poin == null) {
                json.put("found", false);
                json.put("poin", Util.getValueFromJson(null));
                return json.toString();
            }

            json.put("found", true);
            JSONObject poinJson = new JSONObject();
            poinJson.put("id", poin.getCodPoin());
            poinJson.put("name", poin.getNome());
            poinJson.put("coordinates", new JSONArray(poin.getCoordenadas()));

            List<Aispoin> list = AispoinController.getListAispoinsNowByIdPoin(manager, id);

            // Nenhum navio na área do poin
            if (list == null) {
                poinJson.put("vessels", Util.getValueFromJson(null));
                json.put("poin", poinJson);
                return json.toString();
            }

            JSONArray vesselsJson = new JSONArray();

            for (Aispoin aispoin : list) {

                Navio vessel = NavioController.getVesselByMmsi(manager, aispoin.getMmsi());

                JSONObject vesselJson = new JSONObject();

                if (vessel == null) {
                    vesselJson.put("name", Util.getValueFromJson(null));
                    vesselJson.put("imo", Util.getValueFromJson(null));
                    vesselJson.put("dimension", Util.getValueFromJson(null));
                    vesselJson.put("type", Util.getValueFromJson(null));
                }else{
                    NavioMapAis vesselMap = new NavioMapAis(vessel);
                    vesselJson.put("name", Util.getValueFromJson(vessel.getNomeNavio()));
                    vesselJson.put("imo", Util.getValueFromJson(vessel.getImo()));
                    vesselJson.put("dimension", Util.getValueFromJson((vesselMap.getComprimentoReal() + "m X " + vesselMap.getLarguraReal() + "m")));
                    vesselJson.put("type", Util.getValueFromJson(vesselMap.getCategoriaEmbarcacao()));
                }

                JSONObject vesselPoin = new JSONObject();
                vesselPoin.put("input_date", Util.getValueFromJson(Util.dateToString(aispoin.getDataEntrada(), "yyyy-MM-dd HH:mm:ss")));
                vesselPoin.put("input_speed", Util.getValueFromJson(aispoin.getVelocidadeEntrada()));

                vesselJson.put("poin", vesselPoin);

                NavioUltimaAtualizacao vesselLastUpdate = NavioUltimaAtualizacaoController
                        .getRadioLastUpdate(manager, aispoin.getMmsi());

                if (vesselLastUpdate != null) {

                    Ais aisLastUpdate = AisController.getAisByCodAis(manager,
                            vesselLastUpdate.getCodAisTabela(),
                            vesselLastUpdate.getCodAis());

                    // Encontrado última atualização
                    if (aisLastUpdate != null) {
                        JSONObject aisJson = new JSONObject();
                        aisJson.put("lat", Util.getValueFromJson(aisLastUpdate.getLatitude()));
                        aisJson.put("lng", Util.getValueFromJson(aisLastUpdate.getLongitude()));
                        aisJson.put("velocity", Util.getValueFromJson(aisLastUpdate.getVelocidadeSobreSolo()));
                        aisJson.put("direction", Util.getValueFromJson(aisLastUpdate.getCursoSobreSolo()));
                        aisJson.put("destination", Util.getValueFromJson(aisLastUpdate.getDestino()));
                        aisJson.put("state", Util.getValueFromJson(aisLastUpdate.getStatusNavegacao()));
                        aisJson.put("draught", Util.getValueFromJson(aisLastUpdate.getDraught()));
                        aisJson.put("date", Util.getValueFromJson(Util.dateToString(aisLastUpdate.getDataHora(), "yyyy-MM-dd HH:mm:ss")));
                        vesselJson.put("last_update", aisJson);

                    } else {
                        vesselJson.put("last_update", Util.getValueFromJson(null));

                    }

                } else {
                    vesselJson.put("last_update", Util.getValueFromJson(null));

                }

                vesselsJson.put(vesselJson);

            }

            poinJson.put("vessels", vesselsJson);
            json.put("poin", poinJson);

            return json.toString();

        } catch (Exception ex) {
            json = new JSONObject();
            try {
                json.put("error", true);
                json.put("message", "Could not query");
            } catch (Exception ex2) {
            }
            return json.toString();

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
