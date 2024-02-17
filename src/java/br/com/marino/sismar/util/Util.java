package br.com.marino.sismar.util;

import br.com.marino.sismar.chart.Charts_Valores;
import br.com.marino.sismar.controller.UsuariosWebController;
import br.com.marino.sismar.entity.Ais;
import br.com.marino.sismar.entity.AisPlayBack;
import br.com.marino.sismar.entity.Aispoin;
import br.com.marino.sismar.entity.Correntometro;
import br.com.marino.sismar.entity.Monitorar;
import br.com.marino.sismar.entity.MonitorarRegra;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.NavioUltimaAtualizacao;
import br.com.marino.sismar.entity.Poin;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.entity.VesselFila;
import br.com.marino.sismar.entity.VesselSearch;
import br.com.marino.sismar.session.SessionContext;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import sun.misc.BASE64Decoder;

public class Util {

    public static final String TYPE_TOKEN_USER = "USUARIO";
    public static final String TYPE_TOKEN_CLIENT = "CLIENTE";
    public static final String SECRET_API = "2CC858FAD27FC91A672C121C2C583A30";
    public static final String DATE_START_AIS = "24-04-2019 00:00:00";
    private static final DecimalFormat FORMAT_VALUE = new DecimalFormat("###,##0.0");
    private static final NumberFormat VALUE = NumberFormat.getInstance();
    public static final int LEVEL_DEFAULT_CORRENTOMETRO = 6;
    public static final int TMP_MINUTES_ONLINE_AIS = 180;
    public static final int TMP_SECONDS_ONLINE_WIND = 180;
    public static final int TMP_SECONDS_ONLINE_SEACURRENT = 2400;
    public static final int TMP_SECONDS_ONLINE_PIER1 = 180;
    public static final int TMP_SECONDS_ONLINE_PIER2 = 180;
    public static final int TMP_SECONDS_ONLINE_PIER3 = 180;
    public static final int TMP_SECONDS_ONLINE_PIER4 = 180;
    public static final String TOKEN = "c767c9d4e2a584cd2c8644e6db3fd16a";
    private static final String TYPE_DATE_FORMAT_BD = "dd-MM-yyyy HH:mm:ss";
    //private static final String TYPE_DATE_FORMAT_BD = "yyyy-MM-dd HH:mm:ss";

