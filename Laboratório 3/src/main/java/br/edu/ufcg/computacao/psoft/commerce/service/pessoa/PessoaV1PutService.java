package br.edu.ufcg.computacao.psoft.commerce.service.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.dto.pessoa.PessoaPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.repository.pessoa.PessoaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaV1PutService implements PessoaPutService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Pessoa put(Long id, PessoaPutDTO pessoaPutDTO) {
        Pessoa pessoaEncontrada = pessoaRepository.findById(id).get();
        modelMapper.map(pessoaPutDTO, pessoaEncontrada);
        return pessoaRepository.save(pessoaEncontrada);
    }
}
