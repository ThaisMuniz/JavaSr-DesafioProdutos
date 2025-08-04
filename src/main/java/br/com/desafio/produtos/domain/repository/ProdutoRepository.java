package br.com.desafio.produtos.domain.repository;

import br.com.desafio.produtos.domain.entity.ProdutoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Set;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    @Query("SELECT CONCAT(p.nome, '::', p.tipo) FROM ProdutoEntity p WHERE CONCAT(p.nome, '::', p.tipo) IN :chaves")
    Set<String> buscarChavesExistentes(@Param("chaves") Set<String> chaves);

    @Query("SELECT p FROM ProdutoEntity p WHERE " +
            "(:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:precoInicial IS NULL OR p.preco >= :precoInicial) AND " +
            "(:precoFinal IS NULL OR p.preco <= :precoFinal)")
    Page<ProdutoEntity> buscarComFiltros(
            @Param("nome") String nome,
            @Param("precoInicial") BigDecimal precoInicial,
            @Param("precoFinal") BigDecimal precoFinal,
            Pageable pageable);
}
