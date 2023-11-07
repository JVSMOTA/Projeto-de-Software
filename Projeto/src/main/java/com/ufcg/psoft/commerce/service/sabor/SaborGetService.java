package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;
import com.ufcg.psoft.commerce.repository.*;
import com.ufcg.psoft.commerce.model.*;
import java.util.*;

@Service
public class SaborGetService implements SaborGetServiceInterface {

    @Autowired
    private SaborRepository saborRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public Sabor getOne(Long idSabor, Long idEstabelecimento, String codigoEstabelecimento) {
        Estabelecimento estabelecimentoEncontrado = this.estabelecimentoRepository
                                    .findById(idEstabelecimento)
                                    .orElseThrow(CommerceDoesNotExistException::new);
        Sabor saborEncontrado = this.saborRepository
                                .findById(idSabor)
                                .orElseThrow(FlavourDoesNotExistException::new);
        if (!estabelecimentoEncontrado.getCodigoEstabelecimento().equals(codigoEstabelecimento)) {
            throw new NotAuthorizedException();
        }
        return saborEncontrado;
    }

    @Override
    public Collection<Sabor> getAll() {
        List<Sabor> saboresEncontrados = this.saborRepository.findAll();
        return saboresEncontrados;
    }

}
