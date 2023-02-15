package br.com.marino.sismar.chart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

public class ChartLine implements Serializable{
    
    private String title;
    private String subtitle;
    private String titleXAxis;
    private List<SeriesChartLine> series;

    public ChartLine(String title, String subtitle, String titleXAxis, List<SeriesChartLine> series) {
        this.title = title;
        this.subtitle = subtitle;
        this.titleXAxis = titleXAxis;
        this.series = series;
    }

    public ChartLine() {
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitleXAxis() {
        return titleXAxis;
    }

    public void setTitleXAxis(String titleXAxis) {
        this.titleXAxis = titleXAxis;
    }

    public List<SeriesChartLine> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesChartLine> series) {
        this.series = series;
    }
    
    public void addSerie(SeriesChartLine serie){
        if (series == null){
            series = new ArrayList<>();
        }
        series.add(serie);
    }
    
    
    public JSONObject toJson() throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("title", title);
        obj.put("subtitle", subtitle);
        obj.put("titleXAxis", titleXAxis);      
        
        JSONArray seriesArray = new JSONArray();
        
        for(SeriesChartLine serie : series){
            seriesArray.put(serie.toJson());
        }
        
        obj.put("series", seriesArray);

        return obj;

    }
    
}
