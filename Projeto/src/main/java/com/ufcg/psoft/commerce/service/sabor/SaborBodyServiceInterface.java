package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.dto.sabordto.*;
import com.ufcg.psoft.commerce.model.Sabor;

public interface SaborBodyServiceInterface {

    public Sabor post(Long idEstabelecimento, String codigoEstabelecimento, SaborBodyDTO saborBodyDTO);

    public Sabor put(Long idEstabelecimento, String codigoEstabelecimento, Long idSabor, SaborBodyDTO saborBodyDTO);
    
}
