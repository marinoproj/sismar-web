package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.BercoCliente;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class BercoClienteController {

    public static List<BercoCliente> getList(EntityManager manager, Integer codCliente)
            throws Exception {

        try {

            Query query = manager.createNamedQuery("BercoCliente.findByCodCliente");
            query.setParameter("codCliente", codCliente);

            return query.getResultList();

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }

    public static BercoCliente getByCodBerco(EntityManager manager,
            int codBerco) {

        try {

            Query query = manager.createNativeQuery(
                    "SELECT * FROM bercos_clientes "
                    + "WHERE codBerco = " + codBerco,
                    BercoCliente.class);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (BercoCliente) obj;

        } catch (NoResultException ex) {
            return null;

        }

    }

    public static void insert(EntityManager manager, BercoCliente bercoCliente)
            throws Exception {
        manager.persist(bercoCliente);
        manager.flush();
    }

    public static void delete(EntityManager manager, Berco codBerco) throws Exception {
        BercoCliente obj = manager.getReference(BercoCliente.class, codBerco);
        manager.remove(obj);
    }

    public static void delete(EntityManager manager, BercoCliente bercoCliente) throws Exception {
        manager.remove(bercoCliente);
        manager.flush();
    }

    public static void edit(EntityManager manager, BercoCliente cliente) throws Exception {
        manager.merge(cliente);
    }

}
