package com.github.sidartaoss.especialistajpa.ecommerce.conhecendoentitymanager;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

public class CachePrimeiroNivelTest extends EntityManagerTest {

    @Test
    void deveVerificarCache() {
        // arrange
        var id = 1;
        Produto produto = entityManager.find(Produto.class, id);

        System.out.println("produto: " + produto);

        System.out.println("--------------------");

        // entityManager.clear() limpa a memória do Entity Manager e o
        // cache de primeiro nível

        Produto produtoResgatado = entityManager.find(Produto.class, id);

        System.out.println("produtoResgatado: " + produtoResgatado);

        // act

        // assert
    }
}
