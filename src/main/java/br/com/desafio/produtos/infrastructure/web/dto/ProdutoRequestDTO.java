package br.com.desafio.produtos.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ProdutoRequestDTO {

    @NotBlank(message = "O nome não pode ser nulo ou vazio.")
    private String nome;

    @NotNull(message = "A quantidade não pode ser nula.")
    @PositiveOrZero(message = "A quantidade não pode ser um número negativo.")
    private int quantidade;

    @NotNull(message = "O preço não pode ser nulo.")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
    private BigDecimal preco;

    @NotBlank(message = "O tipo não pode ser vazio.")
    private String tipo;

    @NotBlank(message = "A industria não pode ser vazia.")
    private String industria;

    @NotBlank(message = "A origem não pode ser vazia.")
    private String origem;

}
