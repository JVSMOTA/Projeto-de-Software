package br.edu.ufcg.computacao.psoft.commerce.service.logradouro;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouro.LogradouroPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.repository.logradouro.LogradouroRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogradouroV1PutService implements LogradouroPutService {

    @Autowired
    private LogradouroRepository logradouroRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Logradouro put(Long id, LogradouroPostPutDTO logradouroPostPutDTO) {
        return logradouroRepository.update(
                id,
                modelMapper.map(logradouroPostPutDTO, Logradouro.class)
        );
    }
}
