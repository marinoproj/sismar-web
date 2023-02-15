package br.com.marino.sismar.bean.admin;

import br.com.marino.sismar.controller.AisController;
import br.com.marino.sismar.controller.AispoinController;
import br.com.marino.sismar.controller.MonitorarController;
import br.com.marino.sismar.controller.PoinController;
import br.com.marino.sismar.controller.UsuariosWebController;
import br.com.marino.sismar.entity.Ais;
import br.com.marino.sismar.entity.Aispoin;
import br.com.marino.sismar.entity.EventReport;
import br.com.marino.sismar.entity.Monitorar;
import br.com.marino.sismar.entity.MonitorarRegra;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.Poin;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Event;
import br.com.marino.sismar.util.Report;
import br.com.marino.sismar.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "EventsBean")
@ViewScoped
public class EventsBean implements Serializable {

    private List<Monitorar> listVariablesMonitors;
    private List<Poin> listPoin;

    private List<Event> events;
    private List<Event> filteredEvents;

    private String startDateHistoryEvents;
    private String endDateHistoryEvents;

    private String variableSelected;

    private Date dateStart;
    private Date dateEnd;

    private int interval;

    @PostConstruct
    public void init() {

        interval = 120; // 120 segundos - 2m

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MINUTE, -5);  // data atual - 1h

        dateStart = start.getTime();
        dateEnd = new Date(); // data autal

        events = new ArrayList<>();

