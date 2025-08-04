package br.com.desafio.produtos.service;

import br.com.desafio.produtos.domain.entity.ProdutoEntity;
import br.com.desafio.produtos.infrastructure.web.dto.ProdutoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProdutoService {

    Page<ProdutoDTO> buscarComFiltros(String nome, BigDecimal precoInicial, BigDecimal precoFinal, Pageable pageable);

    void criarProduto(ProdutoEntity produtoEntity);

}
