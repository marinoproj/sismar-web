package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.Pratico;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Pratico.class, value = "praticoConverter")
public class PraticoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Pratico) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Pratico) {
            Pratico entity = (Pratico) value;
            if (entity instanceof Pratico && (entity.getCodPratico() + "") != null) {
                component.getAttributes().put(entity.getCodPratico() + "", entity);
                return entity.getCodPratico() + "";
            }
        }

        return "";

    }

}
