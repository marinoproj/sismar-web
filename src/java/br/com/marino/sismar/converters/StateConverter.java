package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.State;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(forClass=State.class, value="stateConverter")
public class StateConverter implements Converter{    
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {                
        if (value != null && !value.isEmpty()) {
            return (State) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        
        if (value instanceof State) {
            State entity= (State) value;
            if (entity instanceof State && entity.getAbv() != null) {
                component.getAttributes().put(entity.getAbv(), entity);
                return entity.getAbv();
            }
        }
        return "";
       
    }
    
}
