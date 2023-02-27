package br.com.marino.sismar.bean.admin.operations;

import br.com.marino.sismar.chart.ChartLine;
import br.com.marino.sismar.chart.Charts_Valores;
import br.com.marino.sismar.chart.SeriesChartLine;
import br.com.marino.sismar.controller.AispoinController;
import br.com.marino.sismar.controller.BercoClienteController;
import br.com.marino.sismar.controller.BercosController;
import br.com.marino.sismar.controller.ClientesController;
import br.com.marino.sismar.entity.Aispoin;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.BercoCliente;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.TimeStayShip;
import br.com.marino.sismar.entity.TimelineBerco;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "BercoDashBean")
@ViewScoped
public class BercoDashBean implements Serializable {

    private List<Aispoin> listAisPoin;
    private List<Berco> listBercos;
    private String periodSelected;

    private Berco bercoSelected;
    private Date start;
    private Date end;

    // métricas
    private Integer totalVessels;
    private String tmpOperative;
    private String tmpDead;
    private String tmpOfStay;

    // segunda linha
    private List<TimelineBerco> listTimeline;
    private List<TimeStayShip> listTimeStayShipLast5;
    private List<TimeStayShip> listTimeStay;
    private List<String> listInsights;

    @PostConstruct
    public void init() {
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        listAisPoin = null;
    }

    public void reloadMetrics(EntityManager manager) throws Exception {

        EntityManagerFactory factory = null;
        EntityManager manager2 = manager;

        try {

            if (manager2 == null) {
                factory = Persistence.createEntityManagerFactory("sismarPU");
                manager2 = factory.createEntityManager();
            }

            end = new Date();

            switch (periodSelected) {
                
                case "1":
                    {
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DAY_OF_MONTH, -7);
                        start = c.getTime();
                        break;
                    }
                
                case "2":
                    {
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DAY_OF_MONTH, -15);
                        start = c.getTime();
                        break;
                    }
                
                case "3":
                    {
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DAY_OF_MONTH, -30);
                        start = c.getTime();
                        break;
                    }
                
