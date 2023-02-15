package br.com.marino.sismar.api;

import br.com.marino.sismar.controller.AisController;
import br.com.marino.sismar.controller.AispoinController;
import br.com.marino.sismar.controller.NavioController;
import br.com.marino.sismar.controller.NavioUltimaAtualizacaoController;
import br.com.marino.sismar.entity.Ais;
import br.com.marino.sismar.entity.Aispoin;
import br.com.marino.sismar.entity.ImageVessel;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.NavioUltimaAtualizacao;
import br.com.marino.sismar.entity.Poin;
import br.com.marino.sismar.entity.VesselSearch;
import br.com.marino.sismar.util.NavioMapAis;
import br.com.marino.sismar.util.Util;
import java.util.Date;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@Path("/vessel")
public class VesselApi {

    @GET
    @Path("/search/{search}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getVesselsBySearch(@PathParam("search") String search,
            @HeaderParam("authorization") String auth) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            // Verifica se o usuário possui permissão para utilizar a api
            if (!Util.isUserAuthenticatedAPI(auth, manager)) {
                try {
                    json.put("error", true);
                    json.put("message", "User is not allowed to use api");
                } catch (JSONException ex) {
                }
                return json.toString();
            }

            List<VesselSearch> vessels = NavioController.getVesselsBySearch(manager, search);

            JSONArray vesselsJson = new JSONArray();

