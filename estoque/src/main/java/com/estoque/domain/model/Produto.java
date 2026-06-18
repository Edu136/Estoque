package com.estoque.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do produto é obrigatório")
    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Min(value = 0, message = "Quantidade não pode ser negativa")
    @Column(nullable = false)
    private int quantidadeAtual;

    @Min(value = 0, message = "Estoque mínimo não pode ser negativo")
    @Column(nullable = false)
    private int estoqueMinimo;

    @DecimalMin(value = "0.0", message = "Preço não pode ser negativo")
    @Column(nullable = false)
    private BigDecimal precoUnitario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Produto(String nome, String descricao, int quantidadeAtual,
                   int estoqueMinimo, BigDecimal precoUnitario, Categoria categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.quantidadeAtual = quantidadeAtual;
        this.estoqueMinimo = estoqueMinimo;
        this.precoUnitario = precoUnitario;
        this.categoria = categoria;
    }

    public boolean estaBaixoDoMinimo() {
        return this.quantidadeAtual <= this.estoqueMinimo;
    }

    public void atualizarQuantidade(int delta) {
        int nova = this.quantidadeAtual + delta;
        if (nova < 0) {
            throw new IllegalArgumentException(
                "Estoque insuficiente. Disponível: " + this.quantidadeAtual
            );
        }
        this.quantidadeAtual = nova;
    }
}
