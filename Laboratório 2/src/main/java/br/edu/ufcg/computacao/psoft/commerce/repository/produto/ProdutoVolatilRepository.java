package br.edu.ufcg.computacao.psoft.commerce.repository.produto;

import br.edu.ufcg.computacao.psoft.commerce.exception.PessoaNaoExisteException;
import br.edu.ufcg.computacao.psoft.commerce.exception.ProdutoNaoExisteException;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProdutoVolatilRepository implements ProdutoRepository {

    private Map<Long, Produto> produtos;
    private Long nextId;

    public ProdutoVolatilRepository() {
        produtos = new HashMap<>();
        nextId = 0L;
    }

    @Override
    public Produto add(Produto produto) {
        produto.setId(++nextId);
        produtos.put(produto.getId(), produto);
        return produtos.get(produto.getId());
    }

    @Override
    public Produto update(Long id, Produto produto) {
        produto.setId(id);
        produtos.put(id, produto);
        return produtos.get(id);
    }

    @Override
    public void delete(Long id){
        Produto produto = produtos.get(id);
        if(produto==null){
            throw new PessoaNaoExisteException();
        } else {
            produtos.remove(id);
        }
    }

    @Override
    public Produto getOne(Long id) {
        Produto produto = produtos.get(id);
        if(produto==null){
            throw new ProdutoNaoExisteException();
        } else {
            return produtos.get(id);
        }
    }

    @Override
    public List<Produto> getAll() {
        return new ArrayList<>(produtos.values());
    }

}
