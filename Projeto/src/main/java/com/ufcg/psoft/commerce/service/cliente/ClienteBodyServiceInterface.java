package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.dto.clientedto.*;

public interface ClienteBodyServiceInterface {

    public Cliente post(ClienteBodyDTO clienteBodyDTO);

    public Cliente put(Long idCliente, String codigoCliente, ClienteBodyDTO clienteBodyDTO);
    
}
