package br.com.marino.sismar.bean.admin.ais;

import br.com.marino.sismar.controller.AispoinController;
import br.com.marino.sismar.controller.BercosController;
import br.com.marino.sismar.controller.MovimentacaoPortoParametrosController;
import br.com.marino.sismar.entity.Aispoin;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.DurationOfStay;
import br.com.marino.sismar.entity.DurationOfStayTotalPeriod;
import br.com.marino.sismar.entity.MovimentacaoPortoParametros;
import br.com.marino.sismar.entity.VesselDockedNow;
import br.com.marino.sismar.entity.VesselDockedStepTimeline;
import br.com.marino.sismar.util.NavioMapAis;
import br.com.marino.sismar.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "BercoDashBean")
@ViewScoped
public class BercoDashBean implements Serializable {

    private List<Aispoin> listAisPoin;
    private List<Berco> listBercos;

    private String historic;
    private Integer codBerco;
    private Berco bercoSelected;

    private Date start;
    private Date end;

    private Integer totalVessels;
    private String tmpOperative;
    private String tmpDead;
    private String tmpOfStay;

    private DurationOfStay durationOfStay;
    private VesselDockedNow vesselDockedNow;

    @PostConstruct
    public void init() {

        try {
            codBerco = Integer.parseInt(FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("berco"));
        } catch (NumberFormatException ex) {
            codBerco = null;
        }

        try {
            historic = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("historic");
        } catch (NumberFormatException ex) {
            historic = null;
        }

        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        listAisPoin = null;
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
        return historic;
    }

    public void setPeriodSelected(String periodSelected) {
        this.historic = periodSelected;
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

    public DurationOfStay getDurationOfStay() {
        return durationOfStay;
    }

    public void setDurationOfStay(DurationOfStay durationOfStay) {
        this.durationOfStay = durationOfStay;
    }

    public VesselDockedNow getVesselDockedNow() {
        return vesselDockedNow;
    }

    public void setVesselDockedNow(VesselDockedNow vesselDockedNow) {
        this.vesselDockedNow = vesselDockedNow;
    }

    private List<Aispoin> organize(List<Aispoin> list) {

        for (Aispoin aispoin : list) {

            System.out.println(aispoin.getAisMmsi().getCodNavio().getMmsi() + " / entrada: " + aispoin.getDataEntrada()
                    + " / saida: " + aispoin.getDataSaida());

        }

        return list;

        /*if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        int codNavioFlag = 0;

        List<List<Aispoin>> newListGroup = new ArrayList<>();

        List<Aispoin> newList = null;

        for (Aispoin ap : list) {

            int codNavio = ap.getAisMmsi().getCodNavio().getCodNavio();

            // inicialmente será diferente
            if (codNavio != codNavioFlag) {

                // inicialmente sera nulo
                if (newList != null && !newList.isEmpty()) {
                    newListGroup.add(newList);
                }

                newList = new ArrayList<>();
                newList.add(ap);
                codNavioFlag = codNavio;

                continue;

            }

            if (newList != null) {
                newList.add(ap);
            }

        }

        if (newList != null && !newList.isEmpty()) {
            newListGroup.add(newList);
        }

        List<Aispoin> newListFinal = new ArrayList<>();

        for (List<Aispoin> newListG : newListGroup) {

            Aispoin first = newListG.get(0);

            if (newListG.size() > 1) {
                Aispoin last = newListG.get(newListG.size() - 1);
                first.setDataSaida(last.getDataSaida());
                first.setVelocidadeSaida(last.getVelocidadeSaida());
            }

            newListFinal.add(first);

        }

        Comparator<Aispoin> comparatorAsc = (tb1, tb2) -> Long.valueOf(
                tb1.getDataEntrada().getTime())
                .compareTo(tb2.getDataEntrada().getTime()
                );

        Collections.sort(newListFinal, comparatorAsc);

        return newListFinal;*/
    }

    public void reloadMetrics(String period, EntityManager manager) throws Exception {

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl() + "admin/ais/berco.xhtml?berco=" + bercoSelected.getCodBerco() + "&historic=" + period);
        } catch (IOException ex) {
        }
        
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            if (historic == null) {
                historic = "7days";
            }

            listBercos = BercosController.getListBercos(manager);

            if (codBerco == null) {
                for (Berco berco : listBercos) {
                    if (berco.getNome().contains("P1")) {
                        bercoSelected = berco;
                        codBerco = bercoSelected.getCodBerco();
                        break;
                    }
                }
            } else {
                for (Berco berco : listBercos) {
                    if (berco.getCodBerco().intValue() == codBerco.intValue()) {
                        bercoSelected = berco;
                        break;
                    }
                }
            }

            System.out.println("total de bercos: " + listBercos.size() + " / berco padrão selecionado: " + (bercoSelected == null ? null : bercoSelected.getNome()));

            System.out.println("periodo inputado recebido como parametro: " + historic);

            loadPeriod(historic);

            System.out.println("data de inicio: " + start + " / data fim: " + end);

