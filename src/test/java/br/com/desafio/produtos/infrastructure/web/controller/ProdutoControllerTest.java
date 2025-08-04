package br.com.desafio.produtos.infrastructure.web.controller;

import br.com.desafio.produtos.infrastructure.web.dto.ProdutoDTO;
import br.com.desafio.produtos.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Test
    void deveRetornarProdutosPaginadoComFiltros() throws Exception {
        ProdutoDTO produtoResponse = new ProdutoDTO("RTIX", 10, new BigDecimal("19.99"), "3XL", "Industrial", "LA");
        Page<ProdutoDTO> paginaDeResposta = new PageImpl<>(List.of(produtoResponse));

        when(produtoService.buscarComFiltros(
                eq("RTIX"),
                eq(new BigDecimal("10.00")),
                eq(new BigDecimal("20.00")),
                any(Pageable.class))
        ).thenReturn(paginaDeResposta);

        mockMvc.perform(get("/produtos")
                        .param("nome", "RTIX")
                        .param("precoInicial", "10.00")
                        .param("precoFinal", "20.00")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].nome").value("RTIX"))
                .andExpect(jsonPath("$.totalElements").value(1));

        // Verifica se o método foi chamado com os parâmetros corretos
        verify(produtoService).buscarComFiltros(
                eq("RTIX"),
                eq(new BigDecimal("10.00")),
                eq(new BigDecimal("20.00")),
                any(Pageable.class)
        );
    }

    @Test
    void deveUsarPaginacaoPadrao() throws Exception {
        Page<ProdutoDTO> paginaVazia = new PageImpl<>(List.of());
        when(produtoService.buscarComFiltros(any(), any(), any(), any(Pageable.class)))
                .thenReturn(paginaVazia);

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(produtoService).buscarComFiltros(any(), any(), any(), pageableCaptor.capture());

        Pageable pageableCapturado = pageableCaptor.getValue();
        assertThat(pageableCapturado.getPageNumber()).isEqualTo(0);
        assertThat(pageableCapturado.getPageSize()).isEqualTo(10);
        assertThat(pageableCapturado.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, "nome"));
    }
}
