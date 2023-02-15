package br.com.marino.sismar.bean.admin.components;

import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "PlaybackBean")
@ViewScoped
public class PlaybackBean implements Serializable {

    private String startDatePlayback,
            endDatePlayback;

    @PostConstruct
    public void init() {
        this.startDatePlayback = null;
        this.endDatePlayback = null;
    }

    @PreDestroy
    public void destroy() {
    }

    public String getStartDatePlayback() {
        return startDatePlayback;
    }

    public void setStartDatePlayback(String startDatePlayback) {
        this.startDatePlayback = startDatePlayback;
    }

    public String getEndDatePlayback() {
        return endDatePlayback;
    }

    public void setEndDatePlayback(String endDatePlayback) {
        this.endDatePlayback = endDatePlayback;
    }

    public void openPlayback() throws IOException {

        if (startDatePlayback == null || endDatePlayback == null) {
            return;
        }

        String regex = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2})";

        if (!startDatePlayback.matches(regex) || !endDatePlayback.matches(regex)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Datas inválidas!"));
            return;
        }

        startDatePlayback = startDatePlayback.replaceAll(regex, "$3")
                + "/" + startDatePlayback.replaceAll(regex, "$2")
                + "/" + startDatePlayback.replaceAll(regex, "$1")
                + " " + startDatePlayback.replaceAll(regex, "$4")
                + ":" + startDatePlayback.replaceAll(regex, "$5");

        endDatePlayback = endDatePlayback.replaceAll(regex, "$3")
                + "/" + endDatePlayback.replaceAll(regex, "$2")
                + "/" + endDatePlayback.replaceAll(regex, "$1")
                + " " + endDatePlayback.replaceAll(regex, "$4")
                + ":" + endDatePlayback.replaceAll(regex, "$5");

        try {

            Date start = Util.stringToDate(startDatePlayback, "dd/MM/yyyy HH:mm");
            Date end = Util.stringToDate(endDatePlayback, "dd/MM/yyyy HH:mm");

            UsuariosWeb user = SessionContext.getInstance().getUserLoggedIn();
            
            int minutes = Util.getRangeSeconds(start, end) / 60; // intervalo informado em minutos
            int minutosAvailable = user.getTempoMaxPlayback() == null ? 120 : user.getTempoMaxPlayback(); // intervalo disponível em minutos
            
            // periodo não informado no cadastro
            if (user.getTempoMaxPlayback() == null) {
                if (minutes > minutosAvailable) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Falha",
                            "O período máximo é de 2 horas. Por favor, informe um período "
                            + "menor!"));
                    return;
                }
            
            }else{
                // periodo informado no cadastro
                if (minutes > minutosAvailable) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Falha",
                            "O período máximo é de " + minutosAvailable + " minutos. Por favor, informe um período "
                            + "menor!"));
                    return;
                }
                
            }            

        } catch (Exception ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Falha ao converter as datas. Verifique se foram informadas corretamente!"));
            return;
        }

        FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl() + "admin/ais/playback.xhtml?start=" + startDatePlayback + "&end=" + endDatePlayback);

    }

}
