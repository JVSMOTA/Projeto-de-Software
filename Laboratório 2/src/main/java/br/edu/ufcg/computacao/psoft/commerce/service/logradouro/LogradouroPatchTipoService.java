package br.edu.ufcg.computacao.psoft.commerce.service.logradouro;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouro.LogradouroPatchTipoDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;

@FunctionalInterface
public interface LogradouroPatchTipoService {
    Logradouro patchTipoLogradouro(Long id, LogradouroPatchTipoDTO logradouroPatchTipoDTO);
}
