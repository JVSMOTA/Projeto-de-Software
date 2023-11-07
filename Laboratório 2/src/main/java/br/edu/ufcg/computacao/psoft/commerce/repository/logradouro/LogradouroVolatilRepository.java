package br.edu.ufcg.computacao.psoft.commerce.repository.logradouro;

import br.edu.ufcg.computacao.psoft.commerce.exception.LogradouroNaoExisteException;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LogradouroVolatilRepository implements LogradouroRepository{

    private Map<Long, Logradouro> logradouros;
    private Long nextId;

    public LogradouroVolatilRepository() {
        logradouros = new HashMap<>();
        nextId = 0L;
    }

    @Override
    public Logradouro add(Logradouro logradouro) {
        logradouro.setId(++nextId);
        logradouros.put(logradouro.getId(), logradouro);
        return logradouros.get(logradouro.getId());
    }

    @Override
    public Logradouro update(Long id, Logradouro logradouro) {
        logradouro.setId(id);
        logradouros.put(id, logradouro);
        return logradouros.get(id);
    }

    @Override
    public void delete(Long id){
        Logradouro logradouro = logradouros.get(id);
        if(logradouro==null){
            throw new LogradouroNaoExisteException();
        } else {
            logradouros.remove(id);
        }
    }

    @Override
    public Logradouro getOne(Long id) {
        Logradouro logradouro = logradouros.get(id);
        if(logradouro==null){
            throw new LogradouroNaoExisteException();
        } else {
            return logradouros.get(id);
        }
    }

    @Override
    public List<Logradouro> getAll() {
        return new ArrayList<>(logradouros.values());
    }
}
