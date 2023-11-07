package br.edu.ufcg.computacao.psoft.commerce.service.logradouro;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouro.LogradouroPatchNomeDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;

@FunctionalInterface
public interface LogradouroPatchNomeService {
    Logradouro patchNomeLogradouro(Long id, LogradouroPatchNomeDTO logradouroPatchNomeDTO);
}
