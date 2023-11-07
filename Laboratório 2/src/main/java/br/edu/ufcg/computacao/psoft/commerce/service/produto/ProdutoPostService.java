package br.edu.ufcg.computacao.psoft.commerce.service.produto;

import br.edu.ufcg.computacao.psoft.commerce.dto.produto.ProdutoPostDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;

@FunctionalInterface
public interface ProdutoPostService {
    public Produto post(ProdutoPostDTO produtoPostDTO);
}
