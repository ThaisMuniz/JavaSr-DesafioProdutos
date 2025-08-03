package br.com.desafio.produtos.infrastructure.web.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoDTO {

    private String nome;
    private int quantidade;
    private BigDecimal preco;
    private String tipo;
    private String industria;
    private String origem;
}
