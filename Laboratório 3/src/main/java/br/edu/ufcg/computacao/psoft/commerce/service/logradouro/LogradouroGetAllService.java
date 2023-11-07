package br.edu.ufcg.computacao.psoft.commerce.service.logradouro;

import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import java.util.List;

@FunctionalInterface
public interface LogradouroGetAllService {
    public List<Logradouro> getAll();
}
