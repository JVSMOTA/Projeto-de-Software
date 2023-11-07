package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;
import com.ufcg.psoft.commerce.repository.*;

@Service
public class SaborDeleteService implements SaborDeleteServiceInterface {

    @Autowired
    private SaborRepository saborRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;
    
    @Override
    public void delete(Long idSabor, Long idEstabelecimento, String codigoEstabelecimento) {
        Estabelecimento estabelecimentoEncontrado = this.estabelecimentoRepository
                                .findById(idEstabelecimento)
                                .orElseThrow(CommerceDoesNotExistException::new);
        Sabor saborEncontrado = this.saborRepository
                                .findById(idSabor)
                                .orElseThrow(FlavourDoesNotExistException::new);
        if (!estabelecimentoEncontrado.getCodigoEstabelecimento().equals(codigoEstabelecimento)) {
            throw new NotAuthorizedException();
        }
        this.saborRepository.delete(saborEncontrado);
    }

}
