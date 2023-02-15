package br.com.marino.sismar.bean.admin;

import br.com.marino.sismar.controller.CorrentometroController;
import br.com.marino.sismar.controller.Dolphin14Controller;
import br.com.marino.sismar.entity.Correntometro;
import br.com.marino.sismar.entity.Atracacao;
import br.com.marino.sismar.entity.Dolphin14;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "HomeBean")
@ViewScoped
public class HomeBean implements Serializable {

    private Dolphin14 wind;
    private Correntometro seaCurrent;
    private Atracacao pier1, pier2, pier3, pier4;
    private Exception windException, seaCurrentException,
            opPier1Exception, opPier2Exception, opPier3Exception, opPier4Exception;

    @PostConstruct
    public void init() {
        reloadValues(); 
    }

    @PreDestroy
    public void destroy() {
        wind = null;
        seaCurrent = null;
        pier1 = null;
        pier2 = null;
        pier3 = null;
        pier4 = null;

        windException = null;
        seaCurrentException = null;
        opPier1Exception = null;
        opPier2Exception = null;
        opPier3Exception = null;
        opPier4Exception = null;
    }

    public void reloadValues() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            reloadWind(manager);
            reloadSeaCurrent(manager);

        } catch (Exception ex) {
            windException = ex;
            wind = null;

            seaCurrentException = ex;
            seaCurrent = null;

            opPier1Exception = ex;
            opPier2Exception = ex;
            opPier3Exception = ex;
            opPier4Exception = ex;

            pier1 = null;
            pier2 = null;
            pier3 = null;
            pier4 = null;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    // WIND
    public void reloadWind(EntityManager managerSuper) {

        if (managerSuper == null) {

            EntityManagerFactory factory = null;
            EntityManager manager = null;

            try {

                factory = Persistence.createEntityManagerFactory("sismarPU");
                manager = factory.createEntityManager();
                wind = Dolphin14Controller.getDolphin14Last(manager);

                if (wind != null && !Util.isOnlineInfo(wind.getDataHora(),
                        Util.TMP_SECONDS_ONLINE_WIND)) {
                    throw new Exception("outdated information.");
                }

                windException = null;

            } catch (Exception ex) {
                windException = ex;
                wind = null;

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

                wind = Dolphin14Controller.getDolphin14Last(managerSuper);
                if (wind != null && !Util.isOnlineInfo(wind.getDataHora(),
                        Util.TMP_SECONDS_ONLINE_WIND)) {
                    throw new Exception("outdated information.");
                }
                windException = null;

            } catch (Exception ex) {
                windException = ex;
                wind = null;

            }

        }

    }

    public String getWindVel() {
        if (windException != null) {
            return "-";
        }
        if (wind == null) {
            return "-";
        }
        return getValueFormatted(wind.getVelocidadeMediaVento()) + " km/h";
    }
    
    public String getWindVelNos() {
        if (windException != null) {
            return "-";
        }
        if (wind == null) {
            return "-";
        }
        return getValueFormatted(wind.getVelocidadeMediaVento()/1.852) + " nós";
    }

    public String getWindRaj() {
        if (windException != null) {
            return "-";
        }
        if (wind == null) {
            return "-";
        }
        return getValueFormatted(wind.getVelocidadeMaximaVento()) + " km/h";
    }
    
    public String getWindRajNos() {
        if (windException != null) {
            return "-";
        }
        if (wind == null) {
            return "-";
        }
        return getValueFormatted(wind.getVelocidadeMaximaVento()/1.852) + " nós";
    }

    public String getWindDir() {
        if (windException != null) {
            return "-";
        }
        if (wind == null) {
            return "-";
        }
        return getValueFormatted(wind.getDirecaoMediaVento()) + " º";
    }

    public String getWindMessageStatus() {
        if (windException != null) {
            return "offline";
        }
        if (wind == null) {
            return "offline";
        }
        return "online";
    }

    // SEA CURRENT
    public void reloadSeaCurrent(EntityManager managerSuper) {

        if (managerSuper == null) {
            EntityManagerFactory factory = null;
            EntityManager manager = null;

            try {

                factory = Persistence.createEntityManagerFactory("sismarPU");
                manager = factory.createEntityManager();
                seaCurrent = CorrentometroController.getCorrentometroLast(manager);

                if (seaCurrent != null && !Util.isOnlineInfo(seaCurrent.getDataHora(),
                        Util.TMP_SECONDS_ONLINE_SEACURRENT)) {
                    throw new Exception("outdated information.");
                }

                seaCurrentException = null;

            } catch (Exception ex) {
                seaCurrentException = ex;
                seaCurrent = null;

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

                seaCurrent = CorrentometroController.getCorrentometroLast(managerSuper);
                if (seaCurrent != null && !Util.isOnlineInfo(seaCurrent.getDataHora(),
                        Util.TMP_SECONDS_ONLINE_SEACURRENT)) {
                    throw new Exception("outdated information.");
                }
                seaCurrentException = null;

            } catch (Exception ex) {
                seaCurrentException = ex;
                seaCurrent = null;

            }

        }

    }

    public String getSeaCurrentSpeed() {
        if (seaCurrentException != null) {
            return "-";
        }
        if (seaCurrent == null) {
            return "-";
        }
        return getValueFormatted(Util.getSpeedCorrentometroByLevel(seaCurrent)) + " nós";
    }

    public String getSeaCurrentDir() {
        if (seaCurrentException != null) {
            return "-";
        }
        if (seaCurrent == null) {
            return "-";
        }
        return getValueFormatted(Util.getDirecaoCorrentometroByLevel(seaCurrent)) + " º";
    }

    public String getSeaCurrentMessageStatus() {
        if (seaCurrentException != null) {
            return "offline";
        }
        if (seaCurrent == null) {
            return "offline";
        }
        return "online";
    }       

    public String getValueFormatted(Double value) {
        if (value == null) {
            return "";
        }
        return Util.converterValue(value);
    }

    public boolean isActiveFeature(String nameFeature) {
        return Util.isActiveFeature(nameFeature);
    }

}
