package com.ufcg.psoft.commerce.service.sabor;

import java.util.List;

@FunctionalInterface
public interface SaborNotificarService {

    public List<String> notificar(Long idSabor);
    
}
