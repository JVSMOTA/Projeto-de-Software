package br.edu.ufcg.computacao.psoft.commerce.service.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.dto.pessoa.PessoaPatchEmailDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;

@FunctionalInterface
public interface PessoaPatchEmailService {
    Pessoa patchEmail(Long id, PessoaPatchEmailDTO pessoaPatchEmailDTO);
}
