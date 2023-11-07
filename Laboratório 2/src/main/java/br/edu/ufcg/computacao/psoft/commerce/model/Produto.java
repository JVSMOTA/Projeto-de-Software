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
public class Produto {

    @Id
    private Long id;
    private String nome;
    private String codigoBarras;
    private Double valor;
    private String fabricante;

}
