package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Porto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class PortoController {
    
    public static List<Porto> getListPortos(EntityManager manager) throws Exception {

        Query query = manager.createNamedQuery("Porto.findAll");

        List<Porto> list = query.getResultList();

        if (list == null) {
            return new ArrayList<>();
        }

        return list;

    }    
            
}
