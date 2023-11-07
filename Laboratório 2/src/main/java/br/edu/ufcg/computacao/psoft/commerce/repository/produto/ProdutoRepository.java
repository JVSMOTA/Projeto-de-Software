package br.edu.ufcg.computacao.psoft.commerce.repository.produto;

import br.edu.ufcg.computacao.psoft.commerce.model.Produto;

import java.util.List;

public interface ProdutoRepository {
    Produto add(Produto produto);
    Produto update(Long id, Produto produto);
    void delete(Long id);
    Produto getOne(Long id);
    List<Produto> getAll();


}
