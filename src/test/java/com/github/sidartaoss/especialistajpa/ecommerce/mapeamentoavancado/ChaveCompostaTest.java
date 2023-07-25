package com.github.sidartaoss.especialistajpa.ecommerce.mapeamentoavancado;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChaveCompostaTest extends EntityManagerTest {

    @Test
    void deveSalvarItem() {
        // arrange
        entityManager.getTransaction().begin();

        var idDoCliente = 1;
        var idDoProduto = 1;
        Cliente cliente = entityManager.find(Cliente.class, idDoCliente);
        Produto produto = entityManager.find(Produto.class, idDoProduto);

        var notaFiscalId = 1;
        var status = StatusPedido.AGUARDANDO;
        Pedido pedido = new Pedido.Builder(cliente, notaFiscalId, status)
                .build();

        var precoProduto = produto.getPreco();
        var quantidade = 5;
        ItemPedido itemPedido = new ItemPedido(pedido, produto, precoProduto, quantidade);

        // act
        entityManager.persist(pedido);
        entityManager.persist(itemPedido);
        entityManager.getTransaction().commit();

        // Limpar memória do EntityManager
        entityManager.clear();

        // assert
        Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoVerificacao);
        assertFalse(pedidoVerificacao.getItens().isEmpty());
    }

    @Test
    void deveBuscarItem() {
        // arrange
        var idDoProduto = 3;
        var idDoPedido = 1;
        var idDoItemPedido = new ItemPedidoId(idDoPedido, idDoProduto);

        // act
        ItemPedido itemPedido = entityManager.find(ItemPedido.class, idDoItemPedido);

        // assert
        assertNotNull(itemPedido);
    }
}
