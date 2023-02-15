package br.com.marino.sismar.converters;

import br.com.marino.api.tidetable.Port;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(forClass=Port.class, value="portConverter")
public class PortConverter implements Converter{    
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {                
        if (value != null && !value.isEmpty()) {
            return (Port) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        
        if (value instanceof Port) {
            Port entity= (Port) value;
            if (entity instanceof Port && (entity.getCode()+"") != null) {
                component.getAttributes().put(entity.getCode()+"", entity);
                return entity.getCode()+"";
            }
        }
        
        return "";
       
    }
    
}
