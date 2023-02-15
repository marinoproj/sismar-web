package br.com.marino.sismar.converters;

import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Date.class, value = "dateTimeConverter")
public class DateTimeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date data = null;        

        try {

            if (value == null || value.isEmpty()) {
                return null;
            }
            
            value = value.replace("T", " ");                        
            format.setLenient(false);
            data = format.parse(value);            

            UsuariosWeb user = SessionContext.getInstance().getUserLoggedIn();
            ZoneId timeZone = ZoneId.of(user.getTimeZone());
            
            data = Util.getDateUTC(data, 
                    TimeZone.getTimeZone(timeZone));
            
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ConverterException("Data invalida");

        } catch (Exception e) { 
            e.printStackTrace();
            throw new ConverterException("Data invalida");
        }

        return data;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value == null) {
            return "";
        }
        
        Date dateUtc = (Date) value;
        dateUtc = Util.getDateTimeClient(dateUtc);
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");        
        return format.format(dateUtc);

    }

}
