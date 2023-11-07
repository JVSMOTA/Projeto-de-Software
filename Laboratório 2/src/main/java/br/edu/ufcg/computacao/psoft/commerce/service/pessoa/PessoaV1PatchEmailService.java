package br.edu.ufcg.computacao.psoft.commerce.service.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.dto.pessoa.PessoaPatchEmailDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.repository.pessoa.PessoaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaV1PatchEmailService implements PessoaPatchEmailService {

    @Autowired
    PessoaRepository pessoaRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Pessoa patchEmail(Long id, PessoaPatchEmailDTO pessoaPatchEmailDTO) {
        return pessoaRepository.update(
                id,
                modelMapper.map(pessoaPatchEmailDTO, Pessoa.class)
        );
    }
}
