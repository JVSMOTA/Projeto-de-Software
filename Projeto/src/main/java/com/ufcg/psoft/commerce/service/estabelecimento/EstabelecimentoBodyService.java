package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.dto.estabelecimentodto.*;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

@Service
public class EstabelecimentoBodyService implements EstabelecimentoBodyServiceInterface {

    @Autowired
    private final EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public EstabelecimentoBodyService(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    @Override
    public Estabelecimento post(EstabelecimentoBodyDTO estabelecimentoBodyDTO) {
        return this.estabelecimentoRepository.save(
                Estabelecimento.builder()
                        .nomeEstabelecimento(estabelecimentoBodyDTO.getNomeEstabelecimento())
                        .codigoEstabelecimento(estabelecimentoBodyDTO.getCodigoEstabelecimento())
                        .build());
    }

    @Override
    public Estabelecimento put(Long idEstabelecimento, EstabelecimentoBodyDTO estabelecimentoBodyDTO) {
        Estabelecimento estabelecimentoEncontrado = this.estabelecimentoRepository
                                    .findById(idEstabelecimento)
                                    .orElseThrow(CommerceDoesNotExistException::new);
        
        this.modelMapper.map(estabelecimentoBodyDTO, estabelecimentoEncontrado);
        return this.estabelecimentoRepository.save(estabelecimentoEncontrado);
    }

}
