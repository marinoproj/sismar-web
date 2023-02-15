package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Empresa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class EmpresaController {

    public static List<Empresa> getListCompany(EntityManager manager)
            throws Exception {
        Query query = manager.createNamedQuery("Empresa.findAll");
        List<Empresa> list = query.getResultList();
        return list;
    }   
    
}