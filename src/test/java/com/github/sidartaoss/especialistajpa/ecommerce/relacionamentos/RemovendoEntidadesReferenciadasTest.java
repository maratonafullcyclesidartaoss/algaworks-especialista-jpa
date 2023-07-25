package com.github.sidartaoss.especialistajpa.ecommerce.relacionamentos;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class RemovendoEntidadesReferenciadasTest extends EntityManagerTest {

//    @Test
    void deveRemoverEntidadeRelacionada() {
        // arrange
        var id = 3;
        Pedido pedido = entityManager.find(Pedido.class, id);
        assertFalse(pedido.getItens().isEmpty());

        // act
        entityManager.getTransaction().begin();
        pedido.getItens().forEach(item -> entityManager.remove(item));
        entityManager.remove(pedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Pedido pedidoVerificacao = entityManager.find(Pedido.class, id);
        assertNull(pedidoVerificacao);
    }
}