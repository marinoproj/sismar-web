package br.com.marino.sismar.bean.admin.administration;

import br.com.marino.sismar.controller.BercoClienteController;
import br.com.marino.sismar.controller.BercosController;
import br.com.marino.sismar.controller.ClientesController;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.BercoCliente;
import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.enums.ModoEncerrarAtracacao;
import br.com.marino.sismar.enums.TipoSensoresAproximacao;
import br.com.marino.sismar.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "ClientBean")
@ViewScoped
public class ClientBean implements Serializable {

    private Clientes client;
    private Integer codClient;
    private Part imageFile;
    private byte[] imageBytes;

    private List<Berco> bercos;
    private List<Berco> bercosSelecionados;
    
    private List<TipoSensoresAproximacao> listTipoSensoresAproximacao;
    private List<ModoEncerrarAtracacao> listModoEncerramentoAtracacao;

    @PostConstruct
    public void init() {

        client = new Clientes();
        bercosSelecionados = new ArrayList<>();
        
        listTipoSensoresAproximacao = Stream.of(TipoSensoresAproximacao.values())
                .collect(Collectors.toList());
        
        listModoEncerramentoAtracacao = Stream.of(ModoEncerrarAtracacao.values())
                .collect(Collectors.toList());

        try {
            String cod = FacesContext.
                    getCurrentInstance().
                    getExternalContext().
                    getRequestParameterMap().
                    get("cliente");
            if (cod != null && !cod.isEmpty()) {
                codClient = Integer.parseInt(cod);
            }
        } catch (NumberFormatException ex) {
            codClient = null;
        }

        reloadInit();

    }

    @PreDestroy
    public void destroy() {
        client = null;
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            if (codClient != null) {

                client = ClientesController.getByCod(manager, codClient);
                if (client.getLogo() != null) {
                    imageBytes = Base64.decodeBase64(client.getLogo());
                }

                for (BercoCliente bc : client.getBercosCliente()) {
                    bercosSelecionados.add(bc.getCodBerco());
                }

            }

            bercos = BercosController.getListBercos(manager);

        } catch (Exception ex) {
            client = new Clientes();
            codClient = null;
            bercos = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public List<Berco> getBercos() {
        return bercos;
    }

    public List<Berco> getBercosSelecionados() {
        return bercosSelecionados;
    }

    public void setBercosSelecionados(List<Berco> bercosSelecionados) {
        this.bercosSelecionados = bercosSelecionados;
    }

    public void setBercos(List<Berco> bercos) {
        this.bercos = bercos;
    }

    public Clientes getClient() {
        return client;
    }

    public void setClient(Clientes client) {
        this.client = client;
    }

    public Integer getCodClient() {
        return codClient;
    }

    public void setCodClient(Integer codClient) {
        this.codClient = codClient;
    }

    public Part getImageFile() {
        return imageFile;
    }

    public void setImageFile(Part imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isActionNew() {
        return codClient == null;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public List<TipoSensoresAproximacao> getListTipoSensoresAproximacao() {
        return listTipoSensoresAproximacao;
    }

    public void setListTipoSensoresAproximacao(List<TipoSensoresAproximacao> listTipoSensoresAproximacao) {
        this.listTipoSensoresAproximacao = listTipoSensoresAproximacao;
    }    

    public List<ModoEncerrarAtracacao> getListModoEncerramentoAtracacao() {
        return listModoEncerramentoAtracacao;
    }

    public void setListModoEncerramentoAtracacao(List<ModoEncerrarAtracacao> listModoEncerramentoAtracacao) {
        this.listModoEncerramentoAtracacao = listModoEncerramentoAtracacao;
    }
    
    public String getLogoClient() {
        if (imageBytes == null) {
            return "/sismar/faces/javax.faces.resource/img/sem_imagem.png";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageBytes, false)));
        return sb.toString();
    }

    public void removeLogoClient() {
        imageFile = null;
        imageBytes = null;
    }

    public void uploadLogoClient(AjaxBehaviorEvent event) throws IOException {

        if (imageFile != null) {
            if (!"image/png".equals(imageFile.getContentType())) {

                FacesContext.getCurrentInstance().addMessage(
                        "form-event",
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Tipo de imagem inválida",
                                "A imagem deve ser do tipo png."));

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Tipo de imagem inválida",
                                "A imagem deve ser do tipo png."));

                imageFile = null;

                return;

            }
        }

        imageBytes = IOUtils.toByteArray(imageFile.getInputStream());
        imageFile = null;

    }

    public void save() {

        if (client.getNome().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(
                    "form-event",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Preencha o formulário corretamente. O nome do cliente é obrigatório"));
            return;
        }

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            client.setStatus(true);

            if (imageBytes != null) {
                client.setLogo(StringUtils.newStringUtf8(Base64.encodeBase64(imageBytes, false)));
            } else {
                client.setLogo(null);
            }

            manager.getTransaction().begin();

            if (codClient == null) {

                ClientesController.insert(manager, client);

                for (Berco bercoSelecionado : bercosSelecionados) {
                    BercoCliente bc = new BercoCliente();
                    bc.setCodBerco(bercoSelecionado);
                    bc.setCodCliente(client);
                    BercoClienteController.insert(manager, bc);
                }

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Cliente cadastrado com sucesso!"));

            } else {

                client.setCod(codClient);
                ClientesController.edit(manager, client);

                List<BercoCliente> bercosClienteSelecionados = BercoClienteController.getList(manager, client.getCod());
                for (BercoCliente bc : bercosClienteSelecionados) {
                    try {
                        BercoClienteController.delete(manager, bc);
                    } catch (Exception ex) {
                    }
                }
                for (Berco bercoSelecionado : bercosSelecionados) {
                    BercoCliente bc = new BercoCliente();
                    bc.setCodBerco(bercoSelecionado);
                    bc.setCodCliente(client);
                    BercoClienteController.insert(manager, bc);
                }

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso",
                        "Cadastro atualizado com sucesso!"));

            }

            manager.getTransaction().commit();
            codClient = client.getCod();

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

    public void delete() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            manager.getTransaction().begin();

            client.setStatus(false);
            ClientesController.edit(manager, client);

            List<BercoCliente> bercosClienteSelecionados = BercoClienteController.getList(manager, client.getCod());
            for (BercoCliente bc : bercosClienteSelecionados) {
                try {
                    BercoClienteController.delete(manager, bc);
                } catch (Exception ex) {
                }
            }

            manager.getTransaction().commit();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso",
                    "Cadastro removido com sucesso!"));

            FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl()
                    + "admin/administration/clients.xhtml");

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
        }

    }

}