                default:
                    break;
            }

            System.out.println("START reloadMetrics berco: " + bercoSelected.getCodBerco()
                    + " start: " + start + " end: " + end);
            System.out.println("------------");

            // load lista ais poins
            listAisPoin = AispoinController.getListShipAttracationsPeriodFromPoin(manager2,
                    bercoSelected.getCodPoin().getCodPoin(), start, end, null, true);

            System.out.println("reloadMetrics listAisPoin.size: " + listAisPoin.size());
            System.out.println("------------");

            // primeira linha
            totalVessels = listAisPoin.size();

            long totalTmpMinutes = Util.getTempoPermanenciaEmMinutos(start, end);

            long totalTmpOperativeMinutes = 0;
            for (Aispoin ap : listAisPoin) {
                totalTmpOperativeMinutes += ap.getTempoPermanenciaEmMinutos();
            }

            tmpOperative = Util.convertMinutesToHoursAndMinutes(totalTmpOperativeMinutes);
            tmpDead = Util.convertMinutesToHoursAndMinutes(totalTmpMinutes - totalTmpOperativeMinutes);
            tmpOfStay = Util.convertMinutesToHoursAndMinutes((totalTmpOperativeMinutes / totalVessels));

            System.out.println("reloadMetrics primeira linha totalTmpMinutes: " + totalTmpMinutes
                    + " totalTmpFormated: " + Util.convertMinutesToHoursAndMinutes(totalTmpMinutes)
                    + " totalTmpOperativeMinutes: " + totalTmpOperativeMinutes + " totalVessels: " + totalVessels + " tmpOperative: " + tmpOperative
                    + " tmpDead: " + tmpDead + " tmpOfStay: " + tmpOfStay);

            System.out.println("------------");

            // segunda linha
            reloadTimeline();
            System.out.println("------------");

            reloadTimeStayLast5Ships();
            System.out.println("------------");

            reloadTimeStay();
            System.out.println("------------");

            reloadInsights();
            System.out.println("------------");

            System.out.println("END reloadMetrics berco: " + bercoSelected.getCodBerco()
                    + " start: " + start + " end: " + end);

        } catch (Exception ex) {
            throw ex;

        } finally {
            if (manager == null) {
                if (manager2 != null) {
                    manager2.close();
                }
                if (factory != null) {
                    factory.close();
                }
            }
        }

    }

    public void reloadTimeline() {

        listTimeline = new ArrayList<>();

        for (Aispoin ap : listAisPoin) {

            TimelineBerco tbEntrada = new TimelineBerco();

            tbEntrada.setIcon("icon-berco-up-arrow.png");
            tbEntrada.setDescription(ap.getAisMmsi().getCodNavio().getNomeNavio() + " entrou no berço com velocidade de " + ap.getVelocidadeEntrada() + " nós");
            tbEntrada.setTime(ap.getDataEntrada());
            tbEntrada.setFormattedDate(Util.getStringDateLastUpdateDash(ap.getDataEntrada()));

            listTimeline.add(tbEntrada);

            if (ap.getDataSaida() != null) {

                TimelineBerco tbSaida = new TimelineBerco();

                tbSaida.setIcon("icon-berco-up-down.png");
                tbSaida.setDescription(ap.getAisMmsi().getCodNavio().getNomeNavio() + " saiu do berço com velocidade de " + ap.getVelocidadeEntrada() + " nós");
                tbSaida.setTime(ap.getDataSaida());
                tbSaida.setFormattedDate(Util.getStringDateLastUpdateDash(ap.getDataSaida()));

                listTimeline.add(tbSaida);

            }

        }

        Comparator<TimelineBerco> comparatorAsc = (tb1, tb2) -> Long.valueOf(
                tb1.getTime().getTime())
                .compareTo(tb2.getTime().getTime()
                );

        Collections.sort(listTimeline, comparatorAsc);

        int removeQtd = listTimeline.size() - 5;
        int qtd = 0;

        while (qtd < removeQtd) {
            listTimeline.remove(0);
            qtd += 1;
        }

        Comparator<TimelineBerco> comparatorDesc = (tb1, tb2) -> Long.valueOf(
                tb2.getTime().getTime())
                .compareTo(tb1.getTime().getTime()
                );

        Collections.sort(listTimeline, comparatorDesc);

        for (TimelineBerco t : listTimeline) {
            System.out.println("reloadTimeline toString: " + t.toString());
        }

    }

    private void reloadTimeStayLast5Ships() {

        listTimeStayShipLast5 = new ArrayList<>();

        for (int i = (listAisPoin.size() - 5); i < listAisPoin.size(); i++) {

            Aispoin ap = listAisPoin.get(i);

            TimeStayShip ts = new TimeStayShip();
            ts.setName(ap.getAisMmsi().getCodNavio().getNomeNavio());
            ts.setImo(ap.getAisMmsi().getCodNavio().getImo());
            ts.setFormattedDate(Util.convertMinutesToHoursAndMinutes(ap.getTempoPermanenciaEmMinutos()));
            ts.setDurationMinutes(ap.getTempoPermanenciaEmMinutos());
            ts.setStart(ap.getDataEntrada());

            listTimeStayShipLast5.add(ts);

        }

        Comparator<TimeStayShip> comparatorDesc = (tb1, tb2) -> Long.valueOf(
                tb2.getStart().getTime())
                .compareTo(tb1.getStart().getTime()
                );

        Collections.sort(listTimeStayShipLast5, comparatorDesc);

        long durationMax = 0;

        for (TimeStayShip t : listTimeStayShipLast5) {
            if (t.getDurationMinutes() > durationMax) {
                durationMax = t.getDurationMinutes();
            }
        }

        for (TimeStayShip t : listTimeStayShipLast5) {
            t.setDurationMaxMinutes(durationMax);
            long pct = (t.getDurationMinutes() * 100) / t.getDurationMaxMinutes();
            t.setPct(pct);
        }

        for (TimeStayShip t : listTimeStayShipLast5) {
            System.out.println("reloadTimeStayLast5Ships toString: " + t.toString());
        }

    }

    private void reloadTimeStay() {

        listTimeStay = new ArrayList<>();

        for (Aispoin ap : listAisPoin) {

            TimeStayShip ts = new TimeStayShip();
            ts.setName(ap.getAisMmsi().getCodNavio().getNomeNavio());
            ts.setImo(ap.getAisMmsi().getCodNavio().getImo());
            ts.setFormattedDate(Util.convertMinutesToHoursAndMinutes(ap.getTempoPermanenciaEmMinutos()));
            ts.setDurationMinutes(ap.getTempoPermanenciaEmMinutos());
            ts.setStart(ap.getDataEntrada());

            listTimeStay.add(ts);

        }

        Comparator<TimeStayShip> comparatorAsc = (tb1, tb2) -> Long.valueOf(
                tb1.getStart().getTime())
                .compareTo(tb2.getStart().getTime()
                );

        Collections.sort(listTimeStay, comparatorAsc);

        List<Charts_Valores> values = new ArrayList<>();
        for (TimeStayShip l : listTimeStay) {
            values.add(new Charts_Valores(l.getStart(), Double.parseDouble(l.getDurationMinutes() + "")));
        }

        SeriesChartLine serie = new SeriesChartLine();
        serie.setNameSerie("");
        serie.setColorLineSerie("#FFB340");
        serie.setOpposite(false);
        serie.setData(values);

        ChartLine chart = new ChartLine();
        chart.addSerie(serie);

        PrimeFaces.current().executeScript("Chart.createChartLineArea('chart', '" + chart.toJson() + "');");

        for (TimeStayShip t : listTimeStay) {
            System.out.println("reloadTimeStay toString: " + t.toString());
        }

    }

    private void reloadInsights() {

        listInsights = new ArrayList<>();

        long durationMax = Long.MIN_VALUE;
        long durationMin = Long.MAX_VALUE;
        long durationMed = 0;
        long totalShipsExceeded24h = 0;

        for (TimeStayShip ts : listTimeStay) {
            if (ts.getDurationMinutes() > durationMax) {
                durationMax = ts.getDurationMinutes();
            }
            if (ts.getDurationMinutes() < durationMin) {
                durationMin = ts.getDurationMinutes();
            }
            durationMed += ts.getDurationMinutes();
            if (ts.getDurationMinutes() > 1440) {
                totalShipsExceeded24h += 1;
            }
        }
        durationMed = durationMed / listTimeStay.size();

        double pctExceeded = (totalShipsExceeded24h * 100.0) / listTimeStay.size();

        String ins1 = "Tempo de duração mínima: " + Util.convertMinutesToHoursAndMinutes(durationMin);
        String ins2 = "Tempo de duração média: " + Util.convertMinutesToHoursAndMinutes(durationMed);
        String ins3 = "Tempo de duração máxima: " + Util.convertMinutesToHoursAndMinutes(durationMax);
        String ins4 = Util.converterValue(pctExceeded) + "% dos navios excederam o tempo limite de estadia";

        listInsights.add(ins1);
        listInsights.add(ins2);
        listInsights.add(ins3);
        listInsights.add(ins4);

        for (String t : listInsights) {
            System.out.println("reloadInsights toString: " + t);
        }

    }

    public Date gtData(int minusMinutes) {
        Calendar d = Calendar.getInstance();
        d.add(Calendar.HOUR_OF_DAY, minusMinutes);
        return d.getTime();
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            periodSelected = "1";
            
            listBercos = BercosController.getListBercos(manager);            

            // load berco selecionado
            bercoSelected = listBercos.get(0);

            // reload metrics
            reloadMetrics(manager);

        } catch (Exception ex) {
            listAisPoin = new ArrayList<>();
            listBercos = new ArrayList<>();
            listInsights = new ArrayList<>();
            listTimeStay = new ArrayList<>();
            listTimeStayShipLast5 = new ArrayList<>();
            listTimeline = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public List<Aispoin> getListAisPoin() {
        return listAisPoin;
    }

    public void setListAisPoin(List<Aispoin> listAisPoin) {
        this.listAisPoin = listAisPoin;
    }

    public List<Berco> getListBercos() {
        return listBercos;
    }

    public void setListBercos(List<Berco> listBercos) {
        this.listBercos = listBercos;
    }

    public Berco getBercoSelected() {
        return bercoSelected;
    }

    public void setBercoSelected(Berco bercoSelected) {
        this.bercoSelected = bercoSelected;
    }

    public String getPeriodSelected() {
        return periodSelected;
    }

    public void setPeriodSelected(String periodSelected) {
        this.periodSelected = periodSelected;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getTotalVessels() {
        return totalVessels;
    }

    public void setTotalVessels(Integer totalVessels) {
        this.totalVessels = totalVessels;
    }

    public String getTmpOperative() {
        return tmpOperative;
    }

    public void setTmpOperative(String tmpOperative) {
        this.tmpOperative = tmpOperative;
    }

    public String getTmpDead() {
        return tmpDead;
    }

    public void setTmpDead(String tmpDead) {
        this.tmpDead = tmpDead;
    }

    public String getTmpOfStay() {
        return tmpOfStay;
    }

    public void setTmpOfStay(String tmpOfStay) {
        this.tmpOfStay = tmpOfStay;
    }

    public List<TimelineBerco> getListTimeline() {
        return listTimeline;
    }

    public void setListTimeline(List<TimelineBerco> listTimeline) {
        this.listTimeline = listTimeline;
    }

    public List<TimeStayShip> getListTimeStayShipLast5() {
        return listTimeStayShipLast5;
    }

    public void setListTimeStayShipLast5(List<TimeStayShip> listTimeStayShipLast5) {
        this.listTimeStayShipLast5 = listTimeStayShipLast5;
    }

    public List<TimeStayShip> getListTimeStay() {
        return listTimeStay;
    }

    public void setListTimeStay(List<TimeStayShip> listTimeStay) {
        this.listTimeStay = listTimeStay;
    }

    public List<String> getListInsights() {
        return listInsights;
    }

    public void setListInsights(List<String> listInsights) {
        this.listInsights = listInsights;
    }

}
