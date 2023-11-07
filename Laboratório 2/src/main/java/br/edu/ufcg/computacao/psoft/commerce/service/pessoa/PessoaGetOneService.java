package br.edu.ufcg.computacao.psoft.commerce.service.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;

@FunctionalInterface
public interface PessoaGetOneService {
    public Pessoa getOne(Long id);
}
