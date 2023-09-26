package br.com.marino.sismar.converters;

import br.com.marino.sismar.dto.response.ResponseDeviceDTO;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = ResponseDeviceDTO.class, value = "deviceConverter")
public class DeviceConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.isEmpty()) {
            return (ResponseDeviceDTO) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        if (value instanceof ResponseDeviceDTO) {
            ResponseDeviceDTO entity = (ResponseDeviceDTO) value;
            if (entity instanceof ResponseDeviceDTO && (entity.getId() + "") != null) {
                component.getAttributes().put(entity.getId() + "", entity);
                return entity.getId() + "";
            }
        }
        return "";
    }

}