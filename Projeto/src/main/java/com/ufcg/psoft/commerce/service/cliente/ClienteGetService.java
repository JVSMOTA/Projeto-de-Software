package com.ufcg.psoft.commerce.service.cliente;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.Cliente;
import java.util.*;

@Service
public class ClienteGetService implements ClienteGetServiceInterface {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente getOne(Long idCliente) {
        Cliente clienteEncontrado = this.clienteRepository
                                    .findById(idCliente)
                                    .orElseThrow(ClientDoesNotExistException::new);
                                    
        return clienteEncontrado;
    }

    @Override
    public Collection<Cliente> getAll() {
        List<Cliente> clientesEncontrados = this.clienteRepository.findAll();
        return clientesEncontrados;
    }

}
