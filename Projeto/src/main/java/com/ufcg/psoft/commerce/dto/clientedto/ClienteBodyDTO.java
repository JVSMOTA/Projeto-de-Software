package com.ufcg.psoft.commerce.dto.clientedto;

import com.ufcg.psoft.commerce.model.Sabor;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteBodyDTO {

    @JsonProperty("nomeCliente")
    @NotBlank(message = "Nome de Cliente Obrigatorio!")
    private String nomeCliente;

    @JsonProperty("enderecoPrincipalCliente")
    @NotBlank(message = "Endereço Principal de Cliente Obrigatorio!")
    private String enderecoPrincipalCliente;

    @JsonProperty("codigoCliente")
    @NotBlank(message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    @Pattern(regexp = "\\d{6}", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    private String codigoCliente;

    @ManyToMany(mappedBy = "clientesInteressados")
    @JsonProperty("saboresInteressados")
    private List<Sabor> saboresInteressados;

}
