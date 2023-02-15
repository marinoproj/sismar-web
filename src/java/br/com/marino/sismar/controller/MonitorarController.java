package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Monitorar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class MonitorarController {

    public static List<Monitorar> getListVariablesMonitors(EntityManager manager)
            throws Exception {
        Query query = manager.createNamedQuery("Monitorar.findAll");
        List<Monitorar> list = query.getResultList();
        return list;
    }

//    public static List<Monitorar> getListVariablesMonitorsByUser(EntityManager manager, int codUser)
//            throws Exception {
//        
//        //String t = "SELECT m FROM Monitorar m JOIN UsuariosWeb u ON m.codUsuario = u WHERE u.codUsuario = :codUsuario";
//        
//        Query q = manager.createQuery("SELECT m FROM Monitorar m JOIN UsuariosWeb u ON m.codUsuario = u WHERE u.codUsuario = :codUsuario");
//        q.setParameter("codUsuario", codUser);
//        List<Monitorar> customerList = q.getResultList();
//        return customerList;
//    }

    public static void insert(EntityManager manager, Monitorar monitorar)
            throws Exception {
        manager.persist(monitorar);
        manager.flush();
    }

    public static void delete(EntityManager manager, int codMonitorar) throws Exception {
        Monitorar monitorar = manager.getReference(Monitorar.class, codMonitorar);
        manager.remove(monitorar);
    }

    public static void edit(EntityManager manager, Monitorar monitorar) throws Exception {
        manager.merge(monitorar);
    }

    public static Monitorar getMonitorarByCodMonitorar(EntityManager manager, int codMonitorar) {

        try {

            Query query = manager.createNamedQuery("Monitorar.findByCodMonitorar");
            query.setParameter("codMonitorar", codMonitorar);

            Object obj = query.getSingleResult();

            if (obj == null) {
                return null;
            }

            Monitorar monitorar = (Monitorar) obj;
            return monitorar;

        } catch (NoResultException ex) {

            return null;
        }

    }

}
