package br.com.marino.sismar.bean.admin.meteorology;

import br.com.marino.sismar.controller.ClientesEquipamentosController;
import br.com.marino.sismar.controller.CorrentometroController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.ClientesEquipamentos;
import br.com.marino.sismar.entity.Correntometro;
import br.com.marino.sismar.entity.Equipamentos;
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

@ManagedBean(name = "MeteorologySeacurrentBean")
@ViewScoped
public class MeteorologySeacurrentBean implements Serializable {

    private List<Equipamentos> equipments;

    private Equipamentos equipmentSelected;
    private String periodSelected;
    private String tmpUpdateSelected;
    
    private boolean pollingEnabled;
    private int delay; // segundos

    @PostConstruct
    public void init() {
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        equipments = null;
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

    public List<Equipamentos> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipamentos> equipments) {
        this.equipments = equipments;
    }

    public Equipamentos getEquipmentSelected() {
        return equipmentSelected;
    }

    public void setEquipmentSelected(Equipamentos equipmentSelected) {
        this.equipmentSelected = equipmentSelected;
    }    

    public String getDataHora(Date date, String formatRegex) {
        if (date == null) {
            return "";
        }
        return Util.dateToString(date, formatRegex);
    }    

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            Clientes cliente = SessionContext.getInstance().getClientLoggedIn();

            List<ClientesEquipamentos> listClientesEquipamentos = ClientesEquipamentosController.getListClientesEquipamentosByCodCliente(manager, cliente.getCod());

            equipments = new ArrayList<>();

            for (ClientesEquipamentos obj : listClientesEquipamentos) {
                if (!obj.isExibirTelaMeteorologia()) {
                    continue;
                }
                Equipamentos eq = obj.getCodEquipamento();                
                if (eq.getTipo().equalsIgnoreCase(TipoEquipamentoEnum.CORRENTE.getValue())) {
                    equipments.add(eq);
                }
            }

            if (equipments.size() > 0) {
                equipmentSelected = equipments.get(0);
            }

            periodSelected = "1";
            tmpUpdateSelected = "1";

            pollingEnabled = false;
            delay = 0;
            
            reload(manager, equipmentSelected, periodSelected, tmpUpdateSelected);

        } catch (Exception ex) {
            ex.printStackTrace();

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
            
            Correntometro corrente = CorrentometroController
                            .getCorrentometroLastByCodEquipamento(managerCurrent, equipmentSelected.getCodEquipamento());
            
            Float seaCurrentSpeed = Util.round(Util.getSpeedCorrentometroByLevel(corrente), 1);
            Float seaCurrentDir = Util.round(Util.getDirecaoCorrentometroByLevel(corrente), 1);

            PrimeFaces.current().executeScript("gaugeSeacurrentSpeed.setValue(" + seaCurrentSpeed + ");");
            PrimeFaces.current().executeScript("gaugeSeacurrentRose.setValue(" + seaCurrentDir + ");");

            Date now = new Date();
            Date start = getStartDate(now, periodSelected);

            List<Correntometro> list = CorrentometroController
                    .getListByCodEquipamentoAndPeriod(managerCurrent, equipmentSelected.getCodEquipamento(), start, now);

            PrimeFaces.current().executeScript("chartSeacurrentSpeed.clear(); chartSeacurrentSpeed.addValues(" + getDataChartSeacurrentSpeed(list) + ");");

            Float maxSeacurrentSpeed = Util.round(getMaxSeacurrentSpeed(list), 2);
            Float medSeacurrentSpeed = Util.round(getMedSeacurrentSpeed(list), 2);
            Float minSeacurrentSpeed = Util.round(getMinSeacurrentSpeed(list), 2);
                        
            PrimeFaces.current().executeScript("updateMaxSeacurrentSpeed(" + maxSeacurrentSpeed + ");");
            PrimeFaces.current().executeScript("updateMedSeacurrentSpeed(" + medSeacurrentSpeed + ");");
            PrimeFaces.current().executeScript("updateMinSeacurrentSpeed(" + minSeacurrentSpeed + ");");            
            
            PrimeFaces.current().executeScript("gaugeSeacurrentDirectionFrequency.clear(); gaugeSeacurrentDirectionFrequency.addValues(" + getDataChartSeacurrentDirectionFrequency(list) + ");");                        
            
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

    public String getDataChartSeacurrentSpeed(List<Correntometro> list) {
        JSONArray chartWindSpeedAndGust = new JSONArray();

        for (Correntometro marcacao : list) {
            Float speed = Util.round(Util.getSpeedCorrentometroByLevel(marcacao), 2);

            JSONObject obj = new JSONObject();
            obj.put("timestamp", marcacao.getDataHora().getTime());
            obj.put("speed", speed);

            chartWindSpeedAndGust.put(obj);
        }

        return chartWindSpeedAndGust.toString();
    }

    public Double getMaxSeacurrentSpeed(List<Correntometro> list) {
        if (list.isEmpty()){
            return 0.0;
        }
        double max = 0;
        for (Correntometro marcacao : list) {
            Double speed = Util.getSpeedCorrentometroByLevel(marcacao);
            if (speed > max) {
                max = speed;
            }
        }
        return max;
    }

    public Double getMinSeacurrentSpeed(List<Correntometro> list) {
        if (list.isEmpty()) {
            return 0.0;
        }
        double min = Double.MAX_VALUE;
        for (Correntometro marcacao : list) {
            Double speed = Util.getSpeedCorrentometroByLevel(marcacao);
            if (speed < min) {
                min = speed;
            }
        }
        return min;
    }
    
    public Double getMedSeacurrentSpeed(List<Correntometro> list) {
        if (list.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Correntometro marcacao : list) {
            Double speed = Util.getSpeedCorrentometroByLevel(marcacao);
            sum += speed;
        }
        double med = sum / ((double) list.size());
        return med;
    }
    
    public String getDataChartSeacurrentDirectionFrequency(List<Correntometro> list) {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        
        Map<String, Integer> directionOcorrencies = new HashMap<>();       
        for(String direction: directions){
            directionOcorrencies.put(direction, 0);
        }
        
        for (Correntometro marcacao : list) {                      
            
            Double dir = Util.getDirecaoCorrentometroByLevel(marcacao);
            
            int index = (int) Math.floor(((dir + 22.5) % 360) / 45);

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
