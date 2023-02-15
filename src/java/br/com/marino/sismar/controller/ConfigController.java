package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Config;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ConfigController {

    public static void save(EntityManager manager, Config config)
            throws Exception {
        if (config.getCod() == null) {
            manager.persist(config);
            manager.flush();
        }else{
            edit(manager, config);
        }
    }

    private static void edit(EntityManager manager, Config config) throws Exception {
        manager.merge(config);
    }

    public static Config getConfig(EntityManager manager)
            throws Exception {
        Query query = manager.createNamedQuery("Config.findAll");
        List<Config> list = query.getResultList();
        if (list.isEmpty()){
            return null;
        }
        return list.get(0);
    }
    
}
