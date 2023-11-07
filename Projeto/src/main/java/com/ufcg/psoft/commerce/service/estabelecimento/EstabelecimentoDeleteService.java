package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

@Service
public class EstabelecimentoDeleteService implements EstabelecimentoDeleteServiceInterface {

    @Autowired
    private final EstabelecimentoRepository estabelecimentoRepository;

    public EstabelecimentoDeleteService(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    @Override
    public void delete(Long idEstabelecimento) {
        this.estabelecimentoRepository.deleteById(idEstabelecimento);
    }

}
