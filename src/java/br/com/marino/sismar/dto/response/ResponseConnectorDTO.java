package br.com.marino.sismar.dto.response;

import java.time.LocalDateTime;

public class ResponseConnectorDTO {

    private Long id;
    private long contractId;
    private long externalId;
    private String externalName;
    private String label;
    private String description;
    private boolean enabled;
    private boolean connected;
    private LocalDateTime dhExclude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
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

    public LocalDateTime getDhExclude() {
        return dhExclude;
    }

    public void setDhExclude(LocalDateTime dhExclude) {
        this.dhExclude = dhExclude;
    }
    
}