package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Etapas;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class EtapasController {

    public static List<Etapas> getListEtapasByCodCliente(EntityManager manager, int codCliente)
            throws Exception {

        try {

            String sql = "SELECT * FROM etapas WHERE codCliente = " + 
                    codCliente + " AND status = 1 ORDER BY cod ASC";
            
            Query query = manager.createNativeQuery(sql,
                    Etapas.class);

            return query.getResultList();            

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }
    
    public static Etapas getByCod(EntityManager manager, int cod) {

        try {
            
            Query query = manager.createNamedQuery("Etapas.findByCod");
            query.setParameter("cod", cod);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Etapas) obj;

        } catch (NoResultException ex) {
            return null;
            
        }

    }

    public static void insert(EntityManager manager, Etapas etapa)
            throws Exception {
        manager.persist(etapa);
        manager.flush();
    }

    public static void delete(EntityManager manager, int codEtapa) throws Exception {
        Etapas user = manager.getReference(Etapas.class, codEtapa);
        manager.remove(user);
    }

    public static void edit(EntityManager manager, Etapas etapa) throws Exception {
        manager.merge(etapa);
    }

}
