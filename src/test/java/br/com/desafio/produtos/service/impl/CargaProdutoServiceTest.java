package br.com.desafio.produtos.service.impl;

import br.com.desafio.produtos.domain.entity.ProdutoEntity;
import br.com.desafio.produtos.domain.repository.ProdutoRepository;
import br.com.desafio.produtos.infrastructure.config.dto.ProdutoJsonDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
public class CargaProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ObjectReader objectReader;

    @InjectMocks
    private CargaProdutoServiceImpl cargaProdutoService;

    @Captor
    private ArgumentCaptor<List<ProdutoEntity>> produtosCaptor;

    @Test
    void deveCarregarProdutosComSucesso() throws Exception {
        ProdutoJsonDTO dto1 = new ProdutoJsonDTO();
        dto1.setProduct("Produto A");
        dto1.setType("Tipo 1");
        dto1.setPrice("$10.50");
        List<ProdutoJsonDTO> listaDeDtos = List.of(dto1);

        when(objectMapper.readerFor(any(TypeReference.class))).thenReturn(objectReader);
        when(objectReader.at("/data")).thenReturn(objectReader);
        when(objectReader.readValue(any(InputStream.class))).thenReturn(listaDeDtos);
        when(produtoRepository.buscarChavesExistentes(any())).thenReturn(Collections.emptySet());

        CompletableFuture<Integer> future = cargaProdutoService.carregarArquivo("dados/teste-unitario.json");

        assertThat(future).isCompletedWithValue(1);

        verify(produtoRepository, times(1)).saveAll(produtosCaptor.capture());

        List<ProdutoEntity> produtosSalvos = produtosCaptor.getValue();
        assertThat(produtosSalvos).hasSize(1);
        assertThat(produtosSalvos.get(0).getNome()).isEqualTo("Produto A");
    }

    @Test
    void naoDeveSalvarProdutosQueJaExistemNoBanco() throws Exception {
        ProdutoJsonDTO dtoExistente = new ProdutoJsonDTO();
        dtoExistente.setProduct("Produto Existente");
        dtoExistente.setType("Tipo X");
        dtoExistente.setPrice("$5.00");

        ProdutoJsonDTO dtoNovo = new ProdutoJsonDTO();
        dtoNovo.setProduct("Produto Novo");
        dtoNovo.setType("Tipo Y");
        dtoNovo.setPrice("$15.00");

        List<ProdutoJsonDTO> listaDeDtos = List.of(dtoExistente, dtoNovo);
        String chaveExistente = "Produto Existente::Tipo X";

        when(objectMapper.readerFor(any(TypeReference.class))).thenReturn(objectReader);
        when(objectReader.at("/data")).thenReturn(objectReader);
        when(objectReader.readValue(any(InputStream.class))).thenReturn(listaDeDtos);
        // Simula que um dos produtos já existente no banco
        when(produtoRepository.buscarChavesExistentes(any())).thenReturn(Set.of(chaveExistente));

        CompletableFuture<Integer> future = cargaProdutoService.carregarArquivo("dados/teste-unitario.json");

        assertThat(future).isCompletedWithValue(1);
        verify(produtoRepository, times(1)).saveAll(produtosCaptor.capture());
        List<ProdutoEntity> produtosSalvos = produtosCaptor.getValue();
        assertThat(produtosSalvos).hasSize(1);
        assertThat(produtosSalvos.get(0).getNome()).isEqualTo("Produto Novo"); // Apenas o novo produto deve ser salvo
    }

    @Test
    void deveRetornarFuturoComExcecaoQuandoArquivoNaoPuderSerLido() throws Exception {

        when(objectMapper.readerFor(any(TypeReference.class))).thenReturn(objectReader);
        when(objectReader.at("/data")).thenReturn(objectReader);
        when(objectReader.readValue(any(InputStream.class))).thenThrow(new RuntimeException("Erro de I/O"));

        CompletableFuture<Integer> future = cargaProdutoService.carregarArquivo("dados/arquivo_corrompido.json");

        assertThat(future).isCompletedExceptionally();
    }
}
