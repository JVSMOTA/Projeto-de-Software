package br.edu.ufcg.computacao.psoft.commerce.service.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.dto.pessoa.PessoaPostDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.repository.pessoa.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaV1PostService implements PessoaPostService {

    @Autowired
    private final PessoaRepository pessoaRepository;

    public PessoaV1PostService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    public Pessoa post(PessoaPostDTO pessoaPostDTO) {
        return pessoaRepository.save(
                Pessoa.builder()
                        .nome(pessoaPostDTO.getNome())
                        .cpf(pessoaPostDTO.getCpf())
                        .email(pessoaPostDTO.getEmail())
                        .telefones(pessoaPostDTO.getTelefones())
                        .dataNascimento(pessoaPostDTO.getDataNascimento())
                        .enderecos(pessoaPostDTO.getEnderecos())
                        .profissao(pessoaPostDTO.getProfissao())
                        .build()
        );
    }
}
