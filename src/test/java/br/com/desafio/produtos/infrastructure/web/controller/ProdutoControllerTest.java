package br.com.desafio.produtos.infrastructure.web.controller;

import br.com.desafio.produtos.domain.exception.RegistroDuplicadoException;
import br.com.desafio.produtos.infrastructure.web.dto.PageResponseDTO;
import br.com.desafio.produtos.infrastructure.web.dto.ProdutoRequestDTO;
import br.com.desafio.produtos.infrastructure.web.dto.ProdutoResponseDTO;
import br.com.desafio.produtos.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornarProdutosPaginadoComFiltros() throws Exception {
        var produtoResponse = new ProdutoResponseDTO("RTIX", 10, new BigDecimal("19.99"), "3XL", "Industrial", "LA");
        PageResponseDTO<ProdutoResponseDTO> paginaProdutos = new PageResponseDTO<>(List.of(produtoResponse));

        when(produtoService.buscarComFiltros(
                eq("RTIX"),
                eq(new BigDecimal("10.00")),
                eq(new BigDecimal("20.00")),
                any(Pageable.class))
        ).thenReturn(paginaProdutos);

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
        PageResponseDTO<ProdutoResponseDTO> paginaVazia = new PageResponseDTO<>(List.of());

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

    @Test
    void deveRetornarStatus204NoContentAoCadastrarProduto() throws Exception {
        var request = new ProdutoRequestDTO("Produto Ok",5, new BigDecimal("25.75"), "Tipo A", "Industria B", "BR");

        doNothing().when(produtoService).cadastrarProduto(any(ProdutoRequestDTO.class));

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus400BadRequestParaRequestInvalido() throws Exception {
        var requestInvalida = new ProdutoRequestDTO("",50, new BigDecimal(0), "Tipo C", null, "BR");

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalida)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Erro de validação na requisição."))
                .andExpect(jsonPath("$.details").isMap());
    }

    @Test
    void deveRetornarStatus409ConflictParaProdutoDuplicado() throws Exception {
        var request = new ProdutoRequestDTO("Produto Ok", 5, new BigDecimal("25.75"), "Tipo A", "Industria B", "BR");

        doThrow(new RegistroDuplicadoException("Já existe um produto cadastrado..."))
                .when(produtoService).cadastrarProduto(any(ProdutoRequestDTO.class));

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
