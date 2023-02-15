package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.SensorProximidadeStatus;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class SensorProximidadeStatusController {
    
    public static SensorProximidadeStatus getByCodSensorProximidade(EntityManager manager, 
            int codSensorProximidade) {

        try {
            
            Query query = manager.createNativeQuery(
                    "SELECT * FROM sensor_proximidade_status "
                            + "WHERE codSensorProximidade = " + codSensorProximidade,
                    SensorProximidadeStatus.class);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (SensorProximidadeStatus) obj;

        } catch (NoResultException ex) {
            return null;
            
        }

    }

}