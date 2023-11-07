package br.edu.ufcg.computacao.psoft.commerce.service.logradouro;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouro.LogradouroPatchTipoDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.repository.logradouro.LogradouroRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogradouroV1PatchTipoService implements LogradouroPatchTipoService {

    @Autowired
    LogradouroRepository logradouroRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Logradouro patchTipoLogradouro(Long id, LogradouroPatchTipoDTO logradouroPatchTipoDTO) {
        Logradouro logradouroEncontrado = logradouroRepository.findById(id).get();
        logradouroEncontrado.setTipoLogradouro(logradouroPatchTipoDTO.getTipoLogradouro());
        return logradouroRepository.save(logradouroEncontrado);
    }
}
