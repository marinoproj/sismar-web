package br.com.marino.sismar.bean.admin.ais;

import br.com.marino.sismar.controller.AispoinController;
import br.com.marino.sismar.controller.BercosController;
import br.com.marino.sismar.entity.Aispoin;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.util.NavioMapAis;
import br.com.marino.sismar.util.Util;
import java.awt.Color;
import java.io.IOException;
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
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@ManagedBean(name = "BercoAboutBean")
@ViewScoped
public class BercoAboutBean implements Serializable {

    private int codBerco;
    private String historic;

    // about
    private Berco berco;

    // history
    private int numberAttracations; // número de atracações
    private int timeActivity; // tempo de atividade em segundos
    private int timeIdle; // tempo ocioso em segundos
    private List<Aispoin> listTop5ShipAttracationsPermanencePeriod; // top 5 de navios com o maior tempo de permanência
    private List<Aispoin> listShipAttracationsPeriod; // lista de todos os navios que atracaram no poin ordenados pela ordem de chegada
    private Map<String, Integer> qtdAttractionsByTypeShip;

    // realtime
    private Aispoin shipNow;

    @PostConstruct
    public void init() {

        qtdAttractionsByTypeShip = new HashMap<>();
        listTop5ShipAttracationsPermanencePeriod = new ArrayList<>();
        listShipAttracationsPeriod = new ArrayList<>();

        numberAttracations = 0;
        timeActivity = 0;
        timeIdle = 0;

        try {
            codBerco = Integer.parseInt(FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("berco"));
        } catch (NumberFormatException ex) {
            codBerco = 0;
        }

        try {
            historic = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("historic");
        } catch (NumberFormatException ex) {
            historic = null;
        }

        reload();

    }

    public int getCodBerco() {
        return codBerco;
    }

    public void setCodBerco(int codBerco) {
        this.codBerco = codBerco;
    }    

    public String getClassForPeriodHistoricSelected(String period) {
        if (historic == null && period.equalsIgnoreCase("7days")) {
            return "active-period";
        } else if (historic == null) {
            return "";
        }

        if (historic.equalsIgnoreCase(period)) {
            return "active-period";
        }
        return "";

    }

    public String getParamPeriodHistoric() {
        if (historic == null) {
            return "7days";
        }
        switch (historic) {
            case "7days":
            case "30days":
            case "6months":
                return historic;
            default:
                return "7days";
        }

    }

    private Calendar[] getPeriodHistoric() {

        Calendar end = Calendar.getInstance();
        Calendar start = Calendar.getInstance();

        switch (getParamPeriodHistoric()) {
            case "7days":
                start.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case "30days":
                start.add(Calendar.MONTH, -1);
                break;
            case "6months":
                start.add(Calendar.MONTH, -6);
                break;
            default:
                start.add(Calendar.DAY_OF_MONTH, -7);
        }

        return new Calendar[]{start, end};

    }

    public int getNumberAttracations() {
        return numberAttracations;
    }

    public void setNumberAttracations(int numberAttracations) {
        this.numberAttracations = numberAttracations;
    }

    public int getTimeActivity() {
        return timeActivity;
    }

    public void setTimeActivity(int timeActivity) {
        this.timeActivity = timeActivity;
    }

    public int getTimeIdle() {
        return timeIdle;
    }

    public void setTimeIdle(int timeIdle) {
        this.timeIdle = timeIdle;
    }

    public List<Aispoin> getListTop5ShipAttracationsPermanencePeriod() {
        return listTop5ShipAttracationsPermanencePeriod;
    }

    public void setListTop5ShipAttracationsPermanencePeriod(List<Aispoin> listTop5ShipAttracationsPermanencePeriod) {
        this.listTop5ShipAttracationsPermanencePeriod = listTop5ShipAttracationsPermanencePeriod;
    }

    public List<Aispoin> getListShipAttracationsPeriod() {
        return listShipAttracationsPeriod;
    }

    public void setListShipAttracationsPeriod(List<Aispoin> listShipAttracationsPeriod) {
        this.listShipAttracationsPeriod = listShipAttracationsPeriod;
    }

    public Map<String, Integer> getQtdAttractionsByTypeShip() {
        return qtdAttractionsByTypeShip;
    }

    public void setQtdAttractionsByTypeShip(Map<String, Integer> qtdAttractionsByTypeShip) {
        this.qtdAttractionsByTypeShip = qtdAttractionsByTypeShip;
    }

    public Aispoin getShipNow() {
        return shipNow;
    }

