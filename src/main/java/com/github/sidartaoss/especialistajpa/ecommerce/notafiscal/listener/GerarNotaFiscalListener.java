package com.github.sidartaoss.especialistajpa.ecommerce.notafiscal.listener;

import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import com.github.sidartaoss.especialistajpa.ecommerce.notafiscal.servico.ServicoNotaFiscal;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class GerarNotaFiscalListener {

    private final ServicoNotaFiscal servicoNotaFiscal = new ServicoNotaFiscal();

    @PrePersist
    @PreUpdate
    public void gerar(Pedido pedido) {
        if (pedido.isPago() && pedido.getNotaFiscal() == null) {
            this.servicoNotaFiscal.gerar(pedido);
        }
    }
}
