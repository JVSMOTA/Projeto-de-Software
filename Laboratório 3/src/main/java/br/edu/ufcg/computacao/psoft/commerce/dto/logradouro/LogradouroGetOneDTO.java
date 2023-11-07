package br.edu.ufcg.computacao.psoft.commerce.dto.logradouro;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogradouroGetOneDTO {

    @JsonProperty("tipoLogradouro")
    private String tipoLogradouro;
    @JsonProperty("nome")
    private String nome;
    @JsonProperty("bairro")
    private String bairro;
    @JsonProperty("cidade")
    private String cidade;
    @JsonProperty("estado")
    private String estado;
    @JsonProperty("pais")
    private String pais;
    @JsonProperty("cep")
    private String cep;
}
