package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Atracacao;
import br.com.marino.sismar.entity.Giaont;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.Pratico;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class AtracacaoController {

    public static List<Atracacao> search(
            EntityManager manager, int codCliente, Navio embarcacao, Giaont giaont, Pratico pratico)
            throws Exception {

        try {

            String sql = "SELECT * FROM atracacoes WHERE codCliente = " + codCliente;

            if (embarcacao != null) {
                sql += " AND codNavio = " + embarcacao.getCodNavio();
            }

            if (giaont != null) {
                sql += " AND codGiaont = " + giaont.getCodGiaont();
            }

            if (pratico != null) {
                sql += " AND codPratico = " + pratico.getCodPratico();
            }

            sql += " ORDER BY dataInicio DESC";

            Query query = manager.createNativeQuery(sql,
                    Atracacao.class);

            return query.getResultList();

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }

    public static List<Atracacao> getListByCodCliente(
            EntityManager manager, int codCliente)
            throws Exception {

        try {

            String sql = "SELECT * FROM atracacoes WHERE codCliente = "
                    + codCliente + " ORDER BY dataInicio DESC";

            Query query = manager.createNativeQuery(sql,
                    Atracacao.class);

            return query.getResultList();

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }

    public static Atracacao getByCodAtracacaoAndCodCliente(EntityManager manager,
            int codAtracacao, int codCliente) {

        try {

            Query query = manager.createNativeQuery("SELECT TOP 1 * FROM atracacoes "
                    + "WHERE codAtracacao = " + codAtracacao + " AND codCliente = " + codCliente, Atracacao.class);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Atracacao) obj;

        } catch (NoResultException ex) {
            return null;

        }

    }

    public static Atracacao getAtracacaoAtivaByCodBercoAndCodCliente(EntityManager manager,
            int codBerco, int codCliente) {

        try {

            Query query = manager.createNativeQuery("SELECT TOP 1 * FROM atracacoes "
                    + "WHERE codBerco = " + codBerco + " AND codCliente = "
                    + codCliente + " AND statusOperacao != 0", Atracacao.class);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Atracacao) obj;

        } catch (NoResultException ex) {
            return null;

        }

    }

    public static List<Atracacao> getAtracacaoAtivaByCodCliente(EntityManager manager, int codCliente) {

        try {

            Query query = manager.createNativeQuery("SELECT * FROM atracacoes "
                    + "WHERE codCliente = "
                    + codCliente + " AND statusOperacao != 0", Atracacao.class);

            return query.getResultList();

        } catch (Exception ex) {
            return new ArrayList<>();

        }

    }

    public static void edit(EntityManager manager, Atracacao atracacao) throws Exception {
        manager.merge(atracacao);
    }

}
