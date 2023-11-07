package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.dto.estabelecimentodto.*;
import com.ufcg.psoft.commerce.model.Estabelecimento;

public interface EstabelecimentoBodyServiceInterface {

    public Estabelecimento post(EstabelecimentoBodyDTO estabelecimentoBodyDTO);

    public Estabelecimento put(Long idEstabelecimento, EstabelecimentoBodyDTO estabelecimentoBodyDTO);
    
}
