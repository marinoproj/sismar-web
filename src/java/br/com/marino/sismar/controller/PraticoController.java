package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Pratico;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class PraticoController {

    public static List<Pratico> getList(EntityManager manager)throws Exception {

        try {
            
            String sql = "SELECT * FROM pratico ORDER BY nomePratico ASC";
            
            Query query = manager.createNativeQuery(sql,
                    Pratico.class);

            return query.getResultList();     
            
        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }
    
    public static Pratico getByCod(EntityManager manager, int cod) {

        try {
            
            Query query = manager.createNativeQuery("SELECT TOP 1 * FROM pratico "
                    + "WHERE codPratico = " + cod, Pratico.class);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Pratico) obj;

        } catch (NoResultException ex) {
            return null;
            
        }

    }
    
    public static List<Pratico> getListByCodCliente(EntityManager manager, int codCliente)
            throws Exception {

        try {

            String sql = "SELECT * FROM pratico WHERE codCliente = " + 
                    codCliente + " AND status = 1 ORDER BY nomePratico ASC";
            
            Query query = manager.createNativeQuery(sql,
                    Pratico.class);

            return query.getResultList();            

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }
    
    public static void insert(EntityManager manager, Pratico obj)
            throws Exception {
        manager.persist(obj);
        manager.flush();
    }

    public static void delete(EntityManager manager, int cod) throws Exception {
        Pratico obj = manager.getReference(Pratico.class, cod);
        manager.remove(obj);
    }

    public static void edit(EntityManager manager, Pratico obj) throws Exception {
        manager.merge(obj);
    }
    
}
