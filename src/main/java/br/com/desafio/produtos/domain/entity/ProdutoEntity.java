package br.com.desafio.produtos.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode(of = {"nome", "tipo"})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "produtos")
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column
    private int quantidade;

    @Column
    private BigDecimal preco;

    @Column
    private String tipo;

    @Column
    private String industria;

    @Column
    private String origem;

}
