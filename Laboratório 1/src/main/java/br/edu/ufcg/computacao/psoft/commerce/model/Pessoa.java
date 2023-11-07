package br.edu.ufcg.computacao.psoft.commerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String dataNascimento;
    @ElementCollection
    @CollectionTable(name = "endereco", joinColumns = @JoinColumn(name = "pessoa_id"))
    private List<Endereco> enderecos;
    private String profissao;

    public void adicionarEndereco(Endereco endereco) {
        if (enderecos == null) {
            enderecos = new ArrayList<>();
        }
        enderecos.add(endereco);
    }

    public void removerEndereco(Endereco endereco) {
        if (enderecos != null) {
            enderecos.remove(endereco);
        }
    }
}
