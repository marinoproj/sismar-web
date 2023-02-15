package br.com.marino.sismar.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * 
 * Document doc = Jsoup.connect("https://www.marinetraffic.com/en/ais/details/ships/mmsi:" + 538003036)
//                    .get();
//
//            MarineTraffic marineTraffic = new MarineTraffic.Builder()
//                    .setLatLng(doc)
//                    .setStatus(doc)
//                    .setVelDir(doc)
//                    .setDataHora(doc)
//                    .setTipo(doc)
//                    .setDimensao(doc)
//                    .setImo(doc)
//                    .setMMSI(doc)
//                    .setNomeEmbarcacao(doc)
//                    .setCalado(doc)
//                    .setDestino(doc)
//                    .setChegadaPrevista(doc)
//                    .setIndicativo(doc)
//                    .build();
 * 
 * @author Leonardo
 */
public class MarineTraffic {
    
    public static final int PORCENTAGEM_DIMENSAO = 15;
    
    private Date dataHora;
    private Float latitude;
    private Float longitude;
    private String status;
    private Float velocidade;
    private Float direcao;
    private String tipo;
    private String dimensao;
    private Integer imo;
    private String nomeEmbarcacao;
    private Integer mmsi;
    private Float calado;
    private String destino;
    private String indicativo;
    private String chegadaPrevista;

    private MarineTraffic(Date dataHora, Float latitude, Float longitude, String status,
            Float velocidade, Float direcao, String tipo, String dimensao,
            Integer imo, String nomeEmbarcacao, Integer mmsi, Float calado,
            String destino, String indicativo, String chegadaPrevista) {
        this.dataHora = dataHora;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.velocidade = velocidade;
        this.direcao = direcao;
        this.tipo = tipo;
        this.dimensao = dimensao;
        this.imo = imo;
        this.nomeEmbarcacao = nomeEmbarcacao;
        this.mmsi = mmsi;
        this.calado = calado;
        this.destino = destino;
        this.indicativo = indicativo;
        this.chegadaPrevista = chegadaPrevista;
    }

    public static class Builder {

        private Date dataHora;
        private Float latitude;
        private Float longitude;
        private String status;
        private Float velocidade;
        private Float direcao;
        private String tipo;
        private String dimensao;
        private Integer imo;
        private String nomeEmbarcacao;
        private Integer mmsi;
        private Float calado;
        private String destino;
        private String indicativo;
        private String chegadaPrevista;

        public Builder() {
        }

        /**
         * Pega a latitude e longitude do site Marine Traffic
         *
         * @param doc
         * @return
         * @throws java.lang.Exception
         */
        public Builder setLatLng(Document doc) throws Exception {

            Elements elements = doc.select(".table-cell, .cell-full, .collapse-768").
                    select(".group-ib");

            String text = null;

            for (Element element : elements) {
                if (element.select("span").isEmpty()
                        || (!element.select("span")
                                .first()
                                .text()
                                .trim()
                                .equalsIgnoreCase("Latitude / Longitude:"))) {
                    continue;
                }
                text = element.select("a").text();
                break;
            }

            if (text == null) {
                throw new Exception("Não foi possível localizar a latitude e longitude da embarcação.");
            }

            String data[] = text.split("/");
            data[0] = data[0].trim().replace("°", "");
            data[1] = data[1].trim().replace("°", "");

            latitude = Float.parseFloat(data[0]);
            longitude = Float.parseFloat(data[1]);

            return this;
        }

        /**
         * Pega o status da embarcação
         *
         * @param doc
         * @return
         * @throws java.lang.Exception
         */
        public Builder setStatus(Document doc) throws Exception {

            Elements elements = doc.select(".table-cell, .cell-full, .collapse-768").
                    select(".group-ib");

            String text = null;

            for (Element element : elements) {
                if (element.select("span").isEmpty()
                        || !element.select("span")
                                .first()
                                .text()
                                .trim()
                                .equalsIgnoreCase("Status:")) {
                    continue;
                }
                text = element.select("span").last().text();
                break;
            }

            if (text == null) {
                throw new Exception("Não foi possível localizar o status da embarcação.");
            }

            status = text;

            return this;
        }

        /**
         * Pega a velocidade e curso da embarcação
         *
         * @param doc
         * @return
         * @throws java.lang.Exception
         */
        public Builder setVelDir(Document doc) throws Exception {

            Elements elements = doc.select(".table-cell, .cell-full, .collapse-768").
                    select(".group-ib");

            String text = null;

            for (Element element : elements) {
                if (element.select("span").isEmpty()
                        || !element.select("span")
                                .first()
                                .text()
                                .trim()
                                .equalsIgnoreCase("Speed/Course:")) {
                    continue;
                }
                text = element.select("span").last().text();
                break;
            }

            if (text == null) {
                throw new Exception("Não foi possível localizar a velocidade e direção de navegação.");
            }

            String data[] = text.split("/");
            data[0] = data[0].trim().equalsIgnoreCase("-") ? "0"
                    : data[0].trim().replace("kn", "");
            data[1] = data[1].trim().equalsIgnoreCase("-") ? "0"
                    : data[1].trim().replace("°", "");

            velocidade = Float.parseFloat(data[0]);
            direcao = Float.parseFloat(data[1]);

            return this;
        }

