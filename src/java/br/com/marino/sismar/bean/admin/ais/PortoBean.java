package br.com.marino.sismar.bean.admin.ais;

import br.com.marino.sismar.controller.AreaController;
import br.com.marino.sismar.controller.MovimentacaoPortoController;
import br.com.marino.sismar.entity.Area;
import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.MovimentacaoPorto;
import br.com.marino.sismar.entity.Poin;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name = "PortoBean")
@ViewScoped
public class PortoBean implements Serializable {

    // filtros (porto agora)
    private Area agoraArea;
    private boolean agoraNaviosFundeados = true;
    private boolean agoraNaviosCanal = true;
    private boolean agoraNaviosAtracados = true;
    private Berco agoraBerco;
    private List<MovimentacaoPorto> agoraListMovimentacoesPorto;
    private List<Berco> agoraBercos;

    // parâmetros
    private List<Area> areas;

    @PostConstruct
    public void init() {
        reloadInit();
    }

    public void reloadInit() {

        EntityManagerFactory factory = null;
        EntityManager manager = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();

            areas = AreaController.getListArea(manager);

            if (!areas.isEmpty()) {
                agoraArea = areas.get(1);
                agoraBercos = agoraArea.getBercoList();
            }

            reloadMovimentacaoPortoAgora(manager);

        } catch (Exception ex) {
            areas = new ArrayList<>();
            agoraListMovimentacoesPorto = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }

        }

    }

    public void agoraUpdateBercos() {

        if (agoraArea == null) {
            agoraBercos = new ArrayList<>();
            agoraBerco = null;
            return;
        }

        agoraBercos = agoraArea.getBercoList();
        agoraBerco = null;

    }

    public void reloadMovimentacaoPortoAgora(EntityManager manager) {

        if (manager != null) {

            try {
                agoraListMovimentacoesPorto = MovimentacaoPortoController
                        .getListMovimentacaoPortoAtivoByCodArea(manager, agoraArea.getCodArea());
            } catch (Exception ex) {
                agoraListMovimentacoesPorto = new ArrayList<>();
            } finally {
                filterAgoraListMovimentacaoPorto();
            }

            return;
        }

        EntityManagerFactory factory = null;

        try {

            factory = Persistence.createEntityManagerFactory("sismarPU");
            manager = factory.createEntityManager();
            agoraListMovimentacoesPorto = MovimentacaoPortoController
                    .getListMovimentacaoPortoAtivoByCodArea(manager, agoraArea.getCodArea());

        } catch (Exception ex) {
            agoraListMovimentacoesPorto = new ArrayList<>();

        } finally {
            if (manager != null) {
                manager.close();
            }
            if (factory != null) {
                factory.close();
            }
            filterAgoraListMovimentacaoPorto();
        }

    }

    private void filterAgoraListMovimentacaoPorto() {

        List<MovimentacaoPorto> agoraListMovimentacoesPortoNew = new ArrayList<>();

        if (agoraNaviosFundeados) {
            agoraListMovimentacoesPorto
                    .stream()
                    .filter(obj -> obj.getDataSaidaFundeio() == null)
                    .forEach(obj -> {
                        if (!agoraListMovimentacoesPortoNew.stream().anyMatch(oN -> oN.getCodMovimentacaoPorto().intValue()
                                == obj.getCodMovimentacaoPorto().intValue())) {
                            agoraListMovimentacoesPortoNew.add(obj);
                        }
                    });
        }

        if (agoraNaviosCanal) {
            agoraListMovimentacoesPorto
                    .stream()
                    .filter(obj -> obj.getDataEntradaCanal() != null
                    && ((obj.getDataEntradaPoinAtracacao1() == null || obj.getDataSaidaPoinAtracacao1() != null)
                    && (obj.getDataEntradaPoinAtracacao2() == null || obj.getDataSaidaPoinAtracacao2() != null)
                    && (obj.getDataEntradaPoinAtracacao3() == null || obj.getDataSaidaPoinAtracacao3() != null)
                    && (obj.getDataEntradaPoinAtracacao4() == null || obj.getDataSaidaPoinAtracacao4() != null)))
                    .forEach(obj -> {
                        if (!agoraListMovimentacoesPortoNew.stream().anyMatch(oN -> oN.getCodMovimentacaoPorto().intValue()
                                == obj.getCodMovimentacaoPorto().intValue())) {
                            agoraListMovimentacoesPortoNew.add(obj);
                        }
                    });
        }

        if (agoraNaviosAtracados) {
            agoraListMovimentacoesPorto
                    .stream()
                    .filter(obj -> (obj.getDataEntradaPoinAtracacao1() != null && obj.getDataSaidaPoinAtracacao1() == null)
                    || (obj.getDataEntradaPoinAtracacao2() != null && obj.getDataSaidaPoinAtracacao2() == null)
                    || (obj.getDataEntradaPoinAtracacao3() != null && obj.getDataSaidaPoinAtracacao3() == null)
                    || (obj.getDataEntradaPoinAtracacao4() != null && obj.getDataSaidaPoinAtracacao4() == null))
                    .forEach(obj -> {
                        if (!agoraListMovimentacoesPortoNew.stream().anyMatch(oN -> oN.getCodMovimentacaoPorto().intValue()
                                == obj.getCodMovimentacaoPorto().intValue())) {
                            agoraListMovimentacoesPortoNew.add(obj);
                        }
                    });
        }

        if (agoraBerco != null) {
            agoraListMovimentacoesPorto
                    .stream()
                    .filter(obj -> (obj.getCodPoinAtracacao1() != null && obj.getCodPoinAtracacao1().getCodPoin().intValue() == agoraBerco.getCodPoin().getCodPoin().intValue() && obj.getDataSaidaPoinAtracacao1() == null)
                    || (obj.getCodPoinAtracacao2() != null && obj.getCodPoinAtracacao2().getCodPoin().intValue() == agoraBerco.getCodPoin().getCodPoin().intValue() && obj.getDataSaidaPoinAtracacao2() == null)
                    || (obj.getCodPoinAtracacao3() != null && obj.getCodPoinAtracacao3().getCodPoin().intValue() == agoraBerco.getCodPoin().getCodPoin().intValue() && obj.getDataSaidaPoinAtracacao3() == null)
                    || (obj.getCodPoinAtracacao4() != null && obj.getCodPoinAtracacao4().getCodPoin().intValue() == agoraBerco.getCodPoin().getCodPoin().intValue() && obj.getDataSaidaPoinAtracacao4() == null))
                    .forEach(obj -> {
                        if (!agoraListMovimentacoesPortoNew.stream().anyMatch(oN -> oN.getCodMovimentacaoPorto().intValue()
                                == obj.getCodMovimentacaoPorto().intValue())) {
                            agoraListMovimentacoesPortoNew.add(obj);
                        }
                    });
        }
        
        agoraListMovimentacoesPorto = agoraListMovimentacoesPortoNew;

    }

    public boolean isAgoraNaviosFundeados() {
        return agoraNaviosFundeados;
    }

    public void setAgoraNaviosFundeados(boolean agoraNaviosFundeados) {
        this.agoraNaviosFundeados = agoraNaviosFundeados;
    }

    public boolean isAgoraNaviosCanal() {
        return agoraNaviosCanal;
    }

    public void setAgoraNaviosCanal(boolean agoraNaviosCanal) {
        this.agoraNaviosCanal = agoraNaviosCanal;
    }

    public boolean isAgoraNaviosAtracados() {
        return agoraNaviosAtracados;
    }

    public void setAgoraNaviosAtracados(boolean agoraNaviosAtracados) {
        this.agoraNaviosAtracados = agoraNaviosAtracados;
    }

    public Berco getAgoraBerco() {
        return agoraBerco;
    }

    public void setAgoraBerco(Berco agoraBerco) {
        this.agoraBerco = agoraBerco;
    }

    public List<MovimentacaoPorto> getAgoraListMovimentacoesPorto() {
        return agoraListMovimentacoesPorto;
    }

    public void setAgoraListMovimentacoesPorto(List<MovimentacaoPorto> agoraListMovimentacoesPorto) {
        this.agoraListMovimentacoesPorto = agoraListMovimentacoesPorto;
    }

    public Area getAgoraArea() {
        return agoraArea;
    }

    public void setAgoraArea(Area agoraArea) {
        this.agoraArea = agoraArea;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public List<Berco> getAgoraBercos() {
        return agoraBercos;
    }

    public void setAgoraBercos(List<Berco> agoraBercos) {
        this.agoraBercos = agoraBercos;
    }

    public String getNameBerco(Poin codPoin){
        if (codPoin == null){
            return "";
        }
        if (codPoin.getBercoList() == null || codPoin.getBercoList().isEmpty()){
            return "";
        }
        return codPoin.getBercoList().get(0).getNome();
    }    
    
    public String getDatetimeToString(Date datetime) {
        if (datetime == null) {
            return "";
        }
        return Util.dateToString(datetime, "dd/MM HH:mm");
    }

    public String getTimeDuration(Date start, Date end) {
        if (start == null){
            return "";
        }
        if (end == null){
            return "";
        }
        return Util.getTimeDuration(start, end);
    }
    
    public boolean containsData(Date data){
        return data != null;
    }
    
    public boolean notContainsData(Date data){
        return data == null;
    }
    
}
