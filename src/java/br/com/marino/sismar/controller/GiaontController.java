package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Giaont;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class GiaontController {

    public static List<Giaont> getList(EntityManager manager) throws Exception {

        try {
            
            String sql = "SELECT * FROM giaont ORDER BY nomeGiaont ASC";
            
            Query query = manager.createNativeQuery(sql,
                    Giaont.class);

            return query.getResultList();     
            
        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }
    
    public static Giaont getByCod(EntityManager manager, int cod) {

        try {
            
            Query query = manager.createNativeQuery("SELECT TOP 1 * FROM giaont "
                    + "WHERE codGiaont = " + cod, Giaont.class);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Giaont) obj;

        } catch (NoResultException ex) {
            return null;
            
        }

    }
    
    public static List<Giaont> getListByCodCliente(EntityManager manager, int codCliente)
            throws Exception {

        try {

            String sql = "SELECT * FROM giaont WHERE codCliente = " + 
                    codCliente + " AND status = 1 ORDER BY nomeGiaont ASC";
            
            Query query = manager.createNativeQuery(sql,
                    Giaont.class);

            return query.getResultList();            

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }
    
    public static void insert(EntityManager manager, Giaont obj)
            throws Exception {
        manager.persist(obj);
        manager.flush();
    }

    public static void delete(EntityManager manager, int cod) throws Exception {
        Giaont obj = manager.getReference(Giaont.class, cod);
        manager.remove(obj);
    }

    public static void edit(EntityManager manager, Giaont obj) throws Exception {
        manager.merge(obj);
    }
    
}
