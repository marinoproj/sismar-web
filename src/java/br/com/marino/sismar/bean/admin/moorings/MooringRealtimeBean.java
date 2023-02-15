package br.com.marino.sismar.bean.admin.moorings;

import br.com.marino.sismar.controller.AtracacaoController;
import br.com.marino.sismar.controller.BercosController;
import br.com.marino.sismar.controller.ConfigController;
import br.com.marino.sismar.controller.ManobraController;
import br.com.marino.sismar.entity.Atracacao;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.Config;
import br.com.marino.sismar.entity.Manobra;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "MooringRealtimeBean")
@ViewScoped
public class MooringRealtimeBean implements Serializable {

    private Integer codCliente;
    private Integer codBerco;
    private Berco berco;
    private Config config;

    private Atracacao atracacao;
    private Manobra manobraDireita;
    private Manobra manobraEsquerda;
    private Float angulo;

    private Exception atracacaoException;

    @PostConstruct
    public void init() {
        codBerco = Integer.parseInt(FacesContext.
                getCurrentInstance().
                getExternalContext().
                getRequestParameterMap().
                get("berco"));
        reloadInit();
        reloadAtracacao();
    }

    @PreDestroy
    public void destroy() {
        codBerco = null;
        atracacao = null;
        atracacaoException = null;
    }

    public Integer getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Integer codCliente) {
        this.codCliente = codCliente;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Manobra getManobraDireita() {
        return manobraDireita;
    }

    public void setManobraDireita(Manobra manobraDireita) {
        this.manobraDireita = manobraDireita;
    }

    public Manobra getManobraEsquerda() {
        return manobraEsquerda;
    }

    public void setManobraEsquerda(Manobra manobraEsquerda) {
        this.manobraEsquerda = manobraEsquerda;
    }

    public Berco getBerco() {
        return berco;
    }

    public void setBerco(Berco berco) {
        this.berco = berco;
    }

    public Integer getCodBerco() {
        return codBerco;
    }

    public void setCodBerco(Integer codBerco) {
        this.codBerco = codBerco;
    }

    public Atracacao getAtracacao() {
        return atracacao;
    }

    public void setAtracacao(Atracacao atracacao) {
        this.atracacao = atracacao;
    }

    public Exception getAtracacaoException() {
        return atracacaoException;
    }

    public void setAtracacaoException(Exception atracacaoException) {
        this.atracacaoException = atracacaoException;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            codCliente = SessionContext.getInstance().getClientLoggedIn().getCod();
            berco = BercosController.getByCod(manager, codBerco);
            config = ConfigController.getConfig(manager);

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    
    public boolean isComAtracacao(){
        return atracacao != null;
    }
    
    public void verNoMapa() throws IOException{
     
        if (atracacao == null){
            return;
        }
        
        FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .redirect(Util.getPathUrl() + "admin/ais/realtime.xhtml?codAtracacao=" + atracacao.getCodAtracacao());
        
    }
    
    public void reloadAtracacao() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            atracacao = AtracacaoController.getAtracacaoAtivaByCodBercoAndCodCliente(manager, codBerco, codCliente);

            if (atracacao != null) {
                manobraDireita = ManobraController.getUltimaMarcacaoLadoDireitoByCodAtracacao(manager, atracacao.getCodAtracacao());
                manobraEsquerda = ManobraController.getUltimaMarcacaoLadoEsquerdoByCodAtracacao(manager, atracacao.getCodAtracacao());
                angulo = ManobraController.getAngulo(manager, atracacao.getCodAtracacao());
            } else {
                manobraDireita = null;
                manobraEsquerda = null;
                angulo = null;
            }

            atracacaoException = null;

        } catch (Exception ex) {
            atracacaoException = ex;
            atracacao = null;
            manobraDireita = null;
            manobraEsquerda = null;

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public String getDataUltimaAtualizacaoDireito() {
        if (manobraDireita == null) {
            return "";
        }
        return Util.getStringDateLastUpdateComSegundos(manobraDireita.getDataHora(), Util.getDateUTC(), true);
    }

    public String getDataUltimaAtualizacaoEsquerdo() {
        if (manobraEsquerda == null) {
            return "";
        }
        return Util.getStringDateLastUpdateComSegundos(manobraEsquerda.getDataHora(), Util.getDateUTC(), true);
    }

    public String getValue(Float value) {
        if (value == null) {
            return "";
        }
        return new DecimalFormat("###,##0.00").format(Util.round(value, 2));
    }

    public String getNomeEmbarcacao() {
        if (atracacao == null
                || atracacao.getCodNavio() == null
                || atracacao.getCodNavio().getNomeNavio().trim().isEmpty()) {
            return "";
        }
        return atracacao.getCodNavio().getNomeNavio().toUpperCase();
    }

    public String getAngulo() {
        if (atracacao == null) {
            return "";
        }       
        if (angulo == null) {
            return "";
        }
        return getValue(angulo) + " º";
    }

    public String getDistanciaDireita() {
        if (manobraDireita == null) {
            return "";
        }
        if (manobraDireita.getDistanciaDireita() == null) {
            return "Sem foco";
        }
        return getValue(manobraDireita.getDistanciaDireita());
    }

    public String getDistanciaEsquerda() {
        if (manobraEsquerda == null) {
            return "";
        }
        if (manobraEsquerda.getDistanciaEsquerda() == null) {
            return "Sem foco";
        }
        return getValue(manobraEsquerda.getDistanciaEsquerda());
    }

    public String getVelocidadeDireita() {
        if (manobraDireita == null) {
            return "";
        }
        if (manobraDireita.getVelocidadeDireita() == null) {
            return "";
        }
        return getValue(manobraDireita.getVelocidadeDireita() * 100);
    }

    public String getVelocidadeEsquerda() {
        if (manobraEsquerda == null) {
            return "";
        }
        if (manobraEsquerda.getVelocidadeEsquerda() == null) {
            return "";
        }
        return getValue(manobraEsquerda.getVelocidadeEsquerda() * 100);
    }
    
    public String getStatus() {
        if (atracacaoException != null) {
            return "Offline";
        }
        if (atracacao == null) {
            return "Sem embarcação";
        }
        return atracacao.getStatusOperacao().getValue();
    }
    
    public String getClassStatus() {
        if (atracacaoException != null) {
            return "status-offline";
        }
        if (atracacao == null) {
            return "status-sem-embarcacao";
        }
        return "status-" + atracacao.getStatusOperacao().getValue().toLowerCase();
    }

}
