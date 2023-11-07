package com.ufcg.psoft.commerce.service.entregador;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.Entregador;
import java.util.*;

@Service
public class EntregadorGetService implements EntregadorGetServiceInterface {

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Override
    public Entregador getOne(Long idEntregador) {
        Entregador entregadorEncontrado = this.entregadorRepository
                                        .findById(idEntregador)
                                        .orElseThrow(DelivererDoesNotExistException::new);
                                        
        return entregadorEncontrado;
    }

    @Override
    public Collection<Entregador> getAll() {
        List<Entregador> entregadoresEncontrados = this.entregadorRepository.findAll();
        return entregadoresEncontrados;
    }

}
