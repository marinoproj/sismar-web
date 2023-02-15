package br.com.marino.sismar.bean.admin.operations;

import br.com.marino.sismar.controller.EtapasOperacaoController;
import br.com.marino.sismar.controller.EventosController;
import br.com.marino.sismar.controller.InterrupcoesController;
import br.com.marino.sismar.controller.NavioController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.EtapasOperacao;
import br.com.marino.sismar.entity.Eventos;
import br.com.marino.sismar.entity.ImageVessel;
import br.com.marino.sismar.entity.Interrupcoes;
import br.com.marino.sismar.entity.Operacao;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.NavioMapAis;
import br.com.marino.sismar.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "OperationStepBean")
@ViewScoped
public class OperationStepBean implements Serializable {

    private Operacao operation;
    private Integer codStepOperation;
    private EtapasOperacao etapaOperacao;
    private Interrupcoes interrupcaoAdicionar;
    private Interrupcoes interrupcaoEditar;
    private Interrupcoes interrupcaoDeletar;
    private List<Eventos> eventos;

    @PostConstruct
    public void init() {

        interrupcaoAdicionar = new Interrupcoes();

        try {
            String cod = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("operation_step");
            codStepOperation = Integer.parseInt(cod);
        } catch (NumberFormatException ex) {
        }

        try {
            reloadInit();
        } catch (Exception ex) {
        }

    }

    @PreDestroy
    public void destroy() {
        codStepOperation = null;
        etapaOperacao = null;
        operation = null;
    }