    public void setShipNow(Aispoin shipNow) {
        this.shipNow = shipNow;
    }

    public String getDataPoinBerco() throws JSONException {

        JSONObject json = new JSONObject();
        json.put("id", berco.getCodPoin().getCodPoin());
        json.put("name", berco.getNome());
        json.put("coordinates", new JSONArray(berco.getCodPoin().getCoordenadas()));
        Color color = new Color(berco.getCodPoin().getCor());
        json.put("color", String.format("#%02X%02X%02X", color.getRed(),
                color.getGreen(), color.getBlue()));
        json.put("opacity", berco.getCodPoin().getTransparencia() / 100.0);

        return json.toString();

    }

    public String getDataForChartTmpActivityTmpIdle() throws JSONException {

        int total = timeActivity + timeIdle;

        double pc1 = (timeActivity * 100.0) / total;
        double pc2 = 100.0 - pc1;

        JSONArray array = new JSONArray();

        JSONObject obj1 = new JSONObject();
        obj1.put("name", "Atividade");
        obj1.put("y", pc1);
        obj1.put("color", "#90ed7d");

        JSONObject obj2 = new JSONObject();
        obj2.put("name", "Ocioso");
        obj2.put("y", pc2);
        obj2.put("color", "#fd8080");

        array.put(obj1);
        array.put(obj2);

        return array.toString();

    }

    public String getDataForChartAttracationsByTypeShip() throws JSONException {

        int total = 0;
        JSONArray array = new JSONArray();

        for (Map.Entry<String, Integer> entry : qtdAttractionsByTypeShip.entrySet()) {
            total += entry.getValue();
        }

        for (Map.Entry<String, Integer> entry : qtdAttractionsByTypeShip.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            double pc = (value * 100.0) / total;

            JSONObject obj = new JSONObject();
            obj.put("name", key);
            obj.put("y", pc);

            array.put(obj);

        }

        return array.toString();

    }

    /**
     * Calcula o tempo de atividade
     *
     * @param list
     * @return
     */
    private int getTimeActivity(List<Aispoin> list) {
        int time = 0;
        for (Aispoin aispoin : list) {
            time += aispoin.getTempoPermanencia();
        }
        return time;
    }

    /**
     * Calcula o tempo ocioso
     *
     * @param timeActivity
     * @param start
     * @param end
     * @return
     */
    private int getTimeIdle(int timeActivity, Date start, Date end) {
        int time = Util.getRangeSeconds(start, end);
        return time - timeActivity;
    }

    /**
     * Calcula a quantidade de atracações por tipo de navios
     *
     * @param list
     * @return
     */
    public Map<String, Integer> qtdAttractionsByTypeShip(List<Aispoin> list) {

        Map<String, Integer> map = new HashMap<>();
        map.put("Navios de carga", 0);
        map.put("Petroleiros", 0);
        map.put("Embarcação desconhecida", 0);
        map.put("Embarcação não especificada", 0);

        for (Aispoin aispoin : list) {
            NavioMapAis navioMapAis = new NavioMapAis(aispoin.getAisMmsi().getCodNavio());
            if (map.containsKey(navioMapAis.getCategoriaEmbarcacao())) {
                map.put(navioMapAis.getCategoriaEmbarcacao(), map.get(navioMapAis.getCategoriaEmbarcacao()) + 1);
            } else {
                map.put(navioMapAis.getCategoriaEmbarcacao(), 1);
            }
        }

        return map;

    }

