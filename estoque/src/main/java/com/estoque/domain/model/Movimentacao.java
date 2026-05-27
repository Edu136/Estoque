package com.estoque.domain.model;

import com.estoque.domain.enums.TipoMovimentacao;
import jakarta.persistence.*;
import java.time.LocalDateTime;

// SRP: Movimentacao representa apenas o registro de uma entrada ou saída
@Entity
@Table(name = "movimentacoes")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    private String observacao;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor; // preenchido apenas em ENTRADAs

    // Construtor usado pelo Builder (padrão Builder)
    public Movimentacao() {}

    // Getters
    public Long getId() { return id; }
    public Produto getProduto() { return produto; }
    public TipoMovimentacao getTipo() { return tipo; }
    public int getQuantidade() { return quantidade; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getObservacao() { return observacao; }
    public Fornecedor getFornecedor() { return fornecedor; }

    // Setters usados pelo Builder
    public void setProduto(Produto produto) { this.produto = produto; }
    public void setTipo(TipoMovimentacao tipo) { this.tipo = tipo; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }
}
