package br.com.marino.sismar.enums;

public enum TipoSensoresAproximacao {
    
    FIXO("Fixo"), PORTATIL("Portátil");
    
    String value;
    
    private TipoSensoresAproximacao(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }    
    
}
