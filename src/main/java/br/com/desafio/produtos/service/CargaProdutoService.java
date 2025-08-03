package br.com.desafio.produtos.service;

import java.util.concurrent.CompletableFuture;

public interface CargaProdutoService {

    public CompletableFuture<Integer> carregarArquivo(String nomeArquivo);
}
