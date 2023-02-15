package br.com.marino.sismar.bean.admin.moorings;

import br.com.marino.sismar.controller.AtracacaoController;
import br.com.marino.sismar.controller.ClientesController;
import br.com.marino.sismar.controller.GiaontController;
import br.com.marino.sismar.controller.NavioController;
import br.com.marino.sismar.controller.PraticoController;
import br.com.marino.sismar.entity.Atracacao;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.entity.Giaont;
import br.com.marino.sismar.entity.Navio;
import br.com.marino.sismar.entity.Pratico;
import br.com.marino.sismar.enums.StatusOperacaoEnum;
import br.com.marino.sismar.session.SessionContext;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "MooringsBean")
@ViewScoped
public class MooringsBean implements Serializable {

    private List<Atracacao> atracacoes;
    private Atracacao atracacaoSelecionada;

    private boolean master = false;
    private Clientes client;    
    
    private List<Pratico> listPraticos;
    private List<Giaont> listGiaonts;
    private List<Berco> listBercos;
    
    // campos de filtros
    private Pratico pratico;
    private Giaont giaont;
    private Navio embarcacao;
    private Berco berco;

    @PostConstruct
    public void init() {
        Boolean m = SessionContext.getInstance().getUserLoggedIn().getMaster();
        master = (m == null ? false : m);
        listBercos = new ArrayList<>(); 
        listPraticos = new ArrayList<>(); 
        listGiaonts = new ArrayList<>(); 
        reloadInit();
    }

    @PreDestroy
    public void destroy() {
        atracacoes = null;
    }

    public List<Pratico> getListPraticos() {
        return listPraticos;
    }

    public void setListPraticos(List<Pratico> listPraticos) {
        this.listPraticos = listPraticos;
    }

    public List<Giaont> getListGiaonts() {
        return listGiaonts;
    }

    public void setListGiaonts(List<Giaont> listGiaonts) {
        this.listGiaonts = listGiaonts;
    }

    public List<Berco> getListBercos() {
        return listBercos;
    }

    public void setListBercos(List<Berco> listBercos) {
        this.listBercos = listBercos;
    }

    public Pratico getPratico() {
        return pratico;
    }

    public void setPratico(Pratico pratico) {
        this.pratico = pratico;
    }

    public Giaont getGiaont() {
        return giaont;
    }

    public void setGiaont(Giaont giaont) {
        this.giaont = giaont;
    }

    public Navio getEmbarcacao() {
        return embarcacao;
    }

    public void setEmbarcacao(Navio embarcacao) {
        this.embarcacao = embarcacao;
    }

    public Berco getBerco() {
        return berco;
    }

    public void setBerco(Berco berco) {
        this.berco = berco;
    }
    
    public String dateToString(Date data) {
        if (data == null) {
            return "";
        }
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(data);
    }
    
    public String getNomeBerco(Berco berco) {
        if (berco == null) {
            return "Não informado";
        }
        return berco.getNome();
    }

    public String getNomeEmbarcacao(Navio embarcacao) {
        if (embarcacao == null) {
            return "Não informado";
        }
        return embarcacao.getNomeNavio();
    }

    public String getNomeGiaont(Giaont giaont) {
        if (giaont == null) {
            return "Não informado";
        }
        return giaont.getNomeGiaont();
    }

    public String getNomePratico(Pratico pratico) {
        if (pratico == null) {
            return "Não informado";
        }
        return pratico.getNomePratico();
    }

    public String getClassNaoInformado(Object obj) {
        if (obj == null){
            return "status-nao-informado";
        }
        return "";
    }
    
    public String getClassStatus(StatusOperacaoEnum status) {
        return "status-" + status.getValue().toLowerCase();
    }

    public boolean isMaster() {
        return master;
    }

    public Clientes getClient() {
        return client;
    }

    public void setClient(Clientes client) {
        this.client = client;
    }

    public List<Atracacao> getAtracacoes() {
        return atracacoes;
    }

    public void setAtracacoes(List<Atracacao> atracacoes) {
        this.atracacoes = atracacoes;
    }

    public Atracacao getAtracacaoSelecionada() {
        return atracacaoSelecionada;
    }

    public void setAtracacaoSelecionada(Atracacao atracacaoSelecionada) {
        this.atracacaoSelecionada = atracacaoSelecionada;
    }
    
    public List<Navio> completeNavios(String query) {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            return NavioController.getVesselsBySearch(manager, query, 10).stream().map(obj -> {

                Navio n = new Navio();

                n.setCodNavio(obj.getCodNavio());
                n.setNomeNavio(obj.getNomeNavio());
                n.setImo(obj.getImo());

                return n;

            }).collect(Collectors.toList());

        } catch (Exception ex) {
            return new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

           // System.out.println("\n\nCAIU AQUI 00\n\n");
            
            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            int codCliente = SessionContext.getInstance().getClientLoggedIn().getCod();
            
            System.out.println("Id do cliente = " + codCliente);
           
            atracacoes = AtracacaoController.search(manager,
                    codCliente, embarcacao,
                    giaont, pratico);

            client = ClientesController.getByCod(manager,
                    SessionContext.getInstance().getClientLoggedIn().getCod());                       
            
            listGiaonts = GiaontController.getListByCodCliente(manager, client.getCod());            
            listPraticos = PraticoController.getListByCodCliente(manager, client.getCod());
            
            System.out.println("Consulta de atracações com sucesso");
            
        } catch (Exception ex) {   
            atracacoes = new ArrayList<>();
            System.out.println("Erro: " + ex.getMessage());
            //ex.printStackTrace();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void filtrar() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            atracacoes = AtracacaoController.search(manager,
                    SessionContext.getInstance().getClientLoggedIn().getCod(), embarcacao,
                    giaont, pratico);

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao efetuar ação!"));

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
            PrimeFaces.current().executeScript("PF('statusdialog').hide()");
        }

    }

}
