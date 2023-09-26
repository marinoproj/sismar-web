package br.com.marino.sismar.dto.response;

import java.time.LocalDateTime;

public class ResponseDataDeviceDTO implements Comparable<ResponseDataDeviceDTO> {

    private Long id;
    private long externalId;
    private String externalName;
    private String label;
    private String description;
    private boolean enabled;
    private ResponseDeviceDTO device;
    private Long memorySize;
    private String memoryType;
    private Long memoryAddress;
    private String valueFormatType;
    private ResponseValueDataDTO currentValueData;
    private Boolean display;
    private boolean readOnly;
    private long readWriteMode;
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

    public ResponseDeviceDTO getDevice() {
        return device;
    }

    public void setDevice(ResponseDeviceDTO device) {
        this.device = device;
    }

    public Long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(Long memorySize) {
        this.memorySize = memorySize;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    public Long getMemoryAddress() {
        return memoryAddress;
    }

    public void setMemoryAddress(Long memoryAddress) {
        this.memoryAddress = memoryAddress;
    }

    public String getValueFormatType() {
        return valueFormatType;
    }

    public void setValueFormatType(String valueFormatType) {
        this.valueFormatType = valueFormatType;
    }

    public ResponseValueDataDTO getCurrentValueData() {
        return currentValueData;
    }

    public void setCurrentValueData(ResponseValueDataDTO currentValueData) {
        this.currentValueData = currentValueData;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public long getReadWriteMode() {
        return readWriteMode;
    }

    public void setReadWriteMode(long readWriteMode) {
        this.readWriteMode = readWriteMode;
    }

    public LocalDateTime getDhExclude() {
        return dhExclude;
    }

    public void setDhExclude(LocalDateTime dhExclude) {
        this.dhExclude = dhExclude;
    }   

    @Override
    public String toString() {
        return "ResponseDataDeviceDTO{" + "id=" + id + ", externalId=" + externalId + ", externalName=" + externalName + ", label=" + label + ", description=" + description + ", enabled=" + enabled + ", device=" + device + ", memorySize=" + memorySize + ", memoryType=" + memoryType + ", memoryAddress=" + memoryAddress + ", valueFormatType=" + valueFormatType + ", currentValueData=" + currentValueData + ", display=" + display + ", readOnly=" + readOnly + ", readWriteMode=" + readWriteMode + ", dhExclude=" + dhExclude + '}';
    }   

    @Override
    public int compareTo(ResponseDataDeviceDTO o) {
        return this.memoryAddress.intValue() - o.getMemoryAddress().intValue();
    }

}