package com.ufcg.psoft.commerce.service.cliente;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.dto.clientedto.*;
import org.modelmapper.ModelMapper;
import com.ufcg.psoft.commerce.exception.*;

@Service
public class ClienteBodyService implements ClienteBodyServiceInterface {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Cliente post(ClienteBodyDTO clienteBodyDTO) {
        Cliente cliente = this.modelMapper.map(clienteBodyDTO, Cliente.class);
        return this.clienteRepository.save(cliente);
    }

    @Override
    public Cliente put(Long idCliente, String codigoCliente, ClienteBodyDTO clienteBodyDTO) {
        Cliente clienteEncontrado = this.clienteRepository
                                    .findById(idCliente)
                                    .orElseThrow(ClientDoesNotExistException::new);
                                    
        if (!clienteEncontrado.getCodigoCliente().equals(codigoCliente)) {
            throw new NotAuthorizedException();
        }
        this.modelMapper.map(clienteBodyDTO, clienteEncontrado);
        return this.clienteRepository.save(clienteEncontrado);
    }
    
}
