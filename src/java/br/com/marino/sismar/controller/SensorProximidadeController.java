package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.SensorProximidade;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class SensorProximidadeController {

    public static List<SensorProximidade> getListByCodCliente(EntityManager manager, int codCliente)
            throws Exception {

        try {

            String sql = "SELECT * FROM sensores_proximidade WHERE codCliente = " + 
                    codCliente + " AND status = 1 ORDER BY cod ASC";
            
            Query query = manager.createNativeQuery(sql,
                    SensorProximidade.class);

            return query.getResultList();            

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }
    
    public static SensorProximidade getByCod(EntityManager manager, int cod) {

        try {
            
            Query query = manager.createNamedQuery("SensorProximidade.findByCod");
            query.setParameter("cod", cod);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (SensorProximidade) obj;

        } catch (NoResultException ex) {
            return null;
            
        }

    }

    public static void insert(EntityManager manager, SensorProximidade sensorProximidade)
            throws Exception {
        manager.persist(sensorProximidade);
        manager.flush();
    }

    public static void delete(EntityManager manager, int cod) throws Exception {
        SensorProximidade obj = manager.getReference(SensorProximidade.class, cod);
        manager.remove(obj);
    }

    public static void edit(EntityManager manager, SensorProximidade obj) throws Exception {
        manager.merge(obj);
    }

}