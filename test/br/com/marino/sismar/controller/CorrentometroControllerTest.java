package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Correntometro;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Test;
import static org.junit.Assert.*;

public class CorrentometroControllerTest {

    public CorrentometroControllerTest() {
    }

    @Test
    public void testGetVaisalaLast() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            Correntometro correntometro = CorrentometroController.getCorrentometroLast(manager);
            assertNotNull(correntometro);
            System.out.println(correntometro);

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
