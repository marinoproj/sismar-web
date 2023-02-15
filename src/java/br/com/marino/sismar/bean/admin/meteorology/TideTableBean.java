package br.com.marino.sismar.bean.admin.meteorology;

import br.com.marino.api.tidetable.APITideTable;
import br.com.marino.api.tidetable.APITideTableException;
import br.com.marino.api.tidetable.Month;
import br.com.marino.api.tidetable.Port;
import br.com.marino.api.tidetable.TideTable;
import br.com.marino.api.tidetable.Year;
import br.com.marino.sismar.util.UAgentInfo;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "TideTableBean")
@ViewScoped
public class TideTableBean implements Serializable {

    private List<Port> listPorts;
    private List<Month> listMonth;
    private List<Year> listYear;

    private Port portSelected;
    private Month monthSelected;
    private Year yearSelected;

    private TideTable tideTable;

    private UAgentInfo uAgentInfo;

    @PostConstruct
    public void init() {

        uAgentInfo = new UAgentInfo((HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest());

        listPorts = Port.getListPorts();
        listMonth = Month.getListMonths();
        listYear = Year.getListYears();

        portSelected = Port.getListPorts().get(46);
        yearSelected = Year.getListYears().get(1);
        monthSelected = Month.getListMonths().get(Calendar.getInstance().get(Calendar.MONTH));

        try {
            reloadTideTable();
        } catch (APITideTableException ex) {
        }

    }

    @PreDestroy
    public void destroy() {
    }

    public boolean createRowTide(int index) {
        if (!uAgentInfo.detectMobileLong()) {
            return (index + 1) % 3 == 0;
        } else {
            return (index + 1) % 2 == 0;
        }
    }

    public TideTable getTideTable() {
        return tideTable;
    }

    public void setTideTable(TideTable tideTable) {
        this.tideTable = tideTable;
    }

    public void displayTideTable() {
        try {
            reloadTideTable();
        } catch (APITideTableException ex) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Não foi possível buscar as informações de maré!"));
            if (tideTable != null){
                portSelected = tideTable.getPort();
                monthSelected = tideTable.getMonth();
                yearSelected = tideTable.getYear();
            }
        }       
    }

    public void reloadTideTable() throws APITideTableException {
        tideTable = APITideTable.getTideTable(portSelected, yearSelected, monthSelected);
    }

    public List<Port> getListPorts() {
        return listPorts;
    }

    public void setListPorts(List<Port> listPorts) {
        this.listPorts = listPorts;
    }

    public List<Month> getListMonth() {
        return listMonth;
    }

    public void setListMonth(List<Month> listMonth) {
        this.listMonth = listMonth;
    }

    public List<Year> getListYear() {
        return listYear;
    }

    public void setListYear(List<Year> listYear) {
        this.listYear = listYear;
    }

    public Port getPortSelected() {
        return portSelected;
    }

    public void setPortSelected(Port portSelected) {
        this.portSelected = portSelected;
    }

    public Month getMonthSelected() {
        return monthSelected;
    }

    public void setMonthSelected(Month monthSelected) {
        this.monthSelected = monthSelected;
    }

    public Year getYearSelected() {
        return yearSelected;
    }

    public void setYearSelected(Year yearSelected) {
        this.yearSelected = yearSelected;
    }

    public String getValueFormatted(Double value) {
        if (value == null) {
            return "";
        }
        return Util.converterValue(value);
    }

    public String getHour(Date hour) {
        return Util.dateToString(hour, "HH:mm");
    }

    public String getDay(Date date) {

        String name = "";

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                name = "Domingo - ";
                break;
            case Calendar.MONDAY:
                name = "Segunda - ";
                break;
            case Calendar.TUESDAY:
                name = "Terça - ";
                break;
            case Calendar.WEDNESDAY:
                name = "Quarta - ";
                break;
            case Calendar.THURSDAY:
                name = "Quinta - ";
                break;
            case Calendar.FRIDAY:
                name = "Sexta - ";
                break;
            case Calendar.SATURDAY:
                name = "Sábado - ";
                break;
            default:
                break;
        }

        name += Util.dateToString(date, "dd/MM");
        return name;

    }

}
