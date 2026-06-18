package com.estoque.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Dados para registrar uma entrada de estoque")
@Getter
@Setter
public class EntradaDTO {

    @Schema(example = "1")
    @NotNull(message = "ID do produto é obrigatório")
    private Long produtoId;

    @Schema(example = "30")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private int quantidade;

    @Schema(example = "1")
    @NotNull(message = "ID do fornecedor é obrigatório para entradas")
    private Long fornecedorId;

    @Schema(example = "Reposição mensal")
    private String observacao;
}
