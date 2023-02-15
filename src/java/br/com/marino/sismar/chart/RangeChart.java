package br.com.marino.sismar.chart;

import java.io.Serializable;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

public class RangeChart implements Serializable{

    private int min;
    private int max;

    public RangeChart(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public RangeChart() {
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("min", min);
        obj.put("max", max);
        return obj;
    }

}