        reloadLists();
        //search(events, dateStart, dateEnd);

    }

    public List<Event> getListEventsArrivalDate(EntityManager manager,
            Monitorar variableMonitor, Date start, Date end) throws Exception {
        List<Event> eventsArrivalDate = new ArrayList<>();
        for (Poin poin : variableMonitor.getPoinList()) {
            List<Aispoin> aisPoin = AispoinController.getListAispoinVesselArrivalDateByPeriod(manager,
                    poin.getCodPoin(),
                    start,
                    end);
            List<Event> newEvents = Event.getListEventsByArrivalDate(manager, aisPoin, variableMonitor);
            eventsArrivalDate.addAll(newEvents);
        }
        return eventsArrivalDate;
    }

    public List<Event> getListEventsDepartureDate(EntityManager manager,
            Monitorar variableMonitor, Date start, Date end) throws Exception {
        List<Event> eventsDepartureDate = new ArrayList<>();
        for (Poin poin : variableMonitor.getPoinList()) {
            List<Aispoin> aisPoin = AispoinController.getListAispoinVesselDepartureDateByPeriod(manager,
                    poin.getCodPoin(),
                    start,
                    end);
            List<Event> newEvents = Event.getListEventsByDepartureDate(manager, aisPoin, variableMonitor);
            eventsDepartureDate.addAll(newEvents);
        }
        return eventsDepartureDate;
    }

    public List<Event> getListEventsPermanenceTime(EntityManager manager,
            Monitorar variableMonitor, Date end) throws Exception {
        List<Event> eventsPermanenceTime = new ArrayList<>();
        if (variableMonitor.getMonitorarRegraList() == null || variableMonitor.getMonitorarRegraList().isEmpty()) {
            return eventsPermanenceTime;
        }
        for (Poin poin : variableMonitor.getPoinList()) {
            List<Aispoin> aisPoins = AispoinController.getListAispoinsOpenByEvent(manager, poin.getCodPoin(), end);
            List<Event> newEvents = Event.getListEventsByPermanenceTime(manager, aisPoins, variableMonitor, end);
            eventsPermanenceTime.addAll(newEvents);
        }
        return eventsPermanenceTime;
    }

    public List<Event> getListEventsNavigationSpeed(EntityManager manager,
            Monitorar variableMonitor, Date start, Date end) throws Exception {
        if (!variableMonitor.variableIsNavigationSpeed() || variableMonitor.getMonitorarRegraList().isEmpty()) {
            return new ArrayList<>();
        }
        List<Ais> aisList = AisController.getAisHistory(manager, start, end);
        List<Event> eventsNavigationSpeed = Event.getListEventsByNavigationSpeed(manager, aisList, variableMonitor);
        return eventsNavigationSpeed;
    }

    public void search(List<Event> eventsHistory, Date startSearch, Date endSearch) {

        Date dateStartRef = startSearch;
        Date dateEndRef;

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        //List<Event> eventsArrivalDate = new ArrayList<>();
        //List<Event> eventsDepartureDate = new ArrayList<>();
        //List<Event> eventsPermanenceTime = new ArrayList<>();
        //List<Event> eventsNavigationSpeed = new ArrayList<>();
        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            // *** search dataEntrada ***
            Monitorar variableMonitorArrivalDate = Util.getVariableMonitorByType(Monitorar.ARRIVAL_DATE, listVariablesMonitors);
            if (variableMonitorArrivalDate != null) {
                eventsHistory.addAll(getListEventsArrivalDate(manager, variableMonitorArrivalDate, dateStartRef, endSearch));
            }
            // *** search dataSaida ***
            Monitorar variableMonitorDepartureDate = Util.getVariableMonitorByType(Monitorar.DEPARTURE_DATE, listVariablesMonitors);
            if (variableMonitorDepartureDate != null) {
                eventsHistory.addAll(getListEventsDepartureDate(manager, variableMonitorDepartureDate, dateStartRef, endSearch));
            }

            Util.orderEventsAsc(eventsHistory);

            // *** search velocidade de navegação *** //
            Monitorar variableNavigationSpeed = Util.getVariableMonitorByType(Monitorar.NAVIGATION_SPEED, listVariablesMonitors);
            if (variableNavigationSpeed != null) {
                List<Event> eventsNavigationSpeedIt = getListEventsNavigationSpeed(manager, variableNavigationSpeed, dateStartRef, endSearch);
                for (int j = 0; j < eventsNavigationSpeedIt.size(); j++) {
                    Event eventNew = eventsNavigationSpeedIt.get(j);
                    boolean flag = true;
                    for (int i = eventsHistory.size() - 1; i >= 0; i--) {
                        Event eventOld = eventsHistory.get(i);
                        if (eventOld.getMmsi() == eventNew.getMmsi() && Objects.equals(eventOld.getPoin().getCodPoin(), eventNew.getPoin().getCodPoin())) {
                            if (Objects.equals(eventOld.getRule().getCodMonitorarRegra(), eventNew.getRule().getCodMonitorarRegra())) {
                                flag = false;
                            }
                            break;
                        }
                    }
                    if (flag) {
                        eventsHistory.add(eventNew);
                    }
                }
            }

            Util.orderEventsAsc(eventsHistory);

            do {

                // append data final
                Calendar date = Calendar.getInstance();
                date.setTime(dateStartRef);
                date.add(Calendar.SECOND, interval);
                dateEndRef = date.getTime();
                if (dateEndRef.after(endSearch)) {
                    dateEndRef = endSearch;
                }

                // faz a pesquisa
                // *** search tempo de permanencia ***
                Monitorar variableMonitorPermanenceTime = Util.getVariableMonitorByType(Monitorar.PERMANENCE_TIME, listVariablesMonitors);
                if (variableMonitorPermanenceTime != null) {
                    List<Event> eventsPermanenceTimeIt = getListEventsPermanenceTime(manager, variableMonitorPermanenceTime, dateEndRef);
                    Iterator<Event> it = eventsPermanenceTimeIt.iterator();
                    while (it.hasNext()) {
                        Event obj = it.next();
                        for (int i = eventsHistory.size() - 1; i >= 0; i--) {
                            Event event = eventsHistory.get(i);
                            if (!event.variableIsPermanenceTime()){
                                continue;
                            }
                            if (event.getMmsi() == obj.getMmsi() && Objects.equals(event.getPoin().getCodPoin(), obj.getPoin().getCodPoin())) {
                                if (Objects.equals(event.getRule().getCodMonitorarRegra(), obj.getRule().getCodMonitorarRegra())) {
                                    it.remove();
                                }
                                break;
                            }
                        }
                    }
                    eventsHistory.addAll(eventsPermanenceTimeIt);
                }                

                // troca            
                dateStartRef = dateEndRef;

            } while (dateEndRef.before(endSearch));

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
        
        Util.orderEventsAsc(eventsHistory);

    }

