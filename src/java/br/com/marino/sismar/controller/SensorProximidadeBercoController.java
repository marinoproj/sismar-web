package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.SensorProximidadeBerco;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class SensorProximidadeBercoController {

    public static List<SensorProximidadeBerco> getListByCodSensorProximidade(
            EntityManager manager, int codSensorProximidade)
            throws Exception {

        try {
            
            String sql = "SELECT * FROM sensores_proximidade_bercos WHERE codSensorProximidade = " + 
                    codSensorProximidade + " ORDER BY cod ASC";
            
            Query query = manager.createNativeQuery(sql,
                    SensorProximidadeBerco.class);

            return query.getResultList();     
            
        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }
    
    public static SensorProximidadeBerco getByCod(EntityManager manager, int cod) {

        try {
            
            Query query = manager.createNamedQuery("SensorProximidadeBerco.findByCod");
            query.setParameter("cod", cod);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (SensorProximidadeBerco) obj;

        } catch (NoResultException ex) {
            return null;
            
        }

    }

    public static void insert(EntityManager manager, SensorProximidadeBerco obj)
            throws Exception {
        manager.persist(obj);
        manager.flush();
    }

    public static void delete(EntityManager manager, int cod) throws Exception {
        SensorProximidadeBerco obj = manager.getReference(SensorProximidadeBerco.class, cod);
        manager.remove(obj);
    }

    public static void edit(EntityManager manager, SensorProximidadeBerco obj) throws Exception {
        manager.merge(obj);
    }

}