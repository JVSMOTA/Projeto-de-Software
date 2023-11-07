package br.edu.ufcg.computacao.psoft.commerce.service.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.dto.pessoa.PessoaPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;

@FunctionalInterface
public interface PessoaPutService {
    public Pessoa put(Long id, PessoaPutDTO PessoaPutDTO);
}
