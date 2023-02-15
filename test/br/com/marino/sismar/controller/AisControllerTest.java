package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Ais;
import br.com.marino.sismar.entity.NavioUltimaAtualizacao;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Test;

public class AisControllerTest {

    public AisControllerTest() {
    }

    public Date[] getPeriod(Date dateToday) {
        Calendar dataInicio = Calendar.getInstance();
        dataInicio.setTime(dateToday);
        dataInicio.add(Calendar.MINUTE, 180 * -1);
        return new Date[]{dateToday, dataInicio.getTime()};
    }

    @Test
    public void testListVesselActive() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            
            Date[] period = getPeriod(new Date());
            
            //List<Ais> listaAis = AisController.getListVesselActive(manager, period[1], period[0]);
            
//            for(Ais ais : listaAis){
//                System.out.println(ais.toString());
//            }            
            
            //System.out.println(listaAis.size());
            
        } catch (Exception ex) {
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

}
