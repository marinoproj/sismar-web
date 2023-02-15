package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Operacao;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class OperacaoController {

    public static List<Operacao> getListOperacaoByCodCliente(EntityManager manager, int codCliente)
            throws Exception {

        try {

            String sql = "SELECT * FROM operacao WHERE codCliente = " + 
                    codCliente + " AND status = 1 ORDER BY cod ASC";
            
            Query query = manager.createNativeQuery(sql,
                    Operacao.class);

            return query.getResultList();            

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }
    
    public static Operacao getByCod(EntityManager manager, int cod) {

        try {

            Query query = manager.createNamedQuery("Operacao.findByCod");
            query.setParameter("cod", cod);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Operacao) obj;

        } catch (NoResultException ex) {
            return null;
            
        }

    }

    public static void insert(EntityManager manager, Operacao operacao)
            throws Exception {
        manager.persist(operacao);
        manager.flush();
    }

    public static void delete(EntityManager manager, int codOperacao) throws Exception {
        Operacao user = manager.getReference(Operacao.class, codOperacao);
        manager.remove(user);
    }

    public static void edit(EntityManager manager, Operacao operacao) throws Exception {
        manager.merge(operacao);
    }

}
