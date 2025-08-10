package br.com.desafio.produtos.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ProdutoResponseDTO {

    private String nome;
    private int quantidade;
    private BigDecimal preco;
    private String tipo;
    private String industria;
    private String origem;

}
