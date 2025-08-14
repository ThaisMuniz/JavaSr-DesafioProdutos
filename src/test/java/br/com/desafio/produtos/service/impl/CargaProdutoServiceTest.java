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
    private ArgumentCaptor<Set<ProdutoEntity>> produtosCaptor;

    @Test
    void deveCarregarProdutosComSucesso() throws Exception {
        var dto1 = new ProdutoJsonDTO("Produto A",1,"$10.50","Tipo 1","","");
        var listaDeDtos = List.of(dto1);

        when(objectMapper.readerFor(any(TypeReference.class))).thenReturn(objectReader);
        when(objectReader.at("/data")).thenReturn(objectReader);
        when(objectReader.readValue(any(InputStream.class))).thenReturn(listaDeDtos);
        when(produtoRepository.buscarChavesExistentes(any())).thenReturn(Collections.emptySet());

        CompletableFuture<Integer> future = cargaProdutoService.carregarArquivo("dados/teste-unitario.json");

        assertThat(future).isCompletedWithValue(1);

        verify(produtoRepository, times(1)).saveAll(produtosCaptor.capture());

        var produtosSalvos = produtosCaptor.getValue();
        assertThat(produtosSalvos).hasSize(1);
        assertThat(produtosSalvos.iterator().next().getNome()).isEqualTo("Produto A");
    }

    @Test
    void naoDeveSalvarProdutosQueJaExistemNoBanco() throws Exception {
        var dtoExistente = new ProdutoJsonDTO("Produto Existente",2,"$5.00","Tipo X","","");
        var dtoNovo = new ProdutoJsonDTO("Produto Novo",1,"$15.00","Tipo Y","","");

        var listaDeDtos = List.of(dtoExistente, dtoNovo);
        var chaveExistente = "Produto Existente::Tipo X";

        when(objectMapper.readerFor(any(TypeReference.class))).thenReturn(objectReader);
        when(objectReader.at("/data")).thenReturn(objectReader);
        when(objectReader.readValue(any(InputStream.class))).thenReturn(listaDeDtos);
        // Simula que um dos produtos já existente no banco
        when(produtoRepository.buscarChavesExistentes(any())).thenReturn(Set.of(chaveExistente));

        CompletableFuture<Integer> future = cargaProdutoService.carregarArquivo("dados/teste-unitario.json");

        assertThat(future).isCompletedWithValue(1);
        verify(produtoRepository, times(1)).saveAll(produtosCaptor.capture());

        var produtosSalvos = produtosCaptor.getValue();
        assertThat(produtosSalvos).hasSize(1);
        assertThat(produtosSalvos.iterator().next().getNome()).isEqualTo("Produto Novo"); // Apenas o novo produto deve ser salvo
    }

    @Test
    void deveSalvarSomenteUmProdutoQuandoProductETypeDuplicados() throws Exception {
        var dto1 = new ProdutoJsonDTO("Produto 1",1,"$5.00","Tipo X","","");
        var dto2 = new ProdutoJsonDTO("Produto 2",2,"$15.00","Tipo Y","","");
        var dto3 = new ProdutoJsonDTO("Produto 2", 3, "$15.00", "Tipo Y", "", "");

        var listaDeDtos = List.of(dto1, dto2, dto3);
        var chaveExistente = "Produto Existente::Tipo Z";

        when(objectMapper.readerFor(any(TypeReference.class))).thenReturn(objectReader);
        when(objectReader.at("/data")).thenReturn(objectReader);
        when(objectReader.readValue(any(InputStream.class))).thenReturn(listaDeDtos);
        when(produtoRepository.buscarChavesExistentes(any())).thenReturn(Set.of(chaveExistente));

        CompletableFuture<Integer> future = cargaProdutoService.carregarArquivo("dados/teste-unitario.json");

        assertThat(future).isCompletedWithValue(2);
        verify(produtoRepository, times(1)).saveAll(produtosCaptor.capture());

        var produtosSalvos = produtosCaptor.getValue();
        assertThat(produtosSalvos).hasSize(2);
        assertThat(produtosSalvos.iterator().next().getNome()).isEqualTo("Produto 1");
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
