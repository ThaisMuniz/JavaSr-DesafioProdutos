package br.com.desafio.produtos.infrastructure.config.dto;

import br.com.desafio.produtos.domain.exception.FormatoDePrecoInvalidoException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(of = {"product", "type"})
@ToString(of = {"product", "type"})
@NoArgsConstructor
@AllArgsConstructor
@Getter
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

    @JsonIgnore
    public BigDecimal getPriceBigDecimal() {
        if (this.price == null || this.price.isBlank()) {
            return new BigDecimal(0);
        }
        try {
            return new BigDecimal(this.price.replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            throw new FormatoDePrecoInvalidoException(this.price, e);
        }
    }

}
