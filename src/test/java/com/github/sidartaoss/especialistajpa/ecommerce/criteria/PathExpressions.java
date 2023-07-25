package com.github.sidartaoss.especialistajpa.ecommerce.criteria;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente_;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PathExpressions extends EntityManagerTest {

    @Test
    void deveUsarPathExpression() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria
                .select(root)
                .where(builder.like(root.get(Pedido_.cliente).get(Cliente_.nome), "M%"));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println(
                "ID do Pedido: " + p.getId()
                        + ", ID do Cliente: " + p.getCliente().getId()
                        + ", Nome do Cliente: " + p.getCliente().getNome()));
    }

}
