package br.edu.ufcg.computacao.psoft.commerce.service.produto;

import br.edu.ufcg.computacao.psoft.commerce.model.Produto;
import br.edu.ufcg.computacao.psoft.commerce.repository.produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoV1GetOneService implements ProdutoGetOneService {

    @Autowired
    private final ProdutoRepository produtoRepository;

    public ProdutoV1GetOneService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Produto getOne(Long id) {
        return produtoRepository.getOne(id);
    }
}
