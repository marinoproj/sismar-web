package br.com.marino.sismar.enums;

public enum StatusOperacaoEnum {

    ENCERRADO("Encerrado"), ATRACANDO("Atracando"), ATRACADO("Atracado"), PARTINDO("Partindo");
    
    String value;
    
    private StatusOperacaoEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }    
	
}