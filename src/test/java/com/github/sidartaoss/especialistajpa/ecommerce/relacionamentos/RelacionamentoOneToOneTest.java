package com.github.sidartaoss.especialistajpa.ecommerce.relacionamentos;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.NotaFiscal;
import com.github.sidartaoss.especialistajpa.ecommerce.model.PagamentoCartao;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RelacionamentoOneToOneTest extends EntityManagerTest {

    @Test
    void deveVerificarRelacionamento() {
        // arrange
        var idDoPedido = 5;
        Pedido pedidoConsultado = entityManager.find(Pedido.class, idDoPedido);

        var numeroCartao = "5317598458580461";
        PagamentoCartao pagamentoCartao = new PagamentoCartao(pedidoConsultado, numeroCartao);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(pagamentoCartao);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Pedido pedidoParaVerificacao = entityManager.find(Pedido.class, pedidoConsultado.getId());
        assertNotNull(pedidoParaVerificacao.getPagamento());
        assertEquals(pedidoConsultado.getId(), pagamentoCartao.getId());
        assertEquals(pagamentoCartao.getId(), pedidoParaVerificacao.getPagamento().getId());
    }

    @Test
    void deveVerificarRelacionamentoNotaFiscal() {
        // arrange
        var idDoPedido = 3;
        Pedido pedido = entityManager.find(Pedido.class, idDoPedido);

        var xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                """;
        var dataEmissao = Instant.now();
        NotaFiscal notaFiscal = new NotaFiscal(pedido, xml.getBytes(), dataEmissao);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(notaFiscal);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoVerificacao.getNotaFiscal());
        assertEquals(notaFiscal.getId(), pedidoVerificacao.getNotaFiscal().getId());
    }
}