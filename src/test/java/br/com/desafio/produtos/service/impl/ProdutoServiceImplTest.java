package br.com.desafio.produtos.service.impl;

import br.com.desafio.produtos.domain.entity.ProdutoEntity;
import br.com.desafio.produtos.domain.exception.RegistroDuplicadoException;
import br.com.desafio.produtos.domain.repository.ProdutoRepository;
import br.com.desafio.produtos.infrastructure.web.dto.PageResponseDTO;
import br.com.desafio.produtos.infrastructure.web.dto.ProdutoRequestDTO;
import br.com.desafio.produtos.infrastructure.web.dto.ProdutoResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceImplTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @Test
    void deveBuscarProdutosDTO() {
        var produto = new ProdutoEntity(1L, "PUK", 9, new BigDecimal("9.90"), "XS", "","");
        Page<ProdutoEntity> paginaDeEntidades = new PageImpl<>(List.of(produto));
        Pageable pageable = PageRequest.of(0, 10);

        when(produtoRepository.buscarComFiltros(any(), any(),any(), eq(pageable))).thenReturn(paginaDeEntidades);

        PageResponseDTO<ProdutoResponseDTO> resultado = produtoService.buscarComFiltros("P", null, null, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(1);
        assertThat(resultado.getContent()).hasSize(1);

        ProdutoResponseDTO response = resultado.getContent().get(0);
        assertThat(response.getNome()).isEqualTo(produto.getNome());
        assertThat(response.getPreco()).isEqualTo(produto.getPreco());
        assertThat(response.getTipo()).isEqualTo(produto.getTipo());
    }

    @Test
    void deveCadastrarProdutoComSucesso() {
        var produtoRequestDTO = new ProdutoRequestDTO("Produto Novo",10, new BigDecimal("100.00"),"Tipo A","Industria X", "BR");

        when(produtoRepository.existsByNomeAndTipo(produtoRequestDTO.getNome(), produtoRequestDTO.getTipo())).thenReturn(false);

        var produtoSalvoComId = new ProdutoEntity(1L, produtoRequestDTO.getNome(), produtoRequestDTO.getQuantidade(),
                produtoRequestDTO.getPreco(), produtoRequestDTO.getTipo(), produtoRequestDTO.getIndustria(), produtoRequestDTO.getOrigem());

        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(produtoSalvoComId);

        produtoService.cadastrarProduto(produtoRequestDTO);

        verify(produtoRepository, times(1)).existsByNomeAndTipo("Produto Novo", "Tipo A");
        verify(produtoRepository, times(1)).save(any(ProdutoEntity.class));
    }

    @Test
    void deveLancarExcecaoAoTentarCadastrarProdutoDuplicado() {
        var produtoRequestDTO = new ProdutoRequestDTO("Produto Existente", 10, new BigDecimal("100.00"), "Tipo B", "Industria Y", "US");

        when(produtoRepository.existsByNomeAndTipo(produtoRequestDTO.getNome(), produtoRequestDTO.getTipo())).thenReturn(true);

        assertThatThrownBy(() -> produtoService.cadastrarProduto(produtoRequestDTO))
                .isInstanceOf(RegistroDuplicadoException.class).hasMessage("Já existe um produto cadastrado com o mesmo nome e tipo.");

        // Garante que o método save não foi chamado
        verify(produtoRepository, never()).save(any(ProdutoEntity.class));
    }
}
