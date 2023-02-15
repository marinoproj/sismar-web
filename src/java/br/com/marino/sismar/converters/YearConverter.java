package br.com.marino.sismar.converters;

import br.com.marino.api.tidetable.Year;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Year.class, value = "yearConverter")
public class YearConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Year) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Year) {
            Year entity = (Year) value;
            if (entity instanceof Year && entity.getYear() + "" != null) {
                component.getAttributes().put(entity.getYear() + "", entity);
                return entity.getYear() + "";
            }
        }

        return "";

    }

}
