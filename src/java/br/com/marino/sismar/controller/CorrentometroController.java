package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Correntometro;
import br.com.marino.sismar.util.Util;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class CorrentometroController {
    
    public static Correntometro getCorrentometroLastByCodEquipamento(EntityManager manager, Integer codEquipamento) {

        try {

            Query query = manager.createNativeQuery("SELECT TOP 1 * FROM correntometro WHERE "
                    + "codEquipamento = " + codEquipamento + " ORDER BY dataHora DESC",
                    Correntometro.class);            

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Correntometro) obj;

        } catch (NoResultException ex) {
            return null;
        }

    }
    
    public static Correntometro getCorrentometroLast(EntityManager manager) {

        try {
            
            CriteriaBuilder cb = manager.getCriteriaBuilder();
            CriteriaQuery cq = manager.getCriteriaBuilder().createQuery();
            Root<Correntometro> cd = cq.from(Correntometro.class);
            cq.select(cd);
            
            cq.orderBy(cb.desc(cd.get("dataHora")));
            
            Query query = manager.createQuery(cq);
                        
            Object obj =query.setMaxResults(1).getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Correntometro) obj;
                
        } catch (NoResultException ex) {
            
            return null;
        }

    }
    
    public static List<Correntometro> getListByCodEquipamentoAndPeriod(EntityManager manager,
            Integer codEquipamento,
            Date startDate, Date endDate) {

        try {

            Query query = manager.createNativeQuery("SELECT * FROM correntometro WHERE "
                    + "codEquipamento = " + codEquipamento + " "
                    + "AND dataHora >= '" + Util.getDateFromBDSQL(startDate) + "' "
                    + "AND dataHora <= '" + Util.getDateFromBDSQL(endDate) + "' "
                    + "ORDER BY dataHora ASC",
                    Correntometro.class);

            return query.getResultList();

        } catch (NoResultException ex) {

            return null;
        }

    }
    
}