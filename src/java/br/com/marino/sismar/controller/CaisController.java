package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Cais;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class CaisController {

    public static List<Cais> getListCais(EntityManager manager)
            throws Exception {
        Query query = manager.createNamedQuery("Cais.findAll");
        List<Cais> list = query.getResultList();
        return list;
    }   
    
}