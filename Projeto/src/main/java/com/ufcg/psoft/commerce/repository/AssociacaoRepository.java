package com.ufcg.psoft.commerce.repository;

import com.ufcg.psoft.commerce.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociacaoRepository extends JpaRepository<Associacao, Long> {

    Associacao findByEstabelecimentoAndEntregador(Estabelecimento estabelecimento, Entregador entregador);
    
}
