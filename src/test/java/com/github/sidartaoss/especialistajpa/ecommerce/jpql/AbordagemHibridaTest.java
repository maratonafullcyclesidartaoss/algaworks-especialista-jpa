package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AbordagemHibridaTest extends EntityManagerTest {

    @BeforeAll
    public static void setUpBeforeAll() {
        entityManagerFactory = Persistence
                .createEntityManagerFactory("Ecommerce-PU");
        EntityManager em = entityManagerFactory.createEntityManager();
        var jpql = """
                select c
                from Categoria c
                """;
        TypedQuery<Categoria> consultaTipada = em.createQuery(jpql, Categoria.class);

        var namedQuery = "Categoria.listar";
        entityManagerFactory.addNamedQuery(namedQuery, consultaTipada);
    }

    @Test
    void deveUsarAbordagemHibrida() {
        // arrange
        var namedQuery = "Categoria.listar";

        // act
        TypedQuery<Categoria> consultaTipada = entityManager.createNamedQuery(namedQuery, Categoria.class);
        List<Categoria> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}
