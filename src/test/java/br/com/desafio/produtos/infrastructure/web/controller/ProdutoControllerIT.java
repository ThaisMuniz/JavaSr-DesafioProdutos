package br.com.desafio.produtos.infrastructure.web.controller;


import br.com.desafio.produtos.infrastructure.config.CargaInicialDados;
import br.com.desafio.produtos.infrastructure.web.dto.ProdutoRequestDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProdutoControllerIT {

    @MockBean
    private CargaInicialDados cargaInicialDados;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Order(1)
    @Test
    void deveCadastrarProdutoComSucesso() {
        ProdutoRequestDTO novoProduto = new ProdutoRequestDTO("Notebook Gamer", 1000, BigDecimal.valueOf(5000.99), "Notebook", "Eletrônicos", "China");

        given()
                .contentType(ContentType.JSON)
                .body(novoProduto)
                .when()
                .post("/produtos")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Order(2)
    @Test
    void deveRetornarConflictAoCadastrarProdutoDuplicado() {
        ProdutoRequestDTO produtoDuplicado = new ProdutoRequestDTO("Notebook Gamer", 1000, BigDecimal.valueOf(5000.99), "Notebook", "Eletrônicos", "China");

        given()
                .contentType(ContentType.JSON)
                .body(produtoDuplicado)
                .when()
                .post("/produtos")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Order(3)
    @Test
    void deveRetornarBadRequestAoCadastrarProdutoInvalido() {
        ProdutoRequestDTO produtoInvalido = new ProdutoRequestDTO(null, 1, BigDecimal.ZERO, "", "", "");

        given()
                .contentType(ContentType.JSON)
                .body(produtoInvalido)
                .when()
                .post("/produtos")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Order(4)
    @Test
    void deveListarProdutosComSucesso() {
        // Cadastra mais um produto para garantir que a lista não está vazia
        ProdutoRequestDTO outroProduto = new ProdutoRequestDTO("Mouse Sem Fio", 1000, BigDecimal.valueOf(150.00), "Acessórios", "Eletrônicos", "US");
        given().contentType(ContentType.JSON).body(outroProduto).post("/produtos").then().statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/produtos")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(greaterThan(0)))
                .body("content[0].nome", is("Mouse Sem Fio"));
    }

    @Order(5)
    @Test
    void deveListarProdutosFiltrandoPorNome() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("nome", "Notebook")
                .when()
                .get("/produtos")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(1))
                .body("content[0].nome", containsString("Notebook Gamer"));
    }

    @Order(6)
    @Test
    void deveListarProdutosFiltrandoPorPreco() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("precoInicial", "5000")
                .queryParam("precoFinal", "6000")
                .when()
                .get("/produtos")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(1))
                .body("content[0].nome", is("Notebook Gamer"));
    }

    @Order(7)
    @Test
    void deveRetornarListaVaziaAoFiltrarPorNomeInexistente() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("nome", "Produto Inexistente")
                .when()
                .get("/produtos")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(0));
    }
}
