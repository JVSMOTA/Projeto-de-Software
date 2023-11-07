package com.ufcg.psoft.commerce.model;

import java.util.*;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sabor")
public class Sabor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("idSabor")
    @Column(name = "pk_id_sabor")
    private Long idSabor;

    @JsonProperty("nomeSabor")
    @Column(name = "nome_sabor")
    private String nomeSabor;

    @JsonProperty("tipoSabor")
    @Column(name = "tipo_sabor")
    private String tipoSabor;

    @JsonProperty("valorMedioSabor")
    @Column(name = "valor_medio_sabor")
    private Double valorMedioSabor;

    @JsonProperty("valorGrandeSabor")
    @Column(name = "valor_grande_sabor")
    private Double valorGrandeSabor;

    @JsonProperty("disponivel")
    @Column(name = "disponivel")
    private Boolean disponivel;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_estabelecimento")
    private Estabelecimento estabelecimento;

    @JsonProperty("clientesInteressados")
    @ManyToMany
    @JoinTable(name = "sabores_clientes_interessados",
            joinColumns = @JoinColumn(name = "pk_id_sabor"),
            inverseJoinColumns = @JoinColumn(name = "pk_id_cliente"))
    private List<Cliente> clientesInteressados;

}
