package br.com.marino.sismar.dto.response;

import java.time.LocalDateTime;

public class ResponseDeviceDTO {

    private Long id;
    private long externalId;
    private String externalName;
    private String label;
    private String description;
    private boolean enabled;
    private boolean connected;
    private ResponseConnectorDTO connector;
    private String latitudeLocation;
    private String longitudeLocation;
    private Long readingTime;
    private Boolean display;
    private Boolean startAutomatically;
    private LocalDateTime dhExclude;

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

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public ResponseConnectorDTO getConnector() {
        return connector;
    }

    public void setConnector(ResponseConnectorDTO connector) {
        this.connector = connector;
    }

    public String getLatitudeLocation() {
        return latitudeLocation;
    }

    public void setLatitudeLocation(String latitudeLocation) {
        this.latitudeLocation = latitudeLocation;
    }

    public String getLongitudeLocation() {
        return longitudeLocation;
    }

    public void setLongitudeLocation(String longitudeLocation) {
        this.longitudeLocation = longitudeLocation;
    }

    public Long getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(Long readingTime) {
        this.readingTime = readingTime;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Boolean getStartAutomatically() {
        return startAutomatically;
    }

    public void setStartAutomatically(Boolean startAutomatically) {
        this.startAutomatically = startAutomatically;
    }

    public LocalDateTime getDhExclude() {
        return dhExclude;
    }

    public void setDhExclude(LocalDateTime dhExclude) {
        this.dhExclude = dhExclude;
    }   

}