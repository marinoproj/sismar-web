package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.Giaont;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Giaont.class, value = "giaontConverter")
public class GiaontConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Giaont) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Giaont) {
            Giaont entity = (Giaont) value;
            if (entity instanceof Giaont && (entity.getCodGiaont() + "") != null) {
                component.getAttributes().put(entity.getCodGiaont() + "", entity);
                return entity.getCodGiaont() + "";
            }
        }

        return "";

    }

}
