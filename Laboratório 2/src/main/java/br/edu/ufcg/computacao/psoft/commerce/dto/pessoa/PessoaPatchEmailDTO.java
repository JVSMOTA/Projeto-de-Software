package br.edu.ufcg.computacao.psoft.commerce.dto.pessoa;

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
public class PessoaPatchEmailDTO {

    @NotEmpty(message = "Email de Logradouro Obrigatorio!")
    @JsonProperty("email")
    private String email;
}