    public void reloadInit() throws Exception {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        Clientes cliente = SessionContext.getInstance().getClientLoggedIn();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            etapaOperacao = EtapasOperacaoController.getByCod(manager, codStepOperation);
            operation = etapaOperacao.getCodOperacao();
            eventos = EventosController.getListEventosByCodCliente(manager, cliente.getCod());

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public Interrupcoes getInterrupcaoDeletar() {
        return interrupcaoDeletar;
    }

    public void setInterrupcaoDeletar(Interrupcoes interrupcaoDeletar) {
        this.interrupcaoDeletar = interrupcaoDeletar;
    }

    public Interrupcoes getInterrupcaoEditar() {
        return interrupcaoEditar;
    }

    public void setInterrupcaoEditar(Interrupcoes interrupcaoEditar) {
        this.interrupcaoEditar = interrupcaoEditar;
    }

    public List<Eventos> getEventos() {
        return eventos;
    }

    public void setEventos(List<Eventos> eventos) {
        this.eventos = eventos;
    }

    public Integer getCodStepOperation() {
        return codStepOperation;
    }

    public void setCodStepOperation(Integer codStepOperation) {
        this.codStepOperation = codStepOperation;
    }

    public EtapasOperacao getEtapaOperacao() {
        return etapaOperacao;
    }

    public void setEtapaOperacao(EtapasOperacao etapaOperacao) {
        this.etapaOperacao = etapaOperacao;
    }

    public Operacao getOperation() {
        return operation;
    }

    public void setOperation(Operacao operation) {
        this.operation = operation;
    }

    public Interrupcoes getInterrupcaoAdicionar() {
        return interrupcaoAdicionar;
    }

    public void setInterrupcaoAdicionar(Interrupcoes interrupcaoAdicionar) {
        this.interrupcaoAdicionar = interrupcaoAdicionar;
    }

    public String getEstimatedTime(EtapasOperacao etapaOperacao) {

        if (etapaOperacao.getDataInicio() == null) {
            return "";
        }

        String estimatedTime = Util.getTimeDuration(etapaOperacao.getCodEtapa().getTempoEstimadoConclusaoMin() * 60);

        Calendar estimatedFinalDate = Calendar.getInstance();
        estimatedFinalDate.setTime(etapaOperacao.getDataInicio());
        estimatedFinalDate.add(Calendar.MINUTE, etapaOperacao.getCodEtapa().getTempoEstimadoConclusaoMin());

        String estimatedFinal = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(Util.getDateTimeClient(estimatedFinalDate.getTime()));

        return "<b>Tempo estimado:</b> " + estimatedTime
                + "<br><b>Término estimado:</b> " + estimatedFinal;

    }

    public String getTempoEstimado(EtapasOperacao etapaOperacao) {

        if (etapaOperacao.getDataInicio() == null) {
            return "";
        }

        String estimatedTime = Util.getTimeDuration(etapaOperacao.getCodEtapa().getTempoEstimadoConclusaoMin() * 60);
        return estimatedTime;

    }

    public String getTerminoEstimado(EtapasOperacao etapaOperacao) {

        if (etapaOperacao.getDataInicio() == null) {
            return "";
        }

        Calendar estimatedFinalDate = Calendar.getInstance();
        estimatedFinalDate.setTime(etapaOperacao.getDataInicio());
        estimatedFinalDate.add(Calendar.MINUTE, etapaOperacao.getCodEtapa().getTempoEstimadoConclusaoMin());

        String estimatedFinal = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(Util.getDateTimeClient(estimatedFinalDate.getTime()));

        return estimatedFinal;

    }

    public String getVesselType() {

        NavioMapAis vesselMapAis = new NavioMapAis(operation.getCodNavio());
        return vesselMapAis.getCategoriaEmbarcacao();

    }

    public String getVesselDimensao() {

        NavioMapAis vesselMapAis = new NavioMapAis(operation.getCodNavio());
        return (vesselMapAis.getComprimentoReal() + "m X " + vesselMapAis.getLarguraReal() + "m");

    }

    public String getSrcMapIframe() {

        return "https://maps.google.com/maps?q=" + etapaOperacao.getLatitude()
                + "," + etapaOperacao.getLongitude() + "&hl=es;z=14&amp;output=embed";

    }

    public boolean isEmAndamento() {
        return operation.getDataTermino() == null;
    }

    public String getDuracaoOperacao() {
        String time = Util.getTimeDuration(operation.getDataInicio(),
                operation.getDataTermino() == null ? Util.getDateUTC() : operation.getDataTermino());
        return time;

    }

    public void back() throws IOException {

        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(Util.getPathUrl() + "admin/operations/operation_steps.xhtml?operacao=" + operation.getCod());

    }

    public String getImageVessel() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            ImageVessel imageVessel = NavioController
                    .getImageVesselByCod(manager, operation.getCodNavio().getCodNavio());

            return Util.getImageVessel(imageVessel.getImagem());

        } catch (Exception ex) {
            return Util.getImageVessel(null);

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }

    }

    public void paralizar() {

        if (interrupcaoAdicionar.getCodEvento() == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Atenção",
                    "É obrigatório informar o motivo!"));
            return;
        }

        if (interrupcaoAdicionar.getDataInicio() != null
                && interrupcaoAdicionar.getDataInicio().before(etapaOperacao.getDataInicio())) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Atenção",
                    "A data de início da interrupção não pode ser inferior a data inicial da etapa!"));
            return;
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            if (interrupcaoAdicionar.getDataInicio() == null) {
                interrupcaoAdicionar.setDataInicio(Util.getDateUTC());
            }

            interrupcaoAdicionar.setCodEtapaOperacao(etapaOperacao);
            interrupcaoAdicionar.setPararOperacao(true);
            interrupcaoAdicionar.setStatus(true);

            InterrupcoesController.insert(manager, interrupcaoAdicionar);

            manager.getTransaction().commit();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Etapa paralizada com sucesso!"));

            etapaOperacao.getInterrupcoesList().add(interrupcaoAdicionar);

            interrupcaoAdicionar = new Interrupcoes();

            PrimeFaces.current().executeScript("PF('dialog-paralizar-etapa').hide()");

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
        }

    }

    public void continuar() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            if (interrupcaoEditar.getDataTermino() != null
                    && interrupcaoEditar.getDataTermino().before(interrupcaoEditar.getDataInicio())) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Atenção",
                        "A data de término da interrupção não pode ser inferior a data inicial da interrupção!"));
                interrupcaoEditar.setDataTermino(null);
                return;
            }

            if (interrupcaoEditar.getDataTermino() == null) {
                interrupcaoEditar.setDataTermino(Util.getDateUTC());
            }

            InterrupcoesController.edit(manager, interrupcaoEditar);

            manager.getTransaction().commit();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Continuando etapa com sucesso!"));

            interrupcaoAdicionar = new Interrupcoes();

            PrimeFaces.current().executeScript("PF('dialog-continuar-etapa').hide()");

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
        }

    }

    public void deletar() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            InterrupcoesController.delete(manager, interrupcaoDeletar.getCod());

            manager.getTransaction().commit();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Interrupção removida com sucesso!"));
            
            etapaOperacao.getInterrupcoesList().remove(interrupcaoDeletar);

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
        }

    }

    public boolean isEditEtapaOperacao(EtapasOperacao etapaOperacao) {
        return etapaOperacao.getDataTermino() == null;
    }

}
