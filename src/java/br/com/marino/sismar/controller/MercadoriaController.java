package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Mercadoria;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class MercadoriaController {

    public static List<Mercadoria> getListMerchandise(EntityManager manager)
            throws Exception {
        Query query = manager.createNamedQuery("Mercadoria.findAll");
        List<Mercadoria> list = query.getResultList();
        return list;
    }   
    
}