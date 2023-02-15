package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.Eventos;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Eventos.class, value = "eventoConverter")
public class EventoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Eventos) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Eventos) {
            Eventos entity = (Eventos) value;
            if (entity instanceof Eventos && (entity.getCod() + "") != null) {
                component.getAttributes().put(entity.getCod() + "", entity);
                return entity.getCod() + "";
            }
        }

        return "";

    }

}
