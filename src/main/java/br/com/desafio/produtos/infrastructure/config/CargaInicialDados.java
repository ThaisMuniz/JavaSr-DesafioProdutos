package br.com.desafio.produtos.infrastructure.config;

import br.com.desafio.produtos.service.CargaProdutoService;
import br.com.desafio.produtos.domain.repository.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Component
public class CargaInicialDados implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(CargaInicialDados.class);

    private final ProdutoRepository produtoRepository;
    private final CargaProdutoService cargaProdutoService;

    public CargaInicialDados(ProdutoRepository produtoRepository, CargaProdutoService cargaProdutoService) {
        this.produtoRepository = produtoRepository;
        this.cargaProdutoService = cargaProdutoService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (produtoRepository.count() > 0) {
            logger.info("[run] Banco de dados já populado. Nenhuma carga será executada.");
            return;
        }

        logger.info("[run] Iniciando carga paralela de produtos...");
        long totalStartTime = System.currentTimeMillis();

        List<String> arquivos = List.of(
                "dados/data_1.json",
                "dados/data_2.json",
                "dados/data_3.json",
                "dados/data_4.json"
        );

        List<CompletableFuture<Integer>> futures = arquivos.stream()
                .map(cargaProdutoService::carregarArquivo)
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long totalEndTime = System.currentTimeMillis();
        int totalProdutosSalvos = futures.stream().mapToInt(future -> future.getNow(0)).sum();
        logger.info("[run] Carga paralela finalizada em {} ms. Total de produtos salvos: {}", (totalEndTime - totalStartTime), totalProdutosSalvos);
    }

}

