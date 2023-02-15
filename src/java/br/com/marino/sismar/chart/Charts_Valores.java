package br.com.marino.sismar.chart;

import java.util.Date;

public class Charts_Valores {

    private Date datetime;
    private Double value;

    public Charts_Valores() {
    }

    public Charts_Valores(Date datetime, Double value) {
        this.datetime = datetime;
        this.value = value;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Charts_Valores{" + "datetime=" + datetime + ", value=" + value + '}';
    }
    
}
