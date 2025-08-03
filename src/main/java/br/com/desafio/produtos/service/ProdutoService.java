package br.com.desafio.produtos.service;

import br.com.desafio.produtos.domain.entity.ProdutoEntity;
import br.com.desafio.produtos.infrastructure.web.dto.ProdutoDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProdutoService {

    void criarProduto(ProdutoEntity produtoEntity);

    List<ProdutoDTO> listarPorNome(String nome);

    List<ProdutoDTO> listarPorIntervaloPreco(BigDecimal precoInicial, BigDecimal precoFinal);
}
