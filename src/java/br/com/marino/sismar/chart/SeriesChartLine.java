package br.com.marino.sismar.chart;

import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

public class SeriesChartLine implements Serializable{

    private String nameSerie;
    private String typeValue;
    private String colorLineSerie;
    private RangeChart range;
    private boolean opposite;
    private List<Charts_Valores> data;

    public SeriesChartLine() {
        data = new ArrayList<>();
    }

    public String getNameSerie() {
        return nameSerie;
    }

    public void setNameSerie(String nameSerie) {
        this.nameSerie = nameSerie;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getColorLineSerie() {
        return colorLineSerie;
    }

    public void setColorLineSerie(String colorLineSerie) {
        this.colorLineSerie = colorLineSerie;
    }

    public List<Charts_Valores> getData() {
        return data;
    }

    public void setData(List<Charts_Valores> data) {
        this.data = data;
    }
    
//    public List<ArrayList<Object>> getData() {
//        return data;
//    }
//
//    public void setData(List<ArrayList<Object>> data) {
//        this.data = data;
//    }

//    public void addValueData(Date date, Object value) {
//        if (data == null) {
//            data = new ArrayList<>();
//        }
//        data.add(new ArrayList<>(Arrays.asList(date, value)));
//    }

    public RangeChart getRange() {
        return range;
    }

    public void setRange(RangeChart range) {
        this.range = range;
    }

    public boolean isOpposite() {
        return opposite;
    }

    public void setOpposite(boolean opposite) {
        this.opposite = opposite;
    }

    public JSONObject toJson() throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("nameSerie", nameSerie);
        obj.put("typeValue", typeValue);
        obj.put("colorLineSerie", colorLineSerie);
        if (range != null) {
            obj.put("range", range.toJson());
        }
        obj.put("opposite", opposite);

        JSONArray dataArray = new JSONArray();
        
        for(Charts_Valores value : data){
            JSONObject objData = new JSONObject();
            objData.put("date", Util.dateToString(value.getDatetime(), "yyyy-MM-dd HH:mm:ss"));
            objData.put("value", value.getValue());
            dataArray.put(objData);
        }

        obj.put("data", dataArray);

        return obj;

    }

}
