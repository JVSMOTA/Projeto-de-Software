package com.ufcg.psoft.commerce.service.sabor;

@FunctionalInterface
public interface SaborDeleteServiceInterface {

    public void delete(Long idSabor, Long idEstabelecimento, String codigoEstabelecimento);
    
}
