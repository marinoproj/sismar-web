package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.UsuariosWebSessao;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class UsuariosWebSessaoController {

    public static void insert(EntityManager manager, UsuariosWebSessao sessao)
            throws Exception {
        manager.persist(sessao);
    }
    
    public static void edit(EntityManager manager, UsuariosWebSessao sessao) throws Exception {
        manager.merge(sessao);
    }
  
    public static UsuariosWebSessao getByCodUsuario(EntityManager manager, int codUsuariosWebSessao) {

        try {

            String sql = "SELECT * "
                    + "FROM usuarios_web_sessao WHERE codUsuariosWebSessao = " + codUsuariosWebSessao;

            Query query = manager.createNativeQuery(sql, UsuariosWebSessao.class);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            UsuariosWebSessao sessao = (UsuariosWebSessao) obj;
            return sessao;

        } catch (Exception ex) {
            return null;
        }

    }
    
    public static UsuariosWebSessao getOpenSessaoByCodUsuario(EntityManager manager, int codUsuario) {

        try {

            String sql = "SELECT * "
                    + "FROM usuarios_web_sessao WHERE codUsuario = " + codUsuario + " AND termino IS NULL";

            Query query = manager.createNativeQuery(sql, UsuariosWebSessao.class);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            UsuariosWebSessao sessao = (UsuariosWebSessao) obj;
            return sessao;

        } catch (Exception ex) {
            return null;
        }

    }
        
}