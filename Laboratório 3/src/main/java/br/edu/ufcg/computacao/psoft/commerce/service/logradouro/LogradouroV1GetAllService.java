package br.edu.ufcg.computacao.psoft.commerce.service.logradouro;

import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.repository.logradouro.LogradouroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogradouroV1GetAllService implements LogradouroGetAllService {

    @Autowired
    private final LogradouroRepository logradouroRepository;

    public LogradouroV1GetAllService(LogradouroRepository logradouroRepository) {
        this.logradouroRepository = logradouroRepository;
    }

    @Override
    public List<Logradouro> getAll() {
        return logradouroRepository.findAll();
    }
}
