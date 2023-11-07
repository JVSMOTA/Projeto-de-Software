package com.ufcg.psoft.commerce.service.cliente;

@FunctionalInterface
public interface ClienteDeleteServiceInterface {

    public void delete(Long idCliente, String codigoCliente);
    
}
