package br.com.marino.sismar.bean.admin.operations;

import br.com.marino.sismar.controller.AispoinController;
import br.com.marino.sismar.controller.BercoClienteController;
import br.com.marino.sismar.entity.Aispoin;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.BercoCliente;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.TimelineBerco;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
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

@ManagedBean(name = "BercoBean")
@ViewScoped
public class BercoBean implements Serializable {

    private List<Aispoin> listAisPoin;
    private List<Berco> listBercos;

    private Berco bercoSelected;
    private Integer periodSelected;
    private Date start;
    private Date end;

    // métricas
    private Integer totalVessels;
    private String tmpOperative;
    private String tmpDead;
    private String tmpOfStay;

    // segunda linha
    private List<TimelineBerco> listTimeline;

    @PostConstruct
    public void init() {
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        listAisPoin = null;
    }

    public void reloadMetrics() {

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

        // segunda linha
        reloadTimeline();

    }

    public void reloadTimeline() {

        listTimeline = new ArrayList<>();
        for (Aispoin ap : listAisPoin) {

            TimelineBerco tbEntrada = new TimelineBerco();

            tbEntrada.setIcon("entrada.png");
            tbEntrada.setDescription(ap.getAisMmsi().getCodNavio().getNomeNavio() + " entrou no berço com velocidade de " + ap.getVelocidadeEntrada() + " nós");
            tbEntrada.setTime(ap.getDataEntrada());
            tbEntrada.setFormattedDate(Util.getStringDateLastUpdate(ap.getDataEntrada(), new Date()));

            listTimeline.add(tbEntrada);

            if (ap.getDataSaida() != null) {

                TimelineBerco tbSaida = new TimelineBerco();

                tbSaida.setIcon("saida.png");
                tbSaida.setDescription(ap.getAisMmsi().getCodNavio().getNomeNavio() + " saiu do berço com velocidade de " + ap.getVelocidadeEntrada() + " nós");
                tbSaida.setTime(ap.getDataSaida());
                tbSaida.setFormattedDate(Util.getStringDateLastUpdate(ap.getDataSaida(), new Date()));

                listTimeline.add(tbSaida);

            }

        }

        Comparator<TimelineBerco> comparatorAsc = (tb1, tb2) -> Long.valueOf(
                tb1.getTime().getTime())
                .compareTo(tb2.getTime().getTime()
                );

        Collections.sort(listTimeline, comparatorAsc);

    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            Clientes cliente = SessionContext.getInstance().getClientLoggedIn();

            // load bercos
            List<BercoCliente> listBercoCliente = BercoClienteController.getList(manager, cliente.getCod());
            listBercos = new ArrayList<>();
            for (BercoCliente bc : listBercoCliente) {
                listBercos.add(bc.getCodBerco());
            }

            // load berco selecionado
            bercoSelected = listBercos.get(0);

            // load lista ais poins
            listAisPoin = AispoinController.getListShipAttracationsPeriodFromPoin(manager,
                    bercoSelected.getCodBerco(), start, end, null, true);

            // reload metrics
            reloadMetrics();

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

}
