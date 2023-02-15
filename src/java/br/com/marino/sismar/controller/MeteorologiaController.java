package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Meteorologia;
import br.com.marino.sismar.util.Util;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class MeteorologiaController {

    public static Meteorologia getLastByCodEquipamento(EntityManager manager, Integer codEquipamento) {

        try {

            Query query = manager.createNativeQuery("SELECT TOP 1 * FROM meteorologia WHERE "
                    + "codEquipamento = " + codEquipamento + " ORDER BY dataHora DESC",
                    Meteorologia.class);            

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Meteorologia) obj;

        } catch (NoResultException ex) {

            return null;
        }

    }

    public static List<Meteorologia> getListByCodEquipamentoAndPeriod(EntityManager manager,
            Integer codEquipamento,
            Date startDate, Date endDate) {

        try {

            Query query = manager.createNativeQuery("SELECT * FROM meteorologia WHERE "
                    + "codEquipamento = " + codEquipamento + " "
                    + "AND dataHora >= '" + Util.getDateFromBDSQL(startDate) + "' "
                    + "AND dataHora <= '" + Util.getDateFromBDSQL(endDate) + "' "
                    + "ORDER BY dataHora ASC",
                    Meteorologia.class);

            return query.getResultList();

        } catch (NoResultException ex) {

            return null;
        }

    }

}
