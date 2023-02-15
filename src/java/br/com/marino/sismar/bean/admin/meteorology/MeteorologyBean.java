package br.com.marino.sismar.bean.admin.meteorology;

import br.com.marino.sismar.chart.ChartLine;
import br.com.marino.sismar.chart.Charts_Valores;
import br.com.marino.sismar.chart.RangeChart;
import br.com.marino.sismar.chart.SeriesChartLine;
import br.com.marino.sismar.controller.EquipamentoController;
import br.com.marino.sismar.controller.MeteorologiaController;
import br.com.marino.sismar.entity.Equipamentos;
import br.com.marino.sismar.entity.Meteorologia;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.PrimeFaces;
import org.primefaces.json.JSONException;

@ManagedBean(name = "MeteorologyBean")
@ViewScoped
public class MeteorologyBean implements Serializable {
    
    private Meteorologia wind;
    private Exception windException;
    
    private String startDate, endDate;
    private String startDateTemp, endDateTemp;
    private String startDateUmidity, endDateUmidity;
    
    private Equipamentos equipamento;
    private List<Equipamentos> equipamentos;
    
    @PostConstruct
    public void init() {
        reloadInit();  
    }  
    
    @PreDestroy
    public void destroy() {
        wind = null;
        windException = null;
    }

    public Meteorologia getWind() {
        return wind;
    }

    public void setWind(Meteorologia wind) {
        this.wind = wind;
    }

    public Equipamentos getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(Equipamentos equipamento) {
        this.equipamento = equipamento;
    }

    public List<Equipamentos> getEquipamentos() {
        return equipamentos;
    }

    public void setEquipamentos(List<Equipamentos> equipamentos) {
        this.equipamentos = equipamentos;
    }

    public String getStartDateUmidity() {
        return startDateUmidity;
    }

    public void setStartDateUmidity(String startDateUmidity) {
        this.startDateUmidity = startDateUmidity;
    }

    public String getEndDateUmidity() {
        return endDateUmidity;
    }

    public void setEndDateUmidity(String endDateUmidity) {
        this.endDateUmidity = endDateUmidity;
    }
    
    public String getStartDateTemp() {
        return startDateTemp;
    }

    public void setStartDateTemp(String startDateTemp) {
        this.startDateTemp = startDateTemp;
    }

    public String getEndDateTemp() {
        return endDateTemp;
    }

    public void setEndDateTemp(String endDateTemp) {
        this.endDateTemp = endDateTemp;
    }
    
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDateLastUpdate() {
        if (wind == null) {
            return "";
        }
        return "Última atualização " + Util.dateToString(wind.getDataHora(), "dd/MM/yyy HH:mm");
    }
        
    public String getDataHora(Date date, String formatRegex) {
        if (date == null) {
            return "";
        }
        return Util.dateToString(date, formatRegex);
    }

    public String getWindVel() {
        if (windException != null) {
            return "-";
        }
        if (wind == null) {
            return "-";
        }
        return getValueFormatted(wind.getVelocidadeMediaVento()) + " km/h";
    }

    public String getWindVelNos() {
        if (windException != null) {
            return "-";
        }
        if (wind == null) {
            return "-";
        }
        return getValueFormatted(wind.getVelocidadeMediaVento() / 1.852) + " nós";
    }

    public String getWindRaj() {
        if (windException != null) {
            return "-";
        }
        if (wind == null) {
            return "-";
        }
        return getValueFormatted(wind.getVelocidadeMaximaVento()) + " km/h";
    }

    public String getWindRajNos() {
        if (windException != null) {
            return "-";
        }
        if (wind == null) {
            return "-";
        }
        return getValueFormatted(wind.getVelocidadeMaximaVento() / 1.852) + " nós";
    }

    public String getWindDir() {
        if (windException != null) {
            return "-";
        }
        if (wind == null) {
            return "-";
        }
        return getValueFormatted(wind.getDirecaoMediaVento()) + " º";
    }
    
    public String getTemperature() {
        if (windException != null) {
            return "-";
        }
        if (wind == null) {
            return "-";
        }
        return getValueFormatted(wind.getTemperaturaAr()) + " ºC";
    }
    
    public String getAirUmidity() {
        if (windException != null) {
            return "-";
        }
        if (wind == null) {
            return "-";
        }
        return getValueFormatted(wind.getUmidadeRelativaAr()) + " %";
    }

