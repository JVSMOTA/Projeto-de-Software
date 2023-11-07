package br.edu.ufcg.computacao.psoft.commerce.service.produto;

import br.edu.ufcg.computacao.psoft.commerce.dto.produto.ProdutoPostDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;
import br.edu.ufcg.computacao.psoft.commerce.repository.produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoV1PostService implements ProdutoPostService {

    @Autowired
    private final ProdutoRepository produtoRepository;

    public ProdutoV1PostService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Produto post(ProdutoPostDTO produtoPostDTO) {
        return produtoRepository.save(
                Produto.builder()
                        .nome(produtoPostDTO.getNome())
                        .codigoBarras(produtoPostDTO.getCodigoBarras())
                        .valor(produtoPostDTO.getValor())
                        .fabricante(produtoPostDTO.getFabricante())
                        .build()
        );
    }
}
