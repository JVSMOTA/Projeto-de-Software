package com.ufcg.psoft.commerce.service.associacao;

import com.ufcg.psoft.commerce.model.Associacao;

public interface AssociacaoServiceInterface {

    public Associacao associarEntregadorEstabelecimento(String codigoEntregador, Long idEstabelecimento, Long idEntregador);

    public Associacao aceitarAssociacao(String codigoEstabelecimento, Long idEstabelecimento, Long idEntregador);
}
