package br.edu.ufcg.computacao.psoft.commerce.dto.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.model.Endereco;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaGetAllDTO{

    @JsonProperty("nome")
    private String nome;
    @JsonProperty("cpf")
    private String cpf;
    @JsonProperty("email")
    private String email;
    @JsonProperty("telefones")
    private List<String> telefones;
    @JsonProperty("dataNascimento")
    private String dataNascimento;
    @JsonProperty("enderecos")
    private List<Endereco> enderecos;
    @JsonProperty("profissao")
    private String profissao;
}
