package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JoinTest extends EntityManagerTest {

    @Test
    void deveUsarJoinFetch() {
        // arrange
        var idDoPedido = 1;
        String jpql = """
                select p
                from Pedido p
                join fetch p.pagamento
                join fetch p.cliente
                left join fetch p.notaFiscal
                """;
        // act
        TypedQuery<Pedido> queryTipadaDePedido = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> listaDePediddos = queryTipadaDePedido.getResultList();

        // assert
        assertNotNull(listaDePediddos);
        assertFalse(listaDePediddos.isEmpty());
    }

    @Test
    void deveFazerLeftOuterJoin() {
        // arrange
        var idDoPedido = 1;
        String jpql = """
                select p
                from Pedido p 
                left join p.pagamento pag 
                on pag.status = 'PROCESSANDO'
                """;
        // act
        TypedQuery<Object[]> queryTipadaDePedido = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> listaDePedidos = queryTipadaDePedido.getResultList();

        // assert
        assertNotNull(listaDePedidos);

    }

    @Test
    void deveFazerJoin() {
        // arrange
        var idDoPedido = 1;
        String jpql = """
                select p, pro
                from Pedido p 
                join p.itens i join i.produto pro
                where i.precoProduto > 10
                """;
        // act
        TypedQuery<Object[]> queryTipadaDePedido = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> listaDePedidos = queryTipadaDePedido.getResultList();

        // assert
        assertNotNull(listaDePedidos);
        assertFalse(listaDePedidos.isEmpty());
    }
}
