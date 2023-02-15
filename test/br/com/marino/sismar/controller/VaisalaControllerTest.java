package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Dolphin14;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Test;
import static org.junit.Assert.*;

public class VaisalaControllerTest {

    public VaisalaControllerTest() {
    }

    @Test
    public void testGetVaisalaLast() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            Dolphin14 vaisala = Dolphin14Controller.getDolphin14Last(manager);
            assertNotNull(vaisala);
            System.out.println(vaisala);

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
