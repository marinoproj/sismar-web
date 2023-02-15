package br.com.marino.sismar.bean.admin.moorings;

import br.com.marino.sismar.controller.ClientesController;
import br.com.marino.sismar.controller.SensorProximidadeBercoController;
import br.com.marino.sismar.controller.SensorProximidadeController;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.BercoCliente;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.SensorProximidade;
import br.com.marino.sismar.entity.SensorProximidadeBerco;
import br.com.marino.sismar.enums.LadoSensorProximidade;
import br.com.marino.sismar.enums.TipoSensoresAproximacao;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import org.primefaces.PrimeFaces;

@ManagedBean(name = "SensorProximidadeBean")
@ViewScoped
public class SensorProximidadeBean implements Serializable {

    private SensorProximidade sensorProximidade;
    private Integer codSensorProximidade;

    private List<LadoSensorProximidade> listLadoSensorProximidade;
    private Clientes client;
    private List<SensorProximidadeBerco> listSensorBercos;
    private List<Berco> listBercos;
    
    private boolean master = false;

    @PostConstruct
    public void init() {

        sensorProximidade = new SensorProximidade();
        listBercos = new ArrayList<>();
        
        Boolean m = SessionContext.getInstance().getUserLoggedIn().getMaster();
        master = (m == null ? false : m);

        listLadoSensorProximidade = Stream.of(LadoSensorProximidade.values())
                .collect(Collectors.toList());

        try {
            String cod = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("sensor");
            if (cod != null && !cod.isEmpty()) {
                codSensorProximidade = Integer.parseInt(cod);
            }
        } catch (NumberFormatException ex) {
            codSensorProximidade = null;
        }

        reloadInit();

    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }
    
    public boolean isFixo(){
        return client.getTipoSensoresAproximacao() == TipoSensoresAproximacao.FIXO;
    }
    
    public boolean isPortatil(){
        return client.getTipoSensoresAproximacao() == TipoSensoresAproximacao.PORTATIL;
    }
    
    public SensorProximidade getSensorProximidade() {
        return sensorProximidade;
    }

    public void setSensorProximidade(SensorProximidade sensorProximidade) {
        this.sensorProximidade = sensorProximidade;
    }

    public Integer getCodSensorProximidade() {
        return codSensorProximidade;
    }

    public void setCodSensorProximidade(Integer codSensorProximidade) {
        this.codSensorProximidade = codSensorProximidade;
    }

    public List<LadoSensorProximidade> getListLadoSensorProximidade() {
        return listLadoSensorProximidade;
    }

    public void setListLadoSensorProximidade(List<LadoSensorProximidade> listLadoSensorProximidade) {
        this.listLadoSensorProximidade = listLadoSensorProximidade;
    }

    public List<Berco> getListBercos() {
        return listBercos;
    }

    public void setListBercos(List<Berco> listBercos) {
        this.listBercos = listBercos;
    }
    
    @PreDestroy
    public void destroy() {
        sensorProximidade = null;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            client = ClientesController.getByCod(manager,
                    SessionContext.getInstance().getClientLoggedIn().getCod());            

            if (codSensorProximidade != null) {

                sensorProximidade = SensorProximidadeController.getByCod(manager, codSensorProximidade);

                listSensorBercos = SensorProximidadeBercoController
                        .getListByCodSensorProximidade(manager, codSensorProximidade);

            } else {

                listSensorBercos = new ArrayList<>();

            }

            for (BercoCliente bc : client.getBercosCliente()) {
                
                if (listSensorBercos.stream().noneMatch(obj -> obj.getCodBerco().getCodBerco()
                        .intValue() == bc.getCodBerco().getCodBerco().intValue())) {

                    SensorProximidadeBerco spb = new SensorProximidadeBerco();
                    spb.setCodBerco(bc.getCodBerco());
                    spb.setDescontoDistancia(0D);

                    listSensorBercos.add(spb);

                }
                
                listBercos.add(bc.getCodBerco());
                
            }            
            
        } catch (Exception ex) {
            sensorProximidade = new SensorProximidade();
            codSensorProximidade = null;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public boolean isActionNew() {
        return codSensorProximidade == null;
    }

    public List<SensorProximidadeBerco> getListSensorBercos() {
        return listSensorBercos;
    }

    public void setListSensorBercos(List<SensorProximidadeBerco> listSensorBercos) {
        this.listSensorBercos = listSensorBercos;
    }

    public void save() {

        if (sensorProximidade.getSerial().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(
                    "form-event",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Preencha o formulário corretamente. Dados obrigatórios não informado"));
            return;
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            sensorProximidade.setStatus(true);
            sensorProximidade.setCodCliente(SessionContext.getInstance().getClientLoggedIn());

            manager.getTransaction().begin();

            if (codSensorProximidade == null) {

                SensorProximidadeController.insert(manager, sensorProximidade);

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Sensor cadastrado com sucesso!"));

            } else {

                sensorProximidade.setCod(codSensorProximidade);
                SensorProximidadeController.edit(manager, sensorProximidade);

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Sensor atualizado com sucesso!"));

            }

            for (SensorProximidadeBerco spb : listSensorBercos) {

                spb.setCodSensorProximidade(sensorProximidade);
                
                if (spb.getCod() == null) {
                    SensorProximidadeBercoController.insert(manager, spb);
                }else{
                    SensorProximidadeBercoController.edit(manager, spb);
                }

            }

            manager.getTransaction().commit();
            codSensorProximidade = sensorProximidade.getCod();

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

    public void delete() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            sensorProximidade.setStatus(false);
            SensorProximidadeController.edit(manager, sensorProximidade);

            manager.getTransaction().commit();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro removido com sucesso!"));

            FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl()
                    + "admin/moorings/sensors.xhtml");

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

}
