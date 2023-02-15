package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.Area;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Area.class, value = "areaConverter")
public class AreaConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Area) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Area) {
            Area entity = (Area) value;
            if (entity instanceof Area && (entity.getCodArea() + "") != null) {
                component.getAttributes().put(entity.getCodArea() + "", entity);
                return entity.getCodArea() + "";
            }
        }

        return "";

    }

}
