package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Categoria;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FuncoesStringsTest extends EntityManagerTest {

    @Test
    void deveAplicarFuncaoFiltroCategoriaQueComecaComLetraN() {
        // arrange
        // concat, length, locate, substring, lower, upper, trim
        var jqpl = """
                select c.nome,
                       length(c.nome) 
                from Categoria c
                where substring(c.nome, 1, 1) = 'N' 
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
    }

    @Test
    void deveAplicarFuncaoFiltroLength() {
        // arrange
        // concat, length, locate, substring, lower, upper, trim
        var jqpl = """
                select c.nome,
                       length(c.nome) 
                from Categoria c
                where length(c.nome) > 10 
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
    }

    @Test
    void deveAplicarFuncaoTrim() {
        // arrange
        // concat, length, locate, substring, lower, upper, trim
        var jqpl = """
                select c.nome,
                       trim(c.nome)
                from Categoria c
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
    }

    @Test
    void deveAplicarFuncaoUpper() {
        // arrange
        // concat, length, locate, substring, lower, upper, trim
        var jqpl = """
                select c.nome,
                       upper(c.nome)
                from Categoria c
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
    }

    @Test
    void deveAplicarFuncaoLower() {
        // arrange
        // concat, length, locate, substring, lower, upper, trim
        var jqpl = """
                select c.nome,
                       lower(c.nome)
                from Categoria c
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
    }

    @Test
    void deveAplicarFuncaoSubstring() {
        // arrange
        // concat, length, locate, substring, lower, upper, trim
        var jqpl = """
                select c.nome,
                       substring(c.nome, 1, 2)
                from Categoria c
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
    }

    @Test
    void deveAplicarFuncaoLocate() {
        // arrange
        // concat, length, locate, substring, lower, upper, trim
        var jqpl = """
                select c.nome,
                       locate('a', c.nome)
                from Categoria c
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
    }

    @Test
    void deveAplicarFuncaoLength() {
        // arrange
        // concat, length, locate, substring, lower, upper, trim
        var jqpl = """
                select c.nome, length(c.nome) 
                from Categoria c
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
    }

    @Test
    void deveAplicarFuncaoConcat() {
        // arrange
        // concat, length, locate, substring, lower, upper, trim
        var jqpl = """
                select c.nome, concat('Categoria: ', c.nome) 
                from Categoria c
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
    }
}
