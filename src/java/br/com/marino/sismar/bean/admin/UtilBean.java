package br.com.marino.sismar.bean.admin;

import br.com.marino.sismar.entity.EtapasOperacao;
import br.com.marino.sismar.entity.Interrupcoes;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "UtilBean")
@ViewScoped
public class UtilBean implements Serializable {

    @PostConstruct
    public void init() {
    }

    @PreDestroy
    public void destroy() {
    }

    public String getDateTimeClient(Date dateUtc) {
        if (dateUtc == null) {
            return "";
        }
        return new SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(Util.getDateTimeClient(dateUtc));

    }
    
    public String getDateTimeClient(Date dateUtc, String format) {
        if (dateUtc == null) {
            return "";
        }
        return new SimpleDateFormat(format)
                .format(Util.getDateTimeClient(dateUtc));

    }

}
