package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.Poin;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Poin.class, value = "poinConverter")
public class PoinConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Poin) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Poin) {
            Poin entity = (Poin) value;
            if (entity instanceof Poin && (entity.getCodPoin() + "") != null) {
                component.getAttributes().put(entity.getCodPoin() + "", entity);
                return entity.getCodPoin() + "";
            }
        }

        return "";

    }

}
