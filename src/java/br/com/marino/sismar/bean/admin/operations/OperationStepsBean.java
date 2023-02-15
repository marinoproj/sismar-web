package br.com.marino.sismar.bean.admin.operations;

import br.com.marino.sismar.controller.EtapasController;
import br.com.marino.sismar.controller.EtapasOperacaoController;
import br.com.marino.sismar.controller.InterrupcoesController;
import br.com.marino.sismar.controller.NavioController;
import br.com.marino.sismar.controller.OperacaoController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Etapas;
import br.com.marino.sismar.entity.EtapasOperacao;
import br.com.marino.sismar.entity.ImageVessel;
import br.com.marino.sismar.entity.Interrupcoes;
import br.com.marino.sismar.entity.Operacao;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.NavioMapAis;
import br.com.marino.sismar.util.Util;
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
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

@ManagedBean(name = "OperationStepsBean")
@ViewScoped
public class OperationStepsBean implements Serializable {

    private Integer codOperation;

    private Operacao operation;
    private List<EtapasOperacao> etapasOperacao;

    private EtapasOperacao etapaOperacaoEditar;
    private EtapasOperacao etapaOperacaoAdicionar;
    private EtapasOperacao etapaOperacaoDelete;

    private List<Etapas> etapas;

    private boolean selectable = true;
    private boolean zoomable = true;
    private boolean moveable = true;
    private boolean stackEvents = true;
    private String eventStyle = "box";
    private boolean axisOnTop;
    private boolean showCurrentTime = true;
    private boolean showNavigation = false;

    @PostConstruct
    public void init() {

        etapaOperacaoAdicionar = new EtapasOperacao();

        try {
            String cod = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("operacao");
            codOperation = Integer.parseInt(cod);
        } catch (NumberFormatException ex) {
        }

        try {
            reloadInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public TimelineModel getModelTimeline() {

        TimelineModel model = new TimelineModel();

        etapasOperacao.forEach(etapaOperacao -> {

            TimelineEvent event = new TimelineEvent();

            event.setStartDate(Util.getDateTimeClient(etapaOperacao.getDataInicio()));
            event.setEndDate((etapaOperacao.getDataTermino() == null
                    ? Util.getDateTimeClient(Util.getDateUTC())
                    : Util.getDateTimeClient(etapaOperacao.getDataTermino())));
            event.setData(etapaOperacao.getCodEtapa().getNome());
            event.setStyleClass("event-timeline");

            model.add(event);

        });

        return model;

    }

    public void onSelectStep(TimelineSelectEvent e) {

        TimelineEvent timelineEvent = e.getTimelineEvent();

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Etapa: ", timelineEvent.getData().toString()
                + ", Início: " + Util.dateToString(timelineEvent.getStartDate(), "dd/MM/yyyy HH:mm"));
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    @PreDestroy
    public void destroy() {
        operation = null;
        etapasOperacao = null;
        etapaOperacaoAdicionar = null;
        etapaOperacaoEditar = null;
    }

    public void reloadInit() throws Exception {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            Clientes cliente = SessionContext.getInstance().getClientLoggedIn();

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            operation = OperacaoController.getByCod(manager, codOperation);
            etapas = EtapasController.getListEtapasByCodCliente(manager, cliente.getCod());
            etapasOperacao = EtapasOperacaoController.getListEtapasOperacaoByCodUsuario(manager, operation.getCod());

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public Integer getCodOperation() {
        return codOperation;
    }

    public void setCodOperation(Integer codOperation) {
        this.codOperation = codOperation;
    }

    public Operacao getOperation() {
        return operation;
    }

    public void setOperation(Operacao operation) {
        this.operation = operation;
    }

    public List<EtapasOperacao> getEtapasOperacao() {
        return etapasOperacao;
    }

    public void setEtapasOperacao(List<EtapasOperacao> etapasOperacao) {
        this.etapasOperacao = etapasOperacao;
    }

    public EtapasOperacao getEtapaOperacaoEditar() {
        return etapaOperacaoEditar;
    }

    public void setEtapaOperacaoEditar(EtapasOperacao etapaOperacaoEditar) {
        this.etapaOperacaoEditar = etapaOperacaoEditar;
    }

    public EtapasOperacao getEtapaOperacaoAdicionar() {
        return etapaOperacaoAdicionar;
    }

    public void setEtapaOperacaoAdicionar(EtapasOperacao etapaOperacaoAdicionar) {
        this.etapaOperacaoAdicionar = etapaOperacaoAdicionar;
    }

    public EtapasOperacao getEtapaOperacaoDelete() {
        return etapaOperacaoDelete;
    }

    public void setEtapaOperacaoDelete(EtapasOperacao etapaOperacaoDelete) {
        this.etapaOperacaoDelete = etapaOperacaoDelete;
    }

    public List<Etapas> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<Etapas> etapas) {
        this.etapas = etapas;
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

    public boolean isEmAndamento() {
        return operation.getDataTermino() == null;
    }

    public String getDuracaoOperacao() {
        String time = Util.getTimeDuration(operation.getDataInicio(),
                operation.getDataTermino() == null ? Util.getDateUTC() : operation.getDataTermino());
        return time;

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

    public boolean isEditEtapaOperacao(EtapasOperacao etapaOperacao) {
        return etapaOperacao.getDataTermino() == null;
    }

    public void addStep() {
        
        if (etapaOperacaoAdicionar.getCodEtapa() == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Atenção",
                    "É obrigatório informar a etapa!"));
            return;
        }        

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            if (etapaOperacaoAdicionar.getDataInicio() == null) {
                etapaOperacaoAdicionar.setDataInicio(Util.getDateUTC());
            }

            etapaOperacaoAdicionar.setStatus(true);
            etapaOperacaoAdicionar.setCodOperacao(operation);

            EtapasOperacaoController.insert(manager, etapaOperacaoAdicionar);

            manager.getTransaction().commit();

            etapasOperacao.add(etapaOperacaoAdicionar);

            PrimeFaces.current().executeScript("PF('dialog-add-step').hide()");

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Etapa adicionada com sucesso!"));

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
            etapaOperacaoAdicionar = new EtapasOperacao();
        }

    }

    public void editStep() {

        if (etapaOperacaoEditar.getDataInicio() == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Atenção",
                    "A data inicial é obrigatório!"));
            return;
        }

