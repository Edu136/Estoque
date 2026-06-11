package com.estoque.service;

import com.estoque.domain.enums.TipoMovimentacao;
import com.estoque.domain.model.Categoria;
import com.estoque.domain.model.Fornecedor;
import com.estoque.domain.model.Movimentacao;
import com.estoque.domain.model.Produto;
import com.estoque.repository.FornecedorRepository;
import com.estoque.repository.MovimentacaoRepository;
import com.estoque.repository.ProdutoRepository;
import com.estoque.service.observer.EstoqueObserver;
import com.estoque.service.strategy.CalculoPrecoAtualStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

    @Mock private ProdutoRepository produtoRepository;
    @Mock private MovimentacaoRepository movimentacaoRepository;
    @Mock private FornecedorRepository fornecedorRepository;

    private EstoqueService estoqueService;
    private EstoqueObserver observerMock;

    private Produto produto;
    private Fornecedor fornecedor;

    @BeforeEach
    void setUp() {
        observerMock = mock(EstoqueObserver.class);

        estoqueService = new EstoqueService(
                produtoRepository,
                movimentacaoRepository,
                fornecedorRepository,
                new CalculoPrecoAtualStrategy(),
                new ArrayList<>()
        );
        estoqueService.addObserver(observerMock);

        Categoria categoria = new Categoria("Alimentos", "Produtos alimentícios");

        produto = new Produto("Arroz 5kg", "Arroz tipo 1",
                10, 5, new BigDecimal("25.00"), categoria);

        fornecedor = new Fornecedor("Distribuidora ABC", "(11) 99999-9999", "12.345.678/0001-00");
    }

    // ───── Testes de Entrada ─────

    @Test
    @DisplayName("Deve registrar entrada de produto com sucesso")
    void deveRegistrarEntradaComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(produtoRepository.save(any())).thenReturn(produto);
        when(movimentacaoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Movimentacao resultado = estoqueService.registrarEntrada(1L, 20, 1L, "Reposição semanal");

        assertNotNull(resultado);
        assertEquals(TipoMovimentacao.ENTRADA, resultado.getTipo());
        assertEquals(20, resultado.getQuantidade());
        assertEquals(30, produto.getQuantidadeAtual()); // 10 + 20
        verify(movimentacaoRepository).save(any(Movimentacao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não encontrado na entrada")
    void deveLancarExcecaoQuandoProdutoNaoEncontradoNaEntrada() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> estoqueService.registrarEntrada(99L, 10, 1L, null));

        assertTrue(ex.getMessage().contains("99"));
    }

    // ───── Testes de Saída ─────

    @Test
    @DisplayName("Deve registrar saída de produto com sucesso")
    void deveRegistrarSaidaComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any())).thenReturn(produto);
        when(movimentacaoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Movimentacao resultado = estoqueService.registrarSaida(1L, 3, "Venda balcão");

        assertNotNull(resultado);
        assertEquals(TipoMovimentacao.SAIDA, resultado.getTipo());
        assertEquals(7, produto.getQuantidadeAtual()); // 10 - 3
    }

    @Test
    @DisplayName("Deve lançar exceção quando estoque insuficiente para saída")
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        assertThrows(IllegalArgumentException.class,
                () -> estoqueService.registrarSaida(1L, 50, "Venda"));
    }

    // ───── Testes do Observer ─────

    @Test
    @DisplayName("Deve notificar observer quando estoque fica abaixo do mínimo após saída")
    void deveNotificarObserverQuandoEstoqueFicaBaixo() {
        produto.setQuantidadeAtual(6); // estoqueMinimo = 5, vai ficar em 1

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any())).thenReturn(produto);
        when(movimentacaoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        estoqueService.registrarSaida(1L, 5, "Venda");

        // Observer deve ser chamado pois 1 <= 5 (estoqueMinimo)
        verify(observerMock, times(1)).onEstoqueBaixo(produto);
    }

    @Test
    @DisplayName("Não deve notificar observer quando estoque está acima do mínimo")
    void naoDeveNotificarObserverQuandoEstoqueAdequado() {
        produto.setQuantidadeAtual(20);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any())).thenReturn(produto);
        when(movimentacaoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        estoqueService.registrarSaida(1L, 3, "Venda");

        // 17 > 5 (estoqueMinimo), não deve notificar
        verify(observerMock, never()).onEstoqueBaixo(any());
    }

    // ───── Testes da Strategy.. ─────

    @Test
    @DisplayName("Deve calcular valor total do estoque usando Strategy de preço atual")
    void deveCalcularValorTotalEstoque() {
        Produto p1 = new Produto("Produto A", "", 10, 2, new BigDecimal("5.00"), null);
        Produto p2 = new Produto("Produto B", "", 4, 1, new BigDecimal("10.00"), null);

        when(produtoRepository.findAll()).thenReturn(List.of(p1, p2));

        BigDecimal total = estoqueService.calcularValorTotalEstoque();

        // (10 * 5.00) + (4 * 10.00) = 50 + 40 = 90
        assertEquals(new BigDecimal("90.00"), total);
    }

    @Test
    @DisplayName("Deve retornar zero quando estoque vazio")
    void deveRetornarZeroParaEstoqueVazio() {
        when(produtoRepository.findAll()).thenReturn(List.of());

        BigDecimal total = estoqueService.calcularValorTotalEstoque();

        assertEquals(BigDecimal.ZERO, total);
    }

    // ───── Testes de Listagem ─────

    @Test
    @DisplayName("Deve listar produtos abaixo do estoque mínimo")
    void deveListarProdutosAbaixoDoMinimo() {
        Produto critico = new Produto("Feijão 1kg", "", 2, 10, new BigDecimal("8.00"), null);

        when(produtoRepository.findAbaixoDoEstoqueMinimo()).thenReturn(List.of(critico));

        List<Produto> resultado = estoqueService.listarAbaixoDoMinimo();

        assertEquals(1, resultado.size());
        assertEquals("Feijão 1kg", resultado.get(0).getNome());
    }
}
