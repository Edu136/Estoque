package com.estoque.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// DTO para registrar saída de produto
public class SaidaDTO {

    @NotNull(message = "ID do produto é obrigatório")
    private Long produtoId;

    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private int quantidade;

    private String observacao;

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
