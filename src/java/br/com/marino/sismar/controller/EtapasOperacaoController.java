package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Etapas;
import br.com.marino.sismar.entity.EtapasOperacao;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class EtapasOperacaoController {

    public static List<EtapasOperacao> getListEtapasOperacaoByCodUsuario(EntityManager manager, int codOperacao)
            throws Exception {

        try {
            
            String sql = "SELECT * FROM etapas_operacao WHERE codOperacao = " + 
                    codOperacao + " AND status = 1 ORDER BY cod ASC";
            
            Query query = manager.createNativeQuery(sql,
                    EtapasOperacao.class);

            return query.getResultList();
            
            /*CriteriaBuilder cb = manager.getCriteriaBuilder();
            CriteriaQuery cq = manager.getCriteriaBuilder().createQuery();
            Root<EtapasOperacao> cd = cq.from(EtapasOperacao.class);
            cq.select(cd);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(cd.get("codOperacao").get("cod"), codOperacao));
            predicates.add(cb.isTrue(cd.get("status")));

            cq.where(predicates.toArray(new Predicate[]{}));
            cq.orderBy(cb.asc(cd.get("cod")));

            Query query = manager.createQuery(cq);
            List<EtapasOperacao> list = query.getResultList();

            return list;*/

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }

    public static EtapasOperacao getByCod(EntityManager manager, int cod) {

        try {

            Query query = manager.createNamedQuery("EtapasOperacao.findByCod");
            query.setParameter("cod", cod);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (EtapasOperacao) obj;

        } catch (NoResultException ex) {
            return null;
            
        }

    }
    
    public static void insert(EntityManager manager, EtapasOperacao etapaOperacao)
            throws Exception {
        manager.persist(etapaOperacao);
        manager.flush();
    }

    public static void delete(EntityManager manager, int codEtapaOperacao) throws Exception {
        Etapas user = manager.getReference(Etapas.class, codEtapaOperacao);
        manager.remove(user);
    }

    public static void edit(EntityManager manager, EtapasOperacao etapaOperacao) throws Exception {
        manager.merge(etapaOperacao);
    }

}
