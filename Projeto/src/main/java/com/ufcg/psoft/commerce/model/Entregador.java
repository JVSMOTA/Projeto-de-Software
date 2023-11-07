package com.ufcg.psoft.commerce.model;

import lombok.*;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entregador")
public class Entregador {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("idEntregador")
    private Long idEntregador;

    @JsonProperty("nomeEntregador")
    private String nomeEntregador;

    @JsonProperty("placaVeiculo")
    private String placaVeiculo;

    @JsonProperty("tipoVeiculo")
    private String tipoVeiculo;

    @JsonProperty("corVeiculo")
    private String corVeiculo;

    @JsonProperty("codigoEntregador")
    private String codigoEntregador;

    @JsonProperty("statusAprovacao")
    private Boolean statusAprovacao;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_estabelecimento")
    private Estabelecimento estabelecimento;

    @JsonProperty("disponibilidade")
    private Boolean disponibilidade;

}
