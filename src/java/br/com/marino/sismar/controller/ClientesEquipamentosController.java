package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.ClientesEquipamentos;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class ClientesEquipamentosController {

    public static List<ClientesEquipamentos> getListClientesEquipamentosByCodCliente(EntityManager manager,
            int codCliente) throws Exception {
        try {

            String sql = "SELECT * FROM clientes_equipamentos WHERE codCliente = "
                    + codCliente;

            Query query = manager.createNativeQuery(sql, ClientesEquipamentos.class);

            return query.getResultList();

        } catch (NoResultException ex) {
            return Arrays.asList();
        }
    }    

    public static void insert(EntityManager manager, ClientesEquipamentos obj)
            throws Exception {
        manager.persist(obj);
        manager.flush();
    }

    public static void delete(EntityManager manager, int codClienteEquipamento) throws Exception {
        ClientesEquipamentos obj = manager.getReference(ClientesEquipamentos.class, codClienteEquipamento);
        manager.remove(obj);
    }

    public static void edit(EntityManager manager, ClientesEquipamentos obj) throws Exception {
        manager.merge(obj);
    }

}
