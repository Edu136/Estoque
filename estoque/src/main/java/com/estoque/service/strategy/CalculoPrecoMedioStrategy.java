package com.estoque.service.strategy;

import com.estoque.domain.model.Produto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Estratégia: calcula o valor médio do estoque
 * dividindo o valor total pelo número de produtos distintos.
 */
@Component("precoMedio")
public class CalculoPrecoMedioStrategy implements CalculoEstoqueStrategy {

    @Override
    public BigDecimal calcularValorEstoque(List<Produto> produtos) {
        if (produtos.isEmpty()) return BigDecimal.ZERO;

        BigDecimal total = produtos.stream()
                .map(p -> p.getPrecoUnitario()
                           .multiply(BigDecimal.valueOf(p.getQuantidadeAtual())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(
                BigDecimal.valueOf(produtos.size()),
                2,
                RoundingMode.HALF_UP
        );
    }
}
