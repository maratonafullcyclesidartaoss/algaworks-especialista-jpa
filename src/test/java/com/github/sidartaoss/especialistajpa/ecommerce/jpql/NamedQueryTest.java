package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NamedQueryTest extends EntityManagerTest {

    @Test
    void deveExecutarConsultaArquivoXMLEspecificoProduto() {
        // arrange
        var namedQuery = "Produto.buscarTodos";

        // act
        TypedQuery<Produto> consultaTipada = entityManager.createNamedQuery(namedQuery, Produto.class);

        List<Produto> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    @Test
    void deveExecutarConsultaArquivoXMLEspecificoPedido() {
        // arrange
        var namedQuery = "Pedido.buscarTodos";

        // act
        TypedQuery<Pedido> consultaTipada = entityManager.createNamedQuery(namedQuery, Pedido.class);

        List<Pedido> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    @Test
    void deveExecutarConsultaArquivoXML() {
        // arrange
        var namedQuery = "Pedido.listar";

        // act
        TypedQuery<Pedido> consultaTipada = entityManager.createNamedQuery(namedQuery, Pedido.class);

        List<Pedido> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    @Test
    void deveExecutarConsultaListarPorCategoria() {
        // arrange
        var categoria = 2;
        var namedQuery = "Produto.listarPorCategoria";

        // act
        TypedQuery<Produto> consultaTipada = entityManager.createNamedQuery(namedQuery, Produto.class)
                .setParameter("categoria", categoria);

        List<Produto> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    @Test
    void deveExecutarConsulta() {
        // arrange
        var namedQuery = "Produto.listar";

        // act
        TypedQuery<Produto> consultaTipada = entityManager.createNamedQuery(namedQuery, Produto.class);
        List<Produto> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}
