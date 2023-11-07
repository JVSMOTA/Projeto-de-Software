package br.edu.ufcg.computacao.psoft.commerce.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Endereco {
    private Logradouro logradouro;
    private String numero;
    private String complemento;
}
