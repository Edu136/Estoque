package com.estoque.dto;

import com.estoque.domain.model.Movimentacao;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class MovimentacaoResponseDTO {

    private Long id;
    private Long produtoId;
    private String produtoNome;
    private String tipo;
    private int quantidade;
    private LocalDateTime dataHora;
    private String observacao;
    private Long fornecedorId;
    private String fornecedorNome;

    public static MovimentacaoResponseDTO de(Movimentacao m) {
        MovimentacaoResponseDTO dto = new MovimentacaoResponseDTO();
        dto.id = m.getId();
        dto.tipo = m.getTipo().name();
        dto.quantidade = m.getQuantidade();
        dto.dataHora = m.getDataHora();
        dto.observacao = m.getObservacao();
        if (m.getProduto() != null) {
            dto.produtoId = m.getProduto().getId();
            dto.produtoNome = m.getProduto().getNome();
        }
        if (m.getFornecedor() != null) {
            dto.fornecedorId = m.getFornecedor().getId();
            dto.fornecedorNome = m.getFornecedor().getNome();
        }
        return dto;
    }
}
