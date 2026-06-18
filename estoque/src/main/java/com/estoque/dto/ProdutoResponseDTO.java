package com.estoque.dto;

import com.estoque.domain.model.Produto;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private int quantidadeAtual;
    private int estoqueMinimo;
    private BigDecimal precoUnitario;
    private Long categoriaId;
    private String categoriaNome;
    private boolean abaixoDoMinimo;

    public static ProdutoResponseDTO de(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.id = produto.getId();
        dto.nome = produto.getNome();
        dto.descricao = produto.getDescricao();
        dto.quantidadeAtual = produto.getQuantidadeAtual();
        dto.estoqueMinimo = produto.getEstoqueMinimo();
        dto.precoUnitario = produto.getPrecoUnitario();
        dto.abaixoDoMinimo = produto.estaBaixoDoMinimo();
        if (produto.getCategoria() != null) {
            dto.categoriaId = produto.getCategoria().getId();
            dto.categoriaNome = produto.getCategoria().getNome();
        }
        return dto;
    }
}
