package br.edu.ufcg.computacao.psoft.commerce.dto.produto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoPutDTO {

    @NotBlank(message = "Nome de produto obrigatorio.")
    @JsonProperty("nome")
    private String nome;
    @NotBlank(message = "Codigo de barras de produto obrigatorio.")
    @JsonProperty("codigoBarras")
    private String codigoBarras;
    @DecimalMin(value = "0.01", message = "O valor do produto deve ser maior que zero.")
    @JsonProperty("valor")
    private Double valor;
    @NotBlank(message = "Fabricante de produto obrigatorio.")
    @JsonProperty("fabricante")
    private String fabricante;

}
