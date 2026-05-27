package com.estoque.service.observer;

import com.estoque.domain.model.Produto;

/**
 * Padrão Observer: interface para quem deseja ser notificado
 * quando um produto atingir o estoque mínimo.
 *
 * ISP: interface coesa, com apenas uma responsabilidade.
 * DIP: EstoqueService depende desta abstração, não de implementações concretas.
 */
public interface EstoqueObserver {
    void onEstoqueBaixo(Produto produto);
}
