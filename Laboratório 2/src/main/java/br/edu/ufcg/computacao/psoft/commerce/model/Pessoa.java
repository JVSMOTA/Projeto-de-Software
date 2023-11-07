package br.edu.ufcg.computacao.psoft.commerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pessoa {

    @Id
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private List<String> telefones;
    private String dataNascimento;
    private List<Endereco> enderecos;
    private String profissao;

}
