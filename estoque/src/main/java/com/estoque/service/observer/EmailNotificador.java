package com.estoque.service.observer;

import com.estoque.domain.model.Produto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementação concreta do Observer.
 * Simula envio de e-mail quando o estoque está baixo.
 *
 * OCP: nova implementação adicionada sem modificar código existente.
 * LSP: substitui qualquer EstoqueObserver sem quebrar o comportamento esperado.
 */
@Component
public class EmailNotificador implements EstoqueObserver {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificador.class);

    // Em produção, seria injetado via @Value("${notificacao.email.destinatario}")
    private final String destinatario = "gerente@comercio.com";

    @Override
    public void onEstoqueBaixo(Produto produto) {
        // Simulação: em produção usaria JavaMailSender
        log.info("[SIMULANDO E-MAIL] Para: {} | Produto '{}' atingiu estoque mínimo ({} unidades).",
                 destinatario,
                 produto.getNome(),
                 produto.getEstoqueMinimo());
    }
}
