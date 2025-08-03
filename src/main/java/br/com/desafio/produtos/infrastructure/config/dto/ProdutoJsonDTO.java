package br.com.desafio.produtos.infrastructure.config.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

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
