package br.com.marino.sismar.entity;

import br.com.marino.api.tidetable.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EmbarcacaoCodesp {

    // navios fundeados
    private String nomeEmbarcacao;
    private Date dataChegada;
    private String prioridade;
    private String terminal;

    // atracações programadas
    private boolean liberadoCodesp;
    private String carga;
    private String evento;
    private String local;
    private String dataProgramacao;
    private int ordem;

    public EmbarcacaoCodesp() {
    }

    public String getNomeEmbarcacao() {
        return nomeEmbarcacao;
    }

    public void setNomeEmbarcacao(String nomeEmbarcacao) {
        this.nomeEmbarcacao = nomeEmbarcacao;
    }

    public Date getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(Date dataChegada) {
        this.dataChegada = dataChegada;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public boolean isLiberadoCodesp() {
        return liberadoCodesp;
    }

    public void setLiberadoCodesp(boolean liberadoCodesp) {
        this.liberadoCodesp = liberadoCodesp;
    }

    public String getDataProgramacao() {
        return dataProgramacao;
    }

    public void setDataProgramacao(String dataProgramacao) {
        this.dataProgramacao = dataProgramacao;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }
   
    private static List<EmbarcacaoCodesp> loadSiteDataProgrammedAttractions(List<EmbarcacaoCodesp> lista) throws Exception {

        String atracacoes_programadas = "http://www.portodesantos.com.br/outros-links/porto-hoje/atracacoes-programadas/";

        Document doc = Jsoup.connect(atracacoes_programadas)
                .get();

        Elements elements = doc.select("table");

        int i = 1;

        for (Element element : elements) {

            Elements table = element.select("tbody").select("tr");

            for (Element row : table) {

                Elements tds = row.select("td");

                String nomeEmbarcacao = tds.get(3).text().trim();
                boolean liberadoCodesp = true;
                String carga = tds.get(4).text().trim();
                String evento = tds.get(5).text().trim();
                String local = tds.get(2).text().trim();
                String dataProgramacao = tds.get(0).text().trim() + " " + tds.get(1).text().trim();
                int ordem = i;
                
                for (EmbarcacaoCodesp atracaoProgramadaCodesp : lista) {
                    if (atracaoProgramadaCodesp.getNomeEmbarcacao()
                            .equalsIgnoreCase(nomeEmbarcacao)) {
                        atracaoProgramadaCodesp.setLiberadoCodesp(liberadoCodesp);
                        atracaoProgramadaCodesp.setCarga(carga);
                        atracaoProgramadaCodesp.setEvento(evento);
                        atracaoProgramadaCodesp.setLocal(local);
                        atracaoProgramadaCodesp.setOrdem(ordem);
                        atracaoProgramadaCodesp.setDataProgramacao(dataProgramacao);
                        break;
                    }
                }

            }

            i += 1;

        }

        return lista;

    }

    public static List<EmbarcacaoCodesp> getPoweredVessels() throws Exception {

        String url_navios_fundeados = "http://www.portodesantos.com.br/outros-links/porto-hoje/navios-fundeados/";

        List<EmbarcacaoCodesp> lista = new ArrayList<>();

        Document doc = Jsoup.connect(url_navios_fundeados)
                .get();

        Elements elements = doc.select("table").select(".padrao").select("tbody").select("tr");
        
        for (Element element : elements) {

            Elements tds = element.select("td");
            tds.get(0).children().remove();
            
            String nomeEmbarcacao = tds.get(0).text().trim();
            Date dataChegada = Util.stringToDate(tds.get(4).text().trim(), "dd/MM/yyyy HH:mm:ss");
            String prioridade = tds.get(11).text().trim();
            String terminal = tds.get(12).text().trim();

            EmbarcacaoCodesp obj = new EmbarcacaoCodesp();
            obj.setNomeEmbarcacao(nomeEmbarcacao);
            obj.setDataChegada(dataChegada);
            obj.setPrioridade(prioridade);
            obj.setTerminal(terminal);
            obj.setLiberadoCodesp(false);

            lista.add(obj);

        }

        return lista;

    }
    
    public static List<EmbarcacaoCodesp> getDataCodesp() throws Exception{
        List<EmbarcacaoCodesp> lista = getPoweredVessels();
        lista = loadSiteDataProgrammedAttractions(lista);
        return lista;
        
    }

    @Override
    public String toString() {
        return "EmbarcacaoCodesp{" + "nomeEmbarcacao=" + nomeEmbarcacao + ", dataChegada=" + dataChegada + ", prioridade=" + prioridade + ", terminal=" + terminal + ", liberadoCodesp=" + liberadoCodesp + ", carga=" + carga + ", evento=" + evento + ", local=" + local + ", dataProgramacao=" + dataProgramacao + ", ordem=" + ordem + '}';
    }    
    
}
