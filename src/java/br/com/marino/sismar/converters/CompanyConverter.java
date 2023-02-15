package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.Area;
import br.com.marino.sismar.entity.Empresa;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Empresa.class, value = "companyConverter")
public class CompanyConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Empresa) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Empresa) {
            Empresa entity = (Empresa) value;
            if (entity instanceof Empresa && (entity.getCodEmpresa() + "") != null) {
                component.getAttributes().put(entity.getCodEmpresa() + "", entity);
                return entity.getCodEmpresa() + "";
            }
        }

        return "";

    }

}
