package br.com.marino.sismar.bean.admin.meteorology;

import br.com.marino.sismar.controller.ClientesEquipamentosController;
import br.com.marino.sismar.controller.MeteorologiaController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.ClientesEquipamentos;
import br.com.marino.sismar.entity.Equipamentos;
import br.com.marino.sismar.entity.Meteorologia;
import br.com.marino.sismar.enums.TipoEquipamentoEnum;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "MeteorologyWindBean")
@ViewScoped
public class MeteorologyWindBean implements Serializable {

    private List<Equipamentos> equipamentosVento;

    private Equipamentos equipmentSelected;
    private String periodSelected;
    private String tmpUpdateSelected;
    
    private boolean pollingEnabled;
    private int delay; // segundos

    private Exception exGeral;
    private Exception exVento;
    private Exception exCorrente;

    @PostConstruct
    public void init() {
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        equipamentosVento = null;
        exGeral = null;
        exVento = null;
        exCorrente = null;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }    

    public boolean isPollingEnabled() {
        return pollingEnabled;
    }

    public void setPollingEnabled(boolean pollingEnabled) {
        this.pollingEnabled = pollingEnabled;
    }
    
    public String getTmpUpdateSelected() {
        return tmpUpdateSelected;
    }

    public void setTmpUpdateSelected(String tmpUpdateSelected) {
        this.tmpUpdateSelected = tmpUpdateSelected;
    }

    public String getPeriodSelected() {
        return periodSelected;
    }

    public void setPeriodSelected(String periodSelected) {
        this.periodSelected = periodSelected;
    }

    public List<Equipamentos> getEquipamentosVento() {
        return equipamentosVento;
    }

    public void setEquipamentosVento(List<Equipamentos> equipamentosVento) {
        this.equipamentosVento = equipamentosVento;
    }

    public Equipamentos getEquipmentSelected() {
        return equipmentSelected;
    }

    public void setEquipmentSelected(Equipamentos equipmentSelected) {
        this.equipmentSelected = equipmentSelected;
    }

    public Exception getExGeral() {
        return exGeral;
    }

    public void setExGeral(Exception exGeral) {
        this.exGeral = exGeral;
    }

    public Exception getExVento() {
        return exVento;
    }

    public void setExVento(Exception exVento) {
        this.exVento = exVento;
    }

    public Exception getExCorrente() {
        return exCorrente;
    }

    public void setExCorrente(Exception exCorrente) {
        this.exCorrente = exCorrente;
    }

    public String getDataHora(Date date, String formatRegex) {
        if (date == null) {
            return "";
        }
        return Util.dateToString(date, formatRegex);
    }

    public boolean isExceptionGeral() {
        return exGeral != null;
    }

    public boolean isExceptionVento() {
        return exVento != null;
    }

    public boolean isExceptionCorrente() {
        return exCorrente != null;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            Clientes cliente = SessionContext.getInstance().getClientLoggedIn();

            List<ClientesEquipamentos> listClientesEquipamentos = ClientesEquipamentosController.getListClientesEquipamentosByCodCliente(manager, cliente.getCod());

            equipamentosVento = new ArrayList<>();

            for (ClientesEquipamentos obj : listClientesEquipamentos) {
                if (!obj.isExibirTelaMeteorologia()) {
                    continue;
                }
                Equipamentos eq = obj.getCodEquipamento();
                if (eq.getTipo().equalsIgnoreCase(TipoEquipamentoEnum.VENTO.getValue())) {
                    equipamentosVento.add(eq);
                }
            }

            if (equipamentosVento.size() > 0) {
                equipmentSelected = equipamentosVento.get(0);
            }

            periodSelected = "1";
            tmpUpdateSelected = "1";

            pollingEnabled = false;
            delay = 0;
            
            reload(manager, equipmentSelected, periodSelected, tmpUpdateSelected);

        } catch (Exception ex) {
            exGeral = ex;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void reload(EntityManager manager, Equipamentos equipmentSelected, String periodSelected, String tmpUpdateSelected) {

        EntityManagerFactory factory = null;
        EntityManager managerCurrent = manager;

        try {
            
            System.out.println("inicio do reload");
            
            if (managerCurrent == null) {
                factory = Persistence.createEntityManagerFactory("sismarPU");
                managerCurrent = factory.createEntityManager();
            }

            this.equipmentSelected = equipmentSelected;
            this.periodSelected = periodSelected;
            this.tmpUpdateSelected = tmpUpdateSelected;
            
            if (tmpUpdateSelected.equalsIgnoreCase("1")){
                pollingEnabled = false;
                delay = 0;
            }
            
            Meteorologia meteorologia = MeteorologiaController.getLastByCodEquipamento(managerCurrent, equipmentSelected.getCodEquipamento());

            Float windSpeed = Util.round(meteorologia.getVelocidadeMediaVento() / 1.852, 2);
            Float windGust = Util.round(meteorologia.getVelocidadeMaximaVento() / 1.852, 2);
            Float windDir = Util.round(meteorologia.getDirecaoMediaVento(), 1);

            PrimeFaces.current().executeScript("gaugeWindSpeed.setValue(" + windSpeed + ");");
            PrimeFaces.current().executeScript("gaugeWindRajada.setValue(" + windGust + ");");
            PrimeFaces.current().executeScript("gaugeWindRose.setValue(" + windDir + ");");

            Date now = new Date();
            Date start = getStartDate(now, periodSelected);

            List<Meteorologia> list = MeteorologiaController
                    .getListByCodEquipamentoAndPeriod(managerCurrent, equipmentSelected.getCodEquipamento(), start, now);

            PrimeFaces.current().executeScript("chartWindSpeedAndGust.clear(); chartWindSpeedAndGust.addValues(" + getDataChartWindSpeedAndGust(list) + ");");

            Float maxWindGust = Util.round(getMaxWindGust(list), 2);
            Float minWindSpeed = Util.round(getMinWindSpeed(list), 2);
            Float medWindSpeed = Util.round(getMedWindSpeed(list), 2);
            
            PrimeFaces.current().executeScript("updateMaxWindGust(" + maxWindGust + ");");
            PrimeFaces.current().executeScript("updateMinWindSpeed(" + minWindSpeed + ");");
            PrimeFaces.current().executeScript("updateMedWindSpeed(" + medWindSpeed + ");");
            
            PrimeFaces.current().executeScript("gaugeWindDirectionFrequency.clear(); gaugeWindDirectionFrequency.addValues(" + getDataChartWindDirectionFrequency(list) + ");");                        
            
            if (tmpUpdateSelected.equalsIgnoreCase("2")){
                pollingEnabled = true;
                delay = 10;
            }
            
            if (tmpUpdateSelected.equalsIgnoreCase("3")){
                pollingEnabled = true;
                delay = 30;
            }
            
            if (tmpUpdateSelected.equalsIgnoreCase("4")){
                pollingEnabled = true;
                delay = 60;
            }            
            
            System.out.println("fim do reload");
            
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            if (manager == null && managerCurrent != null) {
                managerCurrent.close();
                if (factory != null) {
                    factory.close();
                }
            }
        }

    }

    public Date getStartDate(Date now, String periodSelected) {

        Date start = null;

        // última hora
        if (periodSelected.equalsIgnoreCase("1")) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(now);
            startCalendar.add(Calendar.HOUR_OF_DAY, -1);
            start = startCalendar.getTime();
        }

        // últimas 12 horas
        if (periodSelected.equalsIgnoreCase("2")) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(now);
            startCalendar.add(Calendar.HOUR_OF_DAY, -12);
            start = startCalendar.getTime();
        }

