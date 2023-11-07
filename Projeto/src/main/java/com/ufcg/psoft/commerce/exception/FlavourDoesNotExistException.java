package com.ufcg.psoft.commerce.exception;

public class FlavourDoesNotExistException extends CommerceException{

    public FlavourDoesNotExistException() {
        super("O sabor consultado nao existe!");
    }
    
}
