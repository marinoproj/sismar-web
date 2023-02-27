package br.com.marino.sismar.entity;

import java.util.Date;

public class TimeStayShip {

    private String name;
    private int imo;
    private String formattedDate;
    private long pct;
    private long durationMinutes;
    private long durationMaxMinutes;
    private Date start;

    @Override
    public String toString() {
        return "TimeStayShip{" + "name=" + name + ", imo=" + imo + ", formattedDate=" + formattedDate + ", pct=" + pct + ", durationMinutes=" + durationMinutes + ", durationMaxMinutes=" + durationMaxMinutes + ", start=" + start + '}';
    }
    
    public TimeStayShip() {
    }

    public long getPct() {
        return pct;
    }

    public void setPct(long pct) {
        this.pct = pct;
    }

    public int getImo() {
        return imo;
    }

    public void setImo(int imo) {
        this.imo = imo;
    }   
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public long getDurationMaxMinutes() {
        return durationMaxMinutes;
    }

    public void setDurationMaxMinutes(long durationMaxMinutes) {
        this.durationMaxMinutes = durationMaxMinutes;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

}
