package br.com.marino.sismar.bean.admin.components;

import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "FooterBean")
@ViewScoped
public class FooterBean implements Serializable {

    public boolean isActiveFeature(String nameFeature) {
        return Util.isActiveFeature(nameFeature);
    }

}