package br.com.desafio.produtos.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
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
    private String preco;

    @Column
    private String tipo;

    @Column
    private String industria;

    @Column
    private String origem;

}
