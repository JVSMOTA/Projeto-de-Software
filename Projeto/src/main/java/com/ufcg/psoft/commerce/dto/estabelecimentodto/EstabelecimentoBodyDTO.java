package com.ufcg.psoft.commerce.dto.estabelecimentodto;

import com.fasterxml.jackson.annotation.*;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Sabor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstabelecimentoBodyDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("idEstabelecimento")
    @NotNull(message = "Id de Estabelecimento Obrigatorio!")
    @Column(name = "pk_id_estabelecimento")
    private Long idEstabelecimento;

    @JsonProperty("nomeEstabelecimento")
    @NotBlank(message = "Nome de Estabelecimento Obrigatorio!")
    @Column(name = "nome_estabelecimento")
    private String nomeEstabelecimento;

    @JsonProperty("codigoEstabelecimento")
    @NotBlank(message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    @Pattern(regexp = "\\d{6}", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    @Column(name = "codigo_estabelecimento", unique = true, nullable = false, length = 6)
    private String codigoEstabelecimento;

    @JsonProperty("listaSaboresEstabelecimento")
    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL)
    private List<Sabor> listaSabores;

    @JsonProperty("listaEntregadoresEstabelecimento")
    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL)
    private List<Entregador> listaEntregadoresDisponiveis;

    @JsonProperty("pedidosEmEspera")
    @Builder.Default
    private List<Long> pedidosEmEspera = new ArrayList<>();

}
