package com.github.sidartaoss.especialistajpa.ecommerce.mapeamentoavancado;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MapsIdTest extends EntityManagerTest {

    @Test
    void deveInserirNotaFiscal() {
        // arrange
        var idDoPedido = 3;
        Pedido pedido = entityManager.find(Pedido.class, idDoPedido);

        var dataEmissao = Instant.now();
        var xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                """;
        NotaFiscal notaFiscal = new NotaFiscal(pedido, xml.getBytes(), dataEmissao);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(notaFiscal);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        NotaFiscal notaFiscalVerificacao = entityManager.find(NotaFiscal.class, notaFiscal.getId());
        assertNotNull(notaFiscalVerificacao);
        assertEquals(pedido.getId(), notaFiscalVerificacao.getId());
    }

    @Test
    void deveInserirItemPedido() {
        // arrange
        var clienteId = 1;
        var produtoId = 1;
        Cliente cliente = entityManager.find(Cliente.class, clienteId);
        Produto produto = entityManager.find(Produto.class, produtoId);

        var notaFiscalId = 1;
        var status = StatusPedido.AGUARDANDO;
        Pedido pedido = new Pedido.Builder(cliente, notaFiscalId, status)
                .build();

        var precoProduto = produto.getPreco();
        var quantidade = 5;
        ItemPedido itemPedido = new ItemPedido(pedido, produto, precoProduto, quantidade);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.persist(itemPedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        var idDoItemPedido = new ItemPedidoId(pedido.getId(), produto.getId());
        ItemPedido itemPedidoVerificacao = entityManager.find(ItemPedido.class, idDoItemPedido);
        assertNotNull(itemPedidoVerificacao);
    }
}
