package br.com.desafio.produtos.service.impl;

import br.com.desafio.produtos.service.CargaProdutoService;
import br.com.desafio.produtos.domain.entity.ProdutoEntity;
import br.com.desafio.produtos.domain.exception.FormatoDePrecoInvalidoException;
import br.com.desafio.produtos.domain.repository.ProdutoRepository;
import br.com.desafio.produtos.infrastructure.config.dto.ProdutoJsonDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CargaProdutoServiceImpl implements CargaProdutoService {
    private static final Logger logger = LoggerFactory.getLogger(CargaProdutoServiceImpl.class);

    private final ProdutoRepository produtoRepository;
    private final ObjectMapper objectMapper;

    public CargaProdutoServiceImpl(ProdutoRepository produtoRepository, ObjectMapper objectMapper) {
        this.produtoRepository = produtoRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @Async
    public CompletableFuture<Integer> carregarArquivo(String nomeArquivo) {
        long startTime = System.currentTimeMillis();
        logger.info("[carregarArquivo] Iniciando processamento do arquivo {} na thread: {}", nomeArquivo, Thread.currentThread().getName());

        try {
            ClassPathResource resource = new ClassPathResource(nomeArquivo);

            try (InputStream inputStream = resource.getInputStream()) {

                TypeReference<List<ProdutoJsonDTO>> typeReference = new TypeReference<>() {};
                ObjectReader reader = objectMapper.readerFor(typeReference).at("/data");

                List<ProdutoJsonDTO> produtosDto = reader.readValue(inputStream);

                Set<ProdutoEntity> produtosParaSalvar = filtrarProdutosValidos(produtosDto, nomeArquivo);

                produtoRepository.saveAll(produtosParaSalvar);

                long endTime = System.currentTimeMillis();
                logger.info("[carregarArquivo] Arquivo {} processado com sucesso em {} ms. {} produtos salvos.",
                        nomeArquivo, (endTime - startTime), produtosParaSalvar.size());

                return CompletableFuture.completedFuture(produtosParaSalvar.size());
            }

        } catch (Exception e) {
            logger.error("[carregarArquivo] Falha ao processar o arquivo {}", nomeArquivo, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Validação: não deverá permitir incluir um registro com valores de product e type
     * iguais aos de um registro já existente.
     * @param produtosDto List<ProdutoJsonDTO>
     * @param nomeArquivo String
     * @return Set<ProdutoEntity>
     */
    private Set<ProdutoEntity> filtrarProdutosValidos(List<ProdutoJsonDTO> produtosDto, String nomeArquivo) {
        logger.info("[filtrarProdutosValidos] Iniciando filtragem e validacao dos produtos do arquivo {}, quantidade total de registros {}...", nomeArquivo, produtosDto.size());

        Set<ProdutoJsonDTO> produtosUnicosNoArquivo = new HashSet<>(produtosDto);
        if (produtosUnicosNoArquivo.size() < produtosDto.size()) {
            logger.warn("[filtrarProdutosValidos] Arquivo {} continha {} registros duplicados (mesmo product e type), que foram ignorados.", nomeArquivo, produtosDto.size() - produtosUnicosNoArquivo.size());
        }

        Set<String> chavesParaValidar = produtosUnicosNoArquivo.stream()
                .map(dto -> dto.getProduct() + "::" + dto.getType())
                .collect(Collectors.toSet());
        Set<String> chavesExistentesNoBanco = produtoRepository.buscarChavesExistentes(chavesParaValidar);

        if (!chavesExistentesNoBanco.isEmpty()) {
            logger.warn("[filtrarProdutosValidos] Encontrados {} produtos no arquivo {} que já existem no banco de dados e serão ignorados.", chavesExistentesNoBanco.size(), nomeArquivo);
        }

        Set<ProdutoEntity> produtosValidos = produtosUnicosNoArquivo.stream()
                .filter(produtoDto -> !chavesExistentesNoBanco.contains(produtoDto.getProduct() + "::" + produtoDto.getType()))
                .map(this::toProdutoEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return produtosValidos;
    }

    private ProdutoEntity toProdutoEntity(ProdutoJsonDTO dto) {
        try {
            return new ProdutoEntity(null,
                    dto.getProduct(),
                    dto.getQuantity(),
                    dto.getPriceBigDecimal(),
                    dto.getType(),
                    dto.getIndustry(),
                    dto.getOrigin());
        } catch (FormatoDePrecoInvalidoException e) {
            logger.error("Registro ignorado no arquivo. Produto: {}. Erro: {}", dto, e.getMessage());
            return null;
        }
    }
}
