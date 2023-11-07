package br.edu.ufcg.computacao.psoft.commerce.dto.pessoa;

import br.edu.ufcg.computacao.psoft.commerce.model.Endereco;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaPostDTO {

    @NotEmpty(message = "Nome de Pessoa Obrigatorio!")
    @JsonProperty("nome")
    private String nome;
    @NotEmpty(message = "Cpf de Pessoa Obrigatorio!")
    @JsonProperty("cpf")
    private String cpf;
    @NotEmpty(message = "Email de Pessoa Obrigatorio!")
    @JsonProperty("email")
    private String email;
//    @NotBlank(message = "Telefones de Pessoa Obrigatorio!")
    @JsonProperty("telefones")
    private List<String> telefones;
    @NotEmpty(message = "Data de Nascimento de Pessoa Obrigatorio!")
    @JsonProperty("dataNascimento")
    private String dataNascimento;
//    @NotBlank(message = "Endereços de Pessoa Obrigatorio!")
    @JsonProperty("enderecos")
    private List<Endereco> enderecos;
    @NotBlank(message = "Profissao de Pessoa Obrigatorio!")
    @JsonProperty("profissao")
    private String profissao;
}
