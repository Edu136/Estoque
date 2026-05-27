package com.estoque.repository;

import com.estoque.domain.model.Movimentacao;
import com.estoque.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByProduto(Produto produto);

    List<Movimentacao> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Movimentacao> findByProdutoOrderByDataHoraDesc(Produto produto);
}
