package br.edu.ufcg.computacao.psoft.commerce.service.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;

import java.util.List;

@FunctionalInterface
public interface PessoaGetAllService {
    public List<Pessoa> getAll();
}
