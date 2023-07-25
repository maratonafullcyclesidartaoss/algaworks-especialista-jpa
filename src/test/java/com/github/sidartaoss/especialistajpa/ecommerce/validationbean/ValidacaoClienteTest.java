package com.github.sidartaoss.especialistajpa.ecommerce.validationbean;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import org.junit.jupiter.api.Test;

class ValidacaoClienteTest extends EntityManagerTest {

    @Test
    void deveVvalidarCliente() {
        entityManager.getTransaction().begin();
        Cliente cliente = new Cliente();
        entityManager.merge(cliente);
        entityManager.getTransaction().commit();
    }
}
