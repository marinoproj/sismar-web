package br.com.marino.sismar.converters;

import br.com.marino.api.tidetable.Month;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(forClass=Month.class, value="monthConverter")
public class MonthConverter implements Converter{    
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {                
        if (value != null && !value.isEmpty()) {
            return (Month) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        
        if (value instanceof Month) {
            Month entity= (Month) value;
            if (entity instanceof Month && entity.getCode() != null) {
                component.getAttributes().put(entity.getCode(), entity);
                return entity.getCode();
            }
        }
        
        return "";
       
    }
    
}
