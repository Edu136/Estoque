package com.estoque.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Schema(description = "Dados para cadastro ou atualização de produto")
@Getter
@Setter
public class ProdutoRequestDTO {

    @Schema(example = "Arroz Tipo 1")
    @NotBlank(message = "Nome do produto é obrigatório")
    private String nome;

    @Schema(example = "Pacote 5kg")
    private String descricao;

    @Schema(example = "50")
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    private int quantidadeAtual;

    @Schema(example = "10")
    @Min(value = 0, message = "Estoque mínimo não pode ser negativo")
    private int estoqueMinimo;

    @Schema(example = "29.90")
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", message = "Preço não pode ser negativo")
    private BigDecimal precoUnitario;

    @Schema(example = "null", nullable = true)
    private Long categoriaId;
}
