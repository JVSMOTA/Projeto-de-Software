package br.edu.ufcg.computacao.psoft.commerce.dto.produto;

import br.edu.ufcg.computacao.psoft.commerce.model.Endereco;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoGetAllDTO {

    @NotBlank(message = "Nome de produto obrigatorio.")
    @JsonProperty("nome")
    private String nome;
    @NotBlank(message = "Codigo de barras obrigatorio.")
    @JsonProperty("codigoBarras")
    private String codigoBarras;
    @NotNull(message = "Valor do produto obrigatorio.")
    @JsonProperty("valor")
    private Double valor;
    @NotBlank(message = "Fabricante obrigatorio.")
    @JsonProperty("fabricante")
    private String fabricante;

}
