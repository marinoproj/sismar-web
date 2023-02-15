package br.com.marino.sismar.enums;

public enum ModoEncerrarAtracacao {

    AUTOMATICO("Automático: Assim que parar de receber marcações dos sensores"),
    SEMI_AUTOMATICO("Semi-Automático: Assim que parar de receber marcações e a embarcação estiver atracado"),
    MANUAL("Manual: Somente após a confirmação do usuário");
	
    String value;
    
    private ModoEncerrarAtracacao(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }    
	
}