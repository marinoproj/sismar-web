package br.com.marino.sismar.controller;

import br.com.marino.sismar.entity.NavioUltimaAtualizacao;
import br.com.marino.sismar.util.MarineTraffic;
import br.com.marino.sismar.util.Util;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class NavioUltimaAtualizacaoController {

    public static NavioUltimaAtualizacao getNavioUltimaAtualizacaoByMarineTraffic(Integer mmsi)
            throws Exception {

        if (mmsi == null) {
            throw new Exception("mmsi is null.");
        }

        Document doc = Jsoup.connect("https://www.marinetraffic.com/en/ais/details/ships/mmsi:" + mmsi)
                .get();

        MarineTraffic marineTraffic = new MarineTraffic.Builder()
                .setLatLng(doc)
                .setStatus(doc)
                .setVelDir(doc)
                .setDataHora(doc)
                .setCalado(doc)
                .setDestino(doc)
                .setChegadaPrevista(doc)
                .setIndicativo(doc)
                .build();

        return new NavioUltimaAtualizacao(marineTraffic, mmsi);

    }

    public static NavioUltimaAtualizacao getRadioLastUpdate(EntityManager manager, int mmsi) throws Exception {

        try {

            Query query = manager.createNamedQuery("NavioUltimaAtualizacao.findByMmsi");
            query.setParameter("mmsi", mmsi);

            Object obj = query.getSingleResult();

            //if (obj == null) {
            //  return null;
            //}
            NavioUltimaAtualizacao navioUltimaAtualizacao;// = (NavioUltimaAtualizacao) obj;

            if (obj == null) {
                navioUltimaAtualizacao = NavioUltimaAtualizacaoController.getNavioUltimaAtualizacaoByMarineTraffic(mmsi);

            } else {
                navioUltimaAtualizacao = (NavioUltimaAtualizacao) obj;

            }

            if (navioUltimaAtualizacao.getLatitude() == null) {
                navioUltimaAtualizacao = NavioUltimaAtualizacaoController.getNavioUltimaAtualizacaoByMarineTraffic(mmsi);

            } else {

                int tempoUltimaAtualizacao = Util.
                        getRangeSeconds(navioUltimaAtualizacao.getDataUltimaAtualizacao(),
                                new Date()) / 60;

                if (tempoUltimaAtualizacao > Util.TMP_MINUTES_ONLINE_AIS) {
                    navioUltimaAtualizacao = NavioUltimaAtualizacaoController.getNavioUltimaAtualizacaoByMarineTraffic(mmsi);
                }

            }

            return navioUltimaAtualizacao;

        } catch (NoResultException ex) {

            return null;
        }

    }

}
