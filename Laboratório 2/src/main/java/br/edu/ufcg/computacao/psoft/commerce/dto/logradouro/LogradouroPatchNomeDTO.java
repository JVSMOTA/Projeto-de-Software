package br.edu.ufcg.computacao.psoft.commerce.dto.logradouro;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogradouroPatchNomeDTO {

    @NotEmpty(message = "Nome de Logradouro Obrigatorio!")
    @JsonProperty("nome")
    private String nome;
}
