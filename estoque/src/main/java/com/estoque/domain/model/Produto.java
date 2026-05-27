package com.estoque.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

// SRP: Produto cuida apenas de seus próprios dados e regras de domínio
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

    public Produto() {}

    public Produto(String nome, String descricao, int quantidadeAtual,
                   int estoqueMinimo, BigDecimal precoUnitario, Categoria categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.quantidadeAtual = quantidadeAtual;
        this.estoqueMinimo = estoqueMinimo;
        this.precoUnitario = precoUnitario;
        this.categoria = categoria;
    }

    // Regra de domínio encapsulada na entidade
    public boolean estaBaixoDoMinimo() {
        return this.quantidadeAtual <= this.estoqueMinimo;
    }

    // Atualiza quantidade validando para não ficar negativa
    public void atualizarQuantidade(int delta) {
        int nova = this.quantidadeAtual + delta;
        if (nova < 0) {
            throw new IllegalArgumentException(
                "Estoque insuficiente. Disponível: " + this.quantidadeAtual
            );
        }
        this.quantidadeAtual = nova;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public int getQuantidadeAtual() { return quantidadeAtual; }
    public void setQuantidadeAtual(int quantidadeAtual) { this.quantidadeAtual = quantidadeAtual; }
    public int getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(int estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
