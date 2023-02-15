package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Dolphin14;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class Dolphin14Controller {

    public static Dolphin14 getDolphin14Last(EntityManager manager) {

        try {

            CriteriaBuilder cb = manager.getCriteriaBuilder();
            CriteriaQuery cq = manager.getCriteriaBuilder().createQuery();
            Root<Dolphin14> cd = cq.from(Dolphin14.class);
            cq.select(cd);

            cq.orderBy(cb.desc(cd.get("dataHora")));

            Query query = manager.createQuery(cq);

            Object obj = query.setMaxResults(1).getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Dolphin14) obj;

        } catch (NoResultException ex) {

            return null;
        }

    }

    public static List<Dolphin14> getListDolphin14ByPeriod(EntityManager manager, Date startDate, Date endDate) {

        try {

            Query query = manager.createNamedQuery("Dolphin14.findByPeriod");
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();

        } catch (NoResultException ex) {

            return null;
        }

    }

}
