package com.github.sidartaoss.especialistajpa.ecommerce.mapeamentoavancado;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class DetalhesColumnTest extends EntityManagerTest {

    @Test
    void deveImpedirInsercaoDaColunaAtualizacao() {
        // arrange
        var nome = "Teclado para smartphone";
        var preco = new BigDecimal("149.99");
        var descricao = "O mais confortável.";
        Produto tecladoSmartphone = new Produto(nome, descricao, preco);
        tecladoSmartphone.setDataCriacao(Instant.now());
        tecladoSmartphone.setDataUltimaAtualizacao(Instant.now());

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(tecladoSmartphone);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Produto produtoVerificacao = entityManager.find(Produto.class, tecladoSmartphone.getId());
        assertNotNull(produtoVerificacao);
        assertNotNull(produtoVerificacao.getDataCriacao());
        assertNull(produtoVerificacao.getDataUltimaAtualizacao());
    }

    @Test
    void deveImpedirAtualizacaoDaColunaCriacao() {
        // arrange
        entityManager.getTransaction().begin();

        Produto produto = entityManager.find(Produto.class, 1);
        produto.alterarPreco(new BigDecimal("99.99"));
        produto.setDataCriacao(Instant.now());
        produto.setDataUltimaAtualizacao(Instant.now());

        // act
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        assertNotNull(produtoVerificacao);
        assertNotEquals(produto.getDataCriacao().truncatedTo(ChronoUnit.SECONDS),
                produtoVerificacao.getDataCriacao().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(produto.getDataUltimaAtualizacao().truncatedTo(ChronoUnit.SECONDS),
                produtoVerificacao.getDataUltimaAtualizacao().truncatedTo(ChronoUnit.SECONDS));
    }
}
