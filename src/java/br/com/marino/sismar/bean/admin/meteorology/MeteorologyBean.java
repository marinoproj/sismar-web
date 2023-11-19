package br.com.marino.sismar.bean.admin.meteorology;

import br.com.marino.sismar.controller.ClientesEquipamentosController;
import br.com.marino.sismar.controller.CorrentometroController;
import br.com.marino.sismar.controller.MeteorologiaController;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.ClientesEquipamentos;
import br.com.marino.sismar.entity.Correntometro;
import br.com.marino.sismar.entity.Equipamentos;
import br.com.marino.sismar.entity.Meteorologia;
import br.com.marino.sismar.enums.TipoEquipamentoEnum;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "MeteorologyBean")
@ViewScoped
public class MeteorologyBean implements Serializable {

    private List<Equipamentos> equipamentosCorrente;
    private List<Equipamentos> equipamentosVento;

    private HashMap<Integer, Correntometro> correntes; // codEquipamento x Corrente
    private HashMap<Integer, Meteorologia> ventos; // codEquipamento x Vento

    private Exception exGeral;
    private Exception exVento;
    private Exception exCorrente;

    @PostConstruct
    public void init() {
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        correntes = null;
        ventos = null;
        equipamentosCorrente = null;
        equipamentosVento = null;
        exGeral = null;
        exVento = null;
        exCorrente = null;
    }

    public List<Equipamentos> getEquipamentosCorrente() {
        return equipamentosCorrente;
    }

    public void setEquipamentosCorrente(List<Equipamentos> equipamentosCorrente) {
        this.equipamentosCorrente = equipamentosCorrente;
    }

    public List<Equipamentos> getEquipamentosVento() {
        return equipamentosVento;
    }

    public void setEquipamentosVento(List<Equipamentos> equipamentosVento) {
        this.equipamentosVento = equipamentosVento;
    }

    public HashMap<Integer, Correntometro> getCorrentes() {
        return correntes;
    }

    public void setCorrentes(HashMap<Integer, Correntometro> correntes) {
        this.correntes = correntes;
    }

    public HashMap<Integer, Meteorologia> getVentos() {
        return ventos;
    }

    public void setVentos(HashMap<Integer, Meteorologia> ventos) {
        this.ventos = ventos;
    }

    public String getDataHora(Date date, String formatRegex) {
        if (date == null) {
            return "";
        }
        return Util.dateToString(date, formatRegex);
    }
    
    public boolean isExceptionGeral(){
        return exGeral != null;
    }
    
    public boolean isExceptionVento(){
        return exVento != null;
    }
        
    public boolean isExceptionCorrente(){
        return exCorrente != null;
    }   
    
    public String getSeaCurrentSpeed(Integer codEquipamento) {
        Correntometro corrente = correntes.get(codEquipamento);
        if (corrente == null) {
            return "-";
        }
        return getValueFormatted(Util.getSpeedCorrentometroByLevel(corrente)) + " nós";
    }    

    public String getSeaCurrentDir(Integer codEquipamento) {
        Correntometro corrente = correntes.get(codEquipamento);
        if (corrente == null) {
            return "-";
        }
        return getValueFormatted(Util.getDirecaoCorrentometroByLevel(corrente)) + " º";
    }    

    public String getSeaCurrentMessageStatus(Integer codEquipamento) {
        Correntometro corrente = correntes.get(codEquipamento);
        if (corrente == null) {
            return "-";
        }
        if (!Util.isOnlineInfo(corrente.getDataHora(),
                Util.TMP_SECONDS_ONLINE_SEACURRENT)) {
            return "offline";
        }
        return "online";
    }
    
    public String getWindVel(Integer codEquipamento) {
        Meteorologia meteorologia = ventos.get(codEquipamento);
        if (meteorologia == null) {
            return "-";
        }
        return getValueFormatted(meteorologia.getVelocidadeMediaVento()) + " km/h";
    }

    public String getWindVelNos(Integer codEquipamento) {
        Meteorologia meteorologia = ventos.get(codEquipamento);
        if (meteorologia == null) {
            return "-";
        }
        return getValueFormatted(meteorologia.getVelocidadeMediaVento() / 1.852) + " nós";
    }

    public String getWindRaj(Integer codEquipamento) {
        Meteorologia meteorologia = ventos.get(codEquipamento);
        if (meteorologia == null) {
            return "-";
        }
        return getValueFormatted(meteorologia.getVelocidadeMaximaVento()) + " km/h";
    }

    public String getWindRajNos(Integer codEquipamento) {
        Meteorologia meteorologia = ventos.get(codEquipamento);
        if (meteorologia == null) {
            return "-";
        }
        return getValueFormatted(meteorologia.getVelocidadeMaximaVento() / 1.852) + " nós";
    }

