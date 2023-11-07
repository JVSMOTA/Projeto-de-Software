package br.edu.ufcg.computacao.psoft.commerce.repository.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
