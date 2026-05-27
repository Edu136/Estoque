package com.estoque.controller;

import com.estoque.domain.model.Movimentacao;
import com.estoque.domain.model.Produto;
import com.estoque.dto.EntradaDTO;
import com.estoque.dto.SaidaDTO;
import com.estoque.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping("/entrada")
    public ResponseEntity<Movimentacao> registrarEntrada(@Valid @RequestBody EntradaDTO dto) {
        Movimentacao mov = estoqueService.registrarEntrada(
                dto.getProdutoId(),
                dto.getQuantidade(),
                dto.getFornecedorId(),
                dto.getObservacao()
        );
        return ResponseEntity.ok(mov);
    }

    @PostMapping("/saida")
    public ResponseEntity<Movimentacao> registrarSaida(@Valid @RequestBody SaidaDTO dto) {
        Movimentacao mov = estoqueService.registrarSaida(
                dto.getProdutoId(),
                dto.getQuantidade(),
                dto.getObservacao()
        );
        return ResponseEntity.ok(mov);
    }

    @GetMapping("/abaixo-minimo")
    public ResponseEntity<List<Produto>> listarAbaixoDoMinimo() {
        return ResponseEntity.ok(estoqueService.listarAbaixoDoMinimo());
    }

    @GetMapping("/movimentacoes/{produtoId}")
    public ResponseEntity<List<Movimentacao>> listarMovimentacoes(@PathVariable Long produtoId) {
        return ResponseEntity.ok(estoqueService.listarMovimentacoes(produtoId));
    }

    @GetMapping("/valor-total")
    public ResponseEntity<BigDecimal> calcularValorTotal() {
        return ResponseEntity.ok(estoqueService.calcularValorTotalEstoque());
    }
}
