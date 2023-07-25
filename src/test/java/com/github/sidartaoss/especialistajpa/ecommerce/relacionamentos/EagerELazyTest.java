package com.github.sidartaoss.especialistajpa.ecommerce.relacionamentos;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import org.junit.jupiter.api.Test;

class EagerELazyTest extends EntityManagerTest {

    @Test
    void deveVerificarComportamento() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        System.out.println(pedido.getCliente().getNome());
//        System.out.println(pedido.getItens().isEmpty());
    }
}
