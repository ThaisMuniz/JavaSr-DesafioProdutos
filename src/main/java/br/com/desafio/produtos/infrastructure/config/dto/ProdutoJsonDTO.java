package br.com.desafio.produtos.infrastructure.config.dto;

import br.com.desafio.produtos.domain.exception.FormatoDePrecoInvalidoException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProdutoJsonDTO {

    @JsonProperty("product")
    private String product;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("price")
    private String price;

    @JsonProperty("type")
    private String type;

    @JsonProperty("industry")
    private String industry;

    @JsonProperty("origin")
    private String origin;

}