        if (etapaOperacaoEditar.getDataTermino() != null
                && etapaOperacaoEditar.getDataTermino().before(etapaOperacaoEditar.getDataInicio())) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Atenção",
                    "A  data de término não pode ser anterior a data inicial!"));
            etapaOperacaoEditar.setDataTermino(null);
            return;
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            etapaOperacaoEditar.setStatus(true);
            etapaOperacaoEditar.setCodOperacao(operation);

            EtapasOperacaoController.edit(manager, etapaOperacaoEditar);

            if (etapaOperacaoEditar.getDataTermino() != null){
                for(Interrupcoes i : etapaOperacaoEditar.getInterrupcoesList()){
                    if (i.getDataTermino() == null){
                        i.setDataTermino(etapaOperacaoEditar.getDataTermino());
                        InterrupcoesController.edit(manager, i);
                    }
                }
            }
            
            manager.getTransaction().commit();

            etapasOperacao.set(etapasOperacao.indexOf(etapaOperacaoEditar), etapaOperacaoEditar);

            PrimeFaces.current().executeScript("PF('dialog-edit-step').hide()");

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Etapa atualizada com sucesso!"));

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
            etapaOperacaoEditar = null;
        }

    }

    public void delete() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            etapaOperacaoDelete.setStatus(false);
            EtapasOperacaoController.edit(manager, etapaOperacaoDelete);

            manager.getTransaction().commit();

            etapasOperacao.remove(etapaOperacaoDelete);

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Etapa removida com sucesso!"));

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
            etapaOperacaoDelete = null;
        }

    }

    public void close() {

        if (etapasOperacao.stream().anyMatch(etapa -> etapa.getDataTermino() == null)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Atenção",
                    "Existem etapas a serem finalizadas. Finalize-as para poder encerrar essa operação!"));
            return;
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            operation.setDataTermino(Util.getDateUTC());

            OperacaoController.edit(manager, operation);

            manager.getTransaction().commit();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Operação finalizada com sucesso!"));

        } catch (Exception ex) {
            operation.setDataTermino(null);
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

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isZoomable() {
        return zoomable;
    }

    public void setZoomable(boolean zoomable) {
        this.zoomable = zoomable;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    public boolean isStackEvents() {
        return stackEvents;
    }

    public void setStackEvents(boolean stackEvents) {
        this.stackEvents = stackEvents;
    }

    public String getEventStyle() {
        return eventStyle;
    }

    public void setEventStyle(String eventStyle) {
        this.eventStyle = eventStyle;
    }

    public boolean isAxisOnTop() {
        return axisOnTop;
    }

    public void setAxisOnTop(boolean axisOnTop) {
        this.axisOnTop = axisOnTop;
    }

    public boolean isShowCurrentTime() {
        return showCurrentTime;
    }

    public void setShowCurrentTime(boolean showCurrentTime) {
        this.showCurrentTime = showCurrentTime;
    }

    public boolean isShowNavigation() {
        return showNavigation;
    }

    public void setShowNavigation(boolean showNavigation) {
        this.showNavigation = showNavigation;
    }

}
