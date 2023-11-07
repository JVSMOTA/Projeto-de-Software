package br.edu.ufcg.computacao.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Tabela Pessoa")
public class Pessoa {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pk_id_pessoa")
    private Long id;
    @JsonProperty("nome")
    @Column(name = "desc_nome")
    private String nome;
    @JsonProperty("cpf")
    @Column(name = "desc_cpf")
    private String cpf;
    @JsonProperty("email")
    @Column(name = "desc_email")
    private String email;
    private List<String> telefones;
    @JsonProperty("dataNascimento")
    @Column(name = "desc_data_nascimento")
    private String dataNascimento;
    @JsonProperty("enderecos")
    @Column(name = "desc_enderecos")
    @Embedded
    private List<Endereco> enderecos;
    @JsonProperty("profissao")
    @Column(name = "desc_profissao")
    private String profissao;

}
