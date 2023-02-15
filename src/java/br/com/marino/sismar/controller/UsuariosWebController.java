package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.UsuariosWeb;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class UsuariosWebController {

    public static List<UsuariosWeb> getListUsers(EntityManager manager)
            throws Exception {
        Query query = manager.createNamedQuery("UsuariosWeb.findAll");
        List<UsuariosWeb> list = query.getResultList();
        return list;
    }

    public static List<UsuariosWeb> getListUsersByClient(EntityManager manager, int codClient, boolean plusMaster)
            throws Exception {

        String sql;

        if (plusMaster) {

            sql = "select * from usuarios_web as u where u.master = 1 "
                    + "or u.idUsuario in (select cu.codUsuario from clientes_usuarios as cu "
                    + "where cu.codCliente = " + codClient + ")";

        } else {

            sql = "select * from usuarios_web as u where u.master = 0 "
                    + "and u.idUsuario in (select cu.codUsuario from clientes_usuarios as cu "
                    + "where cu.codCliente = " + codClient + ")";

        }

        Query query = manager.createNativeQuery(sql,
                UsuariosWeb.class);

        return query.getResultList();

    }
    
    public static List<UsuariosWeb> getListUsersByNotClient(EntityManager manager, int codClient, boolean plusMaster)
            throws Exception {

        String sql;

        if (plusMaster) {

            sql = "select * from usuarios_web as u where u.master = 1 "
                    + "or u.idUsuario not in (select cu.codUsuario from clientes_usuarios as cu "
                    + "where cu.codCliente = " + codClient + ")";

        } else {

            sql = "select * from usuarios_web as u where u.master = 0 "
                    + "and u.idUsuario not in (select cu.codUsuario from clientes_usuarios as cu "
                    + "where cu.codCliente = " + codClient + ")";

        }

        Query query = manager.createNativeQuery(sql,
                UsuariosWeb.class);

        return query.getResultList();

    }

    public static void insert(EntityManager manager, UsuariosWeb user)
            throws Exception {
        manager.persist(user);
    }

    public static void delete(EntityManager manager, int id) throws Exception {
        UsuariosWeb user = manager.getReference(UsuariosWeb.class, id);
        manager.remove(user);
    }

    public static void edit(EntityManager manager, UsuariosWeb user) throws Exception {
        manager.merge(user);
    }

    public static UsuariosWeb getUserByLogin(EntityManager manager, String login) {

        try {

            Query query = manager.createNamedQuery("UsuariosWeb.findByLogin");
            query.setParameter("login", login);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            UsuariosWeb user = (UsuariosWeb) obj;
            return user;

        } catch (NoResultException ex) {

            return null;
        }

    }

    public static UsuariosWeb getUserById(EntityManager manager, int idUser) {

        try {

            Query query = manager.createNamedQuery("UsuariosWeb.findByIdUsuario");
            query.setParameter("idUsuario", idUser);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            UsuariosWeb user = (UsuariosWeb) obj;
            return user;

        } catch (NoResultException ex) {

            return null;
        }

    }

    public static UsuariosWeb getUserByLoginAndPassword(EntityManager manager,
            String login, String password) {

        try {

            Query query = manager.createNamedQuery("UsuariosWeb.findByLoginAndSenha");
            query.setParameter("login", login);
            query.setParameter("senha", password);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            UsuariosWeb user = (UsuariosWeb) obj;
            return user;

        } catch (NoResultException ex) {

            return null;
        }

    }

}
