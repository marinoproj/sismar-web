package br.com.marino.sismar.bean.admin.components;

import br.com.marino.sismar.controller.AtracacaoController;
import br.com.marino.sismar.controller.ClientesController;
import br.com.marino.sismar.entity.Atracacao;
import br.com.marino.sismar.entity.BercoCliente;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "MenuBean")
@ViewScoped
public class MenuBean implements Serializable {

    public boolean isActiveFeature(String nameFeature) {
        return Util.isActiveFeature(nameFeature);
    }

    public boolean isActiveFeatureMenuAis() {
        return isActiveFeature("ais_realtime")
                || isActiveFeature("ais_search")
                || isActiveFeature("ais_playback")
                || isActiveFeature("ais_track")
                || isActiveFeature("ais_porto");
    }

    public boolean isActiveFeatureMenuMeteorology() {
        return isActiveFeature("meteorology_climatempo")
                || isActiveFeature("meteorology_windy")
                || isActiveFeature("meteorology_all");

    }

    public boolean isActiveFeatureMenuOperation() {
        return isActiveFeature("operations_steps")
                || isActiveFeature("operations_events")
                || isActiveFeature("operations_operations");
    }

    public boolean isActiveFeatureMenuConfig() {
        return isActiveFeature("config_users")
                || isActiveFeature("config_api");
    }

    public boolean isActiveFeatureMenuAdministration() {
        return isActiveFeature("admin_configgerais")
                || isActiveFeature("admin_clients")
                || isActiveFeature("admin_bercos");
    }

    public boolean isActiveFeatureMenuMoorings() {
        return isActiveFeature("moorings_sensors")
                || isActiveFeature("moorings_search")
                || isActiveFeature("moorings_realtime");
    }
    
    public boolean isActiveFeatureSensors() {
        return isActiveFeature("sensors_hydrocarbon");
    }

    public boolean isActiveFeatureMenuManeuvers() {
        return false;
    }

    public void openPageAtracacaoRealtime() throws IOException {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        Integer codBerco = null;
        Clientes client = null;
        
        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            client = ClientesController.getByCod(manager,
                    SessionContext.getInstance().getClientLoggedIn().getCod());

            for (BercoCliente bc : client.getBercosCliente()) {
                Atracacao atracacao = AtracacaoController.getAtracacaoAtivaByCodBercoAndCodCliente(manager,
                        bc.getCodBerco().getCodBerco(), bc.getCodCliente().getCod());
                if (atracacao != null) {                       
                    codBerco = bc.getCodBerco().getCodBerco();                    
                    break;
                }
            }                       

        } catch (Exception ex) {
        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }
        
        if (codBerco == null && client != null){
            for (BercoCliente bc : client.getBercosCliente()) {
                codBerco = bc.getCodBerco().getCodBerco();
            }
        }
        
        if (codBerco != null){
             FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .redirect(Util.getPathUrl() + "admin/moorings/realtime.xhtml?berco=" + codBerco);        
        }

    }

}
