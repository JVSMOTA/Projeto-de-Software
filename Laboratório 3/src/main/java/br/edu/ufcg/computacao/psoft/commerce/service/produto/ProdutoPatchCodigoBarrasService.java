package br.edu.ufcg.computacao.psoft.commerce.service.produto;

import br.edu.ufcg.computacao.psoft.commerce.dto.produto.ProdutoPatchCodigoBarrasDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;

@FunctionalInterface
public interface ProdutoPatchCodigoBarrasService {
    Produto patchCodigoBarras(Long id, ProdutoPatchCodigoBarrasDTO produtoPatchCodigoBarrasDTO);
}
