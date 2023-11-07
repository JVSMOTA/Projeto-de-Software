package br.edu.ufcg.computacao.psoft.commerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Logradouro {

    @Id
    private Long id;
    private String tipoLogradouro;
    private String nome;
    private String bairro;
    private String cidade;
    private String estado;
    private String pais;
    private String cep;

}
