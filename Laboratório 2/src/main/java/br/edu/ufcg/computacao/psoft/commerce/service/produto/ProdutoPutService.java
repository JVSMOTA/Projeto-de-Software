package br.edu.ufcg.computacao.psoft.commerce.service.produto;

import br.edu.ufcg.computacao.psoft.commerce.dto.produto.ProdutoPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;

@FunctionalInterface
public interface ProdutoPutService {
    public Produto put(Long id, ProdutoPutDTO produtoPutDTO);
}
