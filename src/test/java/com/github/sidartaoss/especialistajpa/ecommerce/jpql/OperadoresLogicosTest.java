package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class OperadoresLogicosTest extends EntityManagerTest {

    @Test
    void deveUsarOperadoresAndOr() {
        // arrange
        var jpql = """
                select p
                from Pedido p
                where (p.status = 'AGUARDANDO' or p.status = 'PAGO')
                  and p.valorTotal > 100
                """;
        // act
        TypedQuery<Pedido> consultaTipadaDePedido = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = consultaTipadaDePedido.getResultList();

        // assert
        assertFalse(lista.isEmpty());
    }

    @Test
    void deveUsarOperadoresOr() {
        // arrange
        var jpql = """
                select p
                from Pedido p
                where p.valorTotal > 500
                and p.status = 'AGUARDANDO'
                or p.status = 'PAGO'
                """;
        // act
        TypedQuery<Pedido> consultaTipadaDePedido = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = consultaTipadaDePedido.getResultList();

        // assert
        assertFalse(lista.isEmpty());
    }

    @Test
    void deveUsarOperadoresAnd() {
        // arrange
        var idDoCliente = 1;
        var jpql = """
                select p
                from Pedido p
                where p.valorTotal > 500
                and p.status = 'AGUARDANDO'
                and p.cliente.id = :idDoCliente
                """;
        // act
        TypedQuery<Pedido> consultaTipadaDePedido = entityManager.createQuery(jpql, Pedido.class)
                .setParameter("idDoCliente", idDoCliente);

        List<Pedido> lista = consultaTipadaDePedido.getResultList();

        // assert
        assertFalse(lista.isEmpty());
    }
}
