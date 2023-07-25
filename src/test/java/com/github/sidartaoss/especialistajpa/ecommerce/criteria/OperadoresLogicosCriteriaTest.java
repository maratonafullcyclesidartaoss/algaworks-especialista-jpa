package com.github.sidartaoss.especialistajpa.ecommerce.criteria;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OperadoresLogicosCriteriaTest extends EntityManagerTest {

    @Test
    void deveUsarOperadoresLogicos() {
        // arrange
        var cincoDiasAtras = LocalDate.now().minusDays(5).atStartOfDay().toInstant(ZoneOffset.UTC);

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria
                .select(root)
                .where(builder.and(
                        builder.greaterThan(root.get(Pedido_.valorTotal), new BigDecimal("499.00")),
                        builder.equal(root.get(Pedido_.status), StatusPedido.PAGO)
                ),
                        builder.greaterThan(root.get(Pedido_.dataCriacao), cincoDiasAtras));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Total: " + p.getValorTotal()));
    }
}
