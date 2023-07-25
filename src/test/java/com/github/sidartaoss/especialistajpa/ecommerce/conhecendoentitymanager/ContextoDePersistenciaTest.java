package com.github.sidartaoss.especialistajpa.ecommerce.conhecendoentitymanager;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ContextoDePersistenciaTest extends EntityManagerTest {

    @Test
    void deveUsarContextoDePersistencia() {
        // arrange
        Produto produto = entityManager.find(Produto.class, 1);

        // act
        entityManager.getTransaction().begin();
        produto.alterarPreco(new BigDecimal("100.00"));

        var nome = "Caneca Azul";
        var preco = new BigDecimal("50.00");
        var descricao = "Caneca para Café.";
        Produto canecaAzul = new Produto(nome, descricao, preco);
        entityManager.persist(canecaAzul);

        nome = "Caneca Branca";
        preco = new BigDecimal("60.00");
        descricao = "Caneca para Chá.";
        Produto canecaBranca = new Produto(nome, descricao, preco);
        canecaBranca = entityManager.merge(canecaBranca);

        entityManager.flush();

        canecaBranca.atualizarDescricao("Caneca Branca para Chá.");

        entityManager.getTransaction().commit();

        // assert
    }
}
