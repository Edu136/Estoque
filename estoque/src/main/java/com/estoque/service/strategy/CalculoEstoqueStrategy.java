package com.estoque.service.strategy;

import com.estoque.domain.model.Produto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Padrão Strategy: abstrai o algoritmo de cálculo do valor total do estoque.
 *
 * OCP: novas estratégias de cálculo podem ser adicionadas sem alterar EstoqueService.
 * DIP: EstoqueService depende desta interface, não de implementações concretas.
 */
public interface CalculoEstoqueStrategy {
    BigDecimal calcularValorEstoque(List<Produto> produtos);
}
