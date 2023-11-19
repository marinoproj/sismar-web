package br.com.marino.sismar.enums;

public enum TipoEquipamentoEnum {
    
    VENTO("vento"), CORRENTE("corrente");
    
    String value;
    
    private TipoEquipamentoEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }    
    
}
