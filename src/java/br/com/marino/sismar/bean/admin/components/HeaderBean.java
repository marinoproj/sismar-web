package br.com.marino.sismar.bean.admin.components;

import br.com.marino.sismar.entity.Clientes;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;

@ManagedBean(name = "HeaderBean")
@ViewScoped
public class HeaderBean implements Serializable {

    public boolean isActiveFeature(String nameFeature) {
        return Util.isActiveFeature(nameFeature);
    }    
    
    public String getLogoClient() {
        
        Clientes client = SessionContext.getInstance().getClientLoggedIn();
        
        if (client.getLogo() == null) {
            return "/sismar/faces/javax.faces.resource/img/sem_imagem.png";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(client.getLogo());
        return sb.toString();
        
    }

}