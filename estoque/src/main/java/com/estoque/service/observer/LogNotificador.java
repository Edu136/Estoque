package com.estoque.service.observer;

import com.estoque.domain.model.Produto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementação concreta do Observer.
 * Registra um aviso no log quando o estoque está baixo.
 *
 * OCP: nova implementação sem alterar EstoqueObserver ou EstoqueService.
 */
@Component
public class LogNotificador implements EstoqueObserver {

    private static final Logger log = LoggerFactory.getLogger(LogNotificador.class);

    @Override
    public void onEstoqueBaixo(Produto produto) {
        log.warn("[ALERTA DE ESTOQUE] Produto '{}' (id={}) está abaixo do mínimo. " +
                 "Atual: {} | Mínimo: {}",
                 produto.getNome(),
                 produto.getId(),
                 produto.getQuantidadeAtual(),
                 produto.getEstoqueMinimo());
    }
}
