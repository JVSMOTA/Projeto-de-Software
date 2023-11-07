package br.edu.ufcg.computacao.psoft.commerce.service.logradouro;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouro.LogradouroPatchNomeDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.repository.logradouro.LogradouroRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogradouroV1PatchNomeService implements LogradouroPatchNomeService {

    @Autowired
    LogradouroRepository logradouroRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Logradouro patchNomeLogradouro(Long id, LogradouroPatchNomeDTO logradouroPatchNomeDTO) {
        return logradouroRepository.update(
                id,
                modelMapper.map(logradouroPatchNomeDTO, Logradouro.class)
        );
    }
}
