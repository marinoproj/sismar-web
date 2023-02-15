package br.com.marino.sismar.enums;

public enum LadoSensorProximidade {
    
    DIREITO("Direito"), ESQUERDO("Esquerdo");
    
    String value;
    
    private LadoSensorProximidade(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }    
    
}
