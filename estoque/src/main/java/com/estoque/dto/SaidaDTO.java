package com.estoque.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Dados para registrar uma saída de estoque")
@Getter
@Setter
public class SaidaDTO {

    @Schema(example = "1")
    @NotNull(message = "ID do produto é obrigatório")
    private Long produtoId;

    @Schema(example = "5")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private int quantidade;

    @Schema(example = "Venda balcão")
    private String observacao;
}
