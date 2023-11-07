package com.ufcg.psoft.commerce.dto.pedidodto;

import com.fasterxml.jackson.annotation.*;
import com.ufcg.psoft.commerce.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoBodyDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("idPedido")
    @NotNull(message = "Id de Pedido Obrigatorio!")
    @Column(name = "pk_id_pedido")
    private Long idPedido;

    @JsonProperty("pizzas")
    @NotEmpty(message = "Listagem de Pizzas Obrigatoria!")
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Pizza> pizzas;

    @JsonProperty("cliente")
    @NotNull(message = "Id de Cliente Obrigatorio!")
    private Cliente cliente;

    @JsonProperty("estabelecimento")
    @NotNull(message = "Id de Estabelecimento Obrigatorio!")
    private Estabelecimento estabelecimento;

    @JsonProperty("entregador")
    @NotNull(message = "Id de Entregador Obrigatorio!")
    private Entregador entregador;

    @JsonProperty("enderecoOpcional")
    @Column(name = "endereco_opcional")
    private String enderecoOpcional;

    @JsonProperty("valorPedido")
    @Column(name = "valor_pedido")
    private Double valorPedido;

    @JsonProperty("pagamento")
    @Column(name = "pagamento")
    @Enumerated(EnumType.STRING)
    private Pagamento pagamento;

    @JsonProperty("statusPagamento")
    @Column(name = "status_pagamento")
    private Boolean statusPagamento;

    @JsonProperty("statusEntrega")
    @Column(name = "status_entrega")
    private String statusEntrega;

}
