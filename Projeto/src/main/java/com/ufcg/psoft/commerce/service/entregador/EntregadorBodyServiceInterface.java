package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.dto.entregadordto.*;

public interface EntregadorBodyServiceInterface {

    public Entregador post(EntregadorBodyDTO entregadorBodyDTO);

    public Entregador put(Long idEntregador, String codigoEntregador, EntregadorBodyDTO entregadorBodyDTO);

    public Entregador alteraDisponibilidade(Long idEntregador, String codigoEntregador, Boolean disponibilidade);
    
}
