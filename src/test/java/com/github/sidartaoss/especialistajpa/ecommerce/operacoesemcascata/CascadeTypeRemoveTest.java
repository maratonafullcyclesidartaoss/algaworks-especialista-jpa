package com.github.sidartaoss.especialistajpa.ecommerce.operacoesemcascata;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CascadeTypeRemoveTest extends EntityManagerTest {

//    @Test
    void deveRemoverItensOrfaos() {
        // arrange
        var idDoPedido = 1;
        Pedido pedidoConsultado = entityManager.find(Pedido.class, idDoPedido);

        assertFalse(pedidoConsultado.getItens().isEmpty());

        // act
        entityManager.getTransaction().begin();
        pedidoConsultado.removerItens();
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Pedido pedidoVerificacao = entityManager.find(Pedido.class, idDoPedido);
        assertTrue(pedidoVerificacao.getItens().isEmpty());
    }

    @Test
    void deveRemoverRelacaoProdutoCategoria() {
        // arrange
        var idDoProduto = 1;
        Produto produtoConsultado = entityManager.find(Produto.class, idDoProduto);

        // act
        entityManager.getTransaction().begin();
        produtoConsultado.getCategorias().clear();
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Produto produtoParaVerificacao = entityManager.find(Produto.class, idDoProduto);
        assertTrue(produtoParaVerificacao.getCategorias().isEmpty());
    }

//    @Test
    void deveRemoverPedidoEItens() {
        // arrange
        var idDoPedido = 1;
        var pedidoConsultado = entityManager.find(Pedido.class, idDoPedido);

        // act
        entityManager.getTransaction().begin();
//        pedidoConsultado.getItens().forEach(i -> entityManager.remove(i));
        entityManager.remove(pedidoConsultado);  // CascadeType.REMOVE no atributo itens (OneToMany)
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Pedido pedidoParaVerificacao = entityManager.find(Pedido.class, idDoPedido);
        assertNull(pedidoParaVerificacao);
    }

//    @Test
    void deveRemoverItemPedidoEPedido() {
        // arrange
        var idDoPedido = 1;
        var idDoProduto = 1;
        var idDoItemPedido = new ItemPedidoId(idDoPedido, idDoProduto);

        ItemPedido itemPedidoConsultado = entityManager.find(ItemPedido.class, idDoItemPedido);

        // act
        entityManager.getTransaction().begin();
        entityManager.remove(itemPedidoConsultado);  // CascadeType.REMOVE ManyToOne pedido
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        ItemPedido itemPedidoParaVerificacao = entityManager.find(ItemPedido.class, idDoItemPedido);
        assertNull(itemPedidoParaVerificacao);

        Pedido pedidoParaVerificacao = entityManager.find(Pedido.class, idDoPedido);
        assertNull(pedidoParaVerificacao);
    }

}
