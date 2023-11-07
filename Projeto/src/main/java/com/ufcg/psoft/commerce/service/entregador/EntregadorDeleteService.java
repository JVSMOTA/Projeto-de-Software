package com.ufcg.psoft.commerce.service.entregador;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.Entregador;

@Service
public class EntregadorDeleteService implements EntregadorDeleteServiceInterface {

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Override
    public void delete(Long idEntregador, String codigoEntregador) {
        Entregador entregadorEncontrado = this.entregadorRepository
                                        .findById(idEntregador)
                                        .orElseThrow(DelivererDoesNotExistException::new);
                                        
        if (!entregadorEncontrado.getCodigoEntregador().equals(codigoEntregador)) {
            throw new NotAuthorizedException();
        }
        this.entregadorRepository.delete(entregadorEncontrado);
    }

}
