package br.com.marino.sismar.bean.admin.moorings;

import br.com.marino.sismar.controller.AtracacaoController;
import br.com.marino.sismar.controller.ClientesController;
import br.com.marino.sismar.controller.GiaontController;
import br.com.marino.sismar.controller.ManobraController;
import br.com.marino.sismar.controller.NavioController;
import br.com.marino.sismar.controller.PraticoController;
import br.com.marino.sismar.entity.Atracacao;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Giaont;
import br.com.marino.sismar.entity.Manobra;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.Pratico;
import br.com.marino.sismar.enums.LadoAtracacaoEnum;
import br.com.marino.sismar.enums.StatusOperacaoEnum;
import br.com.marino.sismar.reports.models.RelatorioManobra;
import br.com.marino.sismar.reports.models.RelatorioManobraResumo;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Report;
import br.com.marino.sismar.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "MooringBean")
@ViewScoped
public class MooringBean implements Serializable {

    private Atracacao atracacao;
    private Integer codAtracacao;

    private Clientes client;
    private List<Giaont> listGiaont;
    private List<Pratico> listPratico;
    private List<LadoAtracacaoEnum> listLadoAtracacao;

    private boolean master = false;

    @PostConstruct
    public void init() {

        listGiaont = new ArrayList<>();
        listPratico = new ArrayList<>();

        Boolean m = SessionContext.getInstance().getUserLoggedIn().getMaster();
        master = (m == null ? false : m);

        listLadoAtracacao = Stream.of(LadoAtracacaoEnum.values())
                .collect(Collectors.toList());

        String cod = FacesContext.
                getCurrentInstance().
                getExternalContext().
                getRequestParameterMap().
                get("atracacao");

        codAtracacao = Integer.parseInt(cod);

        reloadInit();

    }

    public boolean showFinalizar() {
        return atracacao.getStatusOperacao() != StatusOperacaoEnum.ENCERRADO;
    }

    public List<LadoAtracacaoEnum> getListLadoAtracacao() {
        return listLadoAtracacao;
    }

    public void setListLadoAtracacao(List<LadoAtracacaoEnum> listLadoAtracacao) {
        this.listLadoAtracacao = listLadoAtracacao;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public Atracacao getAtracacao() {
        return atracacao;
    }

    public void setAtracacao(Atracacao atracacao) {
        this.atracacao = atracacao;
    }

    public Integer getCodAtracacao() {
        return codAtracacao;
    }

    public void setCodAtracacao(Integer codAtracacao) {
        this.codAtracacao = codAtracacao;
    }

    public Clientes getClient() {
        return client;
    }

    public void setClient(Clientes client) {
        this.client = client;
    }

    public List<Giaont> getListGiaont() {
        return listGiaont;
    }

    public void setListGiaont(List<Giaont> listGiaont) {
        this.listGiaont = listGiaont;
    }

    public List<Pratico> getListPratico() {
        return listPratico;
    }

    public void setListPratico(List<Pratico> listPratico) {
        this.listPratico = listPratico;
    }

    public String dateToString(Date data) {
        if (data == null) {
            return "";
        }
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(data);
    }

    public Double convertMetrosEmCentimetros(Double value) {
        if (value == null) {
            return null;
        }
        return value * 100;
    }

    public String getValue(Double value, String tipo) {
        if (value == null) {
            return "";
        }
        return new DecimalFormat("###,##0.00").format(Util.round(value, 2)) + " " + tipo;
    }

    public void openPlayback() throws IOException{
        
        Calendar start = Calendar.getInstance();
        start.setTime(atracacao.getDataInicio());
        start.add(Calendar.MINUTE, -10);
        
        Calendar end = Calendar.getInstance();
        end.setTime(atracacao.getDataDocagem());
        end.add(Calendar.MINUTE, 10);
                
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(Util.getPathUrl() + "admin/ais/playback.xhtml?start=" + 
                        format.format(Util.getDateTimeClient(start.getTime())) + 
                        "&end=" + format.format(Util.getDateTimeClient(end.getTime())) + "&codAtracacao=" + atracacao.getCodAtracacao());
        
    }
    
    public List<Navio> completeNavios(String query) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            return NavioController.getVesselsBySearch(manager, query, 10).stream().map(obj -> {

                Navio n = new Navio();

                n.setCodNavio(obj.getCodNavio());
                n.setNomeNavio(obj.getNomeNavio());
                n.setImo(obj.getImo());

                return n;

            }).collect(Collectors.toList());

        } catch (Exception ex) {
            return new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    @PreDestroy
    public void destroy() {
        atracacao = null;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            client = ClientesController.getByCod(manager,
                    SessionContext.getInstance().getClientLoggedIn().getCod());

            atracacao = AtracacaoController.getByCodAtracacaoAndCodCliente(manager,
                    codAtracacao, client.getCod());

            listGiaont = GiaontController.getListByCodCliente(manager, client.getCod());
            listPratico = PraticoController.getListByCodCliente(manager, client.getCod());

        } catch (Exception ex) {

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void save() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();
            AtracacaoController.edit(manager, atracacao);
            manager.getTransaction().commit();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Dados atualizados com sucesso!"));

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao efetuar ação!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
            PrimeFaces.current().executeScript("PF('statusdialog').hide()");
        }

    }

    public void finalizar() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            atracacao.setStatusOperacao(StatusOperacaoEnum.ENCERRADO);
            atracacao.setDataEncerrado(Util.getDateUTC());
            AtracacaoController.edit(manager, atracacao);

