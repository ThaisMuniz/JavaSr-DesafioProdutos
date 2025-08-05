package br.com.desafio.produtos;

import br.com.desafio.produtos.infrastructure.config.CargaInicialDados;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ProdutosApplicationTest {

    @MockBean
    private CargaInicialDados cargaInicialDados;

    @Test
    void contextLoads() {
        // Este teste passa se a aplicação iniciar o contexto sem lançar uma exceção.
    }
}
