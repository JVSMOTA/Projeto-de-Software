package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.dto.sabordto.*;

public interface ClienteDemonstrarInteresseServiceInterface {

    public SaborBodyDTO demonstrarInteresseSabor(Long idCliente, String codigoCliente, Long idSabor);
    
}
