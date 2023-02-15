package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.City;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(forClass=City.class, value="cityConverter")
public class CityConverter implements Converter{    
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {                
        if (value != null && !value.isEmpty()) {
            return (City) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        
        if (value instanceof City) {
            City entity= (City) value;
            if (entity instanceof City && entity.getId() != null) {
                component.getAttributes().put(entity.getId().toString(), entity);
                return entity.getId().toString();
            }
        }
        return "";
       
    }
    
}
