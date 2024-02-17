package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.ImageVessel;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.VesselSearch;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class NavioController {

    public static List<VesselSearch> getVesselsBySearch(EntityManager manager, String search) throws Exception {

        String sql = "SELECT codNavio, "
                + "nomeNavio, "
                + "imo, "
                + "mmsi, "
                + "tipo, "
                + "dimensao, "
                + "imagemUrl "
                + "FROM navio WHERE nomeNavio LIKE '%" + search + "%' "
                + "OR CONCAT(imo,'') LIKE '%" + search + "%'";

        Query query = manager.createNativeQuery(sql, VesselSearch.class);

        List<VesselSearch> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        return list;

    }
    
    public static List<VesselSearch> getVesselsBySearch(EntityManager manager, 
            String search, int limit) throws Exception {

        String sql = "SELECT TOP " + limit + " codNavio, "
                + "nomeNavio, "
                + "imo, "
                + "mmsi, "
                + "tipo, "
                + "dimensao, "
                + "imagemUrl "
                + "FROM navio WHERE nomeNavio LIKE '%" + search + "%' "
                + "OR CONCAT(imo,'') LIKE '%" + search + "%'";

        Query query = manager.createNativeQuery(sql, VesselSearch.class);

        List<VesselSearch> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        return list;

    }
    
    public static Navio getVesselByMmsi2(EntityManager manager, int mmsi) throws Exception {

        String sql = "SELECT codNavio, "
                + "nomeNavio, "
                + "imo, "
                + "mmsi, "
                + "tipo, "
                + "dimensao, "
                + "imagemUrl "
                + "FROM navio WHERE mmsi = " + mmsi;

        Query query = manager.createNativeQuery(sql, VesselSearch.class);

        List<VesselSearch> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return new Navio(list.get(0));

    }

    public static Navio getVesselByMmsi(EntityManager manager, int mmsi) throws Exception {

        Query query = manager.createNamedQuery("Navio.findByMmsi");
        query.setParameter("mmsi", mmsi);

        List<Navio> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.get(0);

    }

    public static Navio getVesselByObj(Object[] obj) throws Exception {

        Navio objNavio = new Navio();

        objNavio.setCodNavio((int) obj[21]);
        objNavio.setNomeNavio((String) obj[22]);
        objNavio.setTipo((String) obj[23]);
        objNavio.setCorCasco((String) obj[24]);
        objNavio.setNomeComandante((String) obj[25]);
        objNavio.setImo((int) obj[26]);
        objNavio.setDimensao((String) obj[27]);
        objNavio.setDataCadastro((Date) obj[28]);
        objNavio.setIndicativo((String) obj[29]);
        objNavio.setMmsi((int) obj[0]);

        return objNavio;

    }

    public static Navio getVesselByImo(EntityManager manager, int imo) throws Exception {

        Query query = manager.createNamedQuery("Navio.findByImo");
        query.setParameter("imo", imo);

        List<Navio> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.get(0);

    }

    public static ImageVessel getImageVesselByCod(EntityManager manager, int codVessel) throws Exception {

        String sql = "SELECT codNavio, imagemUrl FROM navio WHERE codNavio = " + codVessel;

        Query query = manager.createNativeQuery(sql, ImageVessel.class);

        ImageVessel imageVessel = (ImageVessel) query.getSingleResult();

        return imageVessel;

    }

    public static ImageVessel getImageVesselByImo(EntityManager manager, int imo) throws Exception {

        String sql = "SELECT codNavio, imagemUrl FROM navio WHERE imo = " + imo;

        Query query = manager.createNativeQuery(sql, ImageVessel.class);

        List<ImageVessel> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.get(0);

    }

}
