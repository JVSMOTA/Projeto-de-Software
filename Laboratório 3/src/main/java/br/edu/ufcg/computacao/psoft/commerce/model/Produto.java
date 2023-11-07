package br.edu.ufcg.computacao.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Tabela Produto")
public class Produto {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pk_id_produto")
    private Long id;
    @JsonProperty("nome")
    @Column(name = "desc_nome")
    private String nome;
    @JsonProperty("codigoBarras")
    @Column(name = "desc_codigo_barras")
    private String codigoBarras;
    @JsonProperty("valor")
    @Column(name = "dbl_valor")
    private Double valor;
    @JsonProperty("fabricante")
    @Column(name = "desc_fabricante")
    private String fabricante;

}
