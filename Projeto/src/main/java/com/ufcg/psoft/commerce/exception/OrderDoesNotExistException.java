package com.ufcg.psoft.commerce.exception;

public class OrderDoesNotExistException extends CommerceException {

    public OrderDoesNotExistException() {
        super("O pedido consultado nao existe!");
    }
    
}