    public static String getPathUrl() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/";
    }

    public static String getImageVessel(Byte[] image) {

        if (image == null) {
            return "/sismar/faces/javax.faces.resource/img/sem_imagem.png";
        }
        byte[] bytes = new byte[image.length];
        for (int i = 0; i < image.length; i++) {
            bytes[i] = image[i];
        }
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes, false)));
        return sb.toString();

    }

    public static String getImageVessel(String image) {
        if (image == null || image.isEmpty()) {
            return "/sismar/faces/javax.faces.resource/img/sem_imagem.png";
        }       
        return image;
    }
    
    public static String getDateFromBDSQL(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(TYPE_DATE_FORMAT_BD);
        return format.format(date);
    }

    public static String converterValue(double value) {
        return FORMAT_VALUE.format(value);
    }

    public static String formatarValor(double value) {        
        BigDecimal valorExato = new BigDecimal(value)
                .setScale(1, RoundingMode.HALF_DOWN);
        
        return converterValueWithoutDecimals(valorExato.doubleValue());
    }

    public static String converterValueWithoutDecimals(double value) {
        return VALUE.format(value);
    }

    public static String convertStringToMd5(String valor) {
        MessageDigest mDigest;
        try {
            mDigest = MessageDigest.getInstance("MD5");
            byte[] valorMD5 = mDigest.digest(valor.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : valorMD5) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    public static double getSpeedCorrentometroByLevel(Correntometro obj) {

        switch (LEVEL_DEFAULT_CORRENTOMETRO) {
            case 1:
                return obj.getIntensidade();
            case 2:
                return obj.getIntensidade2();
            case 3:
                return obj.getIntensidade3();
            case 4:
                return obj.getIntensidade4();
            case 5:
                return obj.getIntensidade5();
            case 6:
                return obj.getIntensidade6();
            case 7:
                return obj.getIntensidade7();
            case 8:
                return obj.getIntensidade8();
            case 9:
                return obj.getIntensidade9();
            case 10:
                return obj.getIntensidade10();
            default:
                return 0;
        }

    }

    public static double getDirecaoCorrentometroByLevel(Correntometro obj) {

        switch (LEVEL_DEFAULT_CORRENTOMETRO) {
            case 1:
                return obj.getDirecao();
            case 2:
                return obj.getDirecao2();
            case 3:
                return obj.getDirecao3();
            case 4:
                return obj.getDirecao4();
            case 5:
                return obj.getDirecao5();
            case 6:
                return obj.getDirecao6();
            case 7:
                return obj.getDirecao7();
            case 8:
                return obj.getDirecao8();
            case 9:
                return obj.getDirecao9();
            case 10:
                return obj.getDirecao10();
            default:
                return 0;
        }

    }

    public static String dateToString(Date date, String formatRegex) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatRegex);
        return format.format(date);
    }

    public static Date stringToDate(String date, String formatRegex) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatRegex);
        return format.parse(date);
    }

    public static List<List<Charts_Valores>> getListaForGrafico(Date startDate,
            Date endDate, int intervalo, int intervalo2, List<Date> datas, List<Double>... valores) {

        Calendar dataInicial = Calendar.getInstance();
        dataInicial.setTime(startDate);

        Calendar dataFinal = Calendar.getInstance();
        dataFinal.setTime(endDate);

        if (datas.isEmpty()) {

            List<List<Charts_Valores>> listaValores = new ArrayList<>();

            for (int i = 0; i < valores.length; i++) {
                listaValores.add(new ArrayList<>());
            }

            for (int i = 0; i < valores.length; i++) {
                listaValores.get(i).add(new Charts_Valores(dataInicial.getTime(), null));
                listaValores.get(i).add(new Charts_Valores(dataFinal.getTime(), null));
            }

            return listaValores;
        }

        Calendar dataInicialLista = Calendar.getInstance();
        dataInicialLista.setTime(datas.get(0));

        List<List<Charts_Valores>> listaValores = new ArrayList<>();

        for (int i = 0; i < valores.length; i++) {
            listaValores.add(new ArrayList<>());
        }

        while (dataInicialLista.getTime().after(dataInicial.getTime())) {

            dataInicialLista.add(Calendar.SECOND, (intervalo * -1));
            Charts_Valores d = new Charts_Valores(dataInicialLista.getTime(), null);

            for (int i = 0; i < valores.length; i++) {
                listaValores.get(i).add(d);
            }

        }

        for (int i = 0; i < datas.size() - 1; i++) {

            for (int k = 0; k < valores.length; k++) {
                listaValores.get(k).add(new Charts_Valores(datas.get(i), valores[k].get(i)));
            }

            Date datai = datas.get(i);
            Date datap = datas.get(i + 1);

            DateTime dataDev = new DateTime(datai);
            DateTime dataAtu = new DateTime(datap);

            int segundos = Seconds.secondsBetween(dataDev, dataAtu).getSeconds();

            if (segundos > intervalo2) {

                Calendar dIi = Calendar.getInstance();
                dIi.setTime(datai);

                Calendar dIp = Calendar.getInstance();
                dIp.setTime(datap);

                while (dIi.getTime().before(dIp.getTime())) {

                    dIi.add(Calendar.SECOND, intervalo);

                    if (!dIi.getTime().before(dIp.getTime())) {
                        break;
                    }

                    Charts_Valores d = new Charts_Valores(dIi.getTime(), null);

                    for (int k = 0; k < valores.length; k++) {
                        listaValores.get(k).add(d);
                    }

                }

            }

        }

        for (int k = 0; k < valores.length; k++) {
            listaValores.get(k).add(new Charts_Valores(datas.get(datas.size() - 1), valores[k].get(datas.size() - 1)));
        }

        Calendar dIi = Calendar.getInstance();
        dIi.setTime(datas.get(datas.size() - 1));

        while (dIi.getTime().before(dataFinal.getTime())) {

            dIi.add(Calendar.SECOND, intervalo);
            Charts_Valores d = new Charts_Valores(dIi.getTime(), null);

            for (int k = 0; k < valores.length; k++) {
                listaValores.get(k).add(d);
            }

        }

        return listaValores;
    }

    public static boolean containsFeature(UsuariosWeb user, String nameFeature) {
        String telasHabilitadas = user.getTelasHabilitadas();
        if (telasHabilitadas == null || telasHabilitadas.trim().isEmpty()) {
            return false;
        }
        ArrayList list = new ArrayList<>(Arrays.asList(telasHabilitadas.split(";")));
        return list.contains(nameFeature);
    }

    public static boolean isActiveFeature(String nameFeature) {
        UsuariosWeb user = SessionContext.getInstance().getUserLoggedIn();
        return containsFeature(user, nameFeature);
    }

    public static boolean isActiveFeature(String nameFeature, UsuariosWeb user) {
        return containsFeature(user, nameFeature);
    }

    /**
     * Atenticação básica para uso da API
     *
     * @param auth
     * @param manager
     * @return
     * @throws Exception
     */
    public static boolean isUserAuthenticatedAPI(String auth, EntityManager manager) throws Exception {

        if (auth == null) {
            return false;
        }

        String decodedAuth;
        String[] authParts = auth.split("\\s+");
        String authInfo = authParts[1];

        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(authInfo);
        } catch (IOException ex) {
            throw ex;
        }

        decodedAuth = new String(bytes);
        String user[] = decodedAuth.split(":");

        UsuariosWeb userApi = UsuariosWebController
                .getUserByLoginAndPassword(manager,
                        user[0],
                        Util.convertStringToMd5(user[1]));

        if (userApi == null) {
            return false;
        }

        return isActiveFeature("config_api", userApi);

    }

    public static boolean isOnlineInfo(Date dataHora, int tmpFeature) {
        int seconds = Seconds.secondsBetween(new DateTime(dataHora),
                new DateTime(new Date())).getSeconds();
        return seconds <= tmpFeature;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getStringDateLastUpdateDash(Date date) {
        Date now = new Date();

        Calendar ct = Calendar.getInstance();
        ct.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = ct.getTime();

        SimpleDateFormat s = new SimpleDateFormat("HH:mm");
        SimpleDateFormat ss = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat sss = new SimpleDateFormat("dd/MM/yyyy");

        if (sss.format(date).equals(sss.format(tomorrow))) {
            return "Amanhã às " + s.format(date);
        }

        if (sss.format(date).equals(sss.format(now))) {
            return "Hoje às " + s.format(date);
        }

        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DAY_OF_MONTH, -1);

        if (sss.format(c.getTime()).equals(sss.format(date))) {
            return "Ontem às " + s.format(date);
        }

        return ss.format(date);
    }

    public static String getStringDateLastUpdate(Date date1, Date date2) {
        SimpleDateFormat s = new SimpleDateFormat("HH:mm");
        SimpleDateFormat ss = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat sss = new SimpleDateFormat("dd/MM/yyyy");
        if (sss.format(date1).equals(sss.format(date2))) {
            return "hoje às " + s.format(date2);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        if (sss.format(c.getTime()).equals(sss.format(date2))) {
            return "ontem às " + s.format(date2);
        }
        return ss.format(date1);
    }

    public static String getStringDateLastUpdateComSegundos(Date date1, Date date2) {
        SimpleDateFormat s = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat ss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat sss = new SimpleDateFormat("dd/MM/yyyy");
        if (sss.format(date1).equals(sss.format(date2))) {
            return "Hoje às " + s.format(date1);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        if (sss.format(c.getTime()).equals(sss.format(date2))) {
            return "Ontem às " + s.format(date1);
        }
        return ss.format(date1);
    }

    public static String getStringDateLastUpdateComSegundos(Date date1, Date date2, boolean dataClient) {
        if (!dataClient) {
            return getStringDateLastUpdateComSegundos(date1, date2);
        }
        SimpleDateFormat s = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat ss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat sss = new SimpleDateFormat("dd/MM/yyyy");
        if (sss.format(date1).equals(sss.format(date2))) {
            return "Hoje às " + s.format(Util.getDateTimeClient(date1));
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        if (sss.format(c.getTime()).equals(sss.format(date2))) {
            return "Ontem às " + s.format(Util.getDateTimeClient(date1));
        }
        return ss.format(Util.getDateTimeClient(date1));
    }

    public static String getStringDateLastUpdateComSegundos(Date date1,
            Date date2, String timeZone) {

        SimpleDateFormat s = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat ss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat sss = new SimpleDateFormat("dd/MM/yyyy");
        if (sss.format(date1).equals(sss.format(date2))) {
            return "Hoje às " + s.format(Util.getDateTimeClientWithTimeZone(date1, timeZone));
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        if (sss.format(c.getTime()).equals(sss.format(date2))) {
            return "Ontem às " + s.format(Util.getDateTimeClientWithTimeZone(date1, timeZone));
        }

        return ss.format(Util.getDateTimeClientWithTimeZone(date1, timeZone));

    }

    public static Object getValueFromJson(Object value) {
        if (value == null) {
            return JSONObject.NULL;
        }
        if (value instanceof String) {
            return ((String) value).trim();
        }
        return value;
    }

    public static JSONObject getAisByJson(Ais ais, List<Ais> boatTrail) throws Exception {

        JSONObject json = new JSONObject();
        json.put("lat", ais.getLatitude());
        json.put("lng", ais.getLongitude());
        json.put("mmsi", ais.getMmsi());
        json.put("velocity", ais.getVelocidadeSobreSolo());
        json.put("direction", ais.getCursoSobreSolo());
        json.put("destination", (ais.getDestino() == null || ais.getDestino().trim().isEmpty()) ? "" : ais.getDestino());

        NavioMapAis navio = new NavioMapAis(ais.getCodNavio());

        json.put("color", String.format("#%02X%02X%02X", navio.getCorEmbarcacaoMapa().getRed(),
                navio.getCorEmbarcacaoMapa().getGreen(), navio.getCorEmbarcacaoMapa().getBlue()));
        json.put("opacity", 0.5);
        json.put("codType", navio.getCodCategoriaEmbarcacao());

        if (navio.isDimensaoProporcionalMapa()) {
            // medidas em metros
            JSONObject dimension = new JSONObject();
            dimension.put("a", navio.getA());
            dimension.put("b", navio.getB());
            dimension.put("c", navio.getC());
            dimension.put("d", navio.getD());
            json.put("dimension", dimension);
            json.put("proportionalMap", true);

        } else {
            // medidas em pixels
            int comprimento = navio.getComprimentoMapa();
            int largura = navio.getLarguraMapa();
            int b = (int) (comprimento * 0.2);
            int a = comprimento - b;
            int c = largura / 2;
            int d = largura - c;
            JSONObject dimension = new JSONObject();
            dimension.put("a", a);
            dimension.put("b", b);
            dimension.put("c", c);
            dimension.put("d", d);
            json.put("dimension", dimension);
            json.put("proportionalMap", false);

        }

        if (ais.getCodNavio() != null) {
            JSONObject jsonNavio = new JSONObject();
            jsonNavio.put("name", navio.getNomeEmbarcacaoMapa());
            jsonNavio.put("imo", ais.getCodNavio().getImo());
            json.put("vessel", jsonNavio);
        }

        JSONArray jsonBoatTrail = new JSONArray();

        for (Ais trail : boatTrail) {
            JSONObject jsonTrail = new JSONObject();
            jsonTrail.put("lat", trail.getLatitude());
            jsonTrail.put("lng", trail.getLongitude());
            jsonBoatTrail.put(jsonTrail);
        }

        json.put("trail", jsonBoatTrail);

        return json;

    }

    public static JSONObject getAisByJson(VesselSearch ais, List<Ais> boatTrail) throws Exception {

        if (ais.getNavioUltimaAtualizacao() == null) {
            return new JSONObject();
        }

        JSONObject json = new JSONObject();
        json.put("lat", ais.getNavioUltimaAtualizacao().getLatitude());
        json.put("lng", ais.getNavioUltimaAtualizacao().getLongitude());
        json.put("mmsi", ais.getMmsi());
        json.put("dataFromExternal", ais.getNavioUltimaAtualizacao().isDataFromExternal());
        json.put("velocity", ais.getNavioUltimaAtualizacao().getVelocidadeSobreSolo());
        json.put("direction", ais.getNavioUltimaAtualizacao().getCursoSobreSolo());
        json.put("destination", (ais.getNavioUltimaAtualizacao().getDestino() == null || ais.getNavioUltimaAtualizacao().getDestino().trim().isEmpty()) ? "" : ais.getNavioUltimaAtualizacao().getDestino());

        Navio vessel = new Navio();
        vessel.setCodNavio(ais.getCodNavio());
        vessel.setDimensao(ais.getDimensao());
        vessel.setImo(ais.getImo());
        vessel.setMmsi(ais.getMmsi());
        vessel.setNomeNavio(ais.getNomeNavio());
        vessel.setTipo(ais.getTipo());

        NavioMapAis navio = new NavioMapAis(vessel);

        json.put("color", String.format("#%02X%02X%02X", navio.getCorEmbarcacaoMapa().getRed(),
                navio.getCorEmbarcacaoMapa().getGreen(), navio.getCorEmbarcacaoMapa().getBlue()));
        json.put("opacity", 0.5);
        json.put("codType", navio.getCodCategoriaEmbarcacao());

        if (navio.isDimensaoProporcionalMapa()) {
            // medidas em metros
            JSONObject dimension = new JSONObject();
            dimension.put("a", navio.getA());
            dimension.put("b", navio.getB());
            dimension.put("c", navio.getC());
            dimension.put("d", navio.getD());
            json.put("dimension", dimension);
            json.put("proportionalMap", true);

        } else {
            // medidas em pixels
            int comprimento = navio.getComprimentoMapa();
            int largura = navio.getLarguraMapa();
            int b = (int) (comprimento * 0.2);
            int a = comprimento - b;
            int c = largura / 2;
            int d = largura - c;
            JSONObject dimension = new JSONObject();
            dimension.put("a", a);
            dimension.put("b", b);
            dimension.put("c", c);
            dimension.put("d", d);
            json.put("dimension", dimension);
            json.put("proportionalMap", false);

        }

        if (ais.getCodNavio() != null) {
            JSONObject jsonNavio = new JSONObject();
            jsonNavio.put("name", navio.getNomeEmbarcacaoMapa());
            jsonNavio.put("imo", ais.getImo());
            json.put("vessel", jsonNavio);
        }

        JSONArray jsonBoatTrail = new JSONArray();

        if (boatTrail != null) {
            for (Ais trail : boatTrail) {
                JSONObject jsonTrail = new JSONObject();
                jsonTrail.put("lat", trail.getLatitude());
                jsonTrail.put("lng", trail.getLongitude());
                jsonBoatTrail.put(jsonTrail);
            }
        }

        json.put("trail", jsonBoatTrail);

        return json;

    }

    public static JSONObject getAisByJson(int mmsi, Navio vessel,
            NavioUltimaAtualizacao vesselLastUpdate, List<Ais> boatTrail) throws Exception {

        if (vesselLastUpdate == null) {
            return new JSONObject();
        }

        JSONObject json = new JSONObject();
        json.put("lat", vesselLastUpdate.getLatitude());
        json.put("lng", vesselLastUpdate.getLongitude());
        json.put("mmsi", mmsi);
        json.put("dataFromExternal", vesselLastUpdate.isDataFromExternal());
        json.put("velocity", vesselLastUpdate.getVelocidadeSobreSolo());
        json.put("direction", vesselLastUpdate.getCursoSobreSolo());
        json.put("destination", (vesselLastUpdate.getDestino() == null || vesselLastUpdate.getDestino().trim().isEmpty()) ? "" : vesselLastUpdate.getDestino());

        NavioMapAis vesselMap = new NavioMapAis(vessel);

        json.put("color", String.format("#%02X%02X%02X", vesselMap.getCorEmbarcacaoMapa().getRed(),
                vesselMap.getCorEmbarcacaoMapa().getGreen(), vesselMap.getCorEmbarcacaoMapa().getBlue()));
        json.put("opacity", 0.5);
        json.put("codType", vesselMap.getCodCategoriaEmbarcacao());

        if (vesselMap.isDimensaoProporcionalMapa()) {
            // medidas em metros
            JSONObject dimension = new JSONObject();
            dimension.put("a", vesselMap.getA());
            dimension.put("b", vesselMap.getB());
            dimension.put("c", vesselMap.getC());
            dimension.put("d", vesselMap.getD());
            json.put("dimension", dimension);
            json.put("proportionalMap", true);

        } else {
            // medidas em pixels
            int comprimento = vesselMap.getComprimentoMapa();
            int largura = vesselMap.getLarguraMapa();
            int b = (int) (comprimento * 0.2);
            int a = comprimento - b;
            int c = largura / 2;
            int d = largura - c;
            JSONObject dimension = new JSONObject();
            dimension.put("a", a);
            dimension.put("b", b);
            dimension.put("c", c);
            dimension.put("d", d);
            json.put("dimension", dimension);
            json.put("proportionalMap", false);

        }

        if (vessel != null) {
            JSONObject jsonNavio = new JSONObject();
            jsonNavio.put("name", vesselMap.getNomeEmbarcacaoMapa());
            jsonNavio.put("imo", vessel.getImo());
            json.put("vessel", jsonNavio);
        }

        JSONArray jsonBoatTrail = new JSONArray();

        if (boatTrail != null) {
            for (Ais trail : boatTrail) {
                JSONObject jsonTrail = new JSONObject();
                jsonTrail.put("lat", trail.getLatitude());
                jsonTrail.put("lng", trail.getLongitude());
                jsonBoatTrail.put(jsonTrail);
            }
        }

        json.put("trail", jsonBoatTrail);

        return json;

    }

    public static Date[] getPeriodValidForAis(Date date) {
        Calendar dataInicio = Calendar.getInstance();
        dataInicio.setTime(date);
        dataInicio.add(Calendar.MINUTE, TMP_MINUTES_ONLINE_AIS * -1);
        return new Date[]{date, dataInicio.getTime()};

    }

    public static Navio getFirstNavioFromListAisByMmsi(List<Ais> list, int mmsi) {
        for (Ais ais : list) {
            if (ais.getMmsi() == mmsi) {
                return ais.getCodNavio();
            }
        }
        return null;
    }

//    public static List<Ais> getListVesselActive(EntityManager manager, Date date) throws Exception {
//        Date[] period = getPeriodValidForAis(date);
//        List<Ais> list = AisController.getListVesselActive(manager, period[1], period[0]);
//        return list;
//    }
//    public static boolean containsDate(Date date, List<Date> listDates) {
//        for (int i = listDates.size() - 1; i >= 0; i--) {
//            if (listDates.get(i).getTime() == date.getTime()) {
//                return true;
//            }
//        }
//        return false;
//    }
//    public static List<Ais> getNewHistory(Date data, List<Ais> history) {
//        List<Ais> newHistory = new ArrayList<>();
//        Iterator<Ais> it = history.iterator();
//        while (it.hasNext()) {
//            Ais obj = it.next();
//            if (data.getTime() == obj.getDataHora().getTime()) {
//                newHistory.add(obj);
//                it.remove();
//            }
//        }
//        return newHistory;
//    }
    public static List<Ais> getHistoryPlayBack(Date date,
            List<Ais> historyOld,
            List<Ais> historyNew) {
        List<Ais> historyPlayBack = new ArrayList<>();
        historyPlayBack.addAll(historyNew);
        historyOld.forEach((a) -> {
            int minutes = Minutes.minutesBetween(new DateTime(a.getDataHora()),
                    new DateTime(date)).getMinutes();
            if (minutes <= TMP_MINUTES_ONLINE_AIS
                    && !containsMmsi(historyNew, a.getMmsi())) {
                historyPlayBack.add(a);
            }
        });
        return historyPlayBack;
    }

    public static boolean containsMmsi(List<Ais> list, int mmsi) {
        return list.stream().anyMatch((ais) -> (mmsi == ais.getMmsi()));
    }

//    public static void sortPlayBack(List<Date> listDates, int left, int right) {
//        if (listDates == null || listDates.isEmpty()) {
//            return;
//        }
//        int esq = left;
//        int dir = right;
//        Date pivo = listDates.get((esq + dir) / 2);
//        Date troca;
//        while (esq <= dir) {
//            while (listDates.get(esq).getTime() < pivo.getTime()) {
//                esq = esq + 1;
//            }
//            while (listDates.get(dir).getTime() > pivo.getTime()) {
//                dir = dir - 1;
//            }
//            if (esq <= dir) {
//                troca = listDates.get(esq);
//                listDates.set(esq, listDates.get(dir));
//                listDates.set(dir, troca);
//                esq = esq + 1;
//                dir = dir - 1;
//            }
//        }
//        if (dir > left) {
//            sortPlayBack(listDates, left, dir);
//        }
//        if (esq < right) {
//            sortPlayBack(listDates, esq, right);
//        }
//    }
//    public static List<AisPlayBack> getListPlayBack(List<Ais> listInitial,
//            List<Ais> history, Date startDate) {
//
//        List<AisPlayBack> listPlayBack = new ArrayList<>();
//
//        if (history.isEmpty()) {
//            return listPlayBack;
//        }
//
//        List<Date> listDates = new ArrayList<>();
//        listDates.add(startDate);
//
//        for (Ais ais : history) {
//            if (!containsDate(ais.getDataHora(), listDates)) {
//                listDates.add(ais.getDataHora());
//            }
//        }
//
//        sortPlayBack(listDates, 0, listDates.size() - 1);
//
//        listPlayBack.add(new AisPlayBack(startDate, listInitial));
//
//        for (int i = 1; i < listDates.size(); i++) {
//            List<Ais> historyOld = listPlayBack.get(i - 1).getListPlayBack();
//            List<Ais> historyNew = getNewHistory(listDates.get(i), history);
//            listPlayBack.add(new AisPlayBack(listDates.get(i), getHistoryPlayBack(listDates.get(i),
//                    historyOld, historyNew)));
//        }
//
//        return listPlayBack;
//
//    }
    public static List<AisPlayBack> getListPlayBack(List<Ais> listInitial,
            List<Ais> history, Date startDate) {

        List<AisPlayBack> listPlayBack = new ArrayList<>();

        if (history.isEmpty()) {
            return listPlayBack;
        }

        listPlayBack.add(new AisPlayBack(startDate, listInitial));

        Date date = null;
        List<Ais> listAisNew = new ArrayList<>();

        for (int i = 0; i < history.size(); i++) {

            Ais ais = history.get(i);

            if (date == null) {
                listAisNew.add(ais);
                date = ais.getDataHora();
                continue;
            }

            if (ais.getDataHora().equals(date)) {
                listAisNew.add(ais);

                if (i == history.size() - 1) {
                    // pronto
                    List<Ais> historyOld = listPlayBack.get(listPlayBack.size() - 1).getListPlayBack();
                    listPlayBack.add(new AisPlayBack(date, getHistoryPlayBack(date,
                            historyOld, listAisNew)));
                    continue;
                }

                date = ais.getDataHora();
                continue;
            }

            if (ais.getDataHora().after(date)) {
                // pronto
                List<Ais> historyOld = listPlayBack.get(listPlayBack.size() - 1).getListPlayBack();
                listPlayBack.add(new AisPlayBack(date, getHistoryPlayBack(date,
                        historyOld, listAisNew)));

                listAisNew = new ArrayList<>();
                listAisNew.add(ais);
                date = ais.getDataHora();
            }

        }

        return listPlayBack;

    }

    public static boolean vesselInFila(List<VesselFila> fila, int mmsi) {
        boolean exists = false;
        for (VesselFila vesselFila : fila) {
            if (vesselFila.getMmsi() == mmsi) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public static void sortFilaArrivalDate(List<VesselFila> fila) {
        for (int i = fila.size(); i >= 1; i--) {
            for (int j = 1; j < i; j++) {
                if (fila.get(j - 1).getArrivalDate().after(fila.get(j).getArrivalDate())) {
                    VesselFila aux = fila.get(j);
                    fila.set(j, fila.get(j - 1));
                    fila.set(j - 1, aux);
                }
            }
        }
    }

    public static void sortReleasedCodesp(List<VesselFila> fila) {

        System.out.println("tamanho: " + fila.size());
        // pega todos os navios liberados
        List<VesselFila> listaLiberado = new ArrayList<>();
        for (VesselFila vesselFila : fila) {
            if (vesselFila.getDadosCodesp() != null && vesselFila.getDadosCodesp().isLiberadoCodesp()) {
                listaLiberado.add(vesselFila);
            }
        }
        fila.removeAll(listaLiberado);
        System.out.println("tamanho novo: " + fila.size());
        System.out.println("teste: " + listaLiberado.size());

        // ordenar por prioridade os navios liberados pela codesp
        for (int i = listaLiberado.size(); i >= 1; i--) {
            for (int j = 1; j < i; j++) {
                if (listaLiberado.get(j - 1).getDadosCodesp().getPrioridade().compareToIgnoreCase(listaLiberado.get(j).getDadosCodesp().getPrioridade()) > 0) {
                    VesselFila aux = listaLiberado.get(j);
                    listaLiberado.set(j, listaLiberado.get(j - 1));
                    listaLiberado.set(j - 1, aux);
                }
            }
        }

        // ordenar por ordem os navios liberado pela codesp
        for (int i = listaLiberado.size(); i >= 1; i--) {
            for (int j = 1; j < i; j++) {
                if (listaLiberado.get(j - 1).getDadosCodesp().getOrdem() > listaLiberado.get(j).getDadosCodesp().getOrdem()) {
                    VesselFila aux = listaLiberado.get(j);
                    listaLiberado.set(j, listaLiberado.get(j - 1));
                    listaLiberado.set(j - 1, aux);
                }
            }
        }

        // ordenar por prioridade os navios não liberados pela codesp
        List<VesselFila> listaFilaNaoLiberadoCodesp = new ArrayList<>();
        for (VesselFila vesselFila : fila) {
            if (vesselFila.getDadosCodesp() != null && !vesselFila.getDadosCodesp().isLiberadoCodesp()) {
                listaFilaNaoLiberadoCodesp.add(vesselFila);
            }
        }
        fila.removeAll(listaFilaNaoLiberadoCodesp);

        for (int i = listaFilaNaoLiberadoCodesp.size(); i >= 1; i--) {
            for (int j = 1; j < i; j++) {
                if (listaFilaNaoLiberadoCodesp.get(j - 1).getDadosCodesp().getPrioridade().compareToIgnoreCase(listaFilaNaoLiberadoCodesp.get(j).getDadosCodesp().getPrioridade()) > 0) {
                    VesselFila aux = listaFilaNaoLiberadoCodesp.get(j);
                    listaFilaNaoLiberadoCodesp.set(j, listaFilaNaoLiberadoCodesp.get(j - 1));
                    listaFilaNaoLiberadoCodesp.set(j - 1, aux);
                }
            }
        }

        for (int i = listaFilaNaoLiberadoCodesp.size() - 1; i >= 0; i--) {
            fila.add(0, listaFilaNaoLiberadoCodesp.get(i));
        }

        for (int i = listaLiberado.size() - 1; i >= 0; i--) {
            fila.add(0, listaLiberado.get(i));
        }

    }

    public static int getRangeSeconds(Date inicio, Date fim) {
        DateTime start = new DateTime(inicio);
        DateTime end = new DateTime(fim);
        int seconds = Seconds.secondsBetween(start, end).getSeconds();
        return seconds;
    }

    public static double round(float value, int casas) {
        if (casas < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(casas, RoundingMode.HALF_UP);

        return bd.doubleValue();

    }

    public static float round(double value, int casas) {
        if (casas < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(casas, RoundingMode.HALF_UP);

        return bd.floatValue();

    }

    public static void uploadImageBerco(String nameImage, Part imageFile)
            throws Exception {

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

        String dirPath = servletContext.getRealPath("resources/img/bercos");
        File dir = new File(dirPath);

        if (!dir.exists()) {
            dir.mkdir();
        }

        String filePath = servletContext.getRealPath("resources/img/bercos/" + nameImage);
        File file = new File(filePath);

        try (OutputStream out = new FileOutputStream(file)) {
            out.write(IOUtils.toByteArray(imageFile.getInputStream()));
        }

    }

    public static void saveImageBerco(String nameImage, byte[] imageBytes)
            throws Exception {

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String dirPath = servletContext.getRealPath("resources/img/bercos");
        File dir = new File(dirPath);

        if (!dir.exists()) {
            dir.mkdir();
        }

        String filePath = servletContext.getRealPath("resources/img/bercos/" + nameImage);
        File file = new File(filePath);

        try (OutputStream out = new FileOutputStream(file)) {
            out.write(imageBytes);
        }

    }

    public static void removeImageBerco(String image) {

        if (image == null) {
            return;
        }

        try {

            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            String dirPath = servletContext.getRealPath("resources/img/bercos/" + image);
            File file = new File(dirPath);

            if (file.exists()) {
                file.delete();
            }

        } catch (Exception ex) {
        }

    }

    public static byte[] getImageBercoToBytes(String image) {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        try {
            return IOUtils.toByteArray(servletContext.getResourceAsStream("resources/img/bercos/" + image));
        } catch (Exception ex) {
            return null;
        }
    }

    public static int getLongTimeNow() {
        return (int) new Date().getTime();
    }

    public static MonitorarRegra getRuleByCheckState(double value, List<MonitorarRegra> rules) {
        MonitorarRegra rule = null;
        for (int i = 0; i < rules.size(); i++) {
            boolean checkRule = checkRule(value, rules.get(i));
            if (checkRule) {
                rule = rules.get(i);
            }
        }
        return rule;
    }

    public static boolean checkRule(double value, MonitorarRegra rule) {
        switch (rule.getCondicao()) {
            case MonitorarRegra.BIGGER:
                return value > rule.getValor();
            case MonitorarRegra.BIGGER_OR_EQUAL:
                return value >= rule.getValor();
            case MonitorarRegra.SMALLER:
                return value < rule.getValor();
            case MonitorarRegra.LESS_OR_EQUAL:
                return value <= rule.getValor();
            default:
                return false;
        }

    }

    public static void orderEventsAsc(List<Event> events) {
        int inputLength = events.size();
        Event temp;
        boolean is_sorted;
        for (int i = 0; i < inputLength; i++) {
            is_sorted = true;
            for (int j = 1; j < (inputLength - i); j++) {
                if (events.get(j - 1).getDatetime().after(events.get(j).getDatetime())) {
                    temp = events.get(j - 1);
                    events.set(j - 1, events.get(j));
                    events.set(j, temp);
                    is_sorted = false;
                }
            }
            if (is_sorted) {
                break;
            }
        }
    }

    public static String getMessageByArrivalDate(Monitorar variableMonitor, Navio vessel, Aispoin aispoin) {

        String newMessage = variableMonitor.getMensagem();

        // replace nome embarcação
        if (vessel == null) {
            newMessage = newMessage.replace("{name_vessel}", "");
        } else {
            newMessage = newMessage.replace("{name_vessel}", vessel.getNomeNavio().trim().toUpperCase());
        }

        // replace imo embarcação
        if (vessel == null) {
            newMessage = newMessage.replace("{imo_vessel}", "");
        } else {
            newMessage = newMessage.replace("{imo_vessel}", vessel.getImo() + "");
        }

        // replace data de chegada
        newMessage = newMessage.replace("{arrival_date}", (Util.dateToString(aispoin.getDataEntrada(), "HH:mm") + " do dia " + Util.dateToString(aispoin.getDataEntrada(), "dd/MM/yyyy")));

        // replace nome do poin
        newMessage = newMessage.replace("{name_poin}", aispoin.getCodPoin().getNome().trim());

        return newMessage;

    }

    public static String getMessageByDepartureDate(Monitorar variableMonitor, Navio vessel, Aispoin aispoin) {

        String newMessage = variableMonitor.getMensagem();

        // replace nome embarcação
        if (vessel == null) {
            newMessage = newMessage.replace("{name_vessel}", "");
        } else {
            newMessage = newMessage.replace("{name_vessel}", vessel.getNomeNavio().trim().toUpperCase());
        }

        // replace imo embarcação
        if (vessel == null) {
            newMessage = newMessage.replace("{imo_vessel}", "");
        } else {
            newMessage = newMessage.replace("{imo_vessel}", vessel.getImo() + "");
        }

        // replace data de chegada
        newMessage = newMessage.replace("{departure_date}", (Util.dateToString(aispoin.getDataSaida(), "HH:mm") + " do dia " + Util.dateToString(aispoin.getDataSaida(), "dd/MM/yyyy")));

        // replace nome do poin
        newMessage = newMessage.replace("{name_poin}", aispoin.getCodPoin().getNome().trim());

        return newMessage;

    }

    public static String getMessageByPermanenceTime(Monitorar variableMonitor, Navio vessel,
            Aispoin aispoin, MonitorarRegra rule, int value) {

        String newMessage = variableMonitor.getMensagem();

        // replace nome embarcação
        if (vessel == null) {
            newMessage = newMessage.replace("{name_vessel}", "");
        } else {
            newMessage = newMessage.replace("{name_vessel}", vessel.getNomeNavio().trim().toUpperCase());
        }

        // replace imo embarcação
        if (vessel == null) {
            newMessage = newMessage.replace("{imo_vessel}", "");
        } else {
            newMessage = newMessage.replace("{imo_vessel}", vessel.getImo() + "");
        }

        // replace nome do poin
        newMessage = newMessage.replace("{name_poin}", aispoin.getCodPoin().getNome().trim());

        // replace valor da regra
        newMessage = newMessage.replace("{rule_value}", ((int) rule.getValor()) + "");

        // replace valor
        newMessage = newMessage.replace("{permanence_time}", value + "");

        return newMessage;

    }

    public static String getMessageByNavigationSpeed(Monitorar variableMonitor, Navio vessel,
            Ais ais, Poin poin, MonitorarRegra rule, double value) {

        String newMessage = variableMonitor.getMensagem();

        // replace nome embarcação
        if (vessel == null) {
            newMessage = newMessage.replace("{name_vessel}", "");
        } else {
            newMessage = newMessage.replace("{name_vessel}", vessel.getNomeNavio().trim().toUpperCase());
        }

        // replace imo embarcação
        if (vessel == null) {
            newMessage = newMessage.replace("{imo_vessel}", "");
        } else {
            newMessage = newMessage.replace("{imo_vessel}", vessel.getImo() + "");
        }

        // replace nome do poin
        newMessage = newMessage.replace("{name_poin}", poin.getNome().trim());

        // replace valor da regra
        newMessage = newMessage.replace("{rule_value}", rule.getValor() + "");

        // replace spped_vessel
        newMessage = newMessage.replace("{speed_vessel}", value + "");

        return newMessage;

    }

    public static Monitorar getVariableMonitorByType(int variableType, List<Monitorar> listVariablesMonitors) {

        for (Monitorar variableMonitor : listVariablesMonitors) {
            if (variableMonitor.getVariavel() == variableType) {
                return variableMonitor;
            }
        }

        return null;

    }

    public static List<GeoPosition> converterCoordinatesStringInList(String coordinate) throws Exception {
        JSONArray array = new JSONArray(coordinate);
        List<GeoPosition> lista = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            GeoPosition coordenada = new GeoPosition(obj.getDouble("latitude"), obj.getDouble("longitude"));
            lista.add(coordenada);
        }
        return lista;
    }

    public static boolean pointInPoin(Poin poin, GeoPosition coordinate) throws Exception {

        List<GeoPosition> coordinates = converterCoordinatesStringInList(poin.getCoordenadas());

        double x = coordinate.getLongitude();
        double y = coordinate.getLatitude();

        double polyX[] = new double[coordinates.size()];
        double polyY[] = new double[coordinates.size()];

        for (int i = 0; i < coordinates.size(); i++) {
            polyX[i] = coordinates.get(i).getLongitude();
            polyY[i] = coordinates.get(i).getLatitude();
        }

        int i, j = polyY.length - 1;
        boolean oddNodes = false;

        for (i = 0; i < polyY.length; i++) {
            if (polyY[i] < y && polyY[j] >= y
                    || polyY[j] < y && polyY[i] >= y) {
                if (polyX[i] + (y - polyY[i]) / (polyY[j] - polyY[i]) * (polyX[j] - polyX[i]) < x) {
                    oddNodes = !oddNodes;
                }
            }
            j = i;
        }

        return oddNodes;

    }

    public static List<Poin> getPoinsActiveByAis(Ais ais, List<Poin> poins) throws Exception {
        List<Poin> poinsFlag = new ArrayList<>();
        for (Poin poin : poins) {
            GeoPosition coordenada = new GeoPosition(ais.getLatitude(), ais.getLongitude());
            if (pointInPoin(poin, coordenada)) {
                poinsFlag.add(poin);
            }
        }
        return poinsFlag;
    }

    public static String getTimeDuration(Date start, Date end) {
        int qtdSegundos;
        int horas = 0;
        int minutos = 0;
        int segundos = 0;

        try {
            qtdSegundos = getRangeSeconds(start, end);
            horas = qtdSegundos / 3600;
            minutos = qtdSegundos % 3600 / 60;
            segundos = qtdSegundos % 3600 % 60;
        } catch (Exception ex) {
        }

        return (horas < 10 ? "0" + horas : horas) + ":" + (minutos < 10 ? "0" + minutos : minutos)
                + ":" + (segundos < 10 ? "0" + segundos : segundos);
    }

    public static String getTimeDuration(int qtdSegundos) {
        int horas = 0;
        int minutos = 0;
        int segundos = 0;

        try {
            horas = qtdSegundos / 3600;
            minutos = qtdSegundos % 3600 / 60;
            segundos = qtdSegundos % 3600 % 60;
        } catch (Exception ex) {
        }

        return (horas < 10 ? "0" + horas : horas) + ":" + (minutos < 10 ? "0" + minutos : minutos)
                + ":" + (segundos < 10 ? "0" + segundos : segundos);
    }

    public static String generateToken(Integer cod, String tipo) throws JWTCreationException {

        Algorithm algorithm = Algorithm.HMAC256(SECRET_API);

        String token = JWT.create()
                .withIssuer("auth0")
                .withClaim("cod", cod)
                .withClaim("tipo", tipo)
                .sign(algorithm);

        return token;

    }

    public static String generateTokenLogged(Integer codUser, Integer codClient) throws JWTCreationException {

        Algorithm algorithm = Algorithm.HMAC256(SECRET_API);

        Calendar expiresAt = Calendar.getInstance();
        expiresAt.add(Calendar.HOUR_OF_DAY, 24);

        String token = JWT.create()
                .withIssuer("auth0")
                .withClaim("codUser", codUser)
                .withClaim("codClient", codClient)
                .withExpiresAt(expiresAt.getTime())
                .sign(algorithm);

        return token;

    }

    public static boolean isValidToken(String token) throws TokenExpiredException, JWTVerificationException {

        Algorithm algorithm = Algorithm.HMAC256(SECRET_API);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build();

        DecodedJWT jwt = verifier.verify(token);

        return true;

    }

    public static UserLoggedApi getUserLoggedApi(String auth) throws TokenExpiredException, JWTVerificationException {

        String token = auth.replace("Bearer ", "");
        isValidToken(token);
        return new UserLoggedApi(token);

    }

    public static Date getDateUTC(Date date, TimeZone timeZone) {
        long offsetHours = getOffsetHours(timeZone, date);
        Calendar d = Calendar.getInstance();
        d.setTime(date);
        d.add(Calendar.HOUR, (int) (offsetHours * -1L));
        return d.getTime();
    }

    public static Date getDateUTC() {
        Date date = new Date();
        long offsetHours = getOffsetHours(TimeZone.getDefault(), date);
        Calendar d = Calendar.getInstance();
        d.setTime(date);
        d.add(Calendar.HOUR, (int) (offsetHours * -1L));
        return d.getTime();
    }

    public static ZoneOffset getOffset(TimeZone timeZone, Date date) {
        Calendar dt = Calendar.getInstance(timeZone);
        ZoneId zi = timeZone.toZoneId();
        ZoneRules zr = zi.getRules();
        return zr.getOffset(dt.toInstant());
    }

    public static long getOffsetHours(TimeZone timeZone, Date date) {
        ZoneOffset zo = getOffset(timeZone, date);
        return TimeUnit.SECONDS.toHours(zo.getTotalSeconds());
    }

    public static String getDateTimeClient(Date dateUtc, String format) {
        if (dateUtc == null) {
            return "";
        }
        return new SimpleDateFormat(format)
                .format(getDateTimeClient(dateUtc));

    }

    public static Date getDateTimeClient(Date dateUtc) {

        UsuariosWeb user = SessionContext.getInstance().getUserLoggedIn();

        Instant nowUtc = Instant.now();
        ZoneId timeZone = ZoneId.of(user.getTimeZone());
        ZonedDateTime nowTimeZone = ZonedDateTime.ofInstant(nowUtc, timeZone);
        long offset = Util.getOffsetHours(TimeZone.getTimeZone(timeZone), Date.from(nowTimeZone.toInstant()));

        Calendar d = Calendar.getInstance();
        d.setTime(dateUtc);
        d.add(Calendar.HOUR, (int) offset);

        return d.getTime();

    }

    public static Date getDateTimeClientWithTimeZone(Date dateUtc, String timeZone) {

        Instant nowUtc = Instant.now();
        ZoneId tz = ZoneId.of(timeZone);
        ZonedDateTime nowTimeZone = ZonedDateTime.ofInstant(nowUtc, tz);
        long offset = Util.getOffsetHours(TimeZone.getTimeZone(tz), Date.from(nowTimeZone.toInstant()));

        Calendar d = Calendar.getInstance();
        d.setTime(dateUtc);
        d.add(Calendar.HOUR, (int) offset);

        return d.getTime();

    }

    public static List<String> getListTimeZones() {
        return ZoneId
                .getAvailableZoneIds()
                .stream()
                .collect(Collectors.toList());

    }

    public static LocalDateTime convertToLocalDateTimeViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static long getTempoPermanenciaEmMinutos(Date start, Date end) {

        LocalDateTime a1 = convertToLocalDateTimeViaMilisecond(start);
        LocalDateTime a2 = convertToLocalDateTimeViaMilisecond(end);

        long diferencaMin = Duration.between(a1, a2).toMinutes();
        return diferencaMin;

    }

    public static String convertMinutesToHoursAndMinutes(long t) {
        long hours = t / 60;
        long minutes = t % 60;
        String h = hours < 0 ? "00" : hours < 10 ? ("0" + hours) : (hours + "");
        String m = minutes < 0 ? "" : minutes < 10 ? ("0" + minutes) : (minutes + "");
        return h + "h" + m + "m";
    }

}
