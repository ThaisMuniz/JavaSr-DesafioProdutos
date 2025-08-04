package br.com.desafio.produtos.infrastructure.web.dto;

import br.com.desafio.produtos.util.ConversorFinanceiroUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ProdutoDTO {

    private String nome;
    private int quantidade;
    private BigDecimal preco;
    private String tipo;
    private String industria;
    private String origem;

    public String getPrecoFormatado() {
        return ConversorFinanceiroUtil.getValorFormatado(this.preco);
    }
}
