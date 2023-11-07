package br.edu.ufcg.computacao.psoft.commerce.repository.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.exception.PessoaNaoExisteException;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PessoaVolatilRepository implements PessoaRepository {

    private Map<Long, Pessoa> pessoas;
    private Long nextId;

    public PessoaVolatilRepository() {
        pessoas = new HashMap<>();
        nextId = 0L;
    }

    @Override
    public Pessoa add(Pessoa pessoa) {
        pessoa.setId(++nextId);
        pessoas.put(pessoa.getId(), pessoa);
        return pessoas.get(pessoa.getId());
    }

    @Override
    public Pessoa update(Long id, Pessoa pessoa) {
        pessoa.setId(id);
        pessoas.put(id, pessoa);
        return pessoas.get(id);
    }

    @Override
    public void delete(Long id){
        Pessoa pessoa = pessoas.get(id);
        if(pessoa==null){
            throw new PessoaNaoExisteException();
        } else {
            pessoas.remove(id);
        }
    }

    @Override
    public Pessoa getOne(Long id) {
        Pessoa pessoa = pessoas.get(id);
        if(pessoa==null){
            throw new PessoaNaoExisteException();
        } else {
            return pessoas.get(id);
        }
    }

    @Override
    public List<Pessoa> getAll() {
        return new ArrayList<>(pessoas.values());
    }

}
