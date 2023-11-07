package br.edu.ufcg.computacao.psoft.commerce.service.produto;

import br.edu.ufcg.computacao.psoft.commerce.repository.produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoV1DeleteService implements ProdutoDeleteService {

    @Autowired
    private final ProdutoRepository produtoRepository;

    public ProdutoV1DeleteService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void delete(Long id) {
        produtoRepository.deleteById(id);
    }
}
