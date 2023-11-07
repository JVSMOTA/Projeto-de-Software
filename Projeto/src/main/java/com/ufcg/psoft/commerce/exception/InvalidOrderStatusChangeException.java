package com.ufcg.psoft.commerce.exception;

public class InvalidOrderStatusChangeException extends CommerceException {

    public InvalidOrderStatusChangeException() {
        super("Mudanca de status de pedido invalida!");
    }
    
}