            listAisPoin = AispoinController
                    .getListShipAttracationsPeriodFromPoin(
                            manager,
                            bercoSelected.getCodPoin().getCodPoin(),
                            start,
                            end,
                            20,
                            true
                    );

            System.out.println("total da listAisPoin: " + listAisPoin.size());

            listAisPoin = organize(listAisPoin);

            System.out.println("total da listAisPoin após organize: " + listAisPoin.size());

            totalVessels = listAisPoin.size();

            loadOperationTime(listAisPoin);

            loadDurationOfStay(listAisPoin);

            loadVesselDockedNow(manager);

        } catch (Exception ex) {
            listAisPoin = new ArrayList<>();
            listBercos = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    private void loadPeriod(String period) {

        historic = period;

        end = new Date();

        switch (historic) {

            case "7days": {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, -7);
                start = c.getTime();
                break;
            }

            case "30days": {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, -30);
                start = c.getTime();
                break;
            }

            case "3months": {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, -3);
                start = c.getTime();
                break;
            }

            default:
                historic = "7days";
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, -7);
                start = c.getTime();
                break;
        }

    }

    private void loadOperationTime(List<Aispoin> listAisPoin) {

        double totalTmpMinutes = Util.getTempoPermanenciaEmMinutos(start, end);

        System.out.println("duração total do período em minutos: " + totalTmpMinutes);

        double totalTmpOperativeMinutes = 0;

        for (Aispoin ap : listAisPoin) {

            if (ap.getDataEntrada().before(start) && ap.getDataSaida().after(start)) {
                totalTmpOperativeMinutes += (ap.getTempoPermanenciaEmMinutos() - Util.getTempoPermanenciaEmMinutos(ap.getDataEntrada(), start));
            } else {
                totalTmpOperativeMinutes += ap.getTempoPermanenciaEmMinutos();
            }

        }

        System.out.println("duração total em minutos utilizado: " + totalTmpOperativeMinutes);

        // tempo operante
        tmpOperative = Util.convertMinutesToHoursAndMinutes((long) totalTmpOperativeMinutes);
        tmpOperative += " (" + Util.formatarValor((totalTmpOperativeMinutes * 100.0) / totalTmpMinutes) + "%)";

        System.out.println("tempo operante formatado: " + tmpOperative);

        // tempo inoperante
        tmpDead = Util.convertMinutesToHoursAndMinutes((long) (totalTmpMinutes - totalTmpOperativeMinutes));
        tmpDead += " (" + Util.formatarValor(((totalTmpMinutes - totalTmpOperativeMinutes) * 100.0) / totalTmpMinutes) + "%)";

        System.out.println("tempo inoperante formatado: " + tmpDead);

        // tempo médio
        tmpOfStay = Util.convertMinutesToHoursAndMinutes(totalVessels == 0 ? 0 : (long) (totalTmpOperativeMinutes / totalVessels));

        System.out.println("tempo médio formatado: " + tmpOfStay);

    }

    private void loadDurationOfStay(List<Aispoin> listAisPoin) {

        durationOfStay = new DurationOfStay();

        if (listAisPoin.isEmpty()) {

            DurationOfStayTotalPeriod within24Hours = new DurationOfStayTotalPeriod();
            within24Hours.setTotalVessels(0);
            within24Hours.setTotalPct("");

            DurationOfStayTotalPeriod within48Hours = new DurationOfStayTotalPeriod();
            within48Hours.setTotalVessels(0);
            within48Hours.setTotalPct("");

            DurationOfStayTotalPeriod over48Hours = new DurationOfStayTotalPeriod();
            over48Hours.setTotalVessels(0);
            over48Hours.setTotalPct("");

            durationOfStay.setWithin24Hours(within24Hours);
            durationOfStay.setWithin48Hours(within48Hours);
            durationOfStay.setOver48Hours(over48Hours);

            return;
        }

        long totalWithin24Hours = 0;
        long totalWithin48Hours = 0;
        long totalOver48Hours = 0;
        double total = 0;

        for (Aispoin ap : listAisPoin) {
            if (ap.getDataSaida() == null) {
                continue;
            }
            total += 1;
            long durationMinutes = ap.getTempoPermanenciaEmMinutos();
            if (durationMinutes <= 1440) {
                totalWithin24Hours += 1;
                continue;
            }
            if (durationMinutes <= 2880) {
                totalWithin48Hours += 1;
                continue;
            }
            totalOver48Hours += 1;
        }

        System.out.println("total de atracações: " + total);
        System.out.println("total abaixo de 24h: " + totalWithin24Hours);
        System.out.println("total abaixo de 48h: " + totalWithin48Hours);
        System.out.println("total acima de 48h: " + totalOver48Hours);

        String totalPctWithin24Hours = Util.formatarValor((totalWithin24Hours * 100.0) / total);
        String totalPctWithin48Hours = Util.formatarValor((totalWithin48Hours * 100.0) / total);
        String totalPctOver48Hours = Util.formatarValor((totalOver48Hours * 100.0) / total);

        durationOfStay = new DurationOfStay();

        DurationOfStayTotalPeriod within24Hours = new DurationOfStayTotalPeriod();
        within24Hours.setTotalVessels(totalWithin24Hours);
        within24Hours.setTotalPct(totalPctWithin24Hours);

        DurationOfStayTotalPeriod within48Hours = new DurationOfStayTotalPeriod();
        within48Hours.setTotalVessels(totalWithin48Hours);
        within48Hours.setTotalPct(totalPctWithin48Hours);

        DurationOfStayTotalPeriod over48Hours = new DurationOfStayTotalPeriod();
        over48Hours.setTotalVessels(totalOver48Hours);
        over48Hours.setTotalPct(totalPctOver48Hours);

        durationOfStay.setWithin24Hours(within24Hours);
        durationOfStay.setWithin48Hours(within48Hours);
        durationOfStay.setOver48Hours(over48Hours);

        System.out.println("total pct abaixo de 24h: " + totalPctWithin24Hours);
        System.out.println("total pct abaixo de 48h: " + totalPctWithin48Hours);
        System.out.println("total pct acima de 48h: " + totalPctOver48Hours);

    }

    private void loadVesselDockedNow(EntityManager manager) throws Exception {

        Aispoin shipNow = AispoinController
                .getShipNowFromPoin(
                        manager,
                        bercoSelected.getCodPoin().getCodPoin(),
                        10
                );

        System.out.println("tem navio atracado: " + (shipNow != null) + " / aispoin: " + shipNow);

        vesselDockedNow = new VesselDockedNow();

        if (shipNow == null) {
            vesselDockedNow.setHasShip(false);
            return;
        }

        NavioMapAis vesselMap = new NavioMapAis(shipNow.getAisMmsi().getCodNavio());

        vesselDockedNow.setHasShip(true);

        vesselDockedNow.setName(shipNow.getAisMmsi().getCodNavio().getNomeNavio());
        vesselDockedNow.setImo(shipNow.getAisMmsi().getCodNavio().getImo() + "");
        vesselDockedNow.setType(vesselMap.getCategoriaEmbarcacao());
        vesselDockedNow.setImage(Util.getImageVessel(shipNow.getAisMmsi().getCodNavio().getImage()));

        MovimentacaoPortoParametros movimentacaoPortoParametros = MovimentacaoPortoParametrosController
                .getMovimentacaoPortoParametrosByCodArea(manager, bercoSelected.getCodArea().getCodArea());

        System.out.println("parametros do porto: " + movimentacaoPortoParametros);

        if (movimentacaoPortoParametros == null) {
            return;
        }

        Aispoin aisPoinAnchorage = AispoinController
                .getLastAispoinByMssiAndCodPoin(
                        manager,
                        shipNow.getAisMmsi().getCodNavio().getMmsi(),
                        movimentacaoPortoParametros.getCodPoinFundeio().getCodPoin()
                );

        System.out.println("ais poin de fundeio: " + aisPoinAnchorage);

        VesselDockedStepTimeline anchorage = VesselDockedStepTimeline
                .build(aisPoinAnchorage.getDataEntrada(), aisPoinAnchorage.getDataSaida());

        VesselDockedStepTimeline navigation = VesselDockedStepTimeline
                .build(aisPoinAnchorage.getDataSaida(), shipNow.getDataEntrada());

        VesselDockedStepTimeline berth = VesselDockedStepTimeline
                .build(shipNow.getDataEntrada(), null);

        Calendar expectedEndDate = Calendar.getInstance();
        expectedEndDate.setTime(shipNow.getDataEntrada());
        expectedEndDate.add(Calendar.HOUR_OF_DAY, 24);

        long durationTotal = Util.getTempoPermanenciaEmMinutos(shipNow.getDataEntrada(), expectedEndDate.getTime());
        long duration = Util.getTempoPermanenciaEmMinutos(shipNow.getDataEntrada(), new Date());
        long progress;

        if (duration > durationTotal) {
            progress = 100;
            long durationTimeExceeded = Util.getTempoPermanenciaEmMinutos(expectedEndDate.getTime(), new Date());
            String timeExceeded = Util.convertMinutesToHoursAndMinutes(durationTimeExceeded);
            vesselDockedNow.setTimeExceeded(timeExceeded);

        } else {
            progress = (long) Util.round((duration * 100) / durationTotal, 0);
        }

        vesselDockedNow.setExpectedEndDate(Util.getStringDateLastUpdateDash(expectedEndDate.getTime()));
        vesselDockedNow.setProgress(progress);

        vesselDockedNow.setAnchorage(anchorage);
        vesselDockedNow.setNavigation(navigation);
        vesselDockedNow.setBerth(berth);
        
    }

    public String getClassForPeriodHistoricSelected(String period) {
        if (historic.equalsIgnoreCase(period)) {
            return "active-period";
        }
        return "";
    }

}
