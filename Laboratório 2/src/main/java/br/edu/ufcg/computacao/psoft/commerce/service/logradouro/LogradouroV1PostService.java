package br.edu.ufcg.computacao.psoft.commerce.service.logradouro;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouro.LogradouroPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.repository.logradouro.LogradouroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogradouroV1PostService implements LogradouroPostService {

    @Autowired
    private final LogradouroRepository logradouroRepository;

    public LogradouroV1PostService(LogradouroRepository logradouroRepository) {
        this.logradouroRepository = logradouroRepository;
    }

    @Override
    public Logradouro post(LogradouroPostPutDTO logradouroPostPutDTO) {
        return logradouroRepository.add(
                Logradouro.builder()
                        .tipoLogradouro(logradouroPostPutDTO.getTipoLogradouro())
                        .nome(logradouroPostPutDTO.getNome())
                        .bairro(logradouroPostPutDTO.getBairro())
                        .cidade(logradouroPostPutDTO.getCidade())
                        .estado(logradouroPostPutDTO.getEstado())
                        .pais(logradouroPostPutDTO.getPais())
                        .cep(logradouroPostPutDTO.getCep())
                        .build()
        );
    }
}
