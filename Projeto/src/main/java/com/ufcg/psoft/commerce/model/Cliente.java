package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("idCliente")
    private Long idCliente;

    @JsonProperty("nomeCliente")
    private String nomeCliente;

    @JsonProperty("enderecoPrincipalCliente")
    private String enderecoPrincipalCliente;

    @JsonProperty("codigoCliente")
    private String codigoCliente;

    @ManyToMany(mappedBy = "clientesInteressados")
    @JsonIgnore
    private List<Sabor> saboresInteressados;

    public void notificarPedidoPronto(){

        LocalTime horario = LocalTime.now();
        String saudacao;

        if (horario.getHour() >= 6 && horario.getHour() < 12){
            saudacao = "Bom dia, ";
        } else if (horario.getHour() >= 12 && horario.getHour() < 18){
            saudacao = "Boa tarde, ";
        } else {
            saudacao = "Boa noite, ";
        }

        System.out.println(saudacao + this.nomeCliente + "! Seu pedido está pronto.");

    }

    public void notificarPedidoEmRota(Entregador entregador) {
        System.out.println(this.getNomeCliente() + ", seu pedido está em rota de entrega\n" +
                "--Informações do entregador--:\n" +
                "Nome: " + entregador.getNomeEntregador() + "\n" +
                "Tipo de Veiculo: " + entregador.getTipoVeiculo() + "\n" +
                "Cor do Veiculo: " + entregador.getCorVeiculo() + "\n" +
                "Placa do Veiculo: " + entregador.getPlacaVeiculo());
    }

    public void notificarIndisponibilidadeEntregador() {
        System.out.println(this.nomeCliente + ", o seu pedido está pronto, mas infelizmente não há entregadores disponíveis. " +
                "Pedimos perdão pelo inconveniente, seu pedido será entregue assim que tivermos um entregador disponível!");
    }

}
