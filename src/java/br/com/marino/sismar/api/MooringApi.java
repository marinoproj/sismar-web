package br.com.marino.sismar.api;

import br.com.marino.sismar.controller.AtracacaoController;
import br.com.marino.sismar.controller.ManobraController;
import br.com.marino.sismar.controller.NavioController;
import br.com.marino.sismar.controller.UsuariosWebController;
import br.com.marino.sismar.entity.Atracacao;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.ImageVessel;
import br.com.marino.sismar.entity.Manobra;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.enums.StatusOperacaoEnum;
import br.com.marino.sismar.util.UserLoggedApi;
import br.com.marino.sismar.util.Util;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.primefaces.json.JSONObject;

@Path("/mooring")
public class MooringApi {

    @GET
    @Path("/realtime")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getMooringRealtimeByCodBercoAndCodClient(
            @HeaderParam("authorization") String auth,
            @QueryParam("codBerco") int codBerco) {

        UserLoggedApi userLogged = Util.getUserLoggedApi(auth);

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            UsuariosWeb user = UsuariosWebController.getUserById(manager, userLogged.getCodUser());

            Atracacao atracacao = AtracacaoController
                    .getAtracacaoAtivaByCodBercoAndCodCliente(manager, codBerco, userLogged.getCodClient());

            Manobra manobraDireita = null;
            Manobra manobraEsquerda = null;

            if (atracacao != null) {

                manobraDireita = ManobraController.getUltimaMarcacaoLadoDireitoByCodAtracacao(manager, atracacao.getCodAtracacao());
                manobraEsquerda = ManobraController.getUltimaMarcacaoLadoEsquerdoByCodAtracacao(manager, atracacao.getCodAtracacao());
                Float angulo = ManobraController.getAngulo(manager, atracacao.getCodAtracacao());

                json.put("status", getStatus(atracacao.getStatusOperacao()));
                json.put("statusClass", getClassStatus(atracacao.getStatusOperacao()));
                json.put("angulo", angulo == null ? JSONObject.NULL : angulo);

            }

            json.put("operando", atracacao != null);

            // preenche os dados da embarcação
            if (atracacao != null && atracacao.getCodNavio() != null) {

                JSONObject jsonEmbarcacao = new JSONObject();
                jsonEmbarcacao.put("nome", getNomeEmbarcacao(atracacao));
                jsonEmbarcacao.put("imo", atracacao.getCodNavio().getImo());
                String imagem = getImagemEmbarcacao(manager, atracacao.getCodNavio().getImo());
                jsonEmbarcacao.put("imagem", imagem == null ? JSONObject.NULL : imagem);

                json.put("embarcacao", jsonEmbarcacao);

            }

            // preenche os dados do lado direito
            if (manobraDireita != null) {

                JSONObject jsonDir = new JSONObject();
                jsonDir.put("dataHora", getDataUltimaAtualizacao(manobraDireita.getDataHora(), user.getTimeZone()));
                jsonDir.put("distancia", getDistancia(manobraDireita.getDistanciaDireita()));
                jsonDir.put("velocidade", getVelocidade(manobraDireita.getVelocidadeDireita()));

                json.put("direito", jsonDir);

            }

            // preenche os dados do lado esquerdo
            if (manobraEsquerda != null) {

                JSONObject jsonEsq = new JSONObject();
                jsonEsq.put("dataHora", getDataUltimaAtualizacao(manobraEsquerda.getDataHora(), user.getTimeZone()));
                jsonEsq.put("distancia", getDistancia(manobraEsquerda.getDistanciaEsquerda()));
                jsonEsq.put("velocidade", getVelocidade(manobraEsquerda.getVelocidadeEsquerda()));

                json.put("esquerdo", jsonEsq);

            }

        } catch (Exception ex) {
            System.out.println("Erro na busca dos dados de manobra realtime para atracacao no berço " + codBerco);
            json = new JSONObject();
            try {
                json.put("error", true);
                json.put("message", "Could not query");
            } catch (Exception ex2) {
            }

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

        return json.toString();

    }

