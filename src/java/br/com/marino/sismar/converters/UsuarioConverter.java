package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.UsuariosWeb;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = UsuariosWeb.class, value = "usuarioConverter")
public class UsuarioConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (UsuariosWeb) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof UsuariosWeb) {
            UsuariosWeb entity = (UsuariosWeb) value;
            if (entity instanceof UsuariosWeb && (entity.getIdUsuario() + "") != null) {
                component.getAttributes().put(entity.getIdUsuario() + "", entity);
                return entity.getIdUsuario() + "";
            }
        }

        return "";

    }

}
