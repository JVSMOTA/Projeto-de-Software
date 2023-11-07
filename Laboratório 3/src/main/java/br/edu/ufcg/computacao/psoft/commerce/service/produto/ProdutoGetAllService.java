package br.edu.ufcg.computacao.psoft.commerce.service.produto;

import br.edu.ufcg.computacao.psoft.commerce.model.Produto;

import java.util.List;

@FunctionalInterface
public interface ProdutoGetAllService {
    public List<Produto> getAll();
}