            manager.getTransaction().commit();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Finalização finalizada com sucesso!"));

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao efetuar ação!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
            PrimeFaces.current().executeScript("PF('statusdialog').hide()");
        }

    }

    public void relatorioCompleto() throws Exception {

        Map parametros = new HashMap<>();

        parametros.put("LOCAL_IMAGEM_MARINO", getClass().getResource("/br/com/marino/sismar/images/logo_marino.png"));

        if (client.getLogo() != null){
            parametros.put("LOCAL_IMAGEM_TRANSPETRO", client.getLogo());
        }
        
        parametros.put("codAtracacao", atracacao.getCodAtracacao());
        parametros.put("berco", atracacao.getCodBerco().getNome());
        parametros.put("navio", (atracacao.getCodNavio() == null ? "Não informado" : atracacao.getCodNavio().getNomeNavio()));
        
        parametros.put("dataInicio", Util.getDateTimeClient(atracacao.getDataInicio(), "dd/MM/yyyy HH:mm:ss"));
        parametros.put("dataDocagem", Util.getDateTimeClient(atracacao.getDataDocagem(), "dd/MM/yyyy HH:mm:ss"));
        parametros.put("dataPartida", Util.getDateTimeClient(atracacao.getDataPartida(), "dd/MM/yyyy HH:mm:ss"));
        parametros.put("dataFinal", Util.getDateTimeClient(atracacao.getDataEncerrado(), "dd/MM/yyyy HH:mm:ss"));

        if (atracacao.getDataDocagem() != null) {
            DateTime dataAnt = new DateTime(atracacao.getDataInicio());
            DateTime dataDep = new DateTime(atracacao.getDataDocagem());
            int qtdSegundos;
            int horas = 0;
            int minutos = 0;
            int segundos = 0;
            try {
                qtdSegundos = Seconds.secondsBetween(dataAnt, dataDep).getSeconds();
                horas = qtdSegundos / 3600;
                minutos = qtdSegundos % 3600 / 60;
                segundos = qtdSegundos % 3600 % 60;
            } catch (Exception ex) {
            }
            parametros.put("tmpManobra", "Hrs: " + horas + " Min: " + minutos + " Seg: " + segundos);
        } else {
            parametros.put("tmpManobra", "");
        }

        if (atracacao.getDataDocagem() != null && atracacao.getDataPartida() != null) {
            DateTime dataAnt = new DateTime(atracacao.getDataDocagem());
            DateTime dataDep = new DateTime(atracacao.getDataPartida());
            int qtdSegundos;
            int horas = 0;
            int minutos = 0;
            int segundos = 0;
            try {
                qtdSegundos = Seconds.secondsBetween(dataAnt, dataDep).getSeconds();
                horas = qtdSegundos / 3600;
                minutos = qtdSegundos % 3600 / 60;
                segundos = qtdSegundos % 3600 % 60;
            } catch (Exception ex) {
            }
            parametros.put("tmpDocado", "Hrs: " + horas + " Min: " + minutos + " Seg: " + segundos);
        } else {
            parametros.put("tmpDocado", "");
        }

        parametros.put("comandante", (atracacao.getCodNavio() == null ? "Não informado" : atracacao.getCodNavio().getNomeComandante() == null ? "Não informado" : atracacao.getCodNavio().getNomeComandante()));
        parametros.put("pratico", (atracacao.getCodPratico() == null ? "Não informado" : atracacao.getCodPratico().getNomePratico()));
        parametros.put("velMaxDir", getVelocidade(atracacao.getVelocidadeMaximaDireita(), "cm/s"));
        parametros.put("velMaxEsq", getVelocidade(atracacao.getVelocidadeMaximaEsquerda(), "cm/s"));
        parametros.put("velToqDir", getVelocidade(atracacao.getVelocidadeToqueDireita(), "cm/s"));
        parametros.put("velToqEsq", getVelocidade(atracacao.getVelocidadeToqueEsquerda(), "cm/s"));
        parametros.put("angMax", getValue(atracacao.getAnguloMaximo(), "º"));
        parametros.put("anguloToque", getValue(atracacao.getAnguloToque(), "º"));

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            List<RelatorioManobra> lista = new ArrayList<>();            
            for(Manobra m : ManobraController.getList(manager, atracacao.getCodAtracacao())){
                lista.add(new RelatorioManobra(m));
            }

            Report report = new Report();
            report.getCompletoManobra("/br/com/marino/sismar/reports/relatorio_completo_manobra.jrxml",
                    lista,
                    parametros);

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao efetuar ação!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
            PrimeFaces.current().executeScript("PF('statusdialog').hide()");
        }

    }

    public void relatorioResumido() throws Exception {

        List<RelatorioManobraResumo> lista = new ArrayList<>();
        lista.add(new RelatorioManobraResumo(atracacao));

        Map params = new HashMap<>();
        params.put("LOCAL_IMAGEM_MARINO", getClass().getResource(
                "/br/com/marino/sismar/images/logo_marino.png"));
        
        if (client.getLogo() != null){
            params.put("LOCAL_IMAGEM_TRANSPETRO", client.getLogo());
        }

        Report report = new Report();
        report.getResumoManobra("/br/com/marino/sismar/reports/relatorio_resumo_manobra.jrxml",
                lista,
                params);

    }

    private String getVelocidade(Double value, String tipo) {
        if (value == null) {
            return "";
        }
        return getValue(value * 100, tipo);
    }

}
