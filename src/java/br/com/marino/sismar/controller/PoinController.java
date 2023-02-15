package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Poin;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class PoinController {

    public static List<Poin> getListPoins(EntityManager manager) throws Exception {

        Query query = manager.createNamedQuery("Poin.findByAtivo");
        query.setParameter("ativo", 1);

        List<Poin> list = query.getResultList();

        if (list == null) {
            return new ArrayList<>();
        }

        return list;

    }
    
    public static List<Poin> getListPoinsAll(EntityManager manager) throws Exception {

        Query query = manager.createNamedQuery("Poin.findAll");

        List<Poin> list = query.getResultList();

        if (list == null) {
            return new ArrayList<>();
        }

        return list;

    }
    
    public static Poin getPoinById(EntityManager manager, int id) throws Exception {

        Query query = manager.createNamedQuery("Poin.findByCodPoin");
        query.setParameter("codPoin", id);

        List<Poin> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.get(0);

    }
            
}
