package com.ufcg.psoft.commerce.exception;

public class DelivererDoesNotExistException extends CommerceException {

    public DelivererDoesNotExistException() {
        super("O entregador consultado nao existe!");
    }
    
}
