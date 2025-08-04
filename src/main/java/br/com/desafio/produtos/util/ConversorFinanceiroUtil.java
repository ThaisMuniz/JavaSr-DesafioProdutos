package br.com.desafio.produtos.util;

import br.com.desafio.produtos.domain.exception.FormatoDePrecoInvalidoException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class ConversorFinanceiroUtil {

    public static BigDecimal getValorBigDecimal(String valor) {
        if (valor == null || valor.isBlank()) {
            return new BigDecimal(0);
        }
        try {
            String precoLimpo = valor.replaceAll("[^\\d.]", "");
            return new BigDecimal(precoLimpo);
        } catch (NumberFormatException e) {
            throw new FormatoDePrecoInvalidoException(valor, e);
        }
    }

    public static String getValorFormatado(BigDecimal valor) {
        if (valor == null) {
            valor = new BigDecimal(0);
        }
        NumberFormat formatadorDeMoeda = NumberFormat.getCurrencyInstance(Locale.US);
        return formatadorDeMoeda.format(valor);
    }
}
