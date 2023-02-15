package br.com.marino.sismar.entity;

import br.com.marino.sismar.util.Event;
import br.com.marino.sismar.util.Util;

public class EventReport {

    private String data;
    private String poin;
    private String variable;
    private String priority;
    private String vessel_name;
    private String vessel_imo;
    private String vessel_type;
    private String message;
    private String color_line;

    public EventReport(Event event){
        this.data = Util.dateToString(event.getDatetime(), "dd/MM/yyyy HH:mm:ss");
        this.poin = event.getPoin().getNome();
        this.variable = event.getVariableMonitor().getTypeMonitor();
        this.priority = event.getRule() == null ? "" : event.getRule().getNome();
        this.vessel_name = event.getVessel() == null ? "" : event.getVessel().getNomeNavio();
        this.vessel_imo = event.getVessel() == null ? "" : event.getVessel().getImo()+"";
        this.vessel_type = event.getTypeVessel();
        this.message = event.getMessage();
        this.color_line = event.getRule() == null ? "#000000" : "#"+event.getRule().getCor();
    }

    public EventReport(String data, String poin, String variable, String priority, String vessel_name, String vessel_imo, String vessel_type, String message, String color_line) {
        this.data = data;
        this.poin = poin;
        this.variable = variable;
        this.priority = priority;
        this.vessel_name = vessel_name;
        this.vessel_imo = vessel_imo;
        this.vessel_type = vessel_type;
        this.message = message;
        this.color_line = color_line;
    }    

    public EventReport() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPoin() {
        return poin;
    }

    public void setPoin(String poin) {
        this.poin = poin;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getVessel_name() {
        return vessel_name;
    }

    public void setVessel_name(String vessel_name) {
        this.vessel_name = vessel_name;
    }

    public String getVessel_imo() {
        return vessel_imo;
    }

    public void setVessel_imo(String vessel_imo) {
        this.vessel_imo = vessel_imo;
    }

    public String getVessel_type() {
        return vessel_type;
    }

    public void setVessel_type(String vessel_type) {
        this.vessel_type = vessel_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getColor_line() {
        return color_line;
    }

    public void setColor_line(String color_line) {
        this.color_line = color_line;
    }    

}