            for (VesselSearch vessel : vessels) {

                JSONObject vesselJson = new JSONObject();
                vesselJson.put("name", Util.getValueFromJson(vessel.getNomeNavio()));
                vesselJson.put("imo", Util.getValueFromJson(vessel.getImo()));
                vesselJson.put("mmsi", Util.getValueFromJson(vessel.getMmsi()));

                ImageVessel image = NavioController.getImageVesselByCod(manager, vessel.getCodNavio());

                if (image != null && image.getImagem() != null) {
                    byte[] bytes = new byte[image.getImagem().length];
                    for (int i = 0; i < image.getImagem().length; i++) {
                        bytes[i] = image.getImagem()[i];
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("data:image/png;base64,");
                    sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes, false)));
                    vesselJson.put("image", sb.toString());

                } else {
                    vesselJson.put("image", JSONObject.NULL);
                }

                vesselsJson.put(vesselJson);

            }

            json.put("vessels", vesselsJson);

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

    // =====================================================================
    /**
     * Retorna as informações de um navio com base no imo
     *
     * @param imo
     * @param auth
     * @return
     */
    @GET
    @Path("/{imo}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getInfoVessel(@PathParam("imo") int imo,
            @HeaderParam("authorization") String auth) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            // Verifica se o usuário possui permissão para utilizar a api
            if (!Util.isUserAuthenticatedAPI(auth, manager)) {
                try {
                    json.put("error", true);
                    json.put("message", "User is not allowed to use api");
                } catch (JSONException ex) {
                }
                return json.toString();
            }

            Navio vessel = NavioController.getVesselByImo(manager, imo);

            // Nenhum navio encontrado
            if (vessel == null) {
                json.put("found", false);
                json.put("vessel", Util.getValueFromJson(null));

            } else {
                // Navio encontrado                
                NavioMapAis vesselMap = new NavioMapAis(vessel);

                JSONObject vesselJson = new JSONObject();
                vesselJson.put("name", Util.getValueFromJson(vessel.getNomeNavio()));
                vesselJson.put("imo", Util.getValueFromJson(vessel.getImo()));
                vesselJson.put("dimension", Util.getValueFromJson((vesselMap.getComprimentoReal() + "m X " + vesselMap.getLarguraReal() + "m")));
                vesselJson.put("type", Util.getValueFromJson(vesselMap.getCategoriaEmbarcacao()));

                NavioUltimaAtualizacao vesselLastUpdate = NavioUltimaAtualizacaoController
                        .getRadioLastUpdate(manager, vessel.getMmsi());

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

                // poins atuais
                List<Aispoin> listPoinsOpen = AispoinController.
                        getListAispoinOpenByVessel(manager, vessel.getMmsi());

                JSONArray poinsJson = new JSONArray();

                if (listPoinsOpen == null || listPoinsOpen.isEmpty()) {
                    vesselJson.put("poins", Util.getValueFromJson(null));

                } else {

                    for (Aispoin aisPoin : listPoinsOpen) {
                        JSONObject poinJson = new JSONObject();
                        Poin poin = aisPoin.getCodPoin();
                        poinJson.put("id", Util.getValueFromJson(poin.getCodPoin()));
                        poinJson.put("name", Util.getValueFromJson(poin.getNome()));
                        poinJson.put("coordinates", new JSONArray(poin.getCoordenadas()));
                        poinJson.put("input_date", Util.getValueFromJson(Util.dateToString(aisPoin.getDataEntrada(), "yyyy-MM-dd HH:mm:ss")));
                        poinJson.put("input_speed", Util.getValueFromJson(aisPoin.getVelocidadeEntrada()));
                        poinsJson.put(poinJson);
                    }

                    vesselJson.put("poins", poinsJson);

                }

                json.put("found", true);
                json.put("vessel", vesselJson);

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

    // =====================================================================================
    /**
     * Retorna a imagem da embarcação
     *
     * @param imo
     * @param auth
     * @return
     */
    @GET
    @Path("/{imo}/image")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getImageVessel(@PathParam("imo") int imo,
            @HeaderParam("authorization") String auth) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            // Verifica se o usuário possui permissão para utilizar a api
            if (!Util.isUserAuthenticatedAPI(auth, manager)) {
                try {
                    json.put("error", true);
                    json.put("message", "User is not allowed to use api");
                } catch (JSONException ex) {
                }
                return json.toString();
            }

            ImageVessel imageVessel = NavioController.getImageVesselByImo(manager, imo);

            if (imageVessel != null && imageVessel.getImagem() != null) {
                byte[] bytes = new byte[imageVessel.getImagem().length];
                for (int i = 0; i < imageVessel.getImagem().length; i++) {
                    bytes[i] = imageVessel.getImagem()[i];
                }
                StringBuilder sb = new StringBuilder();
                sb.append("data:image/png;base64,");
                sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes, false)));

                json.put("found", true);
                json.put("image", sb.toString());

            } else {
                json.put("found", false);
                json.put("image", Util.getValueFromJson(null));
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

    // =====================================================================================
    /**
     * Retorna o rastro de navegação de uma embarcação específica
     *
     * @param imo
     * @param startDateMilliseconds
     * @param endDateMilliseconds
     * @param auth
     * @return
     * @throws Exception
     */
    @GET
    @Path("/{imo}/track")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getTrackVessel(@PathParam("imo") int imo,
            @QueryParam("start_date") Long startDateMilliseconds,
            @QueryParam("end_date") Long endDateMilliseconds,
            @HeaderParam("authorization") String auth) throws Exception {

        if ((endDateMilliseconds - startDateMilliseconds) > (1000 * 60 * 60 * 12)) {
            JSONObject json = new JSONObject();
            json.put("error", true);
            json.put("message", "Invalid parameters. The reported period exceeds 12 hours.");
            return json.toString();
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            // Verifica se o usuário possui permissão para utilizar a api
            if (!Util.isUserAuthenticatedAPI(auth, manager)) {
                try {
                    json.put("error", true);
                    json.put("message", "User is not allowed to use api");
                } catch (JSONException ex) {
                }
                return json.toString();
            }

            Navio vessel = NavioController.getVesselByImo(manager, imo);

            // Nenhum navio encontrado
            if (vessel == null) {
                json.put("found", false);
                json.put("vessel", Util.getValueFromJson(null));

            } else {
                // Navio encontrado                
                NavioMapAis vesselMap = new NavioMapAis(vessel);

                JSONObject vesselJson = new JSONObject();
                vesselJson.put("name", Util.getValueFromJson(vessel.getNomeNavio()));
                vesselJson.put("imo", Util.getValueFromJson(vessel.getImo()));
                vesselJson.put("dimension", Util.getValueFromJson((vesselMap.getComprimentoReal() + "m X " + vesselMap.getLarguraReal() + "m")));
                vesselJson.put("type", Util.getValueFromJson(vesselMap.getCategoriaEmbarcacao()));

                // Rastro da embarcação
                List<Ais> track = AisController.getBoatTrail(manager,
                        new Date(startDateMilliseconds),
                        new Date(endDateMilliseconds), vessel.getMmsi(), null);

                JSONArray trackJson = new JSONArray();

                for (Ais ais : track) {
                    JSONObject trackObj = new JSONObject();
                    trackObj.put("lat", ais.getLatitude());
                    trackObj.put("lng", ais.getLongitude());
                    trackObj.put("velocity", Util.getValueFromJson(ais.getVelocidadeSobreSolo()));
                    trackObj.put("direction", Util.getValueFromJson(ais.getCursoSobreSolo()));
                    trackObj.put("destination", Util.getValueFromJson(ais.getDestino()));
                    trackObj.put("state", Util.getValueFromJson(ais.getStatusNavegacao()));
                    trackObj.put("draught", Util.getValueFromJson(ais.getDraught()));
                    trackObj.put("date", Util.getValueFromJson(Util.dateToString(ais.getDataHora(), "yyyy-MM-dd HH:mm:ss")));
                    trackJson.put(trackObj);
                }

                vesselJson.put("track", trackJson);

                json.put("found", true);
                json.put("vessel", vesselJson);

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

    // =====================================================================================
    @GET
    @Path("/{imo}/poins")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getPoinsVessel(@PathParam("imo") int imo,
            @QueryParam("start_date") Long startDateMilliseconds,
            @QueryParam("end_date") Long endDateMilliseconds,
            @HeaderParam("authorization") String auth) throws Exception {

        if ((endDateMilliseconds - startDateMilliseconds) > (1000 * 60 * 60 * 240)) {
            JSONObject json = new JSONObject();
            json.put("error", true);
            json.put("message", "Invalid parameters. The reported period exceeds 10 days.");
            return json.toString();
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            // Verifica se o usuário possui permissão para utilizar a api
            if (!Util.isUserAuthenticatedAPI(auth, manager)) {
                try {
                    json.put("error", true);
                    json.put("message", "User is not allowed to use api");
                } catch (JSONException ex) {
                }
                return json.toString();
            }

            Navio vessel = NavioController.getVesselByImo(manager, imo);

            // Nenhum navio encontrado
            if (vessel == null) {
                json.put("found", false);
                json.put("vessel", Util.getValueFromJson(null));

            } else {
                // Navio encontrado                
                NavioMapAis vesselMap = new NavioMapAis(vessel);

                JSONObject vesselJson = new JSONObject();
                vesselJson.put("name", Util.getValueFromJson(vessel.getNomeNavio()));
                vesselJson.put("imo", Util.getValueFromJson(vessel.getImo()));
                vesselJson.put("dimension", Util.getValueFromJson((vesselMap.getComprimentoReal() + "m X " + vesselMap.getLarguraReal() + "m")));
                vesselJson.put("type", Util.getValueFromJson(vesselMap.getCategoriaEmbarcacao()));

                List<Aispoin> listPoins = AispoinController.getListAispoinVesselByPeriod(manager,
                        vessel.getMmsi(), new Date(startDateMilliseconds),
                        new Date(endDateMilliseconds));

                JSONArray poinsJson = new JSONArray();

                if (listPoins == null || listPoins.isEmpty()) {
                    vesselJson.put("poins", Util.getValueFromJson(null));

                } else {

                    for (Aispoin aisPoin : listPoins) {
                        JSONObject poinJson = new JSONObject();
                        Poin poin = aisPoin.getCodPoin();
                        poinJson.put("id", Util.getValueFromJson(poin.getCodPoin()));
                        poinJson.put("name", Util.getValueFromJson(poin.getNome()));
                        poinJson.put("coordinates", new JSONArray(poin.getCoordenadas()));
                        poinJson.put("input_date", Util.getValueFromJson(Util.dateToString(aisPoin.getDataEntrada(), "yyyy-MM-dd HH:mm:ss")));
                        poinJson.put("output_date", Util.getValueFromJson(Util.dateToString(aisPoin.getDataSaida(), "yyyy-MM-dd HH:mm:ss")));
                        poinJson.put("input_speed", Util.getValueFromJson(aisPoin.getVelocidadeEntrada()));
                        poinJson.put("output_speed", Util.getValueFromJson(aisPoin.getVelocidadeSaida()));
                        poinsJson.put(poinJson);
                    }

                    vesselJson.put("poins", poinsJson);

                }

                json.put("found", true);
                json.put("vessel", vesselJson);

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

    // =====================================================================================
    @GET
    @Path("/image")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getImage(@QueryParam("mmsi") Integer mmsi) throws Exception {

        EntityManagerFactory factory = null;
        EntityManager manager = null;
        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            Navio vessel = NavioController.getVesselByMmsi(manager, mmsi);
            json.put("mmsi", mmsi);

            // Informações da embarcação
            if (vessel != null) {

                ImageVessel image = NavioController.getImageVesselByCod(manager, vessel.getCodNavio());

                if (image != null && image.getImagem() != null) {
                    byte[] bytes = new byte[image.getImagem().length];
                    for (int i = 0; i < image.getImagem().length; i++) {
                        bytes[i] = image.getImagem()[i];
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("data:image/png;base64,");
                    sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes, false)));
                    json.put("image", sb.toString());

                } else {
                    json.put("image", Util.getValueFromJson(null));
                }

            } else {
                json.put("image", Util.getValueFromJson(null));

            }

        } catch (Exception ex) {
            json.put("message", "Não foi possível buscar a imagem da embarcação");

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
    @Path("/track")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getTrack(@QueryParam("mmsi") Integer mmsi,
            @QueryParam("startDate") Long startDateMilliseconds,
            @QueryParam("endDate") Long endDateMilliseconds,
            @QueryParam("printMap") Integer printMap) throws Exception {

        EntityManagerFactory factory = null;
        EntityManager manager = null;
        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            Navio vessel = NavioController.getVesselByMmsi(manager, mmsi);
            NavioMapAis vesselMapAis = new NavioMapAis(vessel);

            // Informações da embarcação
            if (vessel != null) {

                ImageVessel image = NavioController.getImageVesselByCod(manager, vessel.getCodNavio());

                JSONObject jsonVessel = new JSONObject();
                jsonVessel.put("name", Util.getValueFromJson(vessel.getNomeNavio()));
                jsonVessel.put("imo", Util.getValueFromJson(vessel.getImo()));
                jsonVessel.put("dimension", Util.getValueFromJson((vesselMapAis.getComprimentoReal() + "m X " + vesselMapAis.getLarguraReal() + "m")));
                jsonVessel.put("type", Util.getValueFromJson(vesselMapAis.getCategoriaEmbarcacao()));

                if (image != null && image.getImagem() != null) {
                    byte[] bytes = new byte[image.getImagem().length];
                    for (int i = 0; i < image.getImagem().length; i++) {
                        bytes[i] = image.getImagem()[i];
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("data:image/png;base64,");
                    sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes, false)));
                    jsonVessel.put("image", sb.toString());

                } else {
                    jsonVessel.put("image", Util.getValueFromJson(null));
                }

                json.put("vessel", jsonVessel);

            } else {
                json.put("vessel", Util.getValueFromJson(null));

            }

            // Rastro da embarcação
            List<Ais> trail = AisController.getBoatTrail(manager,
                    new Date(startDateMilliseconds),
                    new Date(endDateMilliseconds), mmsi, null);

            JSONArray trailJson = new JSONArray();

            for (Ais ais : trail) {
                JSONObject obj = new JSONObject();
                obj.put("lat", ais.getLatitude());
                obj.put("lng", ais.getLongitude());
                obj.put("velocity", Util.getValueFromJson(ais.getVelocidadeSobreSolo()));
                obj.put("direction", Util.getValueFromJson(ais.getCursoSobreSolo()));
                obj.put("destination", Util.getValueFromJson(ais.getDestino()));
                obj.put("state", Util.getValueFromJson(ais.getStatusNavegacao()));
                obj.put("draught", Util.getValueFromJson(ais.getDraught()));
                obj.put("date", Util.getValueFromJson(Util.dateToString(ais.getDataHora(), "yyyy-MM-dd HH:mm:ss")));
                trailJson.put(obj);
            }

            json.put("trail", trailJson);

            // Ultimo registro do ais do rastro
            if (!trail.isEmpty()) {
                Ais ais = trail.get(0);
                JSONObject jsonLastAisRecordJson = new JSONObject();
                jsonLastAisRecordJson.put("mmsi", Util.getValueFromJson(ais.getMmsi()));
                jsonLastAisRecordJson.put("lat", Util.getValueFromJson(ais.getLatitude()));
                jsonLastAisRecordJson.put("lng", Util.getValueFromJson(ais.getLongitude()));
                jsonLastAisRecordJson.put("velocity", Util.getValueFromJson(ais.getVelocidadeSobreSolo()));
                jsonLastAisRecordJson.put("direction", Util.getValueFromJson(ais.getCursoSobreSolo()));
                jsonLastAisRecordJson.put("destination", Util.getValueFromJson(ais.getDestino()));
                jsonLastAisRecordJson.put("state", Util.getValueFromJson(ais.getStatusNavegacao()));
                jsonLastAisRecordJson.put("draught", Util.getValueFromJson(ais.getDraught()));
                jsonLastAisRecordJson.put("date", Util.getValueFromJson(Util.dateToString(ais.getDataHora(), "yyyy-MM-dd HH:mm:ss")));
                jsonLastAisRecordJson.put("message", Util.getValueFromJson("Última atualização recebida " + Util.getStringDateLastUpdate(new Date(), ais.getDataHora())));
                json.put("lastAisRecord", jsonLastAisRecordJson);

            } else {
                json.put("lastAisRecord", Util.getValueFromJson(null));

            }

            // Informações sobre a embarcação para o desenho no mapa
            if (printMap != null && printMap == 1) {
                if (!trail.isEmpty()) {
                    Ais ais = trail.get(0);
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

                    json.put("printMap", printMapJson);
                } else {
                    json.put("printMap", Util.getValueFromJson(null));

                }

            }

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

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getVessel(@QueryParam("mmsi") Integer mmsi) throws Exception {

        EntityManagerFactory factory = null;
        EntityManager manager = null;
        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            Navio vessel = NavioController.getVesselByMmsi(manager, mmsi);

            if (vessel != null) {

                ImageVessel image = NavioController.getImageVesselByCod(manager, vessel.getCodNavio());
                NavioMapAis embarcacao = new NavioMapAis(vessel);

                JSONObject jsonVessel = new JSONObject();
                jsonVessel.put("name", Util.getValueFromJson(vessel.getNomeNavio()));
                jsonVessel.put("imo", Util.getValueFromJson(vessel.getImo()));
                jsonVessel.put("dimension", Util.getValueFromJson((embarcacao.getComprimentoReal() + "m X " + embarcacao.getLarguraReal() + "m")));
                jsonVessel.put("type", Util.getValueFromJson(embarcacao.getCategoriaEmbarcacao()));

                if (image != null && image.getImagem() != null) {
                    byte[] bytes = new byte[image.getImagem().length];
                    for (int i = 0; i < image.getImagem().length; i++) {
                        bytes[i] = image.getImagem()[i];
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("data:image/png;base64,");
                    sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes, false)));
                    jsonVessel.put("image", sb.toString());

                } else {
                    jsonVessel.put("image", JSONObject.NULL);
                }

                json.put("vessel", jsonVessel);

            } else {
                json.put("vessel", Util.getValueFromJson(null));

            }

            NavioUltimaAtualizacao navioUltimaAtualizacao = NavioUltimaAtualizacaoController.getRadioLastUpdate(manager, mmsi);

            if (navioUltimaAtualizacao != null) {

                JSONObject jsonAis = new JSONObject();
                jsonAis.put("mmsi", Util.getValueFromJson(navioUltimaAtualizacao.getMmsi()));
                jsonAis.put("lat", Util.getValueFromJson(navioUltimaAtualizacao.getLatitude()));
                jsonAis.put("lng", Util.getValueFromJson(navioUltimaAtualizacao.getLongitude()));
                jsonAis.put("velocity", Util.getValueFromJson(navioUltimaAtualizacao.getVelocidadeSobreSolo()));
                jsonAis.put("direction", Util.getValueFromJson(navioUltimaAtualizacao.getCursoSobreSolo()));
                jsonAis.put("destination", Util.getValueFromJson(navioUltimaAtualizacao.getDestino()));
                jsonAis.put("state", Util.getValueFromJson(navioUltimaAtualizacao.getStatusNavegacao()));
                jsonAis.put("draught", Util.getValueFromJson(navioUltimaAtualizacao.getDraught()));
                jsonAis.put("date", Util.getValueFromJson(Util.dateToString(navioUltimaAtualizacao.getDataUltimaAtualizacao(), "yyyy-MM-dd HH:mm:ss")));
                jsonAis.put("message", Util.getValueFromJson("Última atualização recebida " + Util.getStringDateLastUpdate(new Date(), navioUltimaAtualizacao.getDataUltimaAtualizacao())));

                json.put("last_ais_record", jsonAis);

            } else {
                json.put("last_ais_record", Util.getValueFromJson(null));

            }

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