    public String getWindMessageStatus() {
        if (windException != null) {
            return "offline";
        }
        if (wind == null) {
            return "offline";
        }
        return "online";
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;
        
        
        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            equipamentos = EquipamentoController.getListEquipamentos(manager);
            equipamento = equipamentos.isEmpty() ? null : equipamentos.get(0);
            
            if (equipamento != null){
                wind = MeteorologiaController.getLastByCodEquipamento(manager, equipamento.getCodEquipamento());
            }

            windException = null;
            
        } catch (Exception ex) {
            windException = ex;
            wind = null;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void reloadWind() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            wind = MeteorologiaController.getLastByCodEquipamento(manager, equipamento.getCodEquipamento());

            /*if (wind != null && !Util.isOnlineInfo(wind.getDataHora(),
                    Util.TMP_SECONDS_ONLINE_WIND)) {
                throw new Exception("outdated information.");
            }*/

            windException = null;

        } catch (Exception ex) {
            windException = ex;
            wind = null;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void reloadChart(ActionEvent actionEvent) {

        Date start;
        Date end;

        try {
            start = Util.stringToDate(startDate.trim().replace("T", " "), "yyyy-MM-dd HH:mm");
            end = Util.stringToDate(endDate.trim().replace("T", " "), "yyyy-MM-dd HH:mm");
        } catch (ParseException ex) {
            failedCreateChartLine(4, "É necessário informar o período corretamente para poder "
                    + "visualizar o gráfico");
            return;
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;
        List<Meteorologia> list = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            list = MeteorologiaController.getListByCodEquipamentoAndPeriod(manager, equipamento.getCodEquipamento(), start, end);

        } catch (Exception ex) {
            failedCreateChartLine(4, "Falha ao efetuar a busca dos dados");
            return;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }

        if (list == null || list.isEmpty()) {
            failedCreateChartLine(3, "Não há resultados para o período informado");
            return;
        }

        List<Date> datas = new ArrayList<>();

        for (Meteorologia t : list) {
            datas.add(t.getDataHora());
        }

        List<Double> vel = new ArrayList<>();
        List<Double> raj = new ArrayList<>();
        List<Double> dir = new ArrayList<>();

        double rajadaMax = Float.MIN_VALUE;

        for (Meteorologia t : list) {
            vel.add(t.getVelocidadeMediaVento());
            raj.add(t.getVelocidadeMaximaVento());
            dir.add(t.getDirecaoMediaVento());
            if (t.getVelocidadeMaximaVento() > rajadaMax) {
                rajadaMax = t.getVelocidadeMaximaVento();
            }
        }

        rajadaMax = (int) rajadaMax / 10;
        rajadaMax++;
        rajadaMax *= 10;
        rajadaMax += 10;

        List<List<Charts_Valores>> listValues = Util.
                getListaForGrafico(start,
                        end,
                        60,
                        60 + 10,
                        datas,
                        vel,
                        raj,
                        dir);

        SeriesChartLine serie = new SeriesChartLine();
        serie.setNameSerie("Velocidade");
        serie.setTypeValue("km/h");
        serie.setRange(new RangeChart(0, (int) rajadaMax));
        serie.setColorLineSerie("#212121");
        serie.setOpposite(false);
        serie.setData(listValues.get(0));

        SeriesChartLine serie2 = new SeriesChartLine();
        serie2.setNameSerie("Rajada");
        serie2.setTypeValue("km/h");
        serie2.setRange(new RangeChart(0, (int) rajadaMax));
        serie2.setColorLineSerie("#EF5350");
        serie2.setOpposite(false);
        serie2.setData(listValues.get(1));

        SeriesChartLine serie3 = new SeriesChartLine();
        serie3.setNameSerie("Direção");
        serie3.setTypeValue("º");
        serie3.setRange(new RangeChart(0, 360));
        serie3.setColorLineSerie("#1E88E5");
        serie3.setOpposite(true);
        serie3.setData(listValues.get(2));

        ChartLine chart = new ChartLine();
        chart.setTitle("Vento: Condições climáticas");
        chart.setSubtitle("Período " + Util.dateToString(start, "dd/MM/yyyy HH:mm")
                + " até " + Util.dateToString(end, "dd/MM/yyyy HH:mm"));
        chart.setTitleXAxis("Período");

        chart.addSerie(serie);
        chart.addSerie(serie2);
        chart.addSerie(serie3);

        try {
            createChartLine(chart.toJson().toString());
        } catch (JSONException ex) {
            failedCreateChartLine(4, "Falha ao efetuar a busca dos dados");
        }

    }
    
    public void reloadChartTemp(ActionEvent actionEvent) {

        Date start;
        Date end;

        try {
            start = Util.stringToDate(startDateTemp.trim().replace("T", " "), "yyyy-MM-dd HH:mm");
            end = Util.stringToDate(endDateTemp.trim().replace("T", " "), "yyyy-MM-dd HH:mm");
        } catch (ParseException ex) {
            failedCreateChartLine(4, "É necessário informar o período corretamente para poder "
                    + "visualizar o gráfico");
            return;
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;
        List<Meteorologia> list = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            list = MeteorologiaController.getListByCodEquipamentoAndPeriod(manager, equipamento.getCodEquipamento(), start, end);

        } catch (Exception ex) {
            failedCreateChartLine(4, "Falha ao efetuar a busca dos dados");
            return;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }

        if (list == null || list.isEmpty()) {
            failedCreateChartLine(3, "Não há resultados para o período informado");
            return;
        }

        List<Date> datas = new ArrayList<>();

        for (Meteorologia t : list) {
            datas.add(t.getDataHora());
        }

        List<Double> temp = new ArrayList<>();

        double tempMax = Float.MIN_VALUE;

        for (Meteorologia t : list) {
            temp.add(t.getTemperaturaAr());            
            if (t.getTemperaturaAr() > tempMax) {
                tempMax = t.getTemperaturaAr();
            }
        }

        tempMax = (int) tempMax / 10;
        tempMax++;
        tempMax *= 10;
        tempMax += 30;

        List<List<Charts_Valores>> listValues = Util.
                getListaForGrafico(start,
                        end,
                        60,
                        60 + 10,
                        datas,
                        temp);

        SeriesChartLine serie = new SeriesChartLine();
        serie.setNameSerie("Temperatura");
        serie.setTypeValue("ºC");
        serie.setRange(new RangeChart(0, (int) tempMax));
        serie.setColorLineSerie("#212121");
        serie.setOpposite(false);
        serie.setData(listValues.get(0));

        ChartLine chart = new ChartLine();
        chart.setTitle("Temperatura");
        chart.setSubtitle("Período " + Util.dateToString(start, "dd/MM/yyyy HH:mm")
                + " até " + Util.dateToString(end, "dd/MM/yyyy HH:mm"));
        chart.setTitleXAxis("Período");

        chart.addSerie(serie);

        try {            
            PrimeFaces.current().executeScript("Chart.createCartLine('chart-temp', '" + chart.toJson().toString() + "');");            
        } catch (JSONException ex) {
            failedCreateChartLine(4, "Falha ao efetuar a busca dos dados");
        }

    }
    
    public void reloadChartUmidity(ActionEvent actionEvent) {

        Date start;
        Date end;

        try {
            start = Util.stringToDate(startDateUmidity.trim().replace("T", " "), "yyyy-MM-dd HH:mm");
            end = Util.stringToDate(endDateUmidity.trim().replace("T", " "), "yyyy-MM-dd HH:mm");
        } catch (ParseException ex) {
            failedCreateChartLine(4, "É necessário informar o período corretamente para poder "
                    + "visualizar o gráfico");
            return;
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;
        List<Meteorologia> list = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            list = MeteorologiaController.getListByCodEquipamentoAndPeriod(manager, equipamento.getCodEquipamento(), start, end);

        } catch (Exception ex) {
            failedCreateChartLine(4, "Falha ao efetuar a busca dos dados");
            return;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }

        if (list == null || list.isEmpty()) {
            failedCreateChartLine(3, "Não há resultados para o período informado");
            return;
        }

        List<Date> datas = new ArrayList<>();

        for (Meteorologia t : list) {
            datas.add(t.getDataHora());
        }

        List<Double> umidity = new ArrayList<>();

        double umidityMax = Float.MIN_VALUE;

        for (Meteorologia t : list) {
            umidity.add(t.getUmidadeRelativaAr());            
            if (t.getUmidadeRelativaAr() > umidityMax) {
                umidityMax = t.getUmidadeRelativaAr();
            }
        }

        umidityMax = (int) umidityMax / 10;
        umidityMax++;
        umidityMax *= 10;
        umidityMax += 30;

        List<List<Charts_Valores>> listValues = Util.
                getListaForGrafico(start,
                        end,
                        60,
                        60 + 10,
                        datas,
                        umidity);

        SeriesChartLine serie = new SeriesChartLine();
        serie.setNameSerie("Umidade");
        serie.setTypeValue("%");
        serie.setRange(new RangeChart(0, (int) umidityMax));
        serie.setColorLineSerie("#212121");
        serie.setOpposite(false);
        serie.setData(listValues.get(0));

        ChartLine chart = new ChartLine();
        chart.setTitle("Umidade");
        chart.setSubtitle("Período " + Util.dateToString(start, "dd/MM/yyyy HH:mm")
                + " até " + Util.dateToString(end, "dd/MM/yyyy HH:mm"));
        chart.setTitleXAxis("Período");
        
        
        chart.addSerie(serie);

        try {
            PrimeFaces.current().executeScript("Chart.createCartLine('chart-umidity', '" + chart.toJson().toString() + "');");            
        } catch (JSONException ex) {
            failedCreateChartLine(4, "Falha ao efetuar a busca dos dados");
        }

    }

    private void createChartLine(String json) {
        PrimeFaces.current().executeScript("Chart.createCartLine('chart', '" + json + "');");
    }

    private void failedCreateChartLine(int status, String message) {
        PrimeFaces.current().executeScript("Chart.failedCreateChartLine('chart', " + status + ", '" + message + "');");
    }

    public String getValueFormatted(Double value) {
        if (value == null) {
            return "";
        }
        return Util.converterValue(value);
    }

}
