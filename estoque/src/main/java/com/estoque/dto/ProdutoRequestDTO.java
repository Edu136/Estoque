package com.estoque.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoRequestDTO {

    @NotBlank(message = "Nome do produto é obrigatório")
    private String nome;

    private String descricao;

    @Min(value = 0, message = "Quantidade não pode ser negativa")
    private int quantidadeAtual;

    @Min(value = 0, message = "Estoque mínimo não pode ser negativo")
    private int estoqueMinimo;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", message = "Preço não pode ser negativo")
    private BigDecimal precoUnitario;

    private Long categoriaId;
}
