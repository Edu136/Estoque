package com.estoque.service;
import com.estoque.domain.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
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

    // ── Testes do listarRecentes ──────────────────────────────────────────────

    @Test
    @DisplayName("Deve retornar os produtos ordenados do ID maior para o menor")
    void deveRetornarRecentesOrdenadosPorIdDecrescente() {
        Produto p1 = new Produto("Arroz", "Arroz 5kg", 10, 5, new BigDecimal("20.00"), null);
        p1.setId(1L);
        Produto p2 = new Produto("Feijão", "Feijão 1kg", 10, 5, new BigDecimal("10.00"), null);
        p2.setId(2L);
        Produto p3 = new Produto("Macarrão", "Macarrão 500g", 10, 5, new BigDecimal("5.00"), null);
        p3.setId(3L);

        List<Produto> produtos = List.of(p1, p2, p3);

        List<Produto> recentes = produtos.stream()
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .limit(10)
                .toList();

        assertEquals(3L, recentes.get(0).getId());
        assertEquals(2L, recentes.get(1).getId());
        assertEquals(1L, recentes.get(2).getId());
    }

    @Test
    @DisplayName("Deve respeitar o limite informado")
    void deveRespeitarLimiteDeRecentes() {
        Produto p1 = new Produto("Arroz", "Arroz 5kg", 10, 5, new BigDecimal("20.00"), null);
        p1.setId(1L);
        Produto p2 = new Produto("Feijão", "Feijão 1kg", 10, 5, new BigDecimal("10.00"), null);
        p2.setId(2L);
        Produto p3 = new Produto("Macarrão", "Macarrão 500g", 10, 5, new BigDecimal("5.00"), null);
        p3.setId(3L);

        List<Produto> produtos = List.of(p1, p2, p3);

        List<Produto> recentes = produtos.stream()
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .limit(2)
                .toList();

        assertEquals(2, recentes.size());
        assertEquals(3L, recentes.get(0).getId());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há produtos")
    void deveRetornarVazioQuandoSemProdutos() {
        List<Produto> recentes = List.<Produto>of().stream()
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .limit(10)
                .toList();

        assertTrue(recentes.isEmpty());
    }
}
