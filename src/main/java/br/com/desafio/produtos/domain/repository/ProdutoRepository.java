package br.com.desafio.produtos.domain.repository;

import br.com.desafio.produtos.domain.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    @Query("SELECT CONCAT(p.nome, '::', p.tipo) FROM ProdutoEntity p WHERE CONCAT(p.nome, '::', p.tipo) IN :chaves")
    Set<String> findChavesExistentes(@Param("chaves") Set<String> chaves);
}