    public String getWindDir(Integer codEquipamento) {
        Meteorologia meteorologia = ventos.get(codEquipamento);
        if (meteorologia == null) {
            return "-";
        }
        return getValueFormatted(meteorologia.getDirecaoMediaVento()) + " º";
    }

    public String getWindMessageStatus(Integer codEquipamento) {
        Meteorologia meteorologia = ventos.get(codEquipamento);
        if (meteorologia == null) {
            return "-";
        }
        if (!Util.isOnlineInfo(meteorologia.getDataHora(),
                Util.TMP_SECONDS_ONLINE_WIND)) {
            return "offline";
        }
        return "online";
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            Clientes cliente = SessionContext.getInstance().getClientLoggedIn();

            List<ClientesEquipamentos> listClientesEquipamentos = ClientesEquipamentosController.getListClientesEquipamentosByCodCliente(manager, cliente.getCod());

            
            equipamentosCorrente = new ArrayList<>();
            equipamentosVento = new ArrayList<>();

            correntes = new HashMap<>();
            ventos = new HashMap<>();

            for (ClientesEquipamentos obj : listClientesEquipamentos) {

                if (!obj.isExibirTelaMeteorologia()) {
                    continue;
                }
                                
                Equipamentos eq = obj.getCodEquipamento();               
                
                if (eq.getTipo().equalsIgnoreCase(TipoEquipamentoEnum.CORRENTE.getValue())) {
                    equipamentosCorrente.add(eq);
                    correntes.put(eq.getCodEquipamento(), null);
                    continue;
                }

                if (eq.getTipo().equalsIgnoreCase(TipoEquipamentoEnum.VENTO.getValue())) {
                    equipamentosVento.add(eq);
                    ventos.put(eq.getCodEquipamento(), null);
                }

            }

            reloadCorrente(manager);
            reloadVento(manager);

            exGeral = null;
            
        } catch (Exception ex) {
            exGeral = ex;
            ex.printStackTrace();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    
    public void reloadCorrente(EntityManager managerSuper) {

        if (managerSuper == null) {

            EntityManagerFactory factory = null;
            EntityManager manager = null;

            try {

                factory = Persistence.createEntityManagerFactory("sismarPU");
                manager = factory.createEntityManager();

                for (Equipamentos eq : equipamentosCorrente) {
                    Correntometro corrente = CorrentometroController
                            .getCorrentometroLastByCodEquipamento(manager, eq.getCodEquipamento());

                    correntes.put(eq.getCodEquipamento(), corrente);
                }

                exCorrente = null;
                
            } catch (Exception ex) {
                exCorrente = ex;
                ex.printStackTrace();

            } finally {
                if (manager != null) {
                    manager.close();
                }
                if (factory != null) {
                    factory.close();
                }

            }

        } else {

            try {

                for (Equipamentos eq : equipamentosCorrente) {
                    Correntometro corrente = CorrentometroController
                            .getCorrentometroLastByCodEquipamento(managerSuper, eq.getCodEquipamento());

                    correntes.put(eq.getCodEquipamento(), corrente);
                }

                exCorrente = null;
                
            } catch (Exception ex) {
                exCorrente = ex;
                ex.printStackTrace();
            }

        }

    }

    public void reloadVento(EntityManager managerSuper) {

        if (managerSuper == null) {

            EntityManagerFactory factory = null;
            EntityManager manager = null;

            try {

                factory = Persistence.createEntityManagerFactory("sismarPU");
                manager = factory.createEntityManager();

                for (Equipamentos eq : equipamentosVento) {
                    Meteorologia meteorologia = MeteorologiaController.getLastByCodEquipamento(manager, eq.getCodEquipamento());
                    ventos.put(eq.getCodEquipamento(), meteorologia);
                }
                
                exVento = null;

            } catch (Exception ex) {
                exVento = ex;
                ex.printStackTrace();

            } finally {
                if (manager != null) {
                    manager.close();
                }
                if (factory != null) {
                    factory.close();
                }

            }

        } else {

            try {

                for (Equipamentos eq : equipamentosVento) {
                    Meteorologia meteorologia = MeteorologiaController.getLastByCodEquipamento(managerSuper, eq.getCodEquipamento());
                    ventos.put(eq.getCodEquipamento(), meteorologia);
                }
                
                exVento = null;

            } catch (Exception ex) {
                exVento = ex;
                ex.printStackTrace();
            }

        }

    }

    public String getValueFormatted(Double value) {
        if (value == null) {
            return "";
        }
        return Util.converterValue(value);
    }

}
