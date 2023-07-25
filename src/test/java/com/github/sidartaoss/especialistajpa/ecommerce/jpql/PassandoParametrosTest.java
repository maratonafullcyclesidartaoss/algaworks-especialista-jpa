package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.NotaFiscal;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PassandoParametrosTest extends EntityManagerTest {

    @Test
    void devePassarParametroDate() {
        // arrange
        var dataDeEmissao = Instant.now();
        var jpql = """
                select nf
                from NotaFiscal nf
                where nf.dataEmissao <= :dataDeEmissao
                """;

        // act
        TypedQuery<NotaFiscal> queryTipadaDaNotaFiscal = entityManager.createQuery(jpql, NotaFiscal.class)
                .setParameter("dataDeEmissao", dataDeEmissao);
        NotaFiscal notaFiscal = queryTipadaDaNotaFiscal.getSingleResult();

        // assert
        assertNotNull(notaFiscal);
    }

    @Test
    void devePassarParametro() {
        // arrange
        var idDoPedido = 2;
        var jpql = """
                select p
                from Pedido p
                join p.pagamento pag
                where p.id = :id
                and pag.status = 'PROCESSANDO'
                """;

        // act
        TypedQuery<Pedido> queryTipadaDoPedido = entityManager.createQuery(jpql, Pedido.class)
                .setParameter("id", idDoPedido);
        Pedido pedido = queryTipadaDoPedido.getSingleResult();

        // assert
        assertNotNull(pedido);
    }
}
