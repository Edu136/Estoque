package com.estoque.service;

import com.estoque.domain.model.Produto;
import com.estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SRP: ProdutoService cuida exclusivamente do CRUD de produtos.
 * DIP: depende da interface ProdutoRepository, não da implementação JPA.
 */
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    // Injeção por construtor (DIP)
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Produto cadastrar(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto atualizar(Long id, Produto dados) {
        Produto produto = buscarPorId(id);
        produto.setNome(dados.getNome());
        produto.setDescricao(dados.getDescricao());
        produto.setEstoqueMinimo(dados.getEstoqueMinimo());
        produto.setPrecoUnitario(dados.getPrecoUnitario());
        produto.setCategoria(dados.getCategoria());
        return produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional
    public void deletar(Long id) {
        buscarPorId(id); // valida existência antes
        produtoRepository.deleteById(id);
    }
}
