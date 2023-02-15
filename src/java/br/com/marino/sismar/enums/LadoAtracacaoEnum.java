package br.com.marino.sismar.enums;

public enum LadoAtracacaoEnum {
    
    BOMBORDO("Bombordo"), BORESTE("Boreste");
    
    String value;
    
    private LadoAtracacaoEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }    
    
}
