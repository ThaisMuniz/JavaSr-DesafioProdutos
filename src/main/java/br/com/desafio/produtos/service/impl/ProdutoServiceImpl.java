package br.com.desafio.produtos.service.impl;

import br.com.desafio.produtos.service.ProdutoService;
import br.com.desafio.produtos.domain.entity.ProdutoEntity;
import br.com.desafio.produtos.domain.repository.ProdutoRepository;
import br.com.desafio.produtos.infrastructure.web.dto.ProdutoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private ProdutoRepository produtoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Page<ProdutoDTO> buscarComFiltros(String nome, BigDecimal precoInicial, BigDecimal precoFinal, Pageable pageable) {
        Page<ProdutoEntity> paginaDeProdutos = produtoRepository.buscarComFiltros(nome, precoInicial, precoFinal, pageable);
        return paginaDeProdutos.map(this::toProdutoDto);
    }

    private ProdutoDTO toProdutoDto(ProdutoEntity produto) {
        return new ProdutoDTO(
                produto.getNome(),
                produto.getQuantidade(),
                produto.getPreco(),
                produto.getTipo(),
                produto.getIndustria(),
                produto.getOrigem()
        );
    }

    @Transactional
    public void criarProduto(ProdutoEntity produto){
        produtoRepository.save(produto);
    }

}

