package br.com.marino.sismar.bean.admin.moorings;

import br.com.marino.sismar.controller.AtracacaoController;
import br.com.marino.sismar.controller.BercoClienteController;
import br.com.marino.sismar.controller.ClientesController;
import br.com.marino.sismar.controller.SensorProximidadeController;
import br.com.marino.sismar.controller.SensorProximidadeStatusController;
import br.com.marino.sismar.entity.Atracacao;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.BercoCliente;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.SensorProximidade;
import br.com.marino.sismar.entity.SensorProximidadeStatus;
import br.com.marino.sismar.enums.StatusOperacaoEnum;
import br.com.marino.sismar.enums.TipoSensoresAproximacao;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
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

@ManagedBean(name = "SensorsProximidadeBean")
@ViewScoped
public class SensorsProximidadeBean implements Serializable {

    private List<SensorProximidade> sensors;
    private SensorProximidade sensorSelectedDelete;
    private List<Berco> listBercos;
    private Berco bercoSelecionado;
    private Double distanciaEntreSensores = 50.0;

    private boolean master = false;
    private Clientes client;

    @PostConstruct
    public void init() {
        Boolean m = SessionContext.getInstance().getUserLoggedIn().getMaster();
        master = (m == null ? false : m);
        
        listBercos = new ArrayList<>();

        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        sensors = null;
    }

    public Berco getBercoSelecionado() {
        return bercoSelecionado;
    }

    public void setBercoSelecionado(Berco bercoSelecionado) {
        this.bercoSelecionado = bercoSelecionado;
    }

