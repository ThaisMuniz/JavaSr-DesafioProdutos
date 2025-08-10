package br.com.desafio.produtos.infrastructure.config;

import br.com.desafio.produtos.domain.entity.ProdutoEntity;
import br.com.desafio.produtos.domain.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(
        properties = "carga.arquivos.produtos=dados/teste-integracao-carga.json"
)
@ActiveProfiles("test")
@SpringBootTest
public class CargaInicialDadosIT {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveCarregarProdutosIgnorandoInvalidos() {
        List<ProdutoEntity> produtosNoBanco = produtoRepository.findAll();

        assertThat(produtosNoBanco).hasSize(4);
        Optional<ProdutoEntity> produtoValido1 = produtosNoBanco.stream()
                .filter(p -> p.getNome().equals("Produto Válido 1"))
                .findFirst();
        assertThat(produtoValido1).isPresent();
        assertThat(produtoValido1.get().getPreco()).isEqualTo(new BigDecimal("19.99"));
        assertThat(produtoValido1.get().getOrigem()).isEqualTo("BR");

        // Verifica que o produto com preço inválido não foi inserido
        boolean produtoInvalidoExiste = produtosNoBanco.stream()
                .anyMatch(p -> p.getNome().equals("Produto Inválido"));
        assertThat(produtoInvalidoExiste).isFalse();
    }
}
