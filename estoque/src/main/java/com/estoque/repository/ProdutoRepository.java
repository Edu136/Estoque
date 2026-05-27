package com.estoque.repository;

import com.estoque.domain.model.Categoria;
import com.estoque.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DIP: EstoqueService e ProdutoService dependem desta interface,
 * não da implementação concreta gerada pelo Spring Data.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByCategoria(Categoria categoria);

    // Busca todos os produtos em que quantidadeAtual <= estoqueMinimo
    @Query("SELECT p FROM Produto p WHERE p.quantidadeAtual <= p.estoqueMinimo")
    List<Produto> findAbaixoDoEstoqueMinimo();

    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
