package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "associacao")
public class Associacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("idAssociacao")
    @Column(name = "pk_id_associacao")
    private Long idAssociacao;

    @JsonProperty("estabelecimento")
    @ManyToOne
    @JoinColumn(name = "id_estabelecimento")
    private Estabelecimento estabelecimento;

    @JsonProperty("entregador")
    @ManyToOne
    @JoinColumn(name = "id_entregador")
    private Entregador entregador;

    @JsonProperty("statusAssociacao")
    @Column(name = "status_associacao")
    private Boolean statusAssociacao;

}
