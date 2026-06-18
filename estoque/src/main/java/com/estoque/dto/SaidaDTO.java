package com.estoque.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaidaDTO {

    @NotNull(message = "ID do produto é obrigatório")
    private Long produtoId;

    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private int quantidade;

    private String observacao;
}
