package com.ufcg.psoft.commerce.service.associacao;

import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

@Service
public class AssociacaoService implements AssociacaoServiceInterface {

    @Autowired
    AssociacaoRepository associacaoRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public Associacao associarEntregadorEstabelecimento(String codigoEntregador, Long idEstabelecimento, Long idEntregador) {
        Estabelecimento estabelecimentoEncontrado = this.estabelecimentoRepository
                                    .findById(idEstabelecimento)
                                    .orElseThrow(CommerceDoesNotExistException::new);

        Entregador entregadorEncontrado = this.entregadorRepository
                                    .findById(idEntregador)
                                    .orElseThrow(DelivererDoesNotExistException::new);

        if (entregadorEncontrado.getCodigoEntregador().equals(codigoEntregador)) {
            Associacao associacao = Associacao.builder()
                    .entregador(entregadorEncontrado)
                    .estabelecimento(estabelecimentoEncontrado)
                    .statusAssociacao(false)
                    .build();
            return this.associacaoRepository.save(associacao);
        }
        throw new NotAuthorizedException();
    }

    @Override
    public Associacao aceitarAssociacao(String codigoEstabelecimento, Long idEstabelecimento, Long idEntregador) {
        Estabelecimento estabelecimentoEncontrado = this.estabelecimentoRepository
                                    .findById(idEstabelecimento)
                                    .orElseThrow(CommerceDoesNotExistException::new);

        Entregador entregadorEncontrado = this.entregadorRepository
                                    .findById(idEntregador)
                                    .orElseThrow(DelivererDoesNotExistException::new);

        if (estabelecimentoEncontrado.getCodigoEstabelecimento().equals(codigoEstabelecimento)) {
            Associacao associacao = this.associacaoRepository
                        .findByEstabelecimentoAndEntregador(estabelecimentoEncontrado, entregadorEncontrado);
            associacao.setStatusAssociacao(true);
            associacao.getEntregador().setDisponibilidade(false);
            return this.associacaoRepository.save(associacao);
        }
        throw new NotAuthorizedException();
    }

}
