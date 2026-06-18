package com.estoque.controller;

import com.estoque.dto.ProdutoRequestDTO;
import com.estoque.dto.ProdutoResponseDTO;
import com.estoque.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * SRP: Controller cuida apenas do mapeamento HTTP → Service.
 * Não contém regras de negócio.
 */
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/recentes")
    public ResponseEntity<List<ProdutoResponseDTO>> listarRecentes(
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(produtoService.listarRecentes(limite));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(produtoService.buscarPorNome(nome));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id,
                                                        @Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/adicionar-quantidade")
    public ResponseEntity<ProdutoResponseDTO> adicionarQuantidade(@PathVariable Long id,
                                                                   @RequestParam int quantidade) {
        return ResponseEntity.ok(produtoService.adicionarQuantidade(id, quantidade));
    }
}
