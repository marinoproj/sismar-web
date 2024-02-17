package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Ais;
import br.com.marino.sismar.entity.AisTabela;
import br.com.marino.sismar.entity.ImageVessel;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class AisController {

    public static List<Ais> getListVesselActive(EntityManager manager, Date start, Date end)
            throws Exception {

//        String sql = "SELECT at.mmsi, "
//                + "at.codAisTabela, "
//                + "at.codAis, "
//                + "at.dataUltimaAtualizacao, "
//                + "at.destino, "
//                + "at.chegadaPrevista, "
//                + "at.latitude, "
//                + "at.longitude, "
//                + "at.statusNavegacao, "
//                + "at.velocidadeSobreSolo, "
//                + "at.cursoSobreSolo, "
//                + "at.velocidadeVento, "
//                + "at.maximaVento, "
//                + "at.direcaoVento, "
//                + "at.velocidadeCorrente, "
//                + "at.direcaoCorrente, "
//                + "at.epfd, "
//                + "at.draught, "
//                + "at.positionAccurate, "
//                + "at.heading, "
//                + "at.raim, "
//                + "n.codNavio, "
//                + "n.nomeNavio, "
//                + "n.tipo, "
//                + "n.corCasco, "
//                + "n.nomeComandante, "
//                + "n.imo, "
//                + "n.dimensao, "
//                + "n.dataCadastro, "
//                + "n.indicativo "
//                + "FROM ais_radio_ultima_atualizacao AS at "
//                + "LEFT JOIN navio AS n ON n.mmsi = at.mmsi "
//                + "WHERE at.dataUltimaAtualizacao >= '" + Util.getDateFromBDSQL(start) + "' AND "
//                + "at.dataUltimaAtualizacao <= '" + Util.getDateFromBDSQL(end) + "' AND "
//                + "at.latitude IS NOT NULL AND at.longitude IS NOT NULL";

        String sql = "SELECT at.mmsi, "
                + "at.codAisTabela, "
                + "at.codAis, "
                + "at.dataUltimaAtualizacao, "
                + "at.destino, "
                + "at.chegadaPrevista, "
                + "at.latitude, "
                + "at.longitude, "
                + "at.statusNavegacao, "
                + "at.velocidadeSobreSolo, "
                + "at.cursoSobreSolo, "
                + "at.velocidadeVento, "
                + "at.maximaVento, "
                + "at.direcaoVento, "
                + "at.velocidadeCorrente, "
                + "at.direcaoCorrente, "
                + "at.epfd, "
                + "at.draught, "
                + "at.positionAccurate, "
                + "at.heading, "
                + "at.raim, "
                + "n.codNavio "                
                + "FROM ais_radio_ultima_atualizacao AS at "
                + "LEFT JOIN navio AS n ON n.mmsi = at.mmsi "
                + "WHERE at.dataUltimaAtualizacao >= '" + Util.getDateFromBDSQL(start) + "' AND "
                + "at.latitude IS NOT NULL AND at.longitude IS NOT NULL";
        
        Query query = manager.createNativeQuery(sql);
        List<Ais> listAis = new ArrayList<>();

        try {

            List<Object[]> list = query.getResultList();

            for (int i = 0; i < list.size(); i++) {

                Object[] obj = list.get(i);

                Ais ais = new Ais();

                ais.setMmsi((int) obj[0]);
                ais.setCodAisTabela((int) obj[1]);
                ais.setCodAis((int) obj[2]);
                ais.setDataHora((Date) obj[3]);
                ais.setDestino((String) obj[4]);
                ais.setChegadaPrevista((String) obj[5]);
                ais.setLatitude((Double) obj[6]);
                ais.setLongitude((Double) obj[7]);
                ais.setStatusNavegacao((String) obj[8]);
                ais.setVelocidadeSobreSolo((Double) obj[9]);
                ais.setCursoSobreSolo((Double) obj[10]);
                ais.setVelocidadeVento((Double) obj[11]);
                ais.setMaximaVento((Double) obj[12]);
                ais.setDirecaoVento((Double) obj[13]);
                ais.setVelocidadeCorrente((Double) obj[14]);
                ais.setDirecaoCorrente((Double) obj[15]);
                ais.setEpfd((Integer) obj[16]);
                ais.setDraught((Double) obj[17]);
                ais.setPositionAccurate((Short) obj[18]);
                ais.setHeading((Integer) obj[19]);
                ais.setRaim((Short) obj[20]);                
                               
                Integer codNavio = (Integer) obj[21];

                if (codNavio != null) {                    
                    ais.setCodNavio(NavioController.getVesselByMmsi(manager, ais.getMmsi()));                    
                }

                listAis.add(ais);

            }

        } catch (Exception e) {
            return new ArrayList<>();
        }

        return listAis;

    }

    public static List<Ais> getListVesselActiveOldVersion(EntityManager manager, Date start, Date end)
            throws Exception {

        List<AisTabela> lista = AisTabelaController.getListAisTable(manager, start, end);

        if (lista == null || lista.isEmpty()) {
            return new ArrayList<>();
        }

        String sql;

        if (lista.size() < 2) {

            sql = "SELECT mmsi, "
                    + "MAX(codAis) as codAis, "
                    + "MAX(dataHora) as dataHora, "
                    + "MAX(codAisTabela) as codAisTabela "
                    + "FROM ais_" + lista.get(0).getCodAisTabela()
                    + " WHERE dataHora >= '" + Util.getDateFromBDSQL(start) + ".000' AND dataHora <= '" + Util.getDateFromBDSQL(end) + ".000' "
                    + "group by mmsi";

        } else {

            String union = "";

            for (AisTabela a : lista) {
                if (union.isEmpty()) {
                    union = "SELECT codAis, codAisTabela, dataHora, a.mmsi "
                            + "FROM ais_" + a.getCodAisTabela() + " AS a ";
                } else {
                    union += "UNION ALL SELECT codAis, codAisTabela, dataHora, a.mmsi "
                            + "FROM ais_" + a.getCodAisTabela() + " AS a ";
                }
            }

            sql = "SELECT mmsi, "
                    + "MAX(codAis) as codAis, "
                    + "MAX(dataHora) as dataHora, "
                    + "MAX(codAisTabela) as codAisTabela "
                    + "FROM (" + union + ") as r "
                    + "WHERE dataHora >= '" + Util.getDateFromBDSQL(start) + ".000' AND dataHora <= '" + Util.getDateFromBDSQL(end) + ".000' "
                    + "GROUP by mmsi";

        }

        Query query = manager.createNativeQuery(sql);

        List<Object[]> list = query.getResultList();

        List<Ais> listAis = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {

            int mmsi = (int) list.get(i)[0];
            Date date = (Date) list.get(i)[2];
            int codAisTable = (int) list.get(i)[3];

            Ais ais = getAisByDateHourAndMmsi(manager, codAisTable, date, mmsi);

            if (ais == null) {
                continue;
            }

            listAis.add(ais);

        }

        return listAis;
    }

    public static Ais getAisByDateHourAndMmsi(EntityManager manager, int codAisTable,
            Date dateHour, int mmsi) throws Exception {

        String sql = "SELECT TOP 1 codAis, codAisTabela, "
                + "dataHora, mmsi, destino, chegadaPrevista, "
                + "latitude, longitude, statusNavegacao, velocidadeSobreSolo, "
                + "cursoSobreSolo, velocidadeVento, maximaVento, direcaoVento, "
                + "velocidadeCorrente, direcaoCorrente FROM ais_" + codAisTable + " WHERE mmsi = " + mmsi
                + " AND dataHora = '" + Util.getDateFromBDSQL(dateHour) + "' "
                + "AND latitude IS NOT NULL AND longitude IS NOT NULL ORDER BY codAis DESC";

        Query query = manager.createNativeQuery(sql, Ais.class);

        Ais ais;

        try {
            ais = (Ais) query.getSingleResult();
            ais.setCodNavio(NavioController.getVesselByMmsi(manager, ais.getMmsi()));
        } catch (NoResultException e) {
            ais = null;
        }

        return ais;

    }

    public static Ais getAisByCodAis(EntityManager manager, int codAisTable, int codAis) throws Exception {

        String sql = "SELECT codAis, codAisTabela, "
                + "dataHora, mmsi, destino, chegadaPrevista, "
                + "latitude, longitude, statusNavegacao, velocidadeSobreSolo, "
                + "cursoSobreSolo, velocidadeVento, maximaVento, direcaoVento, "
                + "velocidadeCorrente, direcaoCorrente, epfd, draught, positionAccurate, "
                + "heading, raim  FROM ais_" + codAisTable
                + " WHERE codAis = " + codAis;

        Query query = manager.createNativeQuery(sql, Ais.class);

        Ais ais;

        try {
            ais = (Ais) query.getSingleResult();
        } catch (NoResultException e) {
            ais = null;
        }

        return ais;

    }

    public static List<Ais> getBoatTrail(EntityManager manager, Date start, Date end, int mmsi,
            Integer limitTrail) throws Exception {

        List<AisTabela> listAisTable = AisTabelaController.getListAisTable(manager, start, end);

        if (listAisTable == null || listAisTable.isEmpty()) {
            return new ArrayList<>();
        }

        String sql;

        if (listAisTable.size() < 2) {

            sql = "SELECT " + (limitTrail != null ? "TOP " + limitTrail : "")
                    + " codAis "
                    + ",codAisTabela "
                    + ",dataHora "
                    + ",mmsi "
                    + ",destino "
                    + ",chegadaPrevista "
                    + ",latitude "
                    + ",longitude "
                    + ",statusNavegacao "
                    + ",velocidadeSobreSolo "
                    + ",cursoSobreSolo "
                    + ",velocidadeVento "
                    + ",maximaVento "
                    + ",direcaoVento "
                    + ",velocidadeCorrente "
                    + ",direcaoCorrente "
                    + ",epfd "
                    + ",draught "
                    + ",positionAccurate "
                    + ",heading "
                    + ",raim "
                    + "FROM ais_" + listAisTable.get(0).getCodAisTabela()
                    + " WHERE dataHora >= '" + Util.getDateFromBDSQL(start)
                    + ".000' AND dataHora <= '" + Util.getDateFromBDSQL(end)
                    + ".000' AND mmsi = " + mmsi + " AND latitude IS NOT NULL AND longitude IS NOT NULL ORDER BY dataHora ASC";

        } else {

            String union = "";

            for (AisTabela a : listAisTable) {
                if (union.isEmpty()) {
                    union = "SELECT "
                            + " codAis "
                            + ",codAisTabela "
                            + ",dataHora "
                            + ",mmsi "
                            + ",destino "
                            + ",chegadaPrevista "
                            + ",latitude "
                            + ",longitude "
                            + ",statusNavegacao "
                            + ",velocidadeSobreSolo "
                            + ",cursoSobreSolo "
                            + ",velocidadeVento "
                            + ",maximaVento "
                            + ",direcaoVento "
                            + ",velocidadeCorrente "
                            + ",direcaoCorrente "
                            + ",epfd "
                            + ",draught "
                            + ",positionAccurate "
                            + ",heading "
                            + ",raim "
                            + "FROM ais_" + a.getCodAisTabela() + " AS a WHERE mmsi = " + mmsi;
                } else {
                    union += "UNION ALL SELECT "
                            + " codAis "
                            + ",codAisTabela "
                            + ",dataHora "
                            + ",mmsi "
                            + ",destino "
                            + ",chegadaPrevista "
                            + ",latitude "
                            + ",longitude "
                            + ",statusNavegacao "
                            + ",velocidadeSobreSolo "
                            + ",cursoSobreSolo "
                            + ",velocidadeVento "
                            + ",maximaVento "
                            + ",direcaoVento "
                            + ",velocidadeCorrente "
                            + ",direcaoCorrente "
                            + ",epfd "
                            + ",draught "
                            + ",positionAccurate "
                            + ",heading "
                            + ",raim "
                            + "FROM ais_" + a.getCodAisTabela() + " AS a WHERE mmsi = " + mmsi;
                }
            }

            sql = "SELECT " + (limitTrail != null ? "TOP " + limitTrail : "")
                    + " codAis "
                    + ",codAisTabela "
                    + ",dataHora "
                    + ",mmsi "
                    + ",destino "
                    + ",chegadaPrevista "
                    + ",latitude "
                    + ",longitude "
                    + ",statusNavegacao "
                    + ",velocidadeSobreSolo "
                    + ",cursoSobreSolo "
                    + ",velocidadeVento "
                    + ",maximaVento "
                    + ",direcaoVento "
                    + ",velocidadeCorrente "
                    + ",direcaoCorrente "
                    + ",epfd "
                    + ",draught "
                    + ",positionAccurate "
                    + ",heading "
                    + ",raim "
                    + "FROM (" + union + ") as r "
                    + " WHERE dataHora >= '" + Util.getDateFromBDSQL(start)
                    + ".000' AND dataHora <= '" + Util.getDateFromBDSQL(end)
                    + ".000' AND mmsi = " + mmsi + " AND latitude IS NOT NULL AND longitude IS NOT NULL ORDER BY dataHora ASC";

        }

        System.out.println(sql);
        
        Query query = manager.createNativeQuery(sql, Ais.class);

        try {
            List<Ais> list = query.getResultList();

            return list;
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    public static List<Ais> getAisHistory(EntityManager manager, Date startDate, Date endDate) throws Exception {

        List<AisTabela> listAisTable = AisTabelaController.getListAisTable(manager, startDate, endDate);

        if (listAisTable == null || listAisTable.isEmpty()) {
            return new ArrayList<>();
        }

        String sql;

        if (listAisTable.size() < 2) {

            sql = "SELECT codAis "
                    + ",codAisTabela "
                    + ",dataHora "
                    + ",mmsi "
                    + ",destino "
                    + ",chegadaPrevista "
                    + ",latitude "
                    + ",longitude "
                    + ",statusNavegacao "
                    + ",velocidadeSobreSolo "
                    + ",cursoSobreSolo "
                    + ",velocidadeVento "
                    + ",maximaVento "
                    + ",direcaoVento "
                    + ",velocidadeCorrente "
                    + ",direcaoCorrente "
                    + ",epfd "
                    + ",draught "
                    + ",positionAccurate "
                    + ",heading "
                    + ",raim "
                    + "FROM ais_" + listAisTable.get(0).getCodAisTabela()
                    + " WHERE dataHora >= '" + Util.getDateFromBDSQL(startDate)
                    + ".000' AND dataHora <= '" + Util.getDateFromBDSQL(endDate)
                    + ".000' AND latitude IS NOT NULL AND longitude IS NOT NULL ORDER BY dataHora ASC";

        } else {

            String union = "";

            for (AisTabela a : listAisTable) {
                if (union.isEmpty()) {
                    union = "SELECT codAis "
                            + ",codAisTabela "
                            + ",dataHora "
                            + ",mmsi "
                            + ",destino "
                            + ",chegadaPrevista "
                            + ",latitude "
                            + ",longitude "
                            + ",statusNavegacao "
                            + ",velocidadeSobreSolo "
                            + ",cursoSobreSolo "
                            + ",velocidadeVento "
                            + ",maximaVento "
                            + ",direcaoVento "
                            + ",velocidadeCorrente "
                            + ",direcaoCorrente "
                            + ",epfd "
                            + ",draught "
                            + ",positionAccurate "
                            + ",heading "
                            + ",raim "
                            + "FROM ais_" + a.getCodAisTabela() + " AS a ";
                } else {
                    union += "UNION ALL SELECT "
                            + " codAis "
                            + ",codAisTabela "
                            + ",dataHora "
                            + ",mmsi "
                            + ",destino "
                            + ",chegadaPrevista "
                            + ",latitude "
                            + ",longitude "
                            + ",statusNavegacao "
                            + ",velocidadeSobreSolo "
                            + ",cursoSobreSolo "
                            + ",velocidadeVento "
                            + ",maximaVento "
                            + ",direcaoVento "
                            + ",velocidadeCorrente "
                            + ",direcaoCorrente "
                            + ",epfd "
                            + ",draught "
                            + ",positionAccurate "
                            + ",heading "
                            + ",raim "
                            + "FROM ais_" + a.getCodAisTabela() + " AS a ";
                }
            }

            sql = "SELECT codAis "
                    + ",codAisTabela "
                    + ",dataHora "
                    + ",mmsi "
                    + ",destino "
                    + ",chegadaPrevista "
                    + ",latitude "
                    + ",longitude "
                    + ",statusNavegacao "
                    + ",velocidadeSobreSolo "
                    + ",cursoSobreSolo "
                    + ",velocidadeVento "
                    + ",maximaVento "
                    + ",direcaoVento "
                    + ",velocidadeCorrente "
                    + ",direcaoCorrente "
                    + ",epfd "
                    + ",draught "
                    + ",positionAccurate "
                    + ",heading "
                    + ",raim "
                    + "FROM (" + union + ") as r "
                    + " WHERE dataHora >= '" + Util.getDateFromBDSQL(startDate)
                    + ".000' AND dataHora <= '" + Util.getDateFromBDSQL(endDate)
                    + ".000' AND latitude IS NOT NULL AND longitude IS NOT NULL ORDER BY dataHora ASC";

        }
        
        Query query = manager.createNativeQuery(sql, Ais.class);

        try {

            List<Ais> list = query.getResultList();

            if (list != null && !list.isEmpty()) {
                for (Ais ais : list) {
                    Navio navio = Util.getFirstNavioFromListAisByMmsi(list, ais.getMmsi());
                    if (navio == null) {
                        navio = NavioController.getVesselByMmsi(manager, ais.getMmsi());
                        if (navio != null) {
                            ImageVessel imageVessel = NavioController.getImageVesselByCod(manager, navio.getCodNavio());
                            navio.setImageUrl(imageVessel.getImageUrl());
                        }
                    }
                    ais.setCodNavio(navio);
                }
            }

            return list;

        } catch (Exception e) {
            return new ArrayList<>();

        }

    }

}
