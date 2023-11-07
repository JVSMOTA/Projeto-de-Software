package com.ufcg.psoft.commerce.exception;

public class FlavourAvailableException extends CommerceException{

    public FlavourAvailableException() {
        super("O sabor consultado ja esta disponivel!");
    }
    
}