//    public void addTime(int seconds) {
//
//        if (dateStart == null || dateEnd == null) {
//            dateStart = new Date();
//            dateEnd = new Date();
//        }
//
//        Calendar start = Calendar.getInstance();
//        start.setTime(dateEnd);
//
//        Calendar end = Calendar.getInstance();
//        end.setTime(dateEnd);
//        end.add(Calendar.SECOND, seconds);
//
//        dateStart = start.getTime();
//        dateEnd = end.getTime();
//
//        System.out.println("Inicio: " + Util.dateToString(dateStart, "dd/MM/yyyy HH:mm:ss") + " / Fim: " + Util.dateToString(dateEnd, "dd/MM/yyyy HH:mm:ss"));
//
//    }
    /**
     * Atualiza todas as listas
     */
    public void reloadLists() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            listVariablesMonitors = UsuariosWebController.getUserById(manager,
                    SessionContext.getInstance().getUserLoggedIn().getIdUsuario())
                    .getMonitorarList();
            if (listVariablesMonitors == null){
                listVariablesMonitors = new ArrayList<>();
            }
            listPoin = PoinController.getListPoins(manager);

        } catch (Exception ex) {
            listVariablesMonitors = new ArrayList<>();
            listPoin = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    /**
     * Seleciona o tipo de variável
     *
     * @param variableMonitor
     */
    public void selectVariableType(Monitorar variableMonitor) {

        if (variableMonitor.getMonitorarRegraList() == null) {
            variableMonitor.setMonitorarRegraList(new ArrayList<>());
        }

        if (variableMonitor.getMonitorarRegraList().isEmpty()) {

            switch (variableMonitor.getVariavel()) {
                case Monitorar.PERMANENCE_TIME:
                case Monitorar.NAVIGATION_SPEED:
                    MonitorarRegra regra = new MonitorarRegra();
                    regra.setCor("#000000");
                    regra.setValor(0);
                    regra.setCodMonitorar(variableMonitor);
                    variableMonitor.addRule(regra);
                    break;
                default:
                    break;
            }

        }

    }

    /**
     * Adiciona uma variável de monitoramento em branco
     */
    public void addVariableMonitor() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            Monitorar monitorar = new Monitorar();
            monitorar.setMonitorarRegraList(new ArrayList<>());
            monitorar.setCodUsuario(SessionContext.getInstance().getUserLoggedIn());
            monitorar.setVariavel(0);
            monitorar.setMensagem("");

            MonitorarController.insert(manager, monitorar);

            manager.getTransaction().commit();

            listVariablesMonitors.add(monitorar);

        } catch (Exception ex) {

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao inserir variável para monitoramento!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }

    }

    /**
     * Método utilizado para atualizar o cadastro da variável a ser monitorada
     *
     * @param variableMonitor
     * @throws Exception
     */
    public void updateVariableMonitor(Monitorar variableMonitor) throws Exception {

        if (variableMonitor.getVariavel() == 0) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao atualizar cadastro. O campo Variável deve ser selecionado!"));
            return;
        }

        if (variableMonitor.getMensagem() == null || variableMonitor.getMensagem().trim().isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao atualizar cadastro. O campo Mensagem deve ser informado!"));
            return;
        }

        if (variableMonitor.getMonitorarRegraList() != null) {
            for (MonitorarRegra rule : variableMonitor.getMonitorarRegraList()) {
                if ((rule.getNome() == null || rule.getNome().trim().isEmpty()) || (rule.getCor() == null || rule.getCor().isEmpty())
                        || rule.getValor() == 0 || (rule.getCondicao() == null || rule.getCondicao().isEmpty())) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Falha",
                            "Falha ao atualizar cadastro. As regras estão incompletas!"));
                    return;
                }
            }
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        List<MonitorarRegra> rulesBackup = variableMonitor.getMonitorarRegraList();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            switch (variableMonitor.getVariavel()) {
                case Monitorar.ARRIVAL_DATE:
                case Monitorar.DEPARTURE_DATE:
                    variableMonitor.removeAllRules();
                    break;
                default:
                    break;
            }

            MonitorarController.edit(manager, variableMonitor);

            manager.getTransaction().commit();

        } catch (Exception ex) {

            manager.getTransaction().rollback();
            variableMonitor.setMonitorarRegraList(rulesBackup);

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao atualizar cadastro!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }

    }

    /**
     * Remove uma regra de uma variável de monitoramento
     *
     * @param variableMonitor
     * @param index
     */
    public void removeRuleFromVariableMonitor(Monitorar variableMonitor, int index) {
        variableMonitor.removeRule(index);
    }

    /**
     * Adiciona uma regra a uma variável de monitoramento
     *
     * @param variableMonitor
     */
    public void addRuleFromVariableMonitor(Monitorar variableMonitor) {
        MonitorarRegra regra = new MonitorarRegra();
        regra.setCor("#000000");
        regra.setValor(0);
        regra.setCodMonitorar(variableMonitor);
        variableMonitor.addRule(regra);
    }

    /**
     * Verifica se uma variável de monitoramento já se encontra cadastrada
     *
     * @param variableMonitor
     * @param codVariableMonitor
     * @return
     */
    public boolean containsVariableMonitor(int variableMonitor, Integer codVariableMonitor) {
        for (Monitorar obj : listVariablesMonitors) {
            if (obj.getVariavel() == variableMonitor && obj.getCodMonitorar() != codVariableMonitor) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se uma variável de monitoramento já se encontra cadastrada
     *
     * @param variableMonitor
     * @return
     */
    public boolean containsVariableMonitor(int variableMonitor) {
        for (Monitorar obj : listVariablesMonitors) {
            if (obj.getVariavel() == variableMonitor) {
                return true;
            }
        }
        return false;
    }

    /**
     * Informa se o botão de adicionar + variável de monitoramento deve estar
     * visível
     *
     * @return
     */
    public boolean showButtonAddVariableMonitor() {
        return listVariablesMonitors.size() < 4;
    }

    /**
     * Remove uma variável de monitoramento que estava cadastrada
     *
     * @param index
     */
    public void deleteVariableMonitor(int index) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();
            MonitorarController.delete(manager, listVariablesMonitors.get(index).getCodMonitorar());
            manager.getTransaction().commit();

            listVariablesMonitors.remove(index);

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao excluir o cadastro!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }

    }

    public void exportReport() {

        if (filteredEvents == null && (events == null || events.isEmpty())) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Atenção",
                    "É necessário um histórico de eventos para exporta-los ao relatório"));
            return;
        }

        String regex = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2})";

        String startF = startDateHistoryEvents.replaceAll(regex, "$3")
                + "/" + startDateHistoryEvents.replaceAll(regex, "$2")
                + "/" + startDateHistoryEvents.replaceAll(regex, "$1")
                + " " + startDateHistoryEvents.replaceAll(regex, "$4")
                + ":" + startDateHistoryEvents.replaceAll(regex, "$5");

        String endF = endDateHistoryEvents.replaceAll(regex, "$3")
                + "/" + endDateHistoryEvents.replaceAll(regex, "$2")
                + "/" + endDateHistoryEvents.replaceAll(regex, "$1")
                + " " + endDateHistoryEvents.replaceAll(regex, "$4")
                + ":" + endDateHistoryEvents.replaceAll(regex, "$5");

        try {

            Date start = Util.stringToDate(startF, "dd/MM/yyyy HH:mm");
            Date end = Util.stringToDate(endF, "dd/MM/yyyy HH:mm");

            Map params = new HashMap<>();
            params.put("LOCAL_IMAGEM_MARINO", getClass().getResource(
                    "/br/com/marino/sismar/images/logo_marino.png"));
            params.put("PERIODO", Util.dateToString(start, "dd/MM/yyyy HH:mm") + " - "
                    + Util.dateToString(end, "dd/MM/yyyy HH:mm"));

            List<EventReport> list = new ArrayList<>();

            for (Event event : (filteredEvents == null ? events : filteredEvents)) {
                list.add(new EventReport(event));
            }

            Report report = new Report();
            report.getEventReport("/br/com/marino/sismar/reports/report_events_new.jrxml",
                    list,
                    params);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void searchHistory() throws IOException {

        if (startDateHistoryEvents == null || endDateHistoryEvents == null) {
            return;
        }

        String regex = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2})";

        if (!startDateHistoryEvents.matches(regex) || !endDateHistoryEvents.matches(regex)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Datas inválidas!"));
            return;
        }

        String startF = startDateHistoryEvents.replaceAll(regex, "$3")
                + "/" + startDateHistoryEvents.replaceAll(regex, "$2")
                + "/" + startDateHistoryEvents.replaceAll(regex, "$1")
                + " " + startDateHistoryEvents.replaceAll(regex, "$4")
                + ":" + startDateHistoryEvents.replaceAll(regex, "$5");

        String endF = endDateHistoryEvents.replaceAll(regex, "$3")
                + "/" + endDateHistoryEvents.replaceAll(regex, "$2")
                + "/" + endDateHistoryEvents.replaceAll(regex, "$1")
                + " " + endDateHistoryEvents.replaceAll(regex, "$4")
                + ":" + endDateHistoryEvents.replaceAll(regex, "$5");

        try {

            Date start = Util.stringToDate(startF, "dd/MM/yyyy HH:mm");
            Date end = Util.stringToDate(endF, "dd/MM/yyyy HH:mm");

            int seconds = Util.getRangeSeconds(start, end);

            if (seconds > 86400) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Falha",
                        "O período máximo é de 24 horas. Por favor, informe um período "
                        + "menor!"));
                return;
            }

            events.clear();
            filteredEvents = null;            
            search(events, start, end);

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao converter as datas. Verifique se foram informadas corretamente!"));
            return;
        }

        //FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl() + "admin/ais/playback.xhtml?start=" + startDatePlayback + "&end=" + endDatePlayback);
    }

    @PreDestroy
    public void destroy() {
    }

    public String getDateTime(Date date) {
        return Util.dateToString(date, "dd/MM/yyyy HH:mm:ss");
    }

    public String getDate(Date date) {
        return Util.dateToString(date, "dd/MM/yyyy");
    }

    public String getHour(Date date) {
        return Util.dateToString(date, "HH:mm");
    }

    public String getColorRule(MonitorarRegra rule) {
        if (rule == null) {
            return "black";
        }
        return "#" + rule.getCor();
    }

    public String getPriority(MonitorarRegra rule) {
        return (rule == null ? "" : rule.getNome());
    }

    public String getVesselName(Navio vessel) {
        return (vessel == null ? "" : vessel.getNomeNavio().toUpperCase());
    }

    public List<Monitorar> getListVariablesMonitors() {
        return listVariablesMonitors;
    }

    public void setListVariablesMonitors(List<Monitorar> listVariablesMonitors) {
        this.listVariablesMonitors = listVariablesMonitors;
    }

    public List<Poin> getListPoin() {
        return listPoin;
    }

    public void setListPoin(List<Poin> listPoin) {
        this.listPoin = listPoin;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getStartDateHistoryEvents() {
        return startDateHistoryEvents;
    }

    public void setStartDateHistoryEvents(String startDateHistoryEvents) {
        this.startDateHistoryEvents = startDateHistoryEvents;
    }

    public String getEndDateHistoryEvents() {
        return endDateHistoryEvents;
    }

    public void setEndDateHistoryEvents(String endDateHistoryEvents) {
        this.endDateHistoryEvents = endDateHistoryEvents;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Event> getFilteredEvents() {
        return filteredEvents;
    }

    public void setFilteredEvents(List<Event> filteredEvents) {
        this.filteredEvents = filteredEvents;
    }

    public String getVariableSelected() {
        return variableSelected;
    }

    public void setVariableSelected(String variableSelected) {
        this.variableSelected = variableSelected;
    }

    public List<String> getListVariablesMonitorToString() {
        return Arrays.asList("Chegada de Embarcação",
                "Saída de Embarcação", "Tempo de Permanência (h)", "Velocidade de Navegação (kn)");
    }

    public List<String> getListTypesVessels() {
        return Arrays.asList("Navios de carga", "Petroleiros", "Navios de passageiros",
                "Rebocador e embarcações especiais", "Embarcação desconhecida", "Embarcação não especificada");
    }

    public List<String> getListPrioritys() {
        List<String> prioritys = new ArrayList<>();
        for (Monitorar m : listVariablesMonitors) {
            if (m.getMonitorarRegraList() == null) {
                continue;
            }
            for (MonitorarRegra mr : m.getMonitorarRegraList()) {
                if (!prioritys.contains(mr.getNome())) {
                    prioritys.add(mr.getNome());
                }
            }
        }
        return prioritys;
    }

}
