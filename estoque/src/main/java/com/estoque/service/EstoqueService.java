package com.estoque.service;

import com.estoque.builder.MovimentacaoBuilder;
import com.estoque.domain.enums.TipoMovimentacao;
import com.estoque.domain.model.Fornecedor;
import com.estoque.domain.model.Movimentacao;
import com.estoque.domain.model.Produto;
import com.estoque.dto.MovimentacaoResponseDTO;
import com.estoque.dto.ProdutoResponseDTO;
import com.estoque.repository.FornecedorRepository;
import com.estoque.repository.MovimentacaoRepository;
import com.estoque.repository.ProdutoRepository;
import com.estoque.exception.ProdutoNaoEncontradoException;
import com.estoque.service.observer.EstoqueObserver;
import com.estoque.service.strategy.CalculoEstoqueStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SRP: EstoqueService cuida exclusivamente das movimentações de estoque.
 * DIP: depende de interfaces (repositories, observers, strategy).
 * OCP: novos observers e strategies podem ser adicionados sem alterar esta classe.
 */
@Service
public class EstoqueService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CalculoEstoqueStrategy calculoStrategy;

    // Padrão Observer: lista de observadores registrados
    private final List<EstoqueObserver> observers = new ArrayList<>();

    public EstoqueService(ProdutoRepository produtoRepository,
                          MovimentacaoRepository movimentacaoRepository,
                          FornecedorRepository fornecedorRepository,
                          CalculoEstoqueStrategy calculoStrategy,
                          List<EstoqueObserver> observers) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.calculoStrategy = calculoStrategy;
        this.observers.addAll(observers); // injeta todos os observers cadastrados no contexto
    }

    // Permite adicionar observers em runtime (útil para testes)
    public void addObserver(EstoqueObserver observer) {
        this.observers.add(observer);
    }

    @Transactional
    public MovimentacaoResponseDTO registrarEntrada(Long produtoId, int quantidade, Long fornecedorId, String obs) {
        Produto produto = buscarProduto(produtoId);
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado: " + fornecedorId));

        // Padrão Builder: constrói a movimentação de forma legível e segura
        Movimentacao movimentacao = new MovimentacaoBuilder()
                .produto(produto)
                .tipo(TipoMovimentacao.ENTRADA)
                .quantidade(quantidade)
                .fornecedor(fornecedor)
                .observacao(obs)
                .build();

        produto.atualizarQuantidade(quantidade);
        produtoRepository.save(produto);

        Movimentacao salva = movimentacaoRepository.save(movimentacao);

        // Verifica se ainda está abaixo do mínimo mesmo após entrada
        if (produto.estaBaixoDoMinimo()) {
            notificarObservers(produto);
        }

        return MovimentacaoResponseDTO.de(salva);
    }

    @Transactional
    public MovimentacaoResponseDTO registrarSaida(Long produtoId, int quantidade, String obs) {
        Produto produto = buscarProduto(produtoId);

        Movimentacao movimentacao = new MovimentacaoBuilder()
                .produto(produto)
                .tipo(TipoMovimentacao.SAIDA)
                .quantidade(quantidade)
                .observacao(obs)
                .build();

        produto.atualizarQuantidade(-quantidade); // lança exceção se insuficiente
        produtoRepository.save(produto);

        Movimentacao salva = movimentacaoRepository.save(movimentacao);

        // Padrão Observer: notifica se atingiu estoque mínimo
        if (produto.estaBaixoDoMinimo()) {
            notificarObservers(produto);
        }

        return MovimentacaoResponseDTO.de(salva);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarAbaixoDoMinimo() {
        return produtoRepository.findAbaixoDoEstoqueMinimo().stream()
                .map(ProdutoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponseDTO> listarMovimentacoes(Long produtoId) {
        Produto produto = buscarProduto(produtoId);
        return movimentacaoRepository.findByProdutoOrderByDataHoraDesc(produto).stream()
                .map(MovimentacaoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularValorTotalEstoque() {
        List<Produto> todos = produtoRepository.findAll();
        // Padrão Strategy: delega o cálculo para a estratégia injetada
        return calculoStrategy.calcularValorEstoque(todos);
    }

    // Notifica todos os observers registrados
    private void notificarObservers(Produto produto) {
        for (EstoqueObserver observer : observers) {
            observer.onEstoqueBaixo(produto);
        }
    }

    private Produto buscarProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }
}
