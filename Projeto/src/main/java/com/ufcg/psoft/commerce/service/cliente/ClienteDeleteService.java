package com.ufcg.psoft.commerce.service.cliente;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.Cliente;

@Service
public class ClienteDeleteService implements ClienteDeleteServiceInterface {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void delete(Long idCliente, String codigoCliente) {
        Cliente clienteEncontrado = this.clienteRepository
                                    .findById(idCliente)
                                    .orElseThrow(ClientDoesNotExistException::new);
                                    
        if (!clienteEncontrado.getCodigoCliente().equals(codigoCliente)) {
            throw new NotAuthorizedException();
        }
        this.clienteRepository.delete(clienteEncontrado);
    }

}
