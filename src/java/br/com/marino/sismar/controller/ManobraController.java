package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Manobra;
import br.com.marino.sismar.util.Util;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class ManobraController {

    
    public static Manobra getUltimaMarcacaoLadoDireitoByCodAtracacao(EntityManager manager, int codAtracacao) {

        Query query = manager.createNativeQuery("SELECT TOP 5 * FROM manobra_" + codAtracacao
                + " WHERE gravadoDistanciaDireita = 1 ORDER BY dataHora DESC", Manobra.class);

        Manobra manobra = null;

        try {
            
            List<Manobra> list = query.getResultList();
            
            for(Manobra m : list){
                if (m.getDistanciaDireita() != null){
                    manobra = m;
                    break;
                }
            } 
            
            if (manobra == null && !list.isEmpty()){
                manobra = list.get(0);
            }
            
        } catch (NoResultException e) {
            System.out.println("Erro na consulta da ultima marcação lado direito para atracao " + codAtracacao
                    + ". erro: " + e.getMessage());
            manobra = null;
        }

        return manobra;

    }
    
    public static Manobra getUltimaMarcacaoLadoDireitoByCodAtracacao(EntityManager manager, 
            int codAtracacao, Date dataHora) {

        Query query = manager.createNativeQuery("SELECT TOP 1 * FROM manobra_" + codAtracacao
                + " WHERE gravadoDistanciaDireita = 1 "
                        + "AND dataHora <= '" + Util.getDateFromBDSQL(dataHora) + "' ORDER BY dataHora DESC", Manobra.class);

        Manobra manobra;

        try {
            manobra = (Manobra) query.getSingleResult();
        } catch (NoResultException e) {
            manobra = null;
        }

        return manobra;

    }

    public static Float getAngulo(EntityManager manager, int codAtracacao) {

        Query query = manager.createNativeQuery("SELECT TOP 1 * FROM manobra_" + codAtracacao
                + " WHERE gravadoDistanciaDireita = 1 AND gravadoDistanciaEsquerda = 1 ORDER BY dataHora DESC", Manobra.class);

        try {
            Manobra manobra = (Manobra) query.getSingleResult();
            return manobra.getAngulo();
        } catch (Exception e) {
            return null;
        }

    }
    
    public static Float getAngulo(EntityManager manager, 
            int codAtracacao, Date dataHora) {

        Query query = manager.createNativeQuery("SELECT TOP 1 * FROM manobra_" + codAtracacao
                + " WHERE gravadoDistanciaDireita = 1 AND gravadoDistanciaEsquerda = 1 "
                        + "AND dataHora <= '" + Util.getDateFromBDSQL(dataHora) + "' ORDER BY dataHora DESC", Manobra.class);

        try {
            Manobra manobra = (Manobra) query.getSingleResult();
            return manobra.getAngulo();
        } catch (Exception e) {
            return null;
        }

    }

    public static Manobra getUltimaMarcacaoLadoEsquerdoByCodAtracacao(EntityManager manager, int codAtracacao) {

        Query query = manager.createNativeQuery("SELECT TOP 5 * FROM manobra_" + codAtracacao
                + " WHERE gravadoDistanciaEsquerda = 1 ORDER BY dataHora DESC", Manobra.class);

        Manobra manobra = null;

        try {
            
            List<Manobra> list = query.getResultList();
            
            for(Manobra m : list){
                if (m.getDistanciaEsquerda() != null){
                    manobra = m;
                    break;
                }
            }
            
            if (manobra == null && !list.isEmpty()){
                manobra = list.get(0);
            }
            
        } catch (NoResultException e) {
            System.out.println("Erro na consulta da ultima marcação lado esquerdo para atracao " + codAtracacao
                    + ". erro: " + e.getMessage());
            manobra = null;
        }

        return manobra;

    }

    public static Manobra getUltimaMarcacaoLadoEsquerdoByCodAtracacao(EntityManager manager, 
            int codAtracacao, Date dataHora) {

        Query query = manager.createNativeQuery("SELECT TOP 1 * FROM manobra_" + codAtracacao
                + " WHERE gravadoDistanciaEsquerda = 1 "
                        + "AND dataHora <= '" + Util.getDateFromBDSQL(dataHora) + "' ORDER BY dataHora DESC", Manobra.class);

        Manobra manobra;

        try {
            manobra = (Manobra) query.getSingleResult();
        } catch (NoResultException e) {
            manobra = null;
        }

        return manobra;

    }
    
    public static List<Manobra> getList(EntityManager manager, int codAtracacao)
            throws Exception {

        try {

            String sql = "SELECT * FROM manobra_" + codAtracacao + " ORDER BY dataHora ASC";

            Query query = manager.createNativeQuery(sql, Manobra.class);

            return query.getResultList();

        } catch (NoResultException ex) {
            return Arrays.asList();

        }

    }

}
