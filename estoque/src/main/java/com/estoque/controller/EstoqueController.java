package com.estoque.controller;

import com.estoque.dto.EntradaDTO;
import com.estoque.dto.MovimentacaoResponseDTO;
import com.estoque.dto.ProdutoResponseDTO;
import com.estoque.dto.SaidaDTO;
import com.estoque.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Estoque")
@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @Operation(summary = "Registrar entrada de estoque")
    @PostMapping("/entrada")
    public ResponseEntity<MovimentacaoResponseDTO> registrarEntrada(@Valid @RequestBody EntradaDTO dto) {
        return ResponseEntity.ok(estoqueService.registrarEntrada(
                dto.getProdutoId(),
                dto.getQuantidade(),
                dto.getFornecedorId(),
                dto.getObservacao()
        ));
    }

    @Operation(summary = "Registrar saída de estoque")
    @PostMapping("/saida")
    public ResponseEntity<MovimentacaoResponseDTO> registrarSaida(@Valid @RequestBody SaidaDTO dto) {
        return ResponseEntity.ok(estoqueService.registrarSaida(
                dto.getProdutoId(),
                dto.getQuantidade(),
                dto.getObservacao()
        ));
    }

    @Operation(summary = "Listar produtos abaixo do estoque mínimo")
    @GetMapping("/abaixo-minimo")
    public ResponseEntity<List<ProdutoResponseDTO>> listarAbaixoDoMinimo() {
        return ResponseEntity.ok(estoqueService.listarAbaixoDoMinimo());
    }

    @Operation(summary = "Listar histórico de movimentações de um produto")
    @GetMapping("/movimentacoes/{produtoId}")
    public ResponseEntity<List<MovimentacaoResponseDTO>> listarMovimentacoes(
            @PathVariable Long produtoId) {
        return ResponseEntity.ok(estoqueService.listarMovimentacoes(produtoId));
    }

    @Operation(summary = "Calcular valor total do estoque")
    @GetMapping("/valor-total")
    public ResponseEntity<BigDecimal> calcularValorTotal() {
        return ResponseEntity.ok(estoqueService.calcularValorTotalEstoque());
    }
}
