package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Berco;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class BercosController {

    public static Berco getByCod(EntityManager manager, int codBerco) {

        try {

            Query query = manager.createNativeQuery("SELECT TOP 1 * FROM berco "
                    + "WHERE codBerco = " + codBerco, Berco.class);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Berco) obj;

        } catch (NoResultException ex) {
            return null;

        }

    }
    
    public static List<Berco> getBercosBySearch(EntityManager manager, String search) throws Exception {

        String sql = "SELECT * "
                + "FROM berco WHERE nome LIKE '%" + search + "%'";

        Query query = manager.createNativeQuery(sql, Berco.class);

        List<Berco> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        return list;

    }
    
    public static List<Berco> getListBercos(EntityManager manager)
            throws Exception {
        Query query = manager.createNamedQuery("Berco.findAll");
        List<Berco> list = query.getResultList();
        return list;
    }

    public static void insert(EntityManager manager, Berco berco)
            throws Exception {
        manager.persist(berco);
        manager.flush();
    }

    public static void delete(EntityManager manager, int codBerco) throws Exception {
        Berco user = manager.getReference(Berco.class, codBerco);
        manager.remove(user);
    }

    public static void edit(EntityManager manager, Berco berco) throws Exception {
        manager.merge(berco);
    }

    public static Berco getBercoByCodBerco(EntityManager manager, int codBero) {

        try {

            Query query = manager.createNamedQuery("Berco.findByCodBerco");
            query.setParameter("codBerco", codBero);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            Berco user = (Berco) obj;
            return user;

        } catch (NoResultException ex) {

            return null;
        }

    }

}