        /**
         * Pega a data e hora
         *
         * @param doc
         * @return
         * @throws java.lang.Exception
         */
        public Builder setDataHora(Document doc) throws Exception {

            Elements elements = doc.select(".table-cell, .cell-full, .collapse-768").
                    select(".group-ib");

            String text = null;

            for (Element element : elements) {
                if (element.select("span").isEmpty()
                        || !element.select("span")
                                .first()
                                .text()
                                .trim()
                                .equalsIgnoreCase("Position Received:")) {
                    continue;
                }
                text = element.select("strong").text();
                break;
            }

            if (text == null) {
                throw new Exception("Não foi possível localizar a data e hora da marcação.");
            }

            String regex = ".*(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}).*";

            if (!text.matches(regex)) {
                throw new Exception("Não foi possível localizar a data e hora da marcação.");
            }

            String data = text.replaceAll(regex, "$1");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Calendar d = Calendar.getInstance();
            d.setTime(format.parse(data));
            d.add(Calendar.HOUR_OF_DAY, -3);

            dataHora = d.getTime();

            return this;
        }

        /**
         * Pega o tipo de embarcação
         *
         * @param doc
         * @return
         * @throws java.lang.Exception
         */
        public Builder setTipo(Document doc) throws Exception {

            Elements elements = doc.select(".group-ib, .short-line");

            String text = null;

            for (Element element : elements) {
                if (element.select("span").isEmpty()
                        || !element.select("span")
                                .first()
                                .text()
                                .trim()
                                .equalsIgnoreCase("AIS Vessel Type:")) {
                    continue;
                }
                text = element.children().last().text();
                break;
            }

            if (text == null) {
                throw new Exception("Não foi possível localizar o tipo da embarcação.");
            }
            tipo = text;

            return this;
        }

        /**
         * Pega a dimensão da embarcação
         *
         * @param doc
         * @return
         * @throws java.lang.Exception
         */
        public Builder setDimensao(Document doc) throws Exception {

            Elements elements = doc.select(".group-ib, .short-line");

            String text = null;

            for (Element element : elements) {
                if (element.select("span").isEmpty()
                        || !element.select("span")
                                .first()
                                .text()
                                .trim()
                                .replaceAll("\\s+", " ")
                                .trim()
                                .equalsIgnoreCase("Length Overall x Breadth Extreme:")) {
                    continue;
                }
                text = element.children().last().text();
                break;
            }

            if (text == null) {
                throw new Exception("Não foi possível localizar a dimensão da embarcação.");
            }

            String data[] = text.split("×");

            if (data.length != 2) {
                dimensao = "A = " + 0 + " B = " + 0 + " C = " + 0 + " D = " + 0;

            } else {

                data[0] = data[0].trim().replace("m", "");
                data[1] = data[1].trim().replace("m", "");

                int comprimento = (int) Float.parseFloat(data[0]);
                int largura = (int) Float.parseFloat(data[1]);

                int b = (comprimento * PORCENTAGEM_DIMENSAO) / 100;
                int a = comprimento - b;
                int c = largura / 2;
                int d = largura - c;

                dimensao = "A = " + a + " B = " + b + " C = " + c + " D = " + d;

            }

            return this;
        }

        /**
         * Pega o imo da embarcação
         *
         * @param doc
         * @return
         * @throws java.lang.Exception
         */
        public Builder setImo(Document doc) throws Exception {

            Elements elements = doc.select(".group-ib, .short-line");

            String text = null;

            for (Element element : elements) {
                if (element.select("span").isEmpty()
                        || !element.select("span")
                                .first()
                                .text()
                                .trim()
                                .equalsIgnoreCase("IMO:")) {
                    continue;
                }
                text = element.children().last().text();
                break;
            }

            if (text == null) {
                throw new Exception("Não foi possível localizar o imo da embarcação.");
            }

            if (text.matches("\\d+")) {
                imo = Integer.parseInt(text);
            }

            return this;
        }

        /**
         * Pega o mmsi da embarcação
         *
         * @param doc
         * @return
         * @throws java.lang.Exception
         */
        public Builder setMMSI(Document doc) throws Exception {

            Elements elements = doc.select(".group-ib, .short-line");

            String text = null;

            for (Element element : elements) {
                if (element.select("span").isEmpty()
                        || !element.select("span")
                                .first()
                                .text()
                                .trim()
                                .equalsIgnoreCase("MMSI:")) {
                    continue;
                }
                text = element.children().last().text();
                break;
            }

            if (text == null) {
                throw new Exception("Não foi possível localizar o mmsi da embarcação.");
            }

            mmsi = Integer.parseInt(text);

            return this;
        }

