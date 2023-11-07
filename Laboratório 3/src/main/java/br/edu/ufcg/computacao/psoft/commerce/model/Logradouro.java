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
@Table(name = "Tabela Logradouro")
public class Logradouro {

    @Column(name = "pk_id_logradouro")
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @JsonProperty("tipo de logradouro")
    @Column(name = "desc_logradouro")
    private String tipoLogradouro;
    @JsonProperty("nome")
    @Column(name = "desc_nome")
    private String nome;
    @JsonProperty("bairro")
    @Column(name = "desc_bairro")
    private String bairro;
    @JsonProperty("cidade")
    @Column(name = "desc_cidade")
    private String cidade;
    @JsonProperty("estado")
    @Column(name = "desc_estado")
    private String estado;
    @JsonProperty("pais")
    @Column(name = "desc_pais")
    private String pais;
    @JsonProperty("cep")
    @Column(name = "desc_cep")
    private String cep;

}
