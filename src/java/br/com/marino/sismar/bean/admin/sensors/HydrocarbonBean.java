package br.com.marino.sismar.bean.admin.sensors;

import br.com.marino.sismar.dto.response.ResponseDataDeviceDTO;
import br.com.marino.sismar.dto.response.ResponseDeviceDTO;
import br.com.marino.sismar.service.DataService;
import br.com.marino.sismar.service.DeviceService;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "HydrocarbonBean")
@ViewScoped
public class HydrocarbonBean implements Serializable {

    private List<ResponseDeviceDTO> devices;
    
    private List<ResponseDataDeviceDTO> data;
    
    private List<ResponseDataDeviceDTO> dataAnalog;
    private List<ResponseDataDeviceDTO> dataDigital;
    private List<ResponseDataDeviceDTO> dataTriggerDigital;
    
    private ResponseDeviceDTO deviceSelected;
    
    @PostConstruct
    public void init() {
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        devices = null;
        data = null;
        dataAnalog = null;
        dataDigital = null;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            devices = new ArrayList<>();
            
            List<ResponseDeviceDTO> list = DeviceService.getDevices();
            
            for(ResponseDeviceDTO dev: list){               
                if (dev.getDhExclude() != null){
                    continue;
                }
                devices.add(dev);
            }           

            deviceSelected = devices.get(0);                       
            
            reloadMetrics(manager);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            
            devices = new ArrayList<>();
            data = new ArrayList<>();
            dataAnalog = new ArrayList<>();
            dataDigital = new ArrayList<>();
            dataTriggerDigital = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void reloadMetrics(EntityManager manager) throws Exception {

        EntityManagerFactory factory = null;
        EntityManager manager2 = manager;

        try {

            if (manager2 == null) {
                factory = Persistence.createEntityManagerFactory("sismarPU");
                manager2 = factory.createEntityManager();
            }
            
            List<ResponseDataDeviceDTO> list = DataService.getDataByDeviceId(deviceSelected.getId());

            data = new ArrayList<>();
            
            dataAnalog = new ArrayList<>();
            dataDigital = new ArrayList<>();
            dataTriggerDigital = new ArrayList<>();
            
            for(ResponseDataDeviceDTO dataDevice: list){
                if (dataDevice.getDhExclude() != null || dataDevice.getDisplay() == null || !dataDevice.getDisplay()){
                    continue;
                }
                data.add(dataDevice);                
            }
            
            // entradas analógicas
            for(ResponseDataDeviceDTO dataDevice: data){
                if (!dataDevice.isReadOnly() && dataDevice.getReadWriteMode() == 1){
                    continue;
                }            
                if (dataDevice.getExternalName().substring(0, 2).equalsIgnoreCase("EA")){
                    dataAnalog.add(dataDevice);
                }                
            }
            Collections.sort(dataAnalog);
            
            // entradas digitais
            for(ResponseDataDeviceDTO dataDevice: data){
                if (!dataDevice.isReadOnly() && dataDevice.getReadWriteMode() == 1){
                    continue;
                }            
                if (dataDevice.getExternalName().substring(0, 1).equalsIgnoreCase("I")){
                    dataDigital.add(dataDevice);
                }                
            }           
            Collections.sort(dataDigital);
            
            // acionar saidas digitais
            for(ResponseDataDeviceDTO dataDevice: data){
                if (!dataDevice.isReadOnly() && dataDevice.getReadWriteMode() == 1){
                    dataTriggerDigital.add(dataDevice);
                }               
            }        
            Collections.sort(dataTriggerDigital);
            
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public List<ResponseDeviceDTO> getDevices() {
        return devices;
    }

    public void setDevices(List<ResponseDeviceDTO> devices) {
        this.devices = devices;
    }

    public List<ResponseDataDeviceDTO> getData() {
        return data;
    }

    public void setData(List<ResponseDataDeviceDTO> data) {
        this.data = data;
    }

    public List<ResponseDataDeviceDTO> getDataAnalog() {
        return dataAnalog;
    }

    public void setDataAnalog(List<ResponseDataDeviceDTO> dataAnalog) {
        this.dataAnalog = dataAnalog;
    }

    public List<ResponseDataDeviceDTO> getDataDigital() {
        return dataDigital;
    }

    public void setDataDigital(List<ResponseDataDeviceDTO> dataDigital) {
        this.dataDigital = dataDigital;
    }

    public ResponseDeviceDTO getDeviceSelected() {
        return deviceSelected;
    }

    public void setDeviceSelected(ResponseDeviceDTO deviceSelected) {
        this.deviceSelected = deviceSelected;
    }
    
    public String getSensorName(ResponseDeviceDTO device){
        if (device.getLabel() == null || device.getLabel().isEmpty()){
            return device.getExternalName() + " - " + device.getConnector().getExternalName();
        }
        return device.getLabel();
    }
    
    public String getDataName(ResponseDataDeviceDTO dataDevice){
        if (dataDevice.getLabel() == null || dataDevice.getLabel().isEmpty()){
            return dataDevice.getExternalName();
        }
        return dataDevice.getLabel();
    }
    
    public String getFloatValue(ResponseDataDeviceDTO dataDevice) {
        if (dataDevice.getCurrentValueData() == null){
            return "";
        }
        if (dataDevice.getValueFormatType().equalsIgnoreCase("1 casa decimal")){
            return Util.round(Float.parseFloat(dataDevice.getCurrentValueData().getValue()), 1) + "";
        }
        if (dataDevice.getValueFormatType().equalsIgnoreCase("2 casa decimal")){
            return Util.round(Float.parseFloat(dataDevice.getCurrentValueData().getValue()), 2) + "";
        }
        if (dataDevice.getValueFormatType().equalsIgnoreCase("3 casa decimal")){
            return Util.round(Float.parseFloat(dataDevice.getCurrentValueData().getValue()), 3) + "";
        }
        return dataDevice.getCurrentValueData().getValue();
    }
    
    public boolean getBooleanValue(ResponseDataDeviceDTO dataDevice) {
        if (dataDevice.getCurrentValueData() == null){
            return false;
        }        
        return ((int) Float.parseFloat(dataDevice.getCurrentValueData().getValue()) == 1);
    }
    
    public String getBooleanValueClass(ResponseDataDeviceDTO dataDevice) {
        if (getBooleanValue(dataDevice)){
            return "data-device-green";
        }
        return "data-device-red";
    }    
    
    public boolean isBooleanValue(ResponseDataDeviceDTO dataDevice){
        return dataDevice.getMemoryType().equalsIgnoreCase("Coil Status");
    }
    
    public boolean isFloatValue(ResponseDataDeviceDTO dataDevice){
        return dataDevice.getMemoryType().contains("Float");
    }

    public List<ResponseDataDeviceDTO> getDataTriggerDigital() {
        return dataTriggerDigital;
    }

    public void setDataTriggerDigital(List<ResponseDataDeviceDTO> dataTriggerDigital) {
        this.dataTriggerDigital = dataTriggerDigital;
    }   

}