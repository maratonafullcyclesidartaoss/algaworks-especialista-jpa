package com.github.sidartaoss.especialistajpa.ecommerce.notafiscal.servico;

import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;

public class ServicoNotaFiscal {

    public void gerar(Pedido pedido) {
        System.out.println("Gerando nota para o pedido: " + pedido.getId());
    }
}
