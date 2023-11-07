package com.ufcg.psoft.commerce.dto.sabordto;

import com.fasterxml.jackson.annotation.*;
import com.ufcg.psoft.commerce.model.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaborBodyDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("idSabor")
    @NotNull(message = "Id de Sabor Obrigatorio!")
    @Column(name = "pk_id_sabor")
    private Long idSabor;

    @JsonProperty("nomeSabor")
    @NotBlank(message = "Nome de Sabor Obrigatorio!")
    @Column(name = "nome_sabor")
    private String nomeSabor;

    @JsonProperty("tipoSabor")
    @NotBlank(message = "Tipo de Sabor Obrigatorio!")
    @Pattern(regexp = "^(salgado|doce)$", message = "Tipo deve ser salgado ou doce")
    @Column(name = "tipo_sabor")
    private String tipoSabor;

    @JsonProperty("valorMedioSabor")
    @NotNull(message = "Valor Medio Obrigatorio!")
    @DecimalMin(value = "0.01", inclusive = false, message = "Valor medio deve ser maior que zero")
    @Column(name = "valor_medio_sabor")
    private Double valorMedioSabor;

    @JsonProperty("valorGrandeSabor")
    @NotNull(message = "Valor Grande Obrigatorio!")
    @DecimalMin(value = "0.01", inclusive = false, message = "Valor grande deve ser maior que zero")
    @Column(name = "valor_grande_sabor")
    private Double valorGrandeSabor;

    @JsonProperty("disponivel")
    @NotNull(message = "Disponibilidade Obrigatoria!")
    @Column(name = "disponivel")
    private Boolean disponivel;

    @JsonProperty("clientesInteressados")
    private List<Cliente> clientesInteressados;

}
