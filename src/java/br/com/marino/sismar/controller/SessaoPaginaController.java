package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.SessaoPagina;
import br.com.marino.sismar.entity.UsuariosWebSessao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class SessaoPaginaController {

    public static void insert(
            EntityManager manager, 
            int idUsuario, 
            SessaoPagina sessaoPagina)
            throws Exception {
        
        UsuariosWebSessao sessao = UsuariosWebSessaoController.getOpenSessaoByCodUsuario(manager, idUsuario);
        
        if (sessao == null){
            return;
        }
        
        // pega  a página atual que esta logado
        SessaoPagina old = getOpenSessaoPaginaByCodUsuariosWebSessao(manager, sessao.getCodUsuariosWebSessao());        
        
        if (old != null) {
            // o que deseja cadastrar é a mesma do anterior
            if (old.getPagina().equalsIgnoreCase(sessaoPagina.getPagina())) {
                return;
            }
            // fecha a anterior
            old.setSaida(new Date());
            edit(manager, old);
        }
        
        // cadastra a nova
        sessaoPagina.setCodUsuariosWebSessao(sessao.getCodUsuariosWebSessao());
        sessaoPagina.setSaida(null);      
        sessaoPagina.setAcesso(new Date());
        manager.persist(sessaoPagina);
        
    }
    
    public static void edit(EntityManager manager, SessaoPagina sessaoPagina) throws Exception {
        manager.merge(sessaoPagina);
    }
    
    public static SessaoPagina getOpenSessaoPaginaByCodUsuariosWebSessao(EntityManager manager,
            int codUsuariosWebSessao) {

        try {

            String sql = "SELECT * "
                    + "FROM sessao_pagina WHERE codUsuariosWebSessao = " + codUsuariosWebSessao
                    + " AND saida IS NULL";

            Query query = manager.createNativeQuery(sql, SessaoPagina.class);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            SessaoPagina pagina = (SessaoPagina) obj;
            return pagina;

        } catch (Exception ex) {
            return null;
        }

    }
    
    public static List<SessaoPagina> getPaginasByCodUsuariosWebSessao(EntityManager manager,
            int codUsuariosWebSessao) {

        try {

            String sql = "SELECT * "
                    + "FROM sessao_pagina WHERE codUsuariosWebSessao = " + codUsuariosWebSessao
                    + " ORDER BY acesso ASC";

            Query query = manager.createNativeQuery(sql, SessaoPagina.class);

            List<SessaoPagina> list = query.getResultList();

            if (list == null || list.isEmpty()) {
                return new ArrayList<>();
            }

            return list;

        } catch (Exception ex) {
            return new ArrayList<>();
        }

    }
    
}