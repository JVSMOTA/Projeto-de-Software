package com.ufcg.psoft.commerce.dto.entregadordto;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorBodyDTO {

    @JsonProperty("nomeEntregador")
    @NotBlank(message = "Nome de Entregador Obrigatorio!")
    private String nomeEntregador;

    @JsonProperty("placaVeiculo")
    @NotBlank(message = "Placa de Veiculo Obrigatoria!")
    private String placaVeiculo;

    @JsonProperty("tipoVeiculo")
    @NotBlank(message = "Tipo de Veiculo Obrigatorio!")
    private String tipoVeiculo;

    @JsonProperty("corVeiculo")
    @NotBlank(message = "Cor de Veiculo Obrigatoria!")
    private String corVeiculo;

    @JsonProperty("codigoEntregador")
    @NotBlank(message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    @Pattern(regexp = "\\d{6}", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    private String codigoEntregador;

    @JsonProperty("statusAprovacao")
    private Boolean statusAprovacao;

}
