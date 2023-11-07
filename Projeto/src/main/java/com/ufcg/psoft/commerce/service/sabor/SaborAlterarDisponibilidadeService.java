package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.dto.sabordto.SaborAlterarDisponivelDTO;
import com.ufcg.psoft.commerce.model.Sabor;

public interface SaborAlterarDisponibilidadeService {
    
    public Sabor alterar(Long idSabor, String codigoEstabelecimento, SaborAlterarDisponivelDTO saborAlterarDisponivelDTO);

}
