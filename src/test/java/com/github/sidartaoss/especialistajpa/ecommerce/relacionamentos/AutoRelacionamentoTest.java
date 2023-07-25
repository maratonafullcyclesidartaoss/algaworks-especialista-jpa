package com.github.sidartaoss.especialistajpa.ecommerce.relacionamentos;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class AutoRelacionamentoTest extends EntityManagerTest {

    @Test
    void deveVerificarRelacionamento() {
        // arrange
        Categoria categoriaPai = new Categoria("Dispositivos");

        Categoria categoriaFilha = new Categoria("Celulares", categoriaPai);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(categoriaPai);
        entityManager.persist(categoriaFilha);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Categoria categoriaPaiInserida = entityManager.find(Categoria.class, categoriaPai.getId());
        assertNotNull(categoriaPaiInserida);

        Categoria categoriaFilhaInserida = entityManager.find(Categoria.class, categoriaFilha.getId());
        assertNotNull(categoriaFilhaInserida);
        assertNotNull(categoriaFilhaInserida.getCategoriaPai());

        assertFalse(categoriaPaiInserida.getCategorias().isEmpty());
    }
}