package com.ufcg.psoft.commerce.exception;

public class CommerceDoesNotExistException extends CommerceException {

    public CommerceDoesNotExistException() {
        super("O estabelecimento consultado nao existe!");
    }
    
}
