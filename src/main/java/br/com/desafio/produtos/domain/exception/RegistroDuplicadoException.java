package br.com.desafio.produtos.domain.exception;

public class RegistroDuplicadoException extends RuntimeException{

    public RegistroDuplicadoException(String mensage){
        super(mensage);
    }
}
