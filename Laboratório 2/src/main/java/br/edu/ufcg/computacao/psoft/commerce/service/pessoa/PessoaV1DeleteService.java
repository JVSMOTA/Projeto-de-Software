package br.edu.ufcg.computacao.psoft.commerce.service.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.repository.pessoa.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaV1DeleteService implements PessoaDeleteService {

    @Autowired
    private final PessoaRepository pessoaRepository;

    public PessoaV1DeleteService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    public void delete(Long id) {
        pessoaRepository.delete(id);
    }
}
