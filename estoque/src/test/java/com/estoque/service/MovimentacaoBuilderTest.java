package com.estoque.service;

import com.estoque.builder.MovimentacaoBuilder;
import com.estoque.domain.enums.TipoMovimentacao;
import com.estoque.domain.model.Fornecedor;
import com.estoque.domain.model.Movimentacao;
import com.estoque.domain.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MovimentacaoBuilderTest {

    private Produto produto;
    private Fornecedor fornecedor;

    @BeforeEach
    void setUp() {
        produto = new Produto("Café 500g", "", 50, 10, new BigDecimal("12.00"), null);
        fornecedor = new Fornecedor("Café do Sul", "contato@cafedosul.com", "98.765.432/0001-10");
    }

    @Test
    @DisplayName("Deve construir movimentação de entrada com sucesso")
    void deveConstruirEntradaComSucesso() {
        Movimentacao mov = new MovimentacaoBuilder()
                .produto(produto)
                .tipo(TipoMovimentacao.ENTRADA)
                .quantidade(30)
                .fornecedor(fornecedor)
                .observacao("Pedido semanal")
                .build();

        assertNotNull(mov);
        assertEquals(produto, mov.getProduto());
        assertEquals(TipoMovimentacao.ENTRADA, mov.getTipo());
        assertEquals(30, mov.getQuantidade());
        assertEquals(fornecedor, mov.getFornecedor());
        assertNotNull(mov.getDataHora());
    }

    @Test
    @DisplayName("Deve construir movimentação de saída sem fornecedor")
    void deveConstruirSaidaSemFornecedor() {
        Movimentacao mov = new MovimentacaoBuilder()
                .produto(produto)
                .tipo(TipoMovimentacao.SAIDA)
                .quantidade(5)
                .build();

        assertNotNull(mov);
        assertEquals(TipoMovimentacao.SAIDA, mov.getTipo());
        assertNull(mov.getFornecedor());
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto é nulo")
    void deveLancarExcecaoSemProduto() {
        assertThrows(IllegalStateException.class, () ->
                new MovimentacaoBuilder()
                        .tipo(TipoMovimentacao.SAIDA)
                        .quantidade(5)
                        .build()
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando quantidade é zero ou negativa")
    void deveLancarExcecaoQuantidadeInvalida() {
        assertThrows(IllegalStateException.class, () ->
                new MovimentacaoBuilder()
                        .produto(produto)
                        .tipo(TipoMovimentacao.SAIDA)
                        .quantidade(0)
                        .build()
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando entrada não tem fornecedor")
    void deveLancarExcecaoEntradaSemFornecedor() {
        assertThrows(IllegalStateException.class, () ->
                new MovimentacaoBuilder()
                        .produto(produto)
                        .tipo(TipoMovimentacao.ENTRADA)
                        .quantidade(10)
                        .build() // sem fornecedor
        );
    }
}
