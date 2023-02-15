package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Area;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class AreaController {

    public static List<Area> getListArea(EntityManager manager)
            throws Exception {
        Query query = manager.createNamedQuery("Area.findAll");
        List<Area> list = query.getResultList();
        return list;
    }   
    
}