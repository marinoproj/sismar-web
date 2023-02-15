package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Equipamentos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class EquipamentoController {
    
    public static List<Equipamentos> getListEquipamentos(EntityManager manager)
            throws Exception {
        Query query = manager.createNamedQuery("Equipamentos.findAll");
        List<Equipamentos> list = query.getResultList();
        return list;
    }

}
