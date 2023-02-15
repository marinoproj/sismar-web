package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Layer;
import br.com.marino.sismar.entity.Poin;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class LayerController {

    private static void removeUnactivatedPoins(Layer layer) {
        Iterator<Poin> poins = layer.getPoinList().iterator();
        while (poins.hasNext()) {
            if (poins.next().getAtivo() == 0) {
                poins.remove();
            }
        }
    }

    public static List<Layer> getListLayers(EntityManager manager)
            throws Exception {
        Query query = manager.createNamedQuery("Layer.findAll");
        List<Layer> list = query.getResultList();
        if (list != null) {
            for (Layer layer : list) {
                removeUnactivatedPoins(layer);
            }
        }
        return list;
    }

    public static Layer getLayerByCodigo(EntityManager manager, int codigo) {

        try {

            Query query = manager.createNamedQuery("Layer.findByCodigo");
            query.setParameter("codigo", codigo);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            Layer layer = (Layer) obj;
            removeUnactivatedPoins(layer);
            
            return layer;

        } catch (NoResultException ex) {
            return null;
        }

    }

}
