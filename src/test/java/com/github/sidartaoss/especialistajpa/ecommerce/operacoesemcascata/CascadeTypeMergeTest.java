package com.github.sidartaoss.especialistajpa.ecommerce.operacoesemcascata;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CascadeTypeMergeTest extends EntityManagerTest {

//    @Test
    void deveAtualizarPedidoComItens() {
        // arrange
        var idDoCliente = 1;
        var idDoProduto = 1;
        Cliente clienteConsultado = entityManager.find(Cliente.class, idDoCliente);
        Produto produtoConsultado = entityManager.find(Produto.class, idDoProduto);

        var idDoPedido = 1;
        var notaFiscalId = 1;
        var status = StatusPedido.AGUARDANDO;
        Pedido pedidoConsultado = new Pedido.Builder(clienteConsultado, notaFiscalId, status)
                .id(idDoPedido)
                .build();

        var precoProduto = produtoConsultado.getPreco();
        var quantidadeParaAlterar = 3;
        var idDoItemPedido = new ItemPedidoId(pedidoConsultado.getId(), produtoConsultado.getId());
        ItemPedido itemPedidoParaAlterar = new ItemPedido(idDoItemPedido, pedidoConsultado, produtoConsultado, precoProduto, quantidadeParaAlterar);

        var itensParaAlterar = List.of(itemPedidoParaAlterar);
        pedidoConsultado.informarItens(itensParaAlterar);

        // act
        entityManager.getTransaction().begin();
        pedidoConsultado = entityManager.merge(pedidoConsultado);  // CascadeType.MERGE OneToMany Pedido
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        ItemPedido itemPedidoParaVerificacao = entityManager.find(ItemPedido.class, idDoItemPedido);
        assertNotNull(itemPedidoParaVerificacao);
        assertEquals(quantidadeParaAlterar, itemPedidoParaVerificacao.getQuantidade());

        Pedido pedidoParaVerificacao = entityManager.find(Pedido.class, idDoPedido);
        assertEquals(precoProduto.multiply(BigDecimal.valueOf(quantidadeParaAlterar)), pedidoParaVerificacao.getValorTotal());

    }

//    @Test
    void deveAtualizarItemPedidoComPedido() {
        // arrange
        var idDoCliente = 1;
        var idDoProduto = 1;
        Cliente clienteConsultado = entityManager.find(Cliente.class, idDoCliente);
        Produto produtoConsultado = entityManager.find(Produto.class, idDoProduto);

        var idDoPedido = 1;
        var notaFiscalId = 1;
        var statusParaAlterar = StatusPedido.PAGO;
        Pedido pedidoParaAlterar = new Pedido.Builder(clienteConsultado, notaFiscalId, statusParaAlterar)
                .id(idDoPedido)
                .build();

        var idDoItemPedido = new ItemPedidoId(pedidoParaAlterar.getId(), produtoConsultado.getId());
        var precoProduto = produtoConsultado.getPreco();
        var quantidade = 1;
        ItemPedido itemPedidoConsultado = new ItemPedido(idDoItemPedido, pedidoParaAlterar, produtoConsultado, precoProduto, quantidade);

        var itens = List.of(itemPedidoConsultado);
        pedidoParaAlterar.informarItens(itens);

        // act
        entityManager.getTransaction().begin();
        // Não precisa setar Cascade.PERSIST, porque pedido_id faz parte da Primary Key, através da anotação MapsId().
        entityManager.merge(itemPedidoConsultado);  // CascadeType.MERGE ManyToOne ItemPedido
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Pedido pedidoParaVerificacao = entityManager.find(Pedido.class, idDoPedido);
        assertNotNull(pedidoParaVerificacao);
        assertEquals(statusParaAlterar, pedidoParaVerificacao.getStatus());
    }

}
