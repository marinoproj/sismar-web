package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.Mercadoria;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Mercadoria.class, value = "merchandiseConverter")
public class merchandiseConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Mercadoria) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Mercadoria) {
            Mercadoria entity = (Mercadoria) value;
            if (entity instanceof Mercadoria && (entity.getCodMercadoria() + "") != null) {
                component.getAttributes().put(entity.getCodMercadoria() + "", entity);
                return entity.getCodMercadoria() + "";
            }
        }

        return "";

    }

}