        /**
         * Pega o nome da embarcação
         *
         * @param doc
         * @return
         * @throws java.lang.Exception
         */
        public Builder setNomeEmbarcacao(Document doc) throws Exception {

            Elements elements = doc.select(".group-ib");

            String text = null;

            for (Element element : elements) {

                if (element.children().size() < 2
                        || !(element.children().get(0).tagName().equalsIgnoreCase("a")
                        && element.children().get(1).tagName().equalsIgnoreCase("h1"))) {
                    continue;
                }

                text = element.children().get(1).text().trim().toUpperCase();
                break;
            }

            if (text == null) {
                throw new Exception("Não foi possível localizar o nome da embarcação.");
            }

            nomeEmbarcacao = text;

            return this;

        }

        /**
         * Pega o calado da embarcação
         *
         * @param doc
         * @return
         * @throws java.lang.Exception
         */
        public Builder setCalado(Document doc) throws Exception {

            Elements elements = doc.select("tr");

            String text = null;

            for (Element element : elements) {
                if (!element.children().first().text().trim().equalsIgnoreCase("Draught")) {
                    continue;
                }
                text = element.children().last().text();
                break;
            }

            if (text != null) {
                text = text.replace("m", "").trim();
                try {
                    calado = Float.parseFloat(text);
                } catch (Exception ex) {
                }
            }

            return this;
        }

        /**
         * Pega o destino da embarcação
         *
         * @param doc
         * @return
         * @throws Exception
         */
        public Builder setDestino(Document doc) throws Exception {

            Element element = doc.select("div.infowin-container")
                    .select("div.row")
                    .select("div.ib-elem").last().children().first();

            String text = element.attr("title");

            if (text == null) {
                throw new Exception("Não foi possível localizar o destino da embarcação.");
            }

            destino = text.trim();

            return this;
        }

        /**
         * Pega o indicativo da embarcação
         *
         * @param doc
         * @return
         * @throws java.lang.Exception
         */
        public Builder setIndicativo(Document doc) throws Exception {

            Elements elements = doc.select(".group-ib, .short-line");

            String text = null;

            for (Element element : elements) {
                if (element.select("span").isEmpty()
                        || !element.select("span")
                                .first()
                                .text()
                                .trim()
                                .equalsIgnoreCase("Call Sign:")) {
                    continue;
                }
                text = element.children().last().text();
                break;
            }

            if (text == null) {
                throw new Exception("Não foi possível localizar o indicativo da embarcação.");
            }

            indicativo = text;

            return this;
        }

        /**
         * Pega a data de chegada prevista
         *
         * @param doc
         * @return
         * @throws Exception
         */
        public Builder setChegadaPrevista(Document doc) throws Exception {

            Elements elements = doc.select("div.infowin-container")
                    .select("div.row");

            String text = null;

            for (Element element : elements) {

                Element elementChildren = element.children().last();

                if (!elementChildren.select("b")
                        .text()
                        .trim()
                        .replaceAll("\\s+", "")
                        .trim()
                        .equalsIgnoreCase("ETA:")) {
                    continue;
                }

                text = elementChildren.select("span").last().text();
                break;
            }

            if (text == null) {
                chegadaPrevista = "";
            } else {
                text = text.trim();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Timestamp ts = new Timestamp(Long.parseLong(text) * 1000);
                chegadaPrevista = format.format(new Date(ts.getTime()));
            }

            return this;
        }

        public MarineTraffic build() {
            return new MarineTraffic(dataHora, latitude, longitude, status, velocidade,
                    direcao, tipo, dimensao, imo, nomeEmbarcacao,
                    mmsi, calado, destino, indicativo, chegadaPrevista);
        }

    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(Float velocidade) {
        this.velocidade = velocidade;
    }

    public Float getDirecao() {
        return direcao;
    }

    public void setDirecao(Float direcao) {
        this.direcao = direcao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDimensao() {
        return dimensao;
    }

    public void setDimensao(String dimensao) {
        this.dimensao = dimensao;
    }

    public Integer getImo() {
        return imo;
    }

    public void setImo(Integer imo) {
        this.imo = imo;
    }

    public String getNomeEmbarcacao() {
        return nomeEmbarcacao;
    }

    public void setNomeEmbarcacao(String nomeEmbarcacao) {
        this.nomeEmbarcacao = nomeEmbarcacao;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public Float getCalado() {
        return calado;
    }

    public void setCalado(Float calado) {
        this.calado = calado;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getIndicativo() {
        return indicativo;
    }

    public void setIndicativo(String indicativo) {
        this.indicativo = indicativo;
    }

    public String getChegadaPrevista() {
        return chegadaPrevista;
    }

    public void setChegadaPrevista(String chegadaPrevista) {
        this.chegadaPrevista = chegadaPrevista;
    }

    @Override
    public String toString() {
        return "MarineTraffic{" + "dataHora=" + dataHora + ", latitude=" + latitude + ", longitude=" + longitude + ", status=" + status + ", velocidade=" + velocidade + ", direcao=" + direcao + ", tipo=" + tipo + ", dimensao=" + dimensao + ", imo=" + imo + ", nomeEmbarcacao=" + nomeEmbarcacao + ", mmsi=" + mmsi + ", calado=" + calado + ", destino=" + destino + ", indicativo=" + indicativo + ", chegadaPrevista=" + chegadaPrevista + '}';
    }

}
