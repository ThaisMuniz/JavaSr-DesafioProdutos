package br.com.desafio.produtos.infrastructure.config;

import br.com.desafio.produtos.domain.repository.ProdutoRepository;
import br.com.desafio.produtos.service.CargaProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CargaInicialDadosTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CargaProdutoService cargaProdutoService;

    @InjectMocks
    private CargaInicialDados cargaInicialDados;

    @Test
    void deveExecutarCargaQuandoBancoVazio() throws Exception {

        when(produtoRepository.count()).thenReturn(0L);
        when(cargaProdutoService.carregarArquivo(anyString())).thenReturn(CompletableFuture.completedFuture(10));

        cargaInicialDados.run(null);

        verify(cargaProdutoService, times(4)).carregarArquivo(anyString());
    }

    @Test
    void naoDeveExecutarCargaQuandoBancoComDados() throws Exception {

        when(produtoRepository.count()).thenReturn(100L);

        cargaInicialDados.run(null);

        verify(cargaProdutoService, never()).carregarArquivo(anyString());
    }

}
