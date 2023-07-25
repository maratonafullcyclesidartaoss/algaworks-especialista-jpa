package com.github.sidartaoss.especialistajpa.ecommerce.mapeamentobasico;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Categoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstrategiaChavePrimariaTest extends EntityManagerTest {

    @Test
    void deveTestarEstrategiaChave() {
        // arrange
        Categoria categoria = new Categoria("Digital");

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(categoria);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Categoria categoriaInserida = entityManager.find(Categoria.class, categoria.getId());
        assertNotNull(categoriaInserida);
    }
}