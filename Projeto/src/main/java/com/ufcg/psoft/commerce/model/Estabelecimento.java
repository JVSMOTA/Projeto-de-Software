package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "estabelecimento")
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("idEstabelecimento")
    @Column(name = "pk_id_estabelecimento")
    private Long idEstabelecimento;

    @JsonProperty("nomeEstabelecimento")
    @Column(name = "nome_estabelecimento")
    private String nomeEstabelecimento;

    @JsonProperty("codigoEstabelecimento")
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
