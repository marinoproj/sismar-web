package br.com.marino.sismar.entity;

import br.com.marino.sismar.util.Util;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

public class VesselFila {

    private Date arrivalDate;
    private String nameVessel;
    private int mmsi;
    private Integer imoVessel;
    private String permanence;
    private EmbarcacaoCodesp dadosCodesp;

    public VesselFila(int mmsi, Aispoin aisPoin, Navio navio) {
        this.mmsi = mmsi;
        this.arrivalDate = aisPoin.getDataEntrada();
        if (navio != null) {
            this.nameVessel = navio.getNomeNavio();
            this.imoVessel = navio.getImo();
        }
        this.permanence = getCalculatePermanence();
    }

    private String getCalculatePermanence() {
        Date dataFim = new Date();
        int qtdSegundos;
        int horas = 0;
        int minutos = 0;
        int segundos = 0;
        try {
            qtdSegundos = getRangeInSeconds(arrivalDate, dataFim);
            horas = qtdSegundos / 3600;
            minutos = qtdSegundos % 3600 / 60;
            segundos = qtdSegundos % 3600 % 60;
        } catch (Exception ex) {
        }
        return getValuePermanence(horas) + ":" + getValuePermanence(minutos) + ":" + getValuePermanence(segundos);
    }

    private String getValuePermanence(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return value + "";
    }

    private int getRangeInSeconds(Date startDate, Date endDate) {
        DateTime dataAnt = new DateTime(startDate);
        DateTime dataDep = new DateTime(endDate);
        return Seconds.secondsBetween(dataAnt, dataDep).getSeconds();
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getNameVessel() {
        return nameVessel;
    }

    public void setNameVessel(String nameVessel) {
        this.nameVessel = nameVessel;
    }

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
    }

    public Integer getImoVessel() {
        return imoVessel;
    }

    public void setImoVessel(Integer imoVessel) {
        this.imoVessel = imoVessel;
    }

    public String getPermanence() {
        return permanence;
    }

    public void setPermanence(String permanence) {
        this.permanence = permanence;
    }

    public EmbarcacaoCodesp getDadosCodesp() {
        return dadosCodesp;
    }

    public void setDadosCodesp(EmbarcacaoCodesp dadosCodesp) {
        this.dadosCodesp = dadosCodesp;
    }

    public JSONObject toJSONObject(int order) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("order", order);
        jsonObject.put("arrival_date", Util.dateToString(arrivalDate, "dd/MM/yyyy HH:mm"));
        jsonObject.put("name_vessel", Util.getValueFromJson(nameVessel));
        jsonObject.put("imo_vessel", Util.getValueFromJson(imoVessel));
        jsonObject.put("mmsi", mmsi);
        jsonObject.put("permanence", permanence);

        if (dadosCodesp != null) {
            JSONObject jsonExternal = new JSONObject();
            jsonExternal.put("priority", Util.getValueFromJson(dadosCodesp.getPrioridade()));
            jsonExternal.put("terminal", Util.getValueFromJson(dadosCodesp.getTerminal()));
            jsonExternal.put("released_codesp", Util.getValueFromJson(dadosCodesp.isLiberadoCodesp()));
            jsonExternal.put("charge", Util.getValueFromJson(dadosCodesp.getCarga()));
            jsonExternal.put("event", Util.getValueFromJson(dadosCodesp.getEvento()));
            jsonExternal.put("local", Util.getValueFromJson(dadosCodesp.getLocal()));
            jsonExternal.put("programming_date", Util.getValueFromJson(dadosCodesp.getDataProgramacao()));
            jsonObject.put("external", jsonExternal);
        }else{
            jsonObject.put("external", Util.getValueFromJson(null));
        }

        return jsonObject;
    }

}
