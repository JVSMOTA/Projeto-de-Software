package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.exception.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ufcg.psoft.commerce.dto.sabordto.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;

@Service
public class ClienteDemonstrarInteresseService implements ClienteDemonstrarInteresseServiceInterface {

    @Autowired 
    ClienteRepository clienteRepository;
    
    @Autowired
    SaborRepository saborRepository;

    @Autowired
    ModelMapper modelMapper;

    public SaborBodyDTO demonstrarInteresseSabor(Long idCliente, String codigoCliente, Long idSabor) {
        Cliente clienteEncontrado = this.clienteRepository
                                    .findById(idCliente)
                                    .orElseThrow(ClientDoesNotExistException::new);

        if (!clienteEncontrado.getCodigoCliente().equals(codigoCliente)) {
            throw new NotAuthorizedException();
        }

        Sabor saborEncontrado = this.saborRepository
                                .findById(idSabor)
                                .orElseThrow(FlavourDoesNotExistException::new);

        if (saborEncontrado.getDisponivel()) {
            throw new FlavourAvailableException();
        }

        clienteEncontrado.getSaboresInteressados().add(saborEncontrado);
        saborEncontrado.getClientesInteressados().add(clienteEncontrado);
        this.saborRepository.save(saborEncontrado);
        this.clienteRepository.save(clienteEncontrado);
        return modelMapper.map(saborEncontrado, SaborBodyDTO.class);
    }

}
