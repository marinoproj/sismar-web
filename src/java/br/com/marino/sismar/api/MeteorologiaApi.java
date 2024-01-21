package br.com.marino.sismar.api;

import br.com.marino.sismar.controller.ClientesEquipamentosController;
import br.com.marino.sismar.controller.CorrentometroController;
import br.com.marino.sismar.controller.MeteorologiaController;
import br.com.marino.sismar.entity.ClientesEquipamentos;
import br.com.marino.sismar.entity.Correntometro;
import br.com.marino.sismar.entity.Equipamentos;
import br.com.marino.sismar.entity.Meteorologia;
import br.com.marino.sismar.enums.TipoEquipamentoEnum;
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

@Path("/meteorologia")
public class MeteorologiaApi {

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getMeteorologia(@HeaderParam("authorization") String auth) {

        UserLoggedApi userLogged = Util.getUserLoggedApi(auth);
        
        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            List<ClientesEquipamentos> listClientesEquipamentos = ClientesEquipamentosController.getListClientesEquipamentosByCodCliente(manager, userLogged.getCodClient());
                       
            JSONArray jsonMeteorologias = new JSONArray();
            
            for (ClientesEquipamentos obj : listClientesEquipamentos) {

                if (!obj.isExibirTelaMeteorologia()) {
                    continue;
                }

                JSONObject jsonMeteorologia = new JSONObject();
                
                Equipamentos equipamento = obj.getCodEquipamento();                
                
                if (equipamento.getTipo().equalsIgnoreCase(TipoEquipamentoEnum.CORRENTE.getValue())) {                    
                    Correntometro corrente = CorrentometroController
                            .getCorrentometroLastByCodEquipamento(manager, equipamento.getCodEquipamento());
                    
                    String correnteVel = getSeaCurrentSpeed(corrente);
                    String correnteDir = getSeaCurrentDir(corrente);
                    
                    jsonMeteorologia.put("codEquipamento", equipamento.getCodEquipamento());
                    jsonMeteorologia.put("tipo", equipamento.getTipo());
                    jsonMeteorologia.put("vel", correnteVel);
                    jsonMeteorologia.put("dir", correnteDir);
                    jsonMeteorologia.put("status", getSeaCurrentMessageStatus(corrente));
                    
                } else if (equipamento.getTipo().equalsIgnoreCase(TipoEquipamentoEnum.VENTO.getValue())) {
                    Meteorologia meteorologia = MeteorologiaController
                            .getLastByCodEquipamento(manager, equipamento.getCodEquipamento());
                    
                    String ventoVel = getWindVelNos(meteorologia);
                    String ventoDir = getWindDir(meteorologia);
                    
                    jsonMeteorologia.put("codEquipamento", equipamento.getCodEquipamento());
                    jsonMeteorologia.put("tipo", equipamento.getTipo());
                    jsonMeteorologia.put("vel", ventoVel);
                    jsonMeteorologia.put("dir", ventoDir);
                    jsonMeteorologia.put("status", getWindMessageStatus(meteorologia));
                    
                }                
                
                jsonMeteorologias.put(jsonMeteorologia);
                
            }                       
            
            json.put("meteorologia", jsonMeteorologias);            

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
    
    public String getWindVelNos(Meteorologia meteorologia) {
        if (meteorologia == null) {
            return "-";
        }
        return getValueFormatted(meteorologia.getVelocidadeMediaVento() / 1.852) + " nós";
    }
    
    public String getWindDir(Meteorologia meteorologia) {
        if (meteorologia == null) {
            return "-";
        }
        return getValueFormatted(meteorologia.getDirecaoMediaVento()) + " º";
    }
    
    public String getSeaCurrentSpeed(Correntometro corrente) {
        if (corrente == null) {
            return "-";
        }
        return getValueFormatted(Util.getSpeedCorrentometroByLevel(corrente)) + " nós";
    }
    
    public String getSeaCurrentDir(Correntometro corrente) {
        if (corrente == null) {
            return "-";
        }
        return getValueFormatted(Util.getDirecaoCorrentometroByLevel(corrente)) + " º";
    }
    
    public String getValueFormatted(Double value) {
        if (value == null) {
            return "";
        }
        return Util.converterValue(value);
    }
    
    public String getSeaCurrentMessageStatus(Correntometro corrente) {
        if (corrente == null) {
            return "-";
        }
        if (!Util.isOnlineInfo(corrente.getDataHora(),Util.TMP_SECONDS_ONLINE_SEACURRENT)) {
            return "offline";
        }
        return "online";
    }
    
    public String getWindMessageStatus(Meteorologia meteorologia) {
        if (meteorologia == null) {
            return "-";
        }
        if (!Util.isOnlineInfo(meteorologia.getDataHora(),
                Util.TMP_SECONDS_ONLINE_WIND)) {
            return "offline";
        }
        return "online";
    }
    
}
