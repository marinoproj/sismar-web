package br.com.marino.sismar.api;

import br.com.marino.sismar.controller.AispoinController;
import br.com.marino.sismar.controller.LayerController;
import br.com.marino.sismar.controller.NavioController;
import br.com.marino.sismar.entity.Aispoin;
import br.com.marino.sismar.entity.EmbarcacaoCodesp;
import br.com.marino.sismar.entity.Layer;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.Poin;
import br.com.marino.sismar.entity.VesselFila;
import br.com.marino.sismar.util.Util;
import java.util.ArrayList;
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

@Path("/fila")
public class FilaApi {

    @GET
    @Path("/vessels")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getListVesselsFila(@HeaderParam("authorization") String auth) {

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

            Layer layer = LayerController.getLayerByCodigo(manager, Layer.LAYER_AREA_FUNDEIO);

            List<VesselFila> listVesselFila = new ArrayList<>();

            for (Poin poin : layer.getPoinList()) {

                List<Aispoin> listAispoinByCodPoin = AispoinController.
                        getListAispoinsNowByIdPoin(manager, poin.getCodPoin());

                if (listAispoinByCodPoin == null) {
                    continue;
                }

                for (Aispoin aisPoin : listAispoinByCodPoin) {

                    if (Util.vesselInFila(listVesselFila, aisPoin.getMmsi())) {
                        continue;
                    }

                    Navio vessel = NavioController.getVesselByMmsi(manager, aisPoin.getMmsi());

                    VesselFila vesselFila = new VesselFila(aisPoin.getMmsi(), aisPoin, vessel);
                    listVesselFila.add(vesselFila);

                }

            }

            // coleta os dados do site da codesp
            List<EmbarcacaoCodesp> listaEmbarcacaoCodesps = EmbarcacaoCodesp.getDataCodesp();
            for (VesselFila vesselFila : listVesselFila) {
                if (vesselFila.getNameVessel() == null) {
                    continue;
                }
                for (EmbarcacaoCodesp embarcacaoCodesp : listaEmbarcacaoCodesps) {
                    if (vesselFila.getNameVessel().trim().equalsIgnoreCase(embarcacaoCodesp.getNomeEmbarcacao().trim())) {
                        vesselFila.setDadosCodesp(embarcacaoCodesp);
                        break;
                    }
                }
            }

            // ordena por ordem de chegada
            Util.sortFilaArrivalDate(listVesselFila);

            // ordena baseados nos dados da codesp
            Util.sortReleasedCodesp(listVesselFila);

            JSONArray jsonArrayVesselsFila = new JSONArray();

            for (int i = 0; i < listVesselFila.size(); i++) {
                VesselFila vesselFila = listVesselFila.get(i);
                jsonArrayVesselsFila.put(vesselFila.toJSONObject((i + 1)));
            }

            json.put("results", listVesselFila.size());
            json.put("fila", jsonArrayVesselsFila);

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
