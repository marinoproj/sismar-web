package br.com.marino.sismar.service;

import br.com.marino.sismar.dto.response.ResponseDeviceDTO;
import br.com.marino.sismar.util.LocalDateTimeDeserializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.openunirest.http.Unirest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class DeviceService {

    private static final String REST_API = "http://localhost:10005";

    public static List<ResponseDeviceDTO> getDevices() {

        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        
        objectMapper.registerModule(module);
        
        String response = Unirest
                .get(REST_API + "/v1/device")
                .asString().getBody();

        try {
            System.out.println(response);
            return objectMapper.readValue(response, new TypeReference<List<ResponseDeviceDTO>>(){});
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return null;

    }

}
