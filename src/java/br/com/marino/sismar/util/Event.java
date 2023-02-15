package br.com.marino.sismar.util;

import br.com.marino.sismar.controller.NavioController;
import br.com.marino.sismar.entity.Ais;
import br.com.marino.sismar.entity.Aispoin;
import br.com.marino.sismar.entity.Monitorar;
import br.com.marino.sismar.entity.MonitorarRegra;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.Poin;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class Event {

    private Date datetime;
    private Poin poin;
    private int variable;
    private Monitorar variableMonitor;
    private MonitorarRegra rule;
    private Navio vessel;
    private int mmsi;
    private String message;

    private Event(Date datetime, Poin poin, int variable, Monitorar variableMonitor, MonitorarRegra rule, Navio vessel, String message, int mmsi) {
        this.datetime = datetime;
        this.poin = poin;
        this.variable = variable;
        this.variableMonitor = variableMonitor;
        this.rule = rule;
        this.vessel = vessel;
        this.message = message;
        this.mmsi = mmsi;
    }

    public static List<Event> getListEventsByArrivalDate(EntityManager manager,
            List<Aispoin> listAispoins, Monitorar variableMonitor) {

        List<Event> events = new ArrayList<>();

        if (!variableMonitor.variableIsArrivalDate() || listAispoins == null || listAispoins.isEmpty()) {
            return events;
        }

        for (Aispoin aispoin : listAispoins) {

            Navio vessel = null;

            try {
                vessel = NavioController.getVesselByMmsi2(manager, aispoin.getMmsi());
            } catch (Exception ex) {
            }

            String message = Util.getMessageByArrivalDate(variableMonitor, vessel, aispoin);
            Event event = new Event(aispoin.getDataEntrada(), aispoin.getCodPoin(),
                    variableMonitor.getVariavel(), variableMonitor, null, vessel, message, aispoin.getMmsi());
            events.add(event);

        }

        Util.orderEventsAsc(events);

        return events;

    }

    public static List<Event> getListEventsByDepartureDate(EntityManager manager,
            List<Aispoin> listAispoins, Monitorar variableMonitor) {

        List<Event> events = new ArrayList<>();

        if (!variableMonitor.variableIsExitDate() || listAispoins == null || listAispoins.isEmpty()) {
            return events;
        }

        for (Aispoin aispoin : listAispoins) {

            Navio vessel = null;

            try {
                vessel = NavioController.getVesselByMmsi2(manager, aispoin.getMmsi());
            } catch (Exception ex) {
            }

            String message = Util.getMessageByDepartureDate(variableMonitor, vessel, aispoin);
            Event event = new Event(aispoin.getDataSaida(), aispoin.getCodPoin(),
                    variableMonitor.getVariavel(), variableMonitor, null, vessel, message, aispoin.getMmsi());
            events.add(event);

        }

        Util.orderEventsAsc(events);

        return events;

    }

    public static List<Event> getListEventsByPermanenceTime(EntityManager manager,
            List<Aispoin> listAispoins, Monitorar variableMonitor, Date end) {

        List<Event> events = new ArrayList<>();

        if (!variableMonitor.variableIsPermanenceTime()) {
            return events;
        }

        if (variableMonitor.getMonitorarRegraList() == null || variableMonitor.getMonitorarRegraList().isEmpty()) {
            return events;
        }

        for (Aispoin ap : listAispoins) {

            int seconds = Util.getRangeSeconds(ap.getDataEntrada(), end);

            MonitorarRegra rule = Util.getRuleByCheckState((seconds / 3600), variableMonitor.getMonitorarRegraList());

            if (rule == null) {
                continue;
            }

            double diffSecondsExceeded = seconds - (rule.getValor() * 3600);

            Calendar datetime = Calendar.getInstance();
            datetime.setTime(end);
            datetime.add(Calendar.SECOND, (int) (diffSecondsExceeded * -1));

            Navio vessel = null;

            try {
                vessel = NavioController.getVesselByMmsi2(manager, ap.getMmsi());
            } catch (Exception ex) {
            }

            String message = Util.getMessageByPermanenceTime(variableMonitor, vessel, ap, rule, (seconds / 3600));

            Event event = new Event(datetime.getTime(), ap.getCodPoin(),
                    variableMonitor.getVariavel(), variableMonitor, rule, vessel, message, ap.getMmsi());

            events.add(event);

        }

        Util.orderEventsAsc(events);

        return events;

    }

    public static List<Event> getListEventsByNavigationSpeed(EntityManager manager,
            List<Ais> aisList, Monitorar variableMonitor) {

        List<Event> events = new ArrayList<>();

        if (!variableMonitor.variableIsNavigationSpeed() || variableMonitor.getMonitorarRegraList().isEmpty()) {
            return events;
        }

        for (Ais ais : aisList) {            
            
            MonitorarRegra rule = Util.getRuleByCheckState(ais.getVelocidadeSobreSolo(), variableMonitor.getMonitorarRegraList());

            if (rule == null) {
                continue;
            }            
            
            List<Poin> poins;

            try {
                poins = Util.getPoinsActiveByAis(ais, variableMonitor.getPoinList());
            } catch (Exception ex) {
                continue;
            }

            if (poins.isEmpty()) {
                continue;
            }
            
            Navio vessel = null;

            try {
                vessel = NavioController.getVesselByMmsi2(manager, ais.getMmsi());
            } catch (Exception ex) {
            }

            for (Poin poin : poins) {
                
                String message = Util.getMessageByNavigationSpeed(variableMonitor, vessel, ais, poin, rule, ais.getVelocidadeSobreSolo());
                Event event = new Event(ais.getDataHora(), poin,
                        variableMonitor.getVariavel(), variableMonitor, rule, vessel, message, ais.getMmsi());
            
                events.add(event);
                
            }

        }

        Util.orderEventsAsc(events);
        
        return events;               

    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Poin getPoin() {
        return poin;
    }

    public void setPoin(Poin poin) {
        this.poin = poin;
    }

    public int getVariable() {
        return variable;
    }

    public void setVariable(int variable) {
        this.variable = variable;
    }

    public Navio getVessel() {
        return vessel;
    }

    public void setVessel(Navio vessel) {
        this.vessel = vessel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean variableIsArrivalDate() {
        return variable == Monitorar.ARRIVAL_DATE;
    }

    public boolean variableIsExitDate() {
        return variable == Monitorar.DEPARTURE_DATE;
    }

    public boolean variableIsPermanenceTime() {
        return variable == Monitorar.PERMANENCE_TIME;
    }

    public boolean variableIsNavigationSpeed() {
        return variable == Monitorar.NAVIGATION_SPEED;
    }

    public MonitorarRegra getRule() {
        return rule;
    }

    public void setRule(MonitorarRegra rule) {
        this.rule = rule;
    }

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
    }

    public Monitorar getVariableMonitor() {
        return variableMonitor;
    }

    public void setVariableMonitor(Monitorar variableMonitor) {
        this.variableMonitor = variableMonitor;
    }    

    @Override
    public String toString() {
        return "Event{" + "datetime=" + Util.dateToString(datetime, "dd/MM/yyyy HH:mm:ss") + ", poin=" + poin.getNome() + ", variable=" + variable + ", rule=" + (rule == null ? "null" : rule.getNome()) + ", vessel=" + (vessel == null ? "null" : vessel.getNomeNavio()) + ", mmsi=" + mmsi + ", message=" + message + '}';
    }

    public String getTypeVessel() {
        if (vessel == null) {
            return " ";
        }
        NavioMapAis vesselMap = new NavioMapAis(vessel);
        return vesselMap.getCategoriaEmbarcacao();
    }    
    
}
