package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class PathExpressionsTest extends EntityManagerTest {

    @Test
    void deveUsarPathExpressions() {
        // arrange
        var jpql = """
                select p.cliente.nome
                from Pedido p
                """;

        // act & assert
        TypedQuery<Object[]> queryTipadaDePedido = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> listaDePedidos = queryTipadaDePedido.getResultList();

        // assert
        assertFalse(listaDePedidos.isEmpty());
    }
}
