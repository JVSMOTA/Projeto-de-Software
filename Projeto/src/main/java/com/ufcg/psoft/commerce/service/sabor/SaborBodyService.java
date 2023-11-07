package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.dto.sabordto.*;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

@Service
public class SaborBodyService implements SaborBodyServiceInterface {

    @Autowired
    SaborRepository saborRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public Sabor post(Long idEstabelecimento, String codigoEstabelecimento, SaborBodyDTO saborBodyDTO) {
        Estabelecimento estabelecimento = this.estabelecimentoRepository
                                .findById(idEstabelecimento)
                                .orElseThrow(CommerceDoesNotExistException::new);
        if (!estabelecimento.getCodigoEstabelecimento().equals(codigoEstabelecimento)) {
            throw new NotAuthorizedException();
        }
        return this.saborRepository.save(
                Sabor.builder()
                        .nomeSabor(saborBodyDTO.getNomeSabor())
                        .tipoSabor(saborBodyDTO.getTipoSabor())
                        .valorMedioSabor(saborBodyDTO.getValorMedioSabor())
                        .valorGrandeSabor(saborBodyDTO.getValorGrandeSabor())
                        .disponivel(saborBodyDTO.getDisponivel())
                        .estabelecimento(estabelecimento)
                        .build());
    }

    @Override
    public Sabor put(Long idEstabelecimento, String codigoEstabelecimento, Long idSabor, SaborBodyDTO saborBodyDTO) {
        Estabelecimento estabelecimentoEncontrado = this.estabelecimentoRepository
                                .findById(idEstabelecimento)
                                .orElseThrow(CommerceDoesNotExistException::new);
        Sabor saborEncontrado = this.saborRepository
                                .findById(idSabor)
                                .orElseThrow(FlavourDoesNotExistException::new);
        if (!estabelecimentoEncontrado.getCodigoEstabelecimento().equals(codigoEstabelecimento)) {
            throw new NotAuthorizedException();
        }

        this.modelMapper.map(saborBodyDTO, saborEncontrado);
        return this.saborRepository.save(saborEncontrado);
    }

}
