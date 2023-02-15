package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.Equipamentos;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Equipamentos.class, value = "equipamentoConverter")
public class EquipamentoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (Equipamentos) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof Equipamentos) {
            Equipamentos entity = (Equipamentos) value;
            if (entity instanceof Equipamentos && (entity.getCodEquipamento() + "") != null) {
                component.getAttributes().put(entity.getCodEquipamento()+ "", entity);
                return entity.getCodEquipamento()+ "";
            }
        }

        return "";

    }

}
