package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

class ExpressoesCondicionaisTest extends EntityManagerTest {

    @Test
    void deveUsarExpressaoINClientes() {
        var idDoCliente1 = 1;
        var idDoCliente2 = 2;
        Cliente cliente1 = entityManager.find(Cliente.class, idDoCliente1);
        Cliente cliente2 = entityManager.find(Cliente.class, idDoCliente2);

        List<Cliente> clientes = List.of(cliente1, cliente2);
        // arrange
        var jpql = """
                select p
                from Pedido p
                where p.cliente in (:clientes)
                """;

        // act
        TypedQuery<Pedido> consultaTipada = entityManager.createQuery(jpql, Pedido.class)
                .setParameter("clientes", clientes);
        List<Pedido> lista = consultaTipada.getResultList();

        // assert
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void deveUsarExpressaoIN() {
        List<Integer> parametros = List.of(1, 3, 4);
        // arrange
        var jpql = """
                select p
                from Pedido p
                where p.id in (:parametros)
                """;

        // act
        TypedQuery<Pedido> consultaTipada = entityManager.createQuery(jpql, Pedido.class)
                .setParameter("parametros", parametros);
        List<Pedido> lista = consultaTipada.getResultList();

        // assert
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void deveUsarExpressaoDiferente() {
        // arrange
        var jpql = """
                select p
                from Pedido p
                where p.status <> 'PAGO'
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void deveUsarBetween() {
        // arrange
        var dataInicial = LocalDate.now().minusDays(2).atStartOfDay().toInstant(ZoneOffset.UTC);
        var dataFinal = LocalDate.now().atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        // between corresponde a >= and <=
        var jpql = """
                select p
                from Pedido p
                where p.dataCriacao between :dataInicial and :dataFinal
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class)
                .setParameter("dataInicial", dataInicial)
                .setParameter("dataFinal", dataFinal);
        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void deveConsultarPedidosDosUltimos2Dias() {
        // arrange
        var dataInicial = LocalDate.now().minusDays(2).atStartOfDay().toInstant(ZoneOffset.UTC);
        var dataFinal = LocalDate.now().atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        var jpql = """
                select p
                from Pedido p
                where p.dataCriacao >= :dataInicial
                  and p.dataCriacao <= :dataFinal
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class)
                .setParameter("dataInicial", dataInicial)
                .setParameter("dataFinal", dataFinal);
        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void deveUsarMaiorMenor() {
        // arrange
//        var precoProduto = new BigDecimal("499.00");
        var precoInicial = new BigDecimal("400.00");
        var precoFinal = new BigDecimal("1500.00");
        var jpql = """
                select p
                from Produto p
                where p.preco >= :precoInicial 
                  and p.preco <= :precoFinal
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class)
                .setParameter("precoInicial", precoInicial)
                .setParameter("precoFinal", precoFinal);
        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void deveUsarIsNull() {
        // arrange
        var jpql = """
                select p
                from Produto p
                where p.foto is null
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void deveUsarIsEmpty() {
        // arrange
        var jpql = """
                select p
                from Produto p
                where p.categorias is empty
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void deveUsarExpressaoCondicionalLike() {
        // arrange
        var nome = "Fernando";
        var jpql = """
                select c
                from Cliente c
                where c.nome like concat('%', :nome, '%')
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class)
                .setParameter("nome", nome);
        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        Assertions.assertFalse(lista.isEmpty());
    }
}
