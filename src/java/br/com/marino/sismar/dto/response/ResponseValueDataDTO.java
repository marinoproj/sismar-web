package br.com.marino.sismar.dto.response;

import java.time.LocalDateTime;

public class ResponseValueDataDTO {

    private Long id;
    private long externalId;
    private String value;
    private LocalDateTime dhReading;
    private ResponseDataDeviceDTO dataDevice;
    private LocalDateTime dhCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getDhReading() {
        return dhReading;
    }

    public void setDhReading(LocalDateTime dhReading) {
        this.dhReading = dhReading;
    }

    public ResponseDataDeviceDTO getDataDevice() {
        return dataDevice;
    }

    public void setDataDevice(ResponseDataDeviceDTO dataDevice) {
        this.dataDevice = dataDevice;
    }

    public LocalDateTime getDhCreate() {
        return dhCreate;
    }

    public void setDhCreate(LocalDateTime dhCreate) {
        this.dhCreate = dhCreate;
    }
    
}