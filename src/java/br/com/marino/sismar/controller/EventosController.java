package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Eventos;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class EventosController {

    public static List<Eventos> getListEventosByCodCliente(EntityManager manager, int codCliente)
            throws Exception {

        try {

            String sql = "SELECT * FROM eventos WHERE codCliente = " + 
                    codCliente + " AND status = 1 ORDER BY cod ASC";
            
            Query query = manager.createNativeQuery(sql,
                    Eventos.class);

            return query.getResultList();            

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }
    
    public static Eventos getByCod(EntityManager manager, int cod) {

        try {

            Query query = manager.createNamedQuery("Eventos.findByCod");
            query.setParameter("cod", cod);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Eventos) obj;

        } catch (NoResultException ex) {
            return null;
            
        }

    }

    public static void insert(EntityManager manager, Eventos evento)
            throws Exception {
        manager.persist(evento);
        manager.flush();
    }

    public static void delete(EntityManager manager, int codEvento) throws Exception {
        Eventos user = manager.getReference(Eventos.class, codEvento);
        manager.remove(user);
    }

    public static void edit(EntityManager manager, Eventos evento) throws Exception {
        manager.merge(evento);
    }

}
