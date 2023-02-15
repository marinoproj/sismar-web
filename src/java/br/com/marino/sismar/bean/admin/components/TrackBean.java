package br.com.marino.sismar.bean.admin.components;

import br.com.marino.sismar.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "TrackBean")
@ViewScoped
public class TrackBean implements Serializable {

    private String startDateTrack,
            endDateTrack,
            periodTrack;

    @PostConstruct
    public void init() {
        this.startDateTrack = null;
        this.endDateTrack = null;
        periodTrack = "1"; // 1 = ultima hora, 2 = hoje, 3 = personalizado
    }

    @PreDestroy
    public void destroy() {
    }

    public void selectPeriodTrack() {
    }

    public String getStartDateTrack() {
        return startDateTrack;
    }

    public void setStartDateTrack(String startDateTrack) {
        this.startDateTrack = startDateTrack;
    }

    public String getEndDateTrack() {
        return endDateTrack;
    }

    public void setEndDateTrack(String endDateTrack) {
        this.endDateTrack = endDateTrack;
    }    

    public String getPeriodTrack() {
        return periodTrack;
    }

    public void setPeriodTrack(String periodTrack) {
        this.periodTrack = periodTrack;
    }

    public void openTrack() throws IOException {        
        
        if (periodTrack.equalsIgnoreCase("1")) {

            Calendar end = Calendar.getInstance();

            Calendar start = Calendar.getInstance();
            start.setTime(end.getTime());
            start.add(Calendar.HOUR_OF_DAY, -1);

            startDateTrack = Util.dateToString(start.getTime(), "yyyy-MM-dd HH:mm").replace(" ", "T");
            endDateTrack = Util.dateToString(end.getTime(), "yyyy-MM-dd HH:mm").replace(" ", "T");

        } else if (periodTrack.equalsIgnoreCase("2")) {

            Calendar end = Calendar.getInstance();

            Calendar start = Calendar.getInstance();
            start.setTime(end.getTime());
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            start.set(Calendar.MILLISECOND, 0);

            startDateTrack = Util.dateToString(start.getTime(), "yyyy-MM-dd HH:mm").replace(" ", "T");
            endDateTrack = Util.dateToString(end.getTime(), "yyyy-MM-dd HH:mm").replace(" ", "T");

        }

        String regex = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2})";

        if (!startDateTrack.matches(regex) || !endDateTrack.matches(regex)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha",
                    "Datas inválidas!"));
            return;

        }

        startDateTrack = startDateTrack.replaceAll(regex, "$3")
                + "/" + startDateTrack.replaceAll(regex, "$2")
                + "/" + startDateTrack.replaceAll(regex, "$1")
                + " " + startDateTrack.replaceAll(regex, "$4")
                + ":" + startDateTrack.replaceAll(regex, "$5");

        endDateTrack = endDateTrack.replaceAll(regex, "$3")
                + "/" + endDateTrack.replaceAll(regex, "$2")
                + "/" + endDateTrack.replaceAll(regex, "$1")
                + " " + endDateTrack.replaceAll(regex, "$4")
                + ":" + endDateTrack.replaceAll(regex, "$5");

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String mmsi = request.getParameter("mmsi");
        
        FacesContext.getCurrentInstance().getExternalContext().redirect(Util.getPathUrl() + "admin/ais/track.xhtml?mmsi=" + mmsi + "&"
                + "start=" + startDateTrack + "&end=" + endDateTrack);
    }

}
