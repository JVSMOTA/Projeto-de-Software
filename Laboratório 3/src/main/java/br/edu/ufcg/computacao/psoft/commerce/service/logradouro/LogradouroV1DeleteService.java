package br.edu.ufcg.computacao.psoft.commerce.service.logradouro;

import br.edu.ufcg.computacao.psoft.commerce.repository.logradouro.LogradouroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogradouroV1DeleteService implements LogradouroDeleteService {

    @Autowired
    private final LogradouroRepository logradouroRepository;

    public LogradouroV1DeleteService(LogradouroRepository logradouroRepository) {
        this.logradouroRepository = logradouroRepository;
    }

    @Override
    public void delete(Long id) {
        logradouroRepository.deleteById(id);
    }
}
