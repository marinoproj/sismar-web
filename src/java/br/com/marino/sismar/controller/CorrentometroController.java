package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Correntometro;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class CorrentometroController {
    
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
    
}
