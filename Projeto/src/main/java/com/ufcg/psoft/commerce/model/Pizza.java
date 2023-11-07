package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pizza")
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("idPizza")
    @Column(name = "pk_id_pizza")
    private Long idPizza;

    @JsonProperty("idSabor1")
    @ManyToOne()
    @JoinColumn(name = "id_sabor1")
    private Sabor sabor1;

    @JsonProperty("idSabor2")
    @ManyToOne()
    @JoinColumn(name = "id_sabor2")
    private Sabor sabor2;

    @JsonProperty("tamanho")
    @Column(name = "tamanho")
    private String tamanho;

    @JsonProperty("quantidade")
    @Column(name = "quantidade")
    private Integer quantidade;

    @JsonProperty("valorPizza")
    @Column(name = "valor_pizza")
    private Double valorPizza;

    @JsonProperty("idPedido")
    @ManyToOne()
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    public Double calculaValor() {
		Double valorTotal = 0.0;
		if (tamanho.equals("medio")) {
			valorTotal = sabor1.getValorMedioSabor();
		} else if (tamanho.equals("grande")) {
			valorTotal = sabor1.getValorGrandeSabor();
			if (sabor2 != null) {
				valorTotal = sabor2.getValorGrandeSabor();
				valorTotal /= 2;
			}
		}
		return valorTotal;
	}

    public void setValorPizza(Double valorPizza) {
        this.valorPizza = calculaValor();
    }

    public Double getValorPizza() {
        return calculaValor();
    }

}
