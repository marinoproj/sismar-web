package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.AisTabela;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class AisTabelaController {

    public static List<AisTabela> getListAisTable(EntityManager manager, Date start, Date end) 
            throws Exception {

        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(start);
        dateStart.set(dateStart.get(Calendar.YEAR),
                dateStart.get(Calendar.MONTH),
                dateStart.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        dateStart.set(Calendar.MILLISECOND, 0);

        Calendar dateEnd = Calendar.getInstance();
        dateEnd.setTime(end);
        dateEnd.set(dateEnd.get(Calendar.YEAR),
                dateEnd.get(Calendar.MONTH),
                dateEnd.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        dateEnd.set(Calendar.MILLISECOND, 999);

        try {

            Query query = manager.createNamedQuery("AisTabela.findByPeriod");
            query.setParameter("dateStart", dateStart.getTime());
            query.setParameter("dateEnd", dateEnd.getTime());

            List<AisTabela> list = query.getResultList();
            return list;

        } catch (NoResultException ex) {
            return null;
        }

    }

}
