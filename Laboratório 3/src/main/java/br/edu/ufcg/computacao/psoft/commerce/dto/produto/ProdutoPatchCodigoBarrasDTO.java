package br.edu.ufcg.computacao.psoft.commerce.dto.produto;

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
public class ProdutoPatchCodigoBarrasDTO {

    @NotBlank(message = "Código de barras é obrigatório.")
    @JsonProperty("codigoBarras")
    private String codigoBarras;
}
