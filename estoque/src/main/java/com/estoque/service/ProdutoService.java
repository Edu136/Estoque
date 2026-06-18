package com.estoque.service;

import com.estoque.domain.model.Categoria;
import com.estoque.domain.model.Produto;
import com.estoque.dto.ProdutoRequestDTO;
import com.estoque.dto.ProdutoResponseDTO;
import com.estoque.repository.CategoriaRepository;
import com.estoque.repository.ProdutoRepository;
import com.estoque.exception.ProdutoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto) {
        Produto produto = toEntity(dto);
        return ProdutoResponseDTO.de(produtoRepository.save(produto));
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = buscarEntidadePorId(id);
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setEstoqueMinimo(dto.getEstoqueMinimo());
        produto.setPrecoUnitario(dto.getPrecoUnitario());
        produto.setCategoria(resolverCategoria(dto.getCategoriaId()));
        return ProdutoResponseDTO.de(produtoRepository.save(produto));
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        return ProdutoResponseDTO.de(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(ProdutoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(ProdutoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarRecentes(int limite) {
        return produtoRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .limit(limite)
                .map(ProdutoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(Long id) {
        buscarEntidadePorId(id);
        produtoRepository.deleteById(id);
    }

    @Transactional
    public ProdutoResponseDTO adicionarQuantidade(Long id, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade a adicionar deve ser pelo menos 1");
        }
        Produto produto = buscarEntidadePorId(id);
        produto.atualizarQuantidade(quantidade);
        return ProdutoResponseDTO.de(produtoRepository.save(produto));
    }

    // Usado internamente por outros services que precisam da entidade
    @Transactional(readOnly = true)
    public Produto buscarEntidadePorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    private Produto toEntity(ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setQuantidadeAtual(dto.getQuantidadeAtual());
        produto.setEstoqueMinimo(dto.getEstoqueMinimo());
        produto.setPrecoUnitario(dto.getPrecoUnitario());
        produto.setCategoria(resolverCategoria(dto.getCategoriaId()));
        return produto;
    }

    private Categoria resolverCategoria(Long categoriaId) {
        if (categoriaId == null) return null;
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada: " + categoriaId));
    }
}
