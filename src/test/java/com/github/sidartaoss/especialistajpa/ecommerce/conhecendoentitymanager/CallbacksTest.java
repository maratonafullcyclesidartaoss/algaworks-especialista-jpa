package com.github.sidartaoss.especialistajpa.ecommerce.conhecendoentitymanager;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import com.github.sidartaoss.especialistajpa.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CallbacksTest extends EntityManagerTest {

    @Test
    void deveAcionarCallbacks() {
        // arrange
        Cliente cliente = entityManager.find(Cliente.class, 1);
        var notaFiscalId = 1;
        var status = StatusPedido.AGUARDANDO;
        Pedido pedido = new Pedido.Builder(cliente, notaFiscalId, status)
                .build();

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);

        // acionar callback pre-persist: chamar flush
        entityManager.flush();

        pedido.pagar();

        // commit vai chamar o flush e vai fazer a chamada para o callback pre-update
        entityManager.getTransaction().commit();

        // para a consulta não vir da memória do EntityManager
        entityManager.clear();

        // assert
        Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoVerificacao);
        assertNotNull(pedidoVerificacao.getDataCriacao());
        assertNotNull(pedidoVerificacao.getDataUltimaAtualizacao());
    }

}
