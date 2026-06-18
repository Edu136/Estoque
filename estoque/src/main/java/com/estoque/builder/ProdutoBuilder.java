package com.estoque.builder;

import com.estoque.domain.model.Categoria;
import com.estoque.domain.model.Produto;

import java.math.BigDecimal;

public class ProdutoBuilder {

    private String nome;
    private String descricao;
    private int quantidadeAtual;
    private int estoqueMinimo;
    private BigDecimal precoUnitario;
    private Categoria categoria;

    private ProdutoBuilder() {}

    public static ProdutoBuilder umProduto() {
        return new ProdutoBuilder();
    }

    public ProdutoBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public ProdutoBuilder comDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public ProdutoBuilder comQuantidadeAtual(int quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
        return this;
    }

    public ProdutoBuilder comEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
        return this;
    }

    public ProdutoBuilder comPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        return this;
    }

    public ProdutoBuilder comCategoria(Categoria categoria) {
        this.categoria = categoria;
        return this;
    }

    public Produto build() {
        return new Produto(nome, descricao, quantidadeAtual, estoqueMinimo, precoUnitario, categoria);
    }
}
