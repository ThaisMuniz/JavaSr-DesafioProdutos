package br.com.desafio.produtos.service;

import br.com.desafio.produtos.infrastructure.web.dto.ProdutoRequestDTO;
import br.com.desafio.produtos.infrastructure.web.dto.ProdutoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProdutoService {

    Page<ProdutoResponseDTO> buscarComFiltros(String nome, BigDecimal precoInicial, BigDecimal precoFinal, Pageable pageable);

    void cadastrarProduto(ProdutoRequestDTO produtoRequestDTO);

}
