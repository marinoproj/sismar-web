package br.com.marino.sismar.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private final DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
        if (p == null || p.getValueAsString() == null || p.getValueAsString().isEmpty()){
            return null;
        }
        return LocalDateTime.parse(p.getValueAsString(), fmt);
    }

}
