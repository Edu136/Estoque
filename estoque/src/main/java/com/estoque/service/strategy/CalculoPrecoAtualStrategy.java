package com.estoque.service.strategy;

import com.estoque.domain.model.Produto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Estratégia: calcula o valor total multiplicando
 * o preço unitário atual pela quantidade disponível.
 */
@Primary
@Component("precoAtual")
public class CalculoPrecoAtualStrategy implements CalculoEstoqueStrategy {

    @Override
    public BigDecimal calcularValorEstoque(List<Produto> produtos) {
        return produtos.stream()
                .map(p -> p.getPrecoUnitario()
                           .multiply(BigDecimal.valueOf(p.getQuantidadeAtual())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
