package br.edu.ufcg.computacao.psoft.commerce.repository.logradouro;

import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;

import java.util.List;

public interface LogradouroRepository {
    Logradouro add(Logradouro logradouro);
    Logradouro update(Long id, Logradouro logradouro);
    void delete(Long id);
    Logradouro getOne(Long id);
    List<Logradouro> getAll();
}
