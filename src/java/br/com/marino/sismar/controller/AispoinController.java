package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.Ais;
import br.com.marino.sismar.entity.Aispoin;
import br.com.marino.sismar.util.NavioMapAis;
import br.com.marino.sismar.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class AispoinController {

    public static List<Aispoin> getListAispoinsNowByIdPoin(EntityManager manager, int idPoin) throws Exception {

        String sql = "SELECT "
                + "codAisPoin, "
                + "mmsi, "
                + "dataEntrada, "
                + "dataSaida, "
                + "velocidadeEntrada, "
                + "velocidadeSaida, "
                + "codPoin "
                + "FROM aispoin "
                + "WHERE dataEntrada >= '" + Util.DATE_START_AIS + "' AND codPoin = " + idPoin + " AND dataSaida IS NULL ORDER BY dataEntrada DESC";

        Query query = manager.createNativeQuery(sql, Aispoin.class);

        List<Aispoin> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list;

    }
    
    public static List<Aispoin> getListAispoinsOpenByEvent(EntityManager manager, int idPoin, Date date) throws Exception {

        String sql = "SELECT "
                + "codAisPoin, "
                + "mmsi, "
                + "dataEntrada, "
                + "dataSaida, "
                + "velocidadeEntrada, "
                + "velocidadeSaida, "
                + "codPoin "
                + "FROM aispoin "
                + "WHERE codPoin = " + idPoin + " AND dataEntrada >= '" + Util.DATE_START_AIS + "' AND (dataSaida IS NULL OR dataSaida >= '"+Util.getDateFromBDSQL(date)+"') ORDER BY dataEntrada ASC";

        Query query = manager.createNativeQuery(sql, Aispoin.class);

        List<Aispoin> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list;

    }

    public static List<Aispoin> getListAispoinOpenByVessel(EntityManager manager, int mmsi) throws Exception {

        String sql = "SELECT "
                + "codAisPoin, "
                + "mmsi, "
                + "dataEntrada, "
                + "dataSaida, "
                + "velocidadeEntrada, "
                + "velocidadeSaida, "
                + "codPoin "
                + "FROM aispoin "
                + "WHERE dataEntrada >= '" + Util.DATE_START_AIS + "' AND mmsi = " + mmsi + " AND dataSaida IS NULL "
                + "ORDER BY dataEntrada DESC";

        Query query = manager.createNativeQuery(sql, Aispoin.class);

        List<Aispoin> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list;

    }

    
    /**
     * Retorna a lista de AisPoin CRIADO(DATA_CHEGADA) entre um intervalo
     * @param manager
     * @param codPoin
     * @param start
     * @param end
     * @return
     * @throws Exception 
     */
    public static List<Aispoin> getListAispoinVesselArrivalDateByPeriod(EntityManager manager,
            int codPoin, Date start, Date end) throws Exception {

        String sql = "SELECT "
                + "codAisPoin, "
                + "mmsi, "
                + "dataEntrada, "
                + "dataSaida, "
                + "velocidadeEntrada, "
                + "velocidadeSaida, "
                + "codPoin "
                + "FROM aispoin "
                + "WHERE codPoin = " + codPoin + " AND dataEntrada > '" + Util.getDateFromBDSQL(start) + "' "
                + "AND dataEntrada <= '" + Util.getDateFromBDSQL(end) + "' "
                + "ORDER BY dataEntrada ASC";

        Query query = manager.createNativeQuery(sql, Aispoin.class);

        List<Aispoin> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list;

    }
    
    /**
     * Retorna a lista de AisPoin ENCERRADO(DATA_SAIDA) entre um intervalo
     * @param manager
     * @param codPoin
     * @param start
     * @param end
     * @return
     * @throws Exception 
     */
    public static List<Aispoin> getListAispoinVesselDepartureDateByPeriod(EntityManager manager,
            int codPoin, Date start, Date end) throws Exception {

        String sql = "SELECT "
                + "codAisPoin, "
                + "mmsi, "
                + "dataEntrada, "
                + "dataSaida, "
                + "velocidadeEntrada, "
                + "velocidadeSaida, "
                + "codPoin "
                + "FROM aispoin "
                + "WHERE codPoin = " + codPoin + " AND dataSaida > '" + Util.getDateFromBDSQL(start) + "' "
                + "AND dataSaida <= '" + Util.getDateFromBDSQL(end) + "' "
                + "ORDER BY dataSaida ASC";

        Query query = manager.createNativeQuery(sql, Aispoin.class);

        List<Aispoin> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list;

    }
    
    public static List<Aispoin> getListAispoinVesselByPeriod(EntityManager manager,
            int mmsi, Date start, Date end) throws Exception {

        String sql = "SELECT "
                + "codAisPoin, "
                + "mmsi, "
                + "dataEntrada, "
                + "dataSaida, "
                + "velocidadeEntrada, "
                + "velocidadeSaida, "
                + "codPoin "
                + "FROM aispoin "
                + "WHERE mmsi = " + mmsi + " AND (dataEntrada >= '" + Util.getDateFromBDSQL(start) + "' OR dataSaida >= '" + Util.getDateFromBDSQL(start) + "') "
                + "AND (dataEntrada <= '" + Util.getDateFromBDSQL(end) + "' OR dataSaida <= '" + Util.getDateFromBDSQL(end) + "') "
                + "ORDER BY dataEntrada ASC";

        Query query = manager.createNativeQuery(sql, Aispoin.class);

        List<Aispoin> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list;

    }

    /**
     * Retorna uma lista de todos os navios que atracaram no poin ordenados pela
     * ordem de chegada
     *
     * @param manager
     * @param codPoin
     * @param start
     * @param end
     * @param limitIntervalMinutes
     * @param orderStart
     * @return
     * @throws Exception
     */
    public static List<Aispoin> getListShipAttracationsPeriodFromPoin(EntityManager manager,
            int codPoin, Date start, Date end,
            Integer limitIntervalMinutes, boolean orderStart) throws Exception {

        String sql = "SELECT "
                + "codAisPoin, "
                + "mmsi, "
                + "dataEntrada, "
                + "dataSaida, "
                + "velocidadeEntrada, "
                + "velocidadeSaida, "
                + "codPoin "
                + "FROM aispoin "
                + "WHERE codPoin = " + codPoin + " AND (dataEntrada >= '" + Util.getDateFromBDSQL(start) + "' OR dataSaida >= '" + Util.getDateFromBDSQL(start) + "') "
                + "AND (dataEntrada <= '" + Util.getDateFromBDSQL(end) + "' OR dataSaida <= '" + Util.getDateFromBDSQL(end) + "') "
                + (limitIntervalMinutes != null ? ("AND datediff(mi, dataEntrada, dataSaida) > " + limitIntervalMinutes) : "")
                + " ORDER BY dataEntrada " + (orderStart ? "ASC" : "DESC");

        Query query = manager.createNativeQuery(sql, Aispoin.class);

        List<Aispoin> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        List<Aispoin> listNew = new ArrayList<>();

        for (Aispoin aispoin : list) {

            Ais ais = new Ais();
            ais.setMmsi(aispoin.getMmsi());
            ais.setCodNavio(NavioController.getVesselByMmsi2(manager, aispoin.getMmsi()));

            aispoin.setTempoPermanencia(Util.getRangeSeconds(aispoin.getDataEntrada(), aispoin.getDataSaida()));
            aispoin.setAisMmsi(ais);

            NavioMapAis navioMapAis = new NavioMapAis(aispoin.getAisMmsi().getCodNavio());

            if (navioMapAis.getCodCategoriaEmbarcacao() == NavioMapAis.PASSAGEIROS
                    || navioMapAis.getCodCategoriaEmbarcacao() == NavioMapAis.REBOCADOR_EMBARCACOES_ESPECIAIS
                    || !navioMapAis.isDimensaoProporcionalMapa()) {
                continue;
            }

            listNew.add(aispoin);

        }

        return listNew;

    }

    /**
     * Retorna uma lista com os 5 navios que atracaram no poin ordenado por
     * aqueles com o maior tempo de permanência
     *
     * @param manager
     * @param codPoin
     * @param start
     * @param end
     * @param limitIntervalMinutes
     * @return
     * @throws Exception
     */
    public static List<Aispoin> getTopFiveShipAttracationsPermanencePeriodFromPoin(EntityManager manager,
            int codPoin, Date start, Date end,
            Integer limitIntervalMinutes) throws Exception {

        String sql = "SELECT "
                + "codAisPoin, "
                + "mmsi, "
                + "dataEntrada, "
                + "dataSaida, "
                + "velocidadeEntrada, "
                + "velocidadeSaida, "
                + "codPoin "
                + "FROM aispoin "
                + "WHERE codPoin = " + codPoin + " AND (dataEntrada >= '" + Util.getDateFromBDSQL(start) + "' OR dataSaida >= '" + Util.getDateFromBDSQL(start) + "') "
                + "AND (dataEntrada <= '" + Util.getDateFromBDSQL(end) + "' OR dataSaida <= '" + Util.getDateFromBDSQL(end) + "') "
                + (limitIntervalMinutes != null ? ("AND datediff(mi, dataEntrada, dataSaida) > " + limitIntervalMinutes) : "")
                + " ORDER BY datediff(mi, dataEntrada, dataSaida) DESC";

        Query query = manager.createNativeQuery(sql, Aispoin.class);

        List<Aispoin> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        List<Aispoin> listNew = new ArrayList<>();

        for (Aispoin aispoin : list) {

            Ais ais = new Ais();
            ais.setMmsi(aispoin.getMmsi());
            ais.setCodNavio(NavioController.getVesselByMmsi2(manager, aispoin.getMmsi()));

            aispoin.setTempoPermanencia(Util.getRangeSeconds(aispoin.getDataEntrada(), aispoin.getDataSaida()));
            aispoin.setAisMmsi(ais);

            NavioMapAis navioMapAis = new NavioMapAis(aispoin.getAisMmsi().getCodNavio());

            if (navioMapAis.getCodCategoriaEmbarcacao() == NavioMapAis.PASSAGEIROS
                    || navioMapAis.getCodCategoriaEmbarcacao() == NavioMapAis.REBOCADOR_EMBARCACOES_ESPECIAIS
                    || !navioMapAis.isDimensaoProporcionalMapa()) {
                continue;
            }

            listNew.add(aispoin);

            if (listNew.size() == 5) {
                break;
            }

        }

        return listNew;

    }

    /**
     * Retorna o navio atracado no poin agora
     *
     * @param manager
     * @param idPoin
     * @param limitIntervalMinutes
     * @return
     * @throws Exception
     */
    public static Aispoin getShipNowFromPoin(EntityManager manager,
            int idPoin,
            Integer limitIntervalMinutes) throws Exception {

        String sql = "SELECT "
                + "codAisPoin, "
                + "mmsi, "
                + "dataEntrada, "
                + "dataSaida, "
                + "velocidadeEntrada, "
                + "velocidadeSaida, "
                + "codPoin "
                + "FROM aispoin "
                + "WHERE dataEntrada >= '" + Util.DATE_START_AIS + "' "
                + "AND codPoin = " + idPoin + " "
                + "AND dataSaida IS NULL "
                + (limitIntervalMinutes != null ? (" AND datediff(mi, dataEntrada, getdate()) > " + limitIntervalMinutes) : "")
                + " ORDER BY dataEntrada DESC";

        Query query = manager.createNativeQuery(sql, Aispoin.class);

        List<Aispoin> list = query.getResultList();

        if (list == null || list.isEmpty()) {
            return null;
        }

        for (Aispoin aispoin : list) {

            Ais ais = new Ais();
            ais.setMmsi(aispoin.getMmsi());
            ais.setCodNavio(NavioController.getVesselByMmsi2(manager, aispoin.getMmsi()));

            aispoin.setTempoPermanencia(Util.getRangeSeconds(aispoin.getDataEntrada(), new Date()));
            aispoin.setAisMmsi(ais);

            NavioMapAis navioMapAis = new NavioMapAis(aispoin.getAisMmsi().getCodNavio());

            if (navioMapAis.getCodCategoriaEmbarcacao() == NavioMapAis.PASSAGEIROS
                    || navioMapAis.getCodCategoriaEmbarcacao() == NavioMapAis.REBOCADOR_EMBARCACOES_ESPECIAIS
                    || !navioMapAis.isDimensaoProporcionalMapa()) {
                continue;
            }

            return aispoin;

        }

        return null;

    }

}
