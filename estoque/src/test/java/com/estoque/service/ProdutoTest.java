package com.estoque.service;

import com.estoque.domain.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto("Leite", "Leite integral 1L",
                10, 5, new BigDecimal("4.50"), null);
    }

    @Test
    @DisplayName("Deve retornar true quando quantidade atual é igual ao mínimo")
    void deveRetornarTrueQuandoIgualAoMinimo() {
        produto.setQuantidadeAtual(5);
        assertTrue(produto.estaBaixoDoMinimo());
    }

    @Test
    @DisplayName("Deve retornar true quando quantidade atual está abaixo do mínimo")
    void deveRetornarTrueQuandoAbaixoDoMinimo() {
        produto.setQuantidadeAtual(2);
        assertTrue(produto.estaBaixoDoMinimo());
    }

    @Test
    @DisplayName("Deve retornar false quando quantidade está acima do mínimo")
    void deveRetornarFalseQuandoAcimaDoMinimo() {
        produto.setQuantidadeAtual(20);
        assertFalse(produto.estaBaixoDoMinimo());
    }

    @Test
    @DisplayName("Deve atualizar quantidade corretamente em uma entrada")
    void deveAtualizarQuantidadeNaEntrada() {
        produto.atualizarQuantidade(10);
        assertEquals(20, produto.getQuantidadeAtual());
    }

    @Test
    @DisplayName("Deve atualizar quantidade corretamente em uma saída")
    void deveAtualizarQuantidadeNaSaida() {
        produto.atualizarQuantidade(-5);
        assertEquals(5, produto.getQuantidadeAtual());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar remover mais do que o disponível")
    void deveLancarExcecaoQuandoQuantidadeInsuficiente() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> produto.atualizarQuantidade(-100));

        assertTrue(ex.getMessage().contains("Estoque insuficiente"));
    }

    @Test
    @DisplayName("Deve permitir saída que zera o estoque")
    void devePermitirSaidaQueZeraEstoque() {
        produto.atualizarQuantidade(-10);
        assertEquals(0, produto.getQuantidadeAtual());
    }
}
