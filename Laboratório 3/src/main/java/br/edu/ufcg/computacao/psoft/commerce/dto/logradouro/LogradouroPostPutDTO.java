package br.edu.ufcg.computacao.psoft.commerce.dto.logradouro;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogradouroPostPutDTO {

    @NotBlank(message = "Tipo de Logradouro Obrigatorio!")
    @JsonProperty("tipoLogradouro")
    private String tipoLogradouro;
    @NotBlank(message = "Nome de Logradouro Obrigatorio!")
    @JsonProperty("nome")
    private String nome;
    @NotBlank(message = "Bairro de Logradouro Obrigatorio!")
    @JsonProperty("bairro")
    private String bairro;
    @NotBlank(message = "Cidade de Logradouro Obrigatorio!")
    @JsonProperty("cidade")
    private String cidade;
    @NotBlank(message = "Estado de Logradouro Obrigatorio!")
    @JsonProperty("estado")
    private String estado;
    @NotBlank(message = "Pais de Logradouro Obrigatorio!")
    @JsonProperty("pais")
    private String pais;
    @NotBlank(message = "Cep de Logradouro Obrigatorio!")
    @JsonProperty("cep")
    private String cep;
}
