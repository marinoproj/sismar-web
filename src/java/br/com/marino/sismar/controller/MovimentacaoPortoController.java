package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.MovimentacaoPorto;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class MovimentacaoPortoController {

    public static List<MovimentacaoPorto> getListMovimentacaoPortoAtivoByCodArea(EntityManager manager, int codArea)
            throws Exception {

        String sql = "SELECT mp.* FROM movimentacao_porto AS mp "
                + "INNER JOIN movimentacao_porto_parametros AS mpp ON mpp.codMovimentacaoPortoParametro = mp.codMovimentacaoPortoParametro "
                + "WHERE mpp.codArea = " + codArea + " AND mp.dataSaidaCanal IS NULL";

        try {

            Query query = manager.createNativeQuery(sql, MovimentacaoPorto.class);
            List<MovimentacaoPorto> list = query.getResultList();
            return list;

        } catch (Exception ex) {
            return Arrays.asList();
        }

    }

}
