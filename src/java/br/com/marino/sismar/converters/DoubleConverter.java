package br.com.marino.sismar.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Double.class, value = "doubleConverter")
public class DoubleConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {

        try {

            if (value == null || value.isEmpty()) {
                return null;
            }
            
            return Double.parseDouble(value);
            
        } catch (Exception e) {           
            throw new ConverterException("Valor inválido");
        }

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value == null) {
            return "";
        }
        
        return value + "";

    }

}
