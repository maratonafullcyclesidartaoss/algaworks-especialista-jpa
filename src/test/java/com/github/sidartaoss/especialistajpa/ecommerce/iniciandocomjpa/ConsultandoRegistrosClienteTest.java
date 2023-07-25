package com.github.sidartaoss.especialistajpa.ecommerce.iniciandocomjpa;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConsultandoRegistrosClienteTest extends EntityManagerTest {

    @Test
    void deveBuscarPorIdentificador() {
        // arrange
        var id = 1;

        // act
        Cliente cliente = entityManager.find(Cliente.class, id);

        // assert
        assertNotNull(cliente);
        assertEquals("Fernando Medeiros", cliente.getNome());
    }
}