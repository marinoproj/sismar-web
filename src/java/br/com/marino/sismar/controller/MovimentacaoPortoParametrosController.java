package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.MovimentacaoPortoParametros;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class MovimentacaoPortoParametrosController {

    public static MovimentacaoPortoParametros getMovimentacaoPortoParametrosByCodArea(EntityManager manager, int codArea)
            throws Exception {

        String sql = "SELECT * FROM movimentacao_porto_parametros "
                + "WHERE codArea = " + codArea;

        try {

            Query query = manager.createNativeQuery(sql, MovimentacaoPortoParametros.class);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (MovimentacaoPortoParametros) obj;

        } catch (Exception ex) {
            return null;
        }

    }

}
