package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Interrupcoes;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class InterrupcoesController {

    public static List<Interrupcoes> getListInterrupcoesByCodEtapaOperacao(EntityManager manager, int codEtapaOperacao)
            throws Exception {

        try {
            
            String sql = "SELECT * FROM eventos WHERE codEtapaOperacao = " + 
                    codEtapaOperacao + " AND status = 1 ORDER BY cod ASC";
            
            Query query = manager.createNativeQuery(sql,
                    Interrupcoes.class);

            return query.getResultList();

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }

    public static void insert(EntityManager manager, Interrupcoes interrupcao)
            throws Exception {
        manager.persist(interrupcao);
        manager.flush();
    }

    public static void delete(EntityManager manager, int codInterrupcao) throws Exception {
        Interrupcoes user = manager.getReference(Interrupcoes.class, codInterrupcao);
        manager.remove(user);
    }

    public static void edit(EntityManager manager, Interrupcoes interrupcao) throws Exception {
        manager.merge(interrupcao);
    }

}
