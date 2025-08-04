package br.com.desafio.produtos.domain.exception;

public class FormatoDePrecoInvalidoException extends RuntimeException {

    public FormatoDePrecoInvalidoException(String valorInvalido, Throwable causa) {
        super(String.format("O valor de preço '%s' é inválido e não pôde ser convertido.", valorInvalido), causa);
    }

}