    @GET
    @Path("/playback")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getMooringPlaback(
            @HeaderParam("authorization") String auth,
            @QueryParam("codAtracacao") int codAtracacao,
            @QueryParam("dataHoraTime") long dataHoraTime) {

        UserLoggedApi userLogged = Util.getUserLoggedApi(auth);

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            UsuariosWeb user = UsuariosWebController.getUserById(manager, userLogged.getCodUser());

            Date dataHora = Util.getDateUTC(new Date(dataHoraTime), TimeZone.getTimeZone(user.getTimeZone()));

            Atracacao atracacao = AtracacaoController
                    .getByCodAtracacaoAndCodCliente(manager, codAtracacao, userLogged.getCodClient());

            if (dataHora.before(atracacao.getDataInicio())) {
                atracacao = null;
            }

            Manobra manobraDireita = null;
            Manobra manobraEsquerda = null;

            if (atracacao != null) {

                manobraDireita = ManobraController.getUltimaMarcacaoLadoDireitoByCodAtracacao(manager,
                        atracacao.getCodAtracacao(), dataHora);
                manobraEsquerda = ManobraController.getUltimaMarcacaoLadoEsquerdoByCodAtracacao(manager,
                        atracacao.getCodAtracacao(), dataHora);
                Float angulo = ManobraController.getAngulo(manager,
                        atracacao.getCodAtracacao(), dataHora);

                if (manobraDireita != null && manobraEsquerda != null) {

                    if (manobraDireita.getDataHora().before(manobraEsquerda.getDataHora())) {
                        json.put("status", getStatus(manobraEsquerda.getStatus()));
                        json.put("statusClass", getClassStatus(manobraEsquerda.getStatus()));
                    } else {
                        json.put("status", getStatus(manobraDireita.getStatus()));
                        json.put("statusClass", getClassStatus(manobraDireita.getStatus()));
                    }

                } else if (manobraDireita != null) {

                    json.put("status", getStatus(manobraDireita.getStatus()));
                    json.put("statusClass", getClassStatus(manobraDireita.getStatus()));

                } else {

                    json.put("status", getStatus(manobraEsquerda.getStatus()));
                    json.put("statusClass", getClassStatus(manobraEsquerda.getStatus()));

                }

                json.put("angulo", angulo == null ? JSONObject.NULL : angulo);

            }

            json.put("operando", atracacao != null);

            // preenche os dados do lado direito
            if (manobraDireita != null) {

                JSONObject jsonDir = new JSONObject();
                jsonDir.put("dataHora", getDataUltimaAtualizacao(manobraDireita.getDataHora(), user.getTimeZone()));
                jsonDir.put("distancia", getDistancia(manobraDireita.getDistanciaDireita()));
                jsonDir.put("velocidade", getVelocidade(manobraDireita.getVelocidadeDireita()));

                json.put("direito", jsonDir);

            }

            // preenche os dados do lado esquerdo
            if (manobraEsquerda != null) {

                JSONObject jsonEsq = new JSONObject();
                jsonEsq.put("dataHora", getDataUltimaAtualizacao(manobraEsquerda.getDataHora(), user.getTimeZone()));
                jsonEsq.put("distancia", getDistancia(manobraEsquerda.getDistanciaEsquerda()));
                jsonEsq.put("velocidade", getVelocidade(manobraEsquerda.getVelocidadeEsquerda()));

                json.put("esquerdo", jsonEsq);

            }

        } catch (Exception ex) {
            json = new JSONObject();
            try {
                json.put("error", true);
                json.put("message", "Could not query");
            } catch (Exception ex2) {
            }

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

        return json.toString();

    }

    @GET
    @Path("/{cod}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getMooringByCod(
            @HeaderParam("authorization") String auth,
            @PathParam("cod") int cod) {

        UserLoggedApi userLogged = Util.getUserLoggedApi(auth);

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        JSONObject json = new JSONObject();

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            Atracacao atracacao = AtracacaoController
                    .getByCodAtracacaoAndCodCliente(manager, cod, userLogged.getCodClient());

            if (atracacao != null) {

                JSONObject jsonEmbarcacao = null;
                
                if (atracacao.getCodNavio() != null) {

                    jsonEmbarcacao = new JSONObject();
                    jsonEmbarcacao.put("nome", getNomeEmbarcacao(atracacao));
                    jsonEmbarcacao.put("imo", atracacao.getCodNavio().getImo());
                    String imagem = getImagemEmbarcacao(manager, atracacao.getCodNavio().getImo());
                    jsonEmbarcacao.put("imagem", imagem == null ? JSONObject.NULL : imagem);

                }

                JSONObject jsonBerco = new JSONObject();
                Berco berco = atracacao.getCodBerco();
                jsonBerco.put("cod", berco.getCodBerco());
                jsonBerco.put("nome", berco.getNome());
                jsonBerco.put("latitude", berco.getLatitude());
                jsonBerco.put("longitude", berco.getLongitude());

                json.put("cod", atracacao.getCodAtracacao());
                json.put("berco", jsonBerco);
                json.put("embarcacao", jsonEmbarcacao == null ? JSONObject.NULL : jsonEmbarcacao);

            }

        } catch (Exception ex) {
            json = new JSONObject();
            try {
                json.put("error", true);
                json.put("message", "Could not query");
            } catch (Exception ex2) {
            }

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

        return json.toString();

    }

    public String getStatus(StatusOperacaoEnum status) {
        if (status == null) {
            return "Sem embarcação";
        }
        return status.getValue();
    }

    public String getClassStatus(StatusOperacaoEnum statis) {
        if (statis == null) {
            return "status-sem-embarcacao";
        }
        return "status-" + statis.getValue().toLowerCase();
    }

    public String getDistancia(Float distancia) {
        if (distancia == null) {
            return "Sem foco";
        }
        return getValue(distancia);
    }

    public String getVelocidade(Float velocidade) {
        if (velocidade == null) {
            return "";
        }
        return getValue(velocidade * 100);
    }

    public String getValue(Float value) {
        if (value == null) {
            return "";
        }
        return new DecimalFormat("###,##0.00").format(Util.round(value, 2));
    }

    public String getImagemEmbarcacao(EntityManager manager, int imo) throws Exception {

        ImageVessel imageVessel = NavioController.getImageVesselByImo(manager, imo);

        if (imageVessel != null && imageVessel.getImagem() != null) {
            byte[] bytes = new byte[imageVessel.getImagem().length];
            for (int i = 0; i < imageVessel.getImagem().length; i++) {
                bytes[i] = imageVessel.getImagem()[i];
            }
            StringBuilder sb = new StringBuilder();
            sb.append("data:image/png;base64,");
            sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes, false)));
            return sb.toString();

        }

        return null;

    }

    public String getNomeEmbarcacao(Atracacao atracacao) {
        if (atracacao == null
                || atracacao.getCodNavio() == null
                || atracacao.getCodNavio().getNomeNavio().trim().isEmpty()) {
            return "";
        }
        return atracacao.getCodNavio().getNomeNavio().toUpperCase();
    }

    public String getDataUltimaAtualizacao(Date dataHora, String timeZone) {
        return Util.getStringDateLastUpdateComSegundos(dataHora, Util.getDateUTC(), timeZone);
    }

}