    public List<SensorProximidade> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorProximidade> sensors) {
        this.sensors = sensors;
    }

    public SensorProximidade getSensorSelectedDelete() {
        return sensorSelectedDelete;
    }

    public void setSensorSelectedDelete(SensorProximidade sensorSelectedDelete) {
        this.sensorSelectedDelete = sensorSelectedDelete;
    }

    public String getBercoNome(Berco berco) {
        return berco == null ? "" : berco.getNome();
    }

    public boolean isInstalado(SensorProximidade sensor) {
        return sensor.getCodBerco() != null;
    }

    public Double getDistanciaEntreSensores() {
        return distanciaEntreSensores;
    }

    public void setDistanciaEntreSensores(Double distanciaEntreSensores) {
        this.distanciaEntreSensores = distanciaEntreSensores;
    }    

    public void loadDistanciaEntreSensores(){
               
        if (bercoSelecionado == null){
            distanciaEntreSensores = 50.0;
            PrimeFaces.current().executeScript("PF('statusdialog').hide()");
            return;
        }                
        
        EntityManagerFactory factory = null;
        EntityManager manager = null;
        
        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            BercoCliente bc = BercoClienteController.getByCodBerco(manager, bercoSelecionado.getCodBerco());
            
            if (bc == null || bc.getDistanciaEntreSensores() == null){
                distanciaEntreSensores = 50.0;
            } else{
                distanciaEntreSensores = bc.getDistanciaEntreSensores();
            }
            
            
        } catch (Exception ex) {
            distanciaEntreSensores = 50.0;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }
        
        PrimeFaces.current().executeScript("PF('statusdialog').hide()");
        
        
    }
    
    public boolean isComunicando(SensorProximidade sensor) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            SensorProximidadeStatus status = SensorProximidadeStatusController.getByCodSensorProximidade(manager, sensor.getCod());

            if (status == null) {
                return false;
            }

            return Util.getRangeSeconds(status.getDataHora(), Util.getDateUTC()) <= 10;

        } catch (Exception ex) {
            return false;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public boolean isLaserComunicando(SensorProximidade sensor) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            SensorProximidadeStatus status = SensorProximidadeStatusController
                    .getByCodSensorProximidade(manager, sensor.getCod());

            if (status == null) {
                return false;
            }

            return status.getUltimaLeitura() != null;

        } catch (Exception ex) {
            return false;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }
    
    public String getIpSensor(SensorProximidade sensor) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            SensorProximidadeStatus status = SensorProximidadeStatusController
                    .getByCodSensorProximidade(manager, sensor.getCod());

            if (status == null) {
                return "";
            }

            return status.getIp() == null ? "" : status.getIp();

        } catch (Exception ex) {
            return "";

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }
    
    public String getUltimaLeitura(SensorProximidade sensor) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            SensorProximidadeStatus status = SensorProximidadeStatusController
                    .getByCodSensorProximidade(manager, sensor.getCod());

            if (status == null) {
                return "";
            }

            return status.getUltimaLeitura() == null ? "" : status.getUltimaLeitura();

        } catch (Exception ex) {
            return "";

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public boolean isMaster() {
        return master;
    }

    public boolean enableAddSensor() {
        return master && (isFixo() || sensors.size() < 2);
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public boolean isFixo() {
        return client.getTipoSensoresAproximacao() == TipoSensoresAproximacao.FIXO;
    }

    public boolean isPortatil() {
        return client.getTipoSensoresAproximacao() == TipoSensoresAproximacao.PORTATIL;
    }

    public List<Berco> getListBercos() {
        return listBercos;
    }

    public void setListBercos(List<Berco> listBercos) {
        this.listBercos = listBercos;
    }

    public Clientes getClient() {
        return client;
    }

    public void setClient(Clientes client) {
        this.client = client;
    }   

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            sensors = SensorProximidadeController.getListByCodCliente(manager,
                    SessionContext.getInstance().getClientLoggedIn().getCod());

            client = ClientesController.getByCod(manager,
                    SessionContext.getInstance().getClientLoggedIn().getCod());

            bercoSelecionado = sensors.isEmpty() ? null : sensors.get(0).getCodBerco();
            
            for (BercoCliente bc : client.getBercosCliente()) {
                listBercos.add(bc.getCodBerco());
                if (bercoSelecionado != null && bc.getCodBerco().getCodBerco().intValue() == bercoSelecionado.getCodBerco().intValue()){
                    distanciaEntreSensores = bc.getDistanciaEntreSensores();
                }
            }

            
        } catch (Exception ex) {
            sensors = new ArrayList<>();
            
        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }
        

    }

    public void delete() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            sensorSelectedDelete.setStatus(false);
            SensorProximidadeController.edit(manager, sensorSelectedDelete);

            manager.getTransaction().commit();

            sensors.remove(sensorSelectedDelete);

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro removido com sucesso!"));

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
            sensorSelectedDelete = null;
        }

    }
    
    public boolean isIniciado(){        
        for(SensorProximidade sensor : sensors){
            if (sensor.getCodBerco() != null){
                return true;
            }
        }
        return false;
    }

    public void iniciar() {

        if (bercoSelecionado == null){
            PrimeFaces.current().executeScript("PF('statusdialog').hide()");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Selecione o berço para iniciar a manobra!"));
            return;
        }
        
        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            Berco bercoAnterior = null;

            for (SensorProximidade s : sensors) {
                bercoAnterior = s.getCodBerco();
                s.setCodBerco(bercoSelecionado);
                SensorProximidadeController.edit(manager, s);
            }

            // trocou de berço ou desinstalou o laser
            if (bercoAnterior != null && (bercoSelecionado == null
                    || bercoSelecionado.getCodBerco().intValue() != bercoAnterior.getCodBerco().intValue())) {

                Atracacao atracacao = AtracacaoController.getAtracacaoAtivaByCodBercoAndCodCliente(
                        manager, bercoAnterior.getCodBerco(), client.getCod());

                if (atracacao != null) {
                    
                    atracacao.setStatusOperacao(StatusOperacaoEnum.ENCERRADO);
                    atracacao.setDataEncerrado(Util.getDateUTC());
                    AtracacaoController.edit(manager, atracacao);
                    
                }

            }
            
            // salva distancia entre sensores
            BercoCliente bc = BercoClienteController.getByCodBerco(manager, bercoSelecionado.getCodBerco());            
            bc.setDistanciaEntreSensores(distanciaEntreSensores);
            BercoClienteController.edit(manager, bc);

            manager.getTransaction().commit();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Alterado com sucesso!"));            

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
    
    public void encerrar() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {
            
            bercoSelecionado = null;

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            Berco bercoAnterior = null;

            for (SensorProximidade s : sensors) {
                bercoAnterior = s.getCodBerco();
                s.setCodBerco(bercoSelecionado);
                SensorProximidadeController.edit(manager, s);
            }

            // trocou de berço ou desinstalou o laser
            if (bercoAnterior != null && (bercoSelecionado == null
                    || bercoSelecionado.getCodBerco().intValue() != bercoAnterior.getCodBerco().intValue())) {

                Atracacao atracacao = AtracacaoController.getAtracacaoAtivaByCodBercoAndCodCliente(
                        manager, bercoAnterior.getCodBerco(), client.getCod());

                if (atracacao != null) {
                    
                    atracacao.setStatusOperacao(StatusOperacaoEnum.ENCERRADO);
                    atracacao.setDataEncerrado(Util.getDateUTC());
                    AtracacaoController.edit(manager, atracacao);
                    
                }

            }

            manager.getTransaction().commit();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Encerrado com sucesso!"));
            
            distanciaEntreSensores = 50.0;

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

}
