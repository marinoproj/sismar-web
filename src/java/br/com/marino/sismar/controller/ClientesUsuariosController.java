package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.ClientesUsuarios;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class ClientesUsuariosController {

    public static List<ClientesUsuarios> getListClientesUsuariosByCodUsuario(EntityManager manager,
            int codUsuario) throws Exception {
        try {

            String sql = "SELECT * FROM clientes_usuarios WHERE codUsuario = "
                    + codUsuario;

            Query query = manager.createNativeQuery(sql,
                    ClientesUsuarios.class);

            return query.getResultList();

        } catch (NoResultException ex) {
            return Arrays.asList();
        }
    }

    public static List<ClientesUsuarios> getListClientesUsuariosByCodCliente(EntityManager manager,
            int codCliente) throws Exception {
        try {

            String sql = "SELECT * FROM clientes_usuarios WHERE codCliente = "
                    + codCliente;

            Query query = manager.createNativeQuery(sql,
                    ClientesUsuarios.class);

            return query.getResultList();

        } catch (NoResultException ex) {
            return Arrays.asList();
        }
    }

    public static List<ClientesUsuarios> getListClientesUsuariosByCodCliente(EntityManager manager,
            int codCliente, boolean plusMaster) throws Exception {
        try {

            String sql;

            if (plusMaster) {

                sql = "SELECT cu.* FROM clientes_usuarios AS cu WHERE cu.codCliente = "
                        + codCliente + " INNER JOIN usuarios_web AS u ON u.idUsuario = cu.codUsuario AND u.master = 1";
            
            } else {
                
                sql = "SELECT cu.* FROM clientes_usuarios AS cu WHERE cu.codCliente = "
                        + codCliente + " INNER JOIN usuarios_web AS u ON u.idUsuario = cu.codUsuario AND u.master = 0";
                
            }

            Query query = manager.createNativeQuery(sql,
                    ClientesUsuarios.class);

            return query.getResultList();

        } catch (NoResultException ex) {
            return Arrays.asList();
        }
    }

    public static void insert(EntityManager manager, ClientesUsuarios clienteUsuario)
            throws Exception {
        manager.persist(clienteUsuario);
        manager.flush();
    }

    public static void delete(EntityManager manager, int codClienteUsuario) throws Exception {
        ClientesUsuarios obj = manager.getReference(ClientesUsuarios.class, codClienteUsuario);
        manager.remove(obj);
    }

    public static void edit(EntityManager manager, ClientesUsuarios clienteUsuario) throws Exception {
        manager.merge(clienteUsuario);
    }

}
