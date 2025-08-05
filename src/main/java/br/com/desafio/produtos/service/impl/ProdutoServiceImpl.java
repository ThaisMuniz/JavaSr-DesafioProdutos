package br.com.desafio.produtos.service.impl;

import br.com.desafio.produtos.domain.exception.RegistroDuplicadoException;
import br.com.desafio.produtos.infrastructure.web.dto.ProdutoRequestDTO;
import br.com.desafio.produtos.service.ProdutoService;
import br.com.desafio.produtos.domain.entity.ProdutoEntity;
import br.com.desafio.produtos.domain.repository.ProdutoRepository;
import br.com.desafio.produtos.infrastructure.web.dto.ProdutoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Page<ProdutoResponseDTO> buscarComFiltros(String nome, BigDecimal precoInicial, BigDecimal precoFinal, Pageable pageable) {
        Page<ProdutoEntity> paginaDeProdutos = produtoRepository.buscarComFiltros(nome, precoInicial, precoFinal, pageable);
        return paginaDeProdutos.map(this::toProdutoDto);
    }

    @Transactional
    public void cadastrarProduto(ProdutoRequestDTO produtoResponseDTO){
        if (produtoRepository.existsByNomeAndTipo(produtoResponseDTO.getNome(), produtoResponseDTO.getTipo())) {
            throw new RegistroDuplicadoException("Já existe um produto cadastrado com o mesmo nome e tipo.");
        }

        produtoRepository.save(toProdutoEntity(produtoResponseDTO));
    }

    private ProdutoResponseDTO toProdutoDto(ProdutoEntity produto) {
        return new ProdutoResponseDTO(
                produto.getNome(),
                produto.getQuantidade(),
                produto.getPreco(),
                produto.getTipo(),
                produto.getIndustria(),
                produto.getOrigem()
        );
    }

    private ProdutoEntity toProdutoEntity(ProdutoRequestDTO produtoResponseDTO) {
        return new ProdutoEntity(null,
                produtoResponseDTO.getNome(),
                produtoResponseDTO.getQuantidade(),
                produtoResponseDTO.getPreco(),
                produtoResponseDTO.getTipo(),
                produtoResponseDTO.getIndustria(),
                produtoResponseDTO.getOrigem()
        );
    }
}

