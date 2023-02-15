package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.Berco;
import br.com.marino.sismar.entity.Navio;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Berco.class, value = "navioConverter")
public class NavioConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Navio) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Navio) {
            Navio entity = (Navio) value;
            if (entity instanceof Navio && (entity.getCodNavio() + "") != null) {
                component.getAttributes().put(entity.getCodNavio() + "", entity);
                return entity.getCodNavio() + "";
            }
        }

        return "";

    }

}
