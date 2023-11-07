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
public class LogradouroPatchTipoDTO {

    @NotEmpty(message = "Tipo de Logradouro Obrigatorio!")
    @JsonProperty("tipoLogradouro")
    private String tipoLogradouro;
}
