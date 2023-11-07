package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.model.Cliente;
import java.util.*;

public interface ClienteGetServiceInterface {

    public Cliente getOne(Long idCliente);

    public Collection<Cliente> getAll();
    
}
