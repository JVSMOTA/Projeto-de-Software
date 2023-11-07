package com.ufcg.psoft.commerce.exception;

public class FlavourUnavailableException extends CommerceException{

    public FlavourUnavailableException() {
        super("O sabor consultado ja esta indisponivel!");
    }
    
}
