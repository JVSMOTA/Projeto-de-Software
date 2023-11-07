package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.dto.sabordto.SaborAlterarDisponivelDTO;
import com.ufcg.psoft.commerce.exception.CommerceDoesNotExistException;
import com.ufcg.psoft.commerce.exception.FlavourDoesNotExistException;
import com.ufcg.psoft.commerce.exception.NotAuthorizedException;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class SaborAlterarDisponibilidadeServiceImpl implements SaborAlterarDisponibilidadeService{
    @Autowired
    SaborRepository saborRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    SaborNotificarService saborNotificarService;

    @Override
    public Sabor alterar(Long idSabor, String codigoEstabelecimento, SaborAlterarDisponivelDTO saborAlterarDisponivelDTO) {
        Sabor sabor = saborRepository
                        .findById(idSabor)
                        .orElseThrow(FlavourDoesNotExistException::new);
        Estabelecimento estabelecimento = estabelecimentoRepository
                        .findById(sabor.getEstabelecimento().getIdEstabelecimento())
                        .orElseThrow(CommerceDoesNotExistException::new);

        if (!estabelecimento.getCodigoEstabelecimento().equals(codigoEstabelecimento)) {
            throw new NotAuthorizedException();
        }

        sabor.setDisponivel(saborAlterarDisponivelDTO.isDisponivel());
        
        if (sabor.getDisponivel()) {
            saborNotificarService.notificar(idSabor);
        }

        return saborRepository.save(sabor);
    }
}
