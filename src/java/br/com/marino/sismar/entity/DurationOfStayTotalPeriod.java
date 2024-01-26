package br.com.marino.sismar.entity;

public class DurationOfStayTotalPeriod {

    private long totalVessels;
    private String totalPct;

    public DurationOfStayTotalPeriod(long totalVessels, String totalPct) {
        this.totalVessels = totalVessels;
        this.totalPct = totalPct;
    }

    public DurationOfStayTotalPeriod() {
    }   
    
    public long getTotalVessels() {
        return totalVessels;
    }

    public void setTotalVessels(long totalVessels) {
        this.totalVessels = totalVessels;
    }

    public String getTotalPct() {
        return totalPct;
    }

    public void setTotalPct(String totalPct) {
        this.totalPct = totalPct;
    }
   
}
