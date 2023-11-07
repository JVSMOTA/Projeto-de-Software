package com.ufcg.psoft.commerce.service.entregador;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;
import com.ufcg.psoft.commerce.repository.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.dto.entregadordto.*;
import org.modelmapper.ModelMapper;
import com.ufcg.psoft.commerce.exception.*;

@Service
public class EntregadorBodyService implements EntregadorBodyServiceInterface {

    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    AssociacaoRepository associacaoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Entregador post(EntregadorBodyDTO entregadorBodyDTO) {
        Entregador entregador = this.modelMapper.map(entregadorBodyDTO, Entregador.class);
        return this.entregadorRepository.save(entregador);
    }

    @Override
    public Entregador put(Long idEntregador, String codigoEntregador, EntregadorBodyDTO entregadorBodyDTO) {
        Entregador entregadorEncontrado = this.entregadorRepository
                                        .findById(idEntregador)
                                        .orElseThrow(DelivererDoesNotExistException::new);
                                        
        if (!entregadorEncontrado.getCodigoEntregador().equals(codigoEntregador)) {
            throw new NotAuthorizedException();
        }
        this.modelMapper.map(entregadorBodyDTO, entregadorEncontrado);
        return this.entregadorRepository.save(entregadorEncontrado);
    }

    @Override
    public Entregador alteraDisponibilidade(Long idEntregador, String codigoEntregador, Boolean disponibilidade) {
        Entregador entregadorEncontrado = this.entregadorRepository
                                        .findById(idEntregador)
                                        .orElseThrow(DelivererDoesNotExistException::new);

        if (!entregadorEncontrado.getCodigoEntregador().equals(codigoEntregador)) {
            throw new NotAuthorizedException();
        }

        entregadorEncontrado.setDisponibilidade(disponibilidade);

        return this.entregadorRepository.save(entregadorEncontrado);
    }

}
