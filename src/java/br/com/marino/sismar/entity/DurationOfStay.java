package br.com.marino.sismar.entity;

public class DurationOfStay {

    private DurationOfStayTotalPeriod within24Hours;
    private DurationOfStayTotalPeriod within48Hours;
    private DurationOfStayTotalPeriod over48Hours;

    public DurationOfStay(DurationOfStayTotalPeriod within24Hours, DurationOfStayTotalPeriod within48Hours, DurationOfStayTotalPeriod over48Hours) {
        this.within24Hours = within24Hours;
        this.within48Hours = within48Hours;
        this.over48Hours = over48Hours;
    }

    public DurationOfStay() {
    }   

    public DurationOfStayTotalPeriod getWithin24Hours() {
        return within24Hours;
    }

    public void setWithin24Hours(DurationOfStayTotalPeriod within24Hours) {
        this.within24Hours = within24Hours;
    }

    public DurationOfStayTotalPeriod getWithin48Hours() {
        return within48Hours;
    }

    public void setWithin48Hours(DurationOfStayTotalPeriod within48Hours) {
        this.within48Hours = within48Hours;
    }

    public DurationOfStayTotalPeriod getOver48Hours() {
        return over48Hours;
    }

    public void setOver48Hours(DurationOfStayTotalPeriod over48Hours) {
        this.over48Hours = over48Hours;
    }
    
}
