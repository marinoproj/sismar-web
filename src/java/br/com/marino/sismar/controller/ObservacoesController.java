package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Observacoes;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class ObservacoesController {

    public static List<Observacoes> getListObservacoesByCodEtapaOperacao(EntityManager manager, int codEtapaOperacao)
            throws Exception {

        try {

            String sql = "SELECT * FROM observacoes WHERE codEtapaOperacao = " + 
                    codEtapaOperacao + " AND status = 1 ORDER BY cod ASC";
            
            Query query = manager.createNativeQuery(sql,
                    Observacoes.class);

            return query.getResultList();            

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }

    public static void insert(EntityManager manager, Observacoes obs)
            throws Exception {
        manager.persist(obs);
        manager.flush();
    }

    public static void delete(EntityManager manager, int codObservacao) throws Exception {
        Observacoes user = manager.getReference(Observacoes.class, codObservacao);
        manager.remove(user);
    }

    public static void edit(EntityManager manager, Observacoes obs) throws Exception {
        manager.merge(obs);
    }

}
