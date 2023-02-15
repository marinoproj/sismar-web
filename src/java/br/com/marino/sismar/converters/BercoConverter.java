package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.Berco;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Berco.class, value = "bercoConverter")
public class BercoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Berco) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Berco) {
            Berco entity = (Berco) value;
            if (entity instanceof Berco && (entity.getCodBerco() + "") != null) {
                component.getAttributes().put(entity.getCodBerco() + "", entity);
                return entity.getCodBerco() + "";
            }
        }

        return "";

    }

}
