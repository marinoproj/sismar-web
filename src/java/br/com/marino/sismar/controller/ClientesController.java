package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Clientes;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class ClientesController {

    public static List<Clientes> getListClientes(EntityManager manager)
            throws Exception {

        try {

            Query query = manager.createNamedQuery("Clientes.findByStatus");   
            query.setParameter("status", true);    
            
            return query.getResultList();
            
        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }
    
    public static Clientes getByCod(EntityManager manager, int cod) {

        try {
            
            Query query = manager.createNamedQuery("Clientes.findByCod");
            query.setParameter("cod", cod);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            return (Clientes) obj;

        } catch (NoResultException ex) {
            return null;
            
        }

    }

    public static void insert(EntityManager manager, Clientes cliente)
            throws Exception {
        manager.persist(cliente);
        manager.flush();
    }

    public static void delete(EntityManager manager, int codCliente) throws Exception {
        Clientes obj = manager.getReference(Clientes.class, codCliente);
        manager.remove(obj);
    }

    public static void edit(EntityManager manager, Clientes cliente) throws Exception {
        manager.merge(cliente);
    }

}