    public void changePeriod(String period) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl() + "admin/ais/berco.xhtml?berco=" + codBerco + "&historic=" + period);
        } catch (IOException ex) {
        }
    }

    /**
     * Atualiza os dados
     */
    public void reload() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            berco = BercosController.getBercoByCodBerco(manager, codBerco);

            Calendar period[] = getPeriodHistoric();

            Calendar start = period[0];
            Calendar end = period[1];

            listShipAttracationsPeriod = AispoinController.getListShipAttracationsPeriodFromPoin(manager,
                    berco.getCodPoin().getCodPoin(),
                    start.getTime(),
                    end.getTime(),
                    30,
                    false); // lista de todos os navios que atracaram no poin ordenados pela ordem de chegada

            numberAttracations = listShipAttracationsPeriod.size(); // pega o número de atracações
            timeActivity = getTimeActivity(listShipAttracationsPeriod); // calcula o tempo de atividade
            timeIdle = getTimeIdle(timeActivity, start.getTime(), end.getTime()); // calcula o tempo ocioso

            listTop5ShipAttracationsPermanencePeriod = AispoinController.getTopFiveShipAttracationsPermanencePeriodFromPoin(manager,
                    berco.getCodPoin().getCodPoin(),
                    start.getTime(),
                    end.getTime(),
                    30); // lista com os 5 navios que atracaram no poin ordenado por aqueles com o maior tempo de permanência

            qtdAttractionsByTypeShip = qtdAttractionsByTypeShip(listShipAttracationsPeriod); // calcula a quantidade de atracações por tipo de navios                            

            shipNow = AispoinController.getShipNowFromPoin(manager,
                    berco.getCodPoin().getCodPoin(),
                    10); // pega os dados do navio atracado atualmente

        } catch (Exception ex) {
            shipNow = null;

            qtdAttractionsByTypeShip = new HashMap<>();
            listTop5ShipAttracationsPermanencePeriod = new ArrayList<>();
            listShipAttracationsPeriod = new ArrayList<>();

            numberAttracations = 0;
            timeActivity = 0;
            timeIdle = 0;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    @PreDestroy
    public void destroy() {
    }

    public Berco getBerco() {
        return berco;
    }

    public void setBerco(Berco berco) {
        this.berco = berco;
    }

    public String getCabecos(Berco berco) {
        if (berco == null || berco.getCabecos() == null) {
            return "";
        }
        return berco.getCabecos();
    }

    public String getComprimento(Berco berco) {
        if (berco == null || berco.getComprimento() == null) {
            return "";
        }
        return berco.getComprimento() + " m";
    }

    public String getTipoCais(Berco berco) {
        if (berco == null || berco.getCodCais() == null) {
            return "";
        }
        return berco.getCodCais().getNome();
    }

    public String getProfundidade(Berco berco) {
        if (berco == null || berco.getProfundidade() == null) {
            return "";
        }
        return berco.getProfundidade() + " m";
    }

    public String getEmpresa(Berco berco) {
        if (berco == null || berco.getCodEmpresa() == null) {
            return "";
        }
        return berco.getCodEmpresa().getNome();
    }

    public String getMercadoria(Berco berco) {
        if (berco == null || berco.getCodMercadoria() == null) {
            return "";
        }
        return berco.getCodMercadoria().getNome();
    }

    public String getBercoArredondado(Berco berco) {
        if (berco == null || berco.getBercoArredondado() == null) {
            return "";
        }
        return berco.getBercoArredondado() == 1 ? "S" : "N";
    }

    public String getArea(Berco berco) {
        if (berco == null || berco.getCodArea() == null) {
            return "";
        }
        return berco.getCodArea().getNome();
    }

    public String getPoin(Berco berco) {
        if (berco == null || berco.getCodPoin() == null) {
            return "";
        }
        return berco.getCodPoin().getNome();
    }

    public String getLatitude(Berco berco) {
        if (berco == null || berco.getLatitude() == null) {
            return "";
        }
        return berco.getLatitude() + "";
    }

    public String getLongitude(Berco berco) {
        if (berco == null || berco.getLongitude() == null) {
            return "";
        }
        return berco.getLongitude() + "";
    }

    public String getCaladoBaixaMare(Berco berco) {
        if (berco == null || berco.getCaladoBaixaMare() == null) {
            return "";
        }
        return berco.getCaladoBaixaMare() + " m";
    }

    public String getCaladoPreaMare(Berco berco) {
        if (berco == null || berco.getCaladoPreaMare() == null) {
            return "";
        }
        return berco.getCaladoPreaMare() + " m";
    }

    public String getImageBerco(Berco berco) {
        if (berco == null || berco.getImagem() == null) {
            return "img/sem_imagem.png";
        }
        return "img/bercos/" + berco.getImagem();
    }

    // NAVIO
    public String getImageVessel(Byte[] image) {
        return Util.getImageVessel(image);
    }

    public String getDimension(Navio vessel) {

        if (vessel == null) {
            return " ";
        }

        NavioMapAis vesselMap = new NavioMapAis(vessel);

        return vesselMap.getComprimentoReal() + "m X " + vesselMap.getLarguraReal() + "m";

    }

    public String getType(Navio vessel) {

        if (vessel == null) {
            return " ";
        }

        NavioMapAis vesselMap = new NavioMapAis(vessel);

        return vesselMap.getCategoriaEmbarcacao();

    }

    public String getDatetime(Date datetime) {
        if (datetime == null) {
            return "";
        }
        return Util.dateToString(datetime, "dd/MM/yyyy HH:mm");
    }

}
