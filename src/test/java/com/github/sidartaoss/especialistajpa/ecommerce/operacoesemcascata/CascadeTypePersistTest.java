package com.github.sidartaoss.especialistajpa.ecommerce.operacoesemcascata;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CascadeTypePersistTest extends EntityManagerTest {

    //    @Test
    void devePersistirPedidoComItens() {
        // arrange
        var idDoCliente = 1;
        var idDoProduto = 1;
        Cliente cliente = entityManager.find(Cliente.class, idDoCliente);
        Produto produto = entityManager.find(Produto.class, idDoProduto);

        var notaFiscalId = 1;
        var status = StatusPedido.AGUARDANDO;
        Pedido pedido = new Pedido.Builder(cliente, notaFiscalId, status)
                .build();

        var precoProduto = produto.getPreco();
        var quantidade = 1;
        ItemPedido itemPedido = new ItemPedido(pedido, produto, precoProduto, quantidade);

        var itens = List.of(itemPedido);
        pedido.informarItens(itens);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);  // CascadeType.PERSIST OneToMany Pedido
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Pedido pedidoParaVerificacao = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoParaVerificacao);
        assertFalse(pedidoParaVerificacao.getItens().isEmpty());
    }

    @Test
    void devePersistirItemPedidoComPedido() {
        // arrange
        var idDoCliente = 1;
        var idDoProduto = 1;
        Cliente cliente = entityManager.find(Cliente.class, idDoCliente);
        Produto produto = entityManager.find(Produto.class, idDoProduto);

        var notaFiscalId = 1;
        var status = StatusPedido.AGUARDANDO;
        Pedido pedido = new Pedido.Builder(cliente, notaFiscalId, status)
                .build();

        var precoProduto = produto.getPreco();
        var quantidade = 1;
        ItemPedido itemPedido = new ItemPedido(pedido, produto, precoProduto, quantidade);

        // act
        entityManager.getTransaction().begin();
        // Não precisa setar Cascade, porque pedido_id faz parte da Primary Key, através da anotação MapsId().
        entityManager.persist(itemPedido);  // CascadeType.PERSIST ManyToOne ItemPedido
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Pedido pedidoParaVerificacao = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoParaVerificacao);
        assertFalse(pedidoParaVerificacao.getItens().isEmpty());
    }

    @Test
    void devePersistirPedidoComCliente() {
        // arrange
        var nome = "Manuel Mineiro";
        var sexo = Sexo.MASCULINO;
        var cpf = "99553970044";
        var cliente = new Cliente(nome, sexo, cpf);
        var dataNascimento = LocalDate.of(1977, 11, 3).atStartOfDay()
                .toInstant(ZoneOffset.UTC);
        cliente.informarDataDeNascimento(dataNascimento);

        var notaFiscalId = 1;
        var status = StatusPedido.AGUARDANDO;
        Pedido pedido = new Pedido.Builder(cliente, notaFiscalId, status)
                .build();

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.persist(pedido);  // CascadeType.PERSIST ManyToOne Cliente
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Pedido pedidoParaVerificacao = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoParaVerificacao);
        assertNotNull(pedidoParaVerificacao.getCliente());
        assertEquals(nome, pedidoParaVerificacao.getCliente().getNome());
        assertEquals(sexo, pedidoParaVerificacao.getCliente().getSexo());
        assertEquals(cpf, pedidoParaVerificacao.getCliente().getCpf());
    }
}
