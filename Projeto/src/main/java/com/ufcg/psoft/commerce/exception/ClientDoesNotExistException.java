package com.ufcg.psoft.commerce.exception;

public class ClientDoesNotExistException extends CommerceException {

    public ClientDoesNotExistException() {
        super("O cliente consultado nao existe!");
    }
    
}
