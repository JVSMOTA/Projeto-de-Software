package br.edu.ufcg.computacao.psoft.commerce.service.logradouro;

import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.repository.logradouro.LogradouroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogradouroV1GetOneService implements LogradouroGetOneService {

    @Autowired
    private final LogradouroRepository logradouroRepository;

    public LogradouroV1GetOneService(LogradouroRepository logradouroRepository) {
        this.logradouroRepository = logradouroRepository;
    }

    @Override
    public Logradouro getOne(Long id) {
        return logradouroRepository.findById(id).get();
    }
}
