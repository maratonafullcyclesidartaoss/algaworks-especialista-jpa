package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Categoria;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaginacaoJPQLTest extends EntityManagerTest {

    @Test
    void devePaginarResultados() {
        // arrange
        var jpql = """
                select c
                 from Categoria c
                order by c.nome              
                """;
        // act
        TypedQuery<Categoria> consultaTipadaDeCategorias = entityManager.createQuery(jpql, Categoria.class);
        // FIRST_RESULT = MAX_RESULTS * (pagina - 1)
        // FIRST_RESULT = 2 * (1 - 1)           = 0     (página 1)
        // FIRST_RESULT = 2 * (2 - 1) = (2 * 1) = 2     (página 2)
        // FIRST_RESULT - 2 * (3 - 1) = (2 * 2) = 4     (página 3)
        // FIRST_RESULT - 2 * (4 - 1) = (2 * 3) = 6     (página 4)
        consultaTipadaDeCategorias.setFirstResult(6);
        consultaTipadaDeCategorias.setMaxResults(2);

        List<Categoria> listaDeCategorias = consultaTipadaDeCategorias.getResultList();

        // assert
        assertNotNull(listaDeCategorias);
        assertFalse(listaDeCategorias.isEmpty());

        listaDeCategorias.forEach(categoria -> System.out.println(categoria.getId() +
                ", " + categoria.getNome()));
    }
}
