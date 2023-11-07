package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("idPedido")
    @Column(name = "pk_id_pedido")
    private Long idPedido;

    @JsonProperty("pizzas")
    @NotEmpty(message = "Listagem de Pizzas Obrigatoria!")
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @NotNull(message = "Lista de Pizzas Obrigatoria!")
    private List<Pizza> pizzas;

    @JsonProperty("cliente")
    @JoinColumn(name = "cliente")
    @ManyToOne
    private Cliente cliente;

    @JsonProperty("estabelecimento")
    @JoinColumn(name = "estabelecimento")
    @ManyToOne
    private Estabelecimento estabelecimento;

    @JsonProperty("entregador")
    @JoinColumn(name = "entregador")
    @ManyToOne
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

    public Double calculaValorTotal() {
        Double valorTotal = 0.0;
        Double fator = 1.0;
        for (Pizza p: pizzas) {
            valorTotal += p.calculaValor();
        }
        if (pagamento != null && pagamento.toString().equals("DEBITO")) {
            fator = 0.975;
        }
        else if (pagamento != null && pagamento.toString().equals("PIX")) {
            fator = 0.95;
        }
        return valorTotal * fator;
    }
    
    public void setValorPedido(Double valorPedido) {
        this.valorPedido = calculaValorTotal();
    }

    public Double getValorPedido() {
        return calculaValorTotal();
    }

}