        // últimas 24 horas
        if (periodSelected.equalsIgnoreCase("3")) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(now);
            startCalendar.add(Calendar.HOUR_OF_DAY, -24);
            start = startCalendar.getTime();
        }

        // últimas 7 dias
        if (periodSelected.equalsIgnoreCase("4")) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(now);
            startCalendar.add(Calendar.DAY_OF_MONTH, -7);
            start = startCalendar.getTime();
        }

        // últimas 30 dias
        if (periodSelected.equalsIgnoreCase("5")) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(now);
            startCalendar.add(Calendar.DAY_OF_MONTH, -30);
            start = startCalendar.getTime();
        }

        return start;

    }

    public String getDataChartWindSpeedAndGust(List<Meteorologia> list) {
        JSONArray chartWindSpeedAndGust = new JSONArray();

        for (Meteorologia marcacao : list) {
            Float windSpeedMarcacao = Util.round(marcacao.getVelocidadeMediaVento() / 1.852, 2);
            Float windGustMarcacao = Util.round(marcacao.getVelocidadeMaximaVento() / 1.852, 2);

            JSONObject obj = new JSONObject();
            obj.put("timestamp", marcacao.getDataHora().getTime());
            obj.put("windSpeed", windSpeedMarcacao);
            obj.put("gustSpeed", windGustMarcacao);

            chartWindSpeedAndGust.put(obj);
        }

        return chartWindSpeedAndGust.toString();
    }

    public Double getMaxWindGust(List<Meteorologia> list) {
        if (list.isEmpty()){
            return 0.0;
        }
        double max = 0;
        for (Meteorologia marcacao : list) {
            if (marcacao.getVelocidadeMaximaVento() > max) {
                max = marcacao.getVelocidadeMaximaVento();
            }
        }
        return max / 1.852;
    }

    public Double getMinWindSpeed(List<Meteorologia> list) {
        if (list.isEmpty()) {
            return 0.0;
        }
        double min = Double.MAX_VALUE;
        for (Meteorologia marcacao : list) {
            if (marcacao.getVelocidadeMediaVento() < min) {
                min = marcacao.getVelocidadeMediaVento();
            }
        }
        return min / 1.852;
    }
    
    public Double getMedWindSpeed(List<Meteorologia> list) {
        if (list.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Meteorologia marcacao : list) {
            sum += marcacao.getVelocidadeMediaVento();
        }
        double med = sum / ((double) list.size());
        return med / 1.852;
    }
    
    public String getDataChartWindDirectionFrequency(List<Meteorologia> list) {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        
        Map<String, Integer> directionOcorrencies = new HashMap<>();       
        for(String direction: directions){
            directionOcorrencies.put(direction, 0);
        }
        
        for (Meteorologia marcacao : list) {                      
            
            int index = (int) Math.floor(((marcacao.getDirecaoMediaVento() + 22.5) % 360) / 45);

            String direction = directions[index];
            Integer ocorrecies = directionOcorrencies.get(direction) + 1;           

            directionOcorrencies.put(direction, ocorrecies);
            
        }

        JSONObject obj = new JSONObject();
        
        for(String direction: directions){            
            obj.put(direction, directionOcorrencies.get(direction));                        
        }        

        return obj.toString();
    }

}
