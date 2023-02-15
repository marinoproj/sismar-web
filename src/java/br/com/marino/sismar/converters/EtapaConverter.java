package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.Etapas;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Etapas.class, value = "etapaConverter")
public class EtapaConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Etapas) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Etapas) {
            Etapas entity = (Etapas) value;
            if (entity instanceof Etapas && (entity.getCod() + "") != null) {
                component.getAttributes().put(entity.getCod() + "", entity);
                return entity.getCod() + "";
            }
        }

        return "";

    }

}
