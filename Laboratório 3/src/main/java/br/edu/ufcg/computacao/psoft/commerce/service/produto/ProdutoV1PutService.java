package br.edu.ufcg.computacao.psoft.commerce.service.produto;

import br.edu.ufcg.computacao.psoft.commerce.dto.produto.ProdutoPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;
import br.edu.ufcg.computacao.psoft.commerce.repository.produto.ProdutoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoV1PutService implements ProdutoPutService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Produto put(Long id, ProdutoPutDTO produtoPutDTO) {
        Produto produtoEncontrado = produtoRepository.findById(id).get();
        modelMapper.map(produtoPutDTO, produtoEncontrado);
        return produtoRepository.save(produtoEncontrado);
    }
}
