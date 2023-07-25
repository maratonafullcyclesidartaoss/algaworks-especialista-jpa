package com.github.sidartaoss.especialistajpa.ecommerce.relacionamentos;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelacionamentoOneToManyTest extends EntityManagerTest {

    @Test
    void deveVerificarRelacionamento() {
        // arrange
        Cliente cliente = entityManager.find(Cliente.class, 1);
        var logradouro = "Rua abc";
        var numero = "111";
        var bairro = "Bairro Centro";
        var cidade = "São Paulo";
        var estado = "SP";
        var cep = "90999300";
        var enderecoEntrega = new Pedido.EnderecoEntrega.Builder(logradouro, numero, bairro, cidade, estado)
                .cep(cep)
                .build();
        var notaFiscalId = 1;
        var status = StatusPedido.AGUARDANDO;
        Pedido pedido = new Pedido.Builder(cliente, notaFiscalId, status)
                .enderecoEntrega(enderecoEntrega)
                .build();

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Cliente clienteVerificacao = entityManager.find(Cliente.class, pedido.getCliente().getId());
        assertNotNull(clienteVerificacao);
        assertFalse(clienteVerificacao.getPedidos().isEmpty());
    }

    @Test
    void deveVerificarRelacionamentoPedido() {
        // arrange
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);

        var notaFiscalId = 1;
        var status = StatusPedido.AGUARDANDO;
        Pedido pedido = new Pedido.Builder(cliente, notaFiscalId, status)
                .build();

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);

        var quantidade = 1;
        ItemPedido itemPedido = new ItemPedido(pedido, produto, produto.getPreco(), quantidade);
        entityManager.persist(itemPedido);

        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Pedido pedidoInserido = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoInserido);

        var idDoItemPedido = new ItemPedidoId(pedido.getId(), produto.getId());
        ItemPedido itemPedidoInserido = entityManager.find(ItemPedido.class, idDoItemPedido);
        assertNotNull(itemPedidoInserido);
        assertNotNull(itemPedidoInserido.getPedido());
        assertEquals(pedidoInserido.getId(), itemPedidoInserido.getPedido().getId());
        assertNotNull(pedidoInserido.getItens());
        assertFalse(pedidoInserido.getItens().isEmpty());
    }
}