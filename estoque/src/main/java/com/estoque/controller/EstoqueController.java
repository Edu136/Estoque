package com.estoque.controller;

import com.estoque.dto.EntradaDTO;
import com.estoque.dto.MovimentacaoResponseDTO;
import com.estoque.dto.ProdutoResponseDTO;
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
    public ResponseEntity<MovimentacaoResponseDTO> registrarEntrada(@Valid @RequestBody EntradaDTO dto) {
        return ResponseEntity.ok(estoqueService.registrarEntrada(
                dto.getProdutoId(),
                dto.getQuantidade(),
                dto.getFornecedorId(),
                dto.getObservacao()
        ));
    }

    @PostMapping("/saida")
    public ResponseEntity<MovimentacaoResponseDTO> registrarSaida(@Valid @RequestBody SaidaDTO dto) {
        return ResponseEntity.ok(estoqueService.registrarSaida(
                dto.getProdutoId(),
                dto.getQuantidade(),
                dto.getObservacao()
        ));
    }

    @GetMapping("/abaixo-minimo")
    public ResponseEntity<List<ProdutoResponseDTO>> listarAbaixoDoMinimo() {
        return ResponseEntity.ok(estoqueService.listarAbaixoDoMinimo());
    }

    @GetMapping("/movimentacoes/{produtoId}")
    public ResponseEntity<List<MovimentacaoResponseDTO>> listarMovimentacoes(
            @PathVariable Long produtoId) {
        return ResponseEntity.ok(estoqueService.listarMovimentacoes(produtoId));
    }

    @GetMapping("/valor-total")
    public ResponseEntity<BigDecimal> calcularValorTotal() {
        return ResponseEntity.ok(estoqueService.calcularValorTotalEstoque());
    }
}
