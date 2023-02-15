package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.Cais;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Cais.class, value = "caisConverter")
public class CaisConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Cais) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Cais) {
            Cais entity = (Cais) value;
            if (entity instanceof Cais && (entity.getCodCais() + "") != null) {
                component.getAttributes().put(entity.getCodCais()+ "", entity);
                return entity.getCodCais()+ "";
            }
        }

        return "";

    }

}
