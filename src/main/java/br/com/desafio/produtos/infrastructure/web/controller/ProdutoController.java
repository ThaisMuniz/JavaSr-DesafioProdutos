package br.com.desafio.produtos.infrastructure.web.controller;

import br.com.desafio.produtos.infrastructure.web.dto.ProdutoDTO;
import br.com.desafio.produtos.service.ProdutoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    private static final int MAX_PAGE_SIZE = 100;

    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> listarComFiltros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) BigDecimal precoInicial,
            @RequestParam(required = false) BigDecimal precoFinal,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        Sort sortPadrao = Sort.by(Sort.Direction.ASC, "nome");
        Pageable pageable = PageRequest.of(page, size, sortPadrao);
        Page<ProdutoDTO> paginaDeProdutos = produtoService.buscarComFiltros(nome, precoInicial, precoFinal, pageable);
        return ResponseEntity.ok(paginaDeProdutos);
    }

}
