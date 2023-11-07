package br.edu.ufcg.computacao.psoft.commerce.repository.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;

import java.util.List;

public interface PessoaRepository {
    Pessoa add(Pessoa pessoa);
    Pessoa update(Long id, Pessoa pessoa);
    void delete(Long id);
    Pessoa getOne(Long id);
    List<Pessoa> getAll();

}
