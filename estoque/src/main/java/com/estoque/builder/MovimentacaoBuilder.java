package com.estoque.builder;

import com.estoque.domain.enums.TipoMovimentacao;
import com.estoque.domain.model.Fornecedor;
import com.estoque.domain.model.Movimentacao;
import com.estoque.domain.model.Produto;

import java.time.LocalDateTime;

/**
 * Padrão Builder: constrói Movimentacao passo a passo com validações.
 * Garante que o objeto seja criado em estado consistente.
 */
public class MovimentacaoBuilder {

    private Produto produto;
    private TipoMovimentacao tipo;
    private int quantidade;
    private String observacao;
    private Fornecedor fornecedor;

    public MovimentacaoBuilder produto(Produto produto) {
        this.produto = produto;
        return this;
    }

    public MovimentacaoBuilder tipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
        return this;
    }

    public MovimentacaoBuilder quantidade(int quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public MovimentacaoBuilder observacao(String observacao) {
        this.observacao = observacao;
        return this;
    }

    public MovimentacaoBuilder fornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
        return this;
    }

    public Movimentacao build() {
        validar();

        Movimentacao mov = new Movimentacao();
        mov.setProduto(this.produto);
        mov.setTipo(this.tipo);
        mov.setQuantidade(this.quantidade);
        mov.setObservacao(this.observacao);
        mov.setFornecedor(this.fornecedor);
        mov.setDataHora(LocalDateTime.now());

        return mov;
    }

    private void validar() {
        if (produto == null) throw new IllegalStateException("Produto é obrigatório");
        if (tipo == null) throw new IllegalStateException("Tipo de movimentação é obrigatório");
        if (quantidade <= 0) throw new IllegalStateException("Quantidade deve ser maior que zero");
        if (tipo == TipoMovimentacao.ENTRADA && fornecedor == null) {
            throw new IllegalStateException("Fornecedor é obrigatório para entradas");
        }
    }
}
