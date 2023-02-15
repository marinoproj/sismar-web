package br.com.marino.sismar.util;

import br.com.marino.sismar.entity.EventReport;
import br.com.marino.sismar.reports.models.RelatorioManobra;
import br.com.marino.sismar.reports.models.RelatorioManobraResumo;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class Report {

    private HttpServletResponse response;
    private FacesContext context;
    private ByteArrayOutputStream baos;

    public Report() {
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();
    }

    public void getEventReport(String locationJasper, 
            List<EventReport> list, Map params) throws Exception {

        baos = new ByteArrayOutputStream();
        
        JasperReport report = JasperCompileManager
                .compileReport(JRLoader.getInputStream(
                        getClass().getResource(locationJasper)));
                
        JasperPrint print = JasperFillManager.fillReport(report, params,
                new JRBeanCollectionDataSource(list));
        
        JasperExportManager.exportReportToPdfStream(print, baos);
        
        response.reset();
        response.setContentType("application/pdf");
        response.setContentLength(baos.size());
        response.setHeader("Content-Disposition", "attachment; filename=events_report.pdf");
        response.getOutputStream().write(baos.toByteArray());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        
        context.responseComplete();

    }  
    
    public void getResumoManobra(String locationJasper, 
            List<RelatorioManobraResumo> list, Map params) throws Exception {

        baos = new ByteArrayOutputStream();
        
        JasperReport report = JasperCompileManager
                .compileReport(JRLoader.getInputStream(
                        getClass().getResource(locationJasper)));
                
        JasperPrint print = JasperFillManager.fillReport(report, params,
                new JRBeanCollectionDataSource(list));
        
        JasperExportManager.exportReportToPdfStream(print, baos);
        
        response.reset();
        response.setContentType("application/pdf");
        response.setContentLength(baos.size());
        response.setHeader("Content-Disposition", "attachment; filename=relatorio_resumo_manobra.pdf");
        response.getOutputStream().write(baos.toByteArray());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        
        context.responseComplete();

    }
    
    public void getCompletoManobra(String locationJasper, 
            List<RelatorioManobra> list, Map params) throws Exception {

        baos = new ByteArrayOutputStream();
        
        JasperReport report = JasperCompileManager
                .compileReport(JRLoader.getInputStream(
                        getClass().getResource(locationJasper)));
                
        JasperPrint print = JasperFillManager.fillReport(report, params,
                new JRBeanCollectionDataSource(list));
        
        JasperExportManager.exportReportToPdfStream(print, baos);
        
        response.reset();
        response.setContentType("application/pdf");
        response.setContentLength(baos.size());
        response.setHeader("Content-Disposition", "attachment; filename=relatorio_completo_manobra.pdf");
        response.getOutputStream().write(baos.toByteArray());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        
        context.responseComplete();

    }

}
