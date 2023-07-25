package com.github.sidartaoss.especialistajpa.ecommerce.conhecendoentitymanager;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GerenciamentoTransacoesTest extends EntityManagerTest {

    private static Logger logger = Logger.getLogger(GerenciamentoTransacoesTest.class.getName());

    @Test
    void deveAbrirFecharCancelarTransacao() {
        // arrange

        // act
        assertThrows(RuntimeException.class, () -> {
            try {
                entityManager.getTransaction().begin();
                metodoDeNegocio();
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao pagar", e);
                entityManager.getTransaction().rollback();
                throw e;
            }
        });

        // assert
    }

    private void metodoDeNegocio() {
        var idDoPedido = 5;
        Pedido pedido = entityManager.find(Pedido.class, idDoPedido);

        pedido.pagar();
        if (pedido.getPagamento() == null) {
            throw new RuntimeException("Pedido ainda não foi pago com cartão.");
        }
    }
}
