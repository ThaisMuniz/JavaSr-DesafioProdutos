package br.com.desafio.produtos.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ProdutoResponseDTO {

    private String nome;
    private int quantidade;
    private BigDecimal preco;
    private String tipo;
    private String industria;
    private String origem;

}
