package com.github.sidartaoss.especialistajpa.ecommerce.criteria;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Categoria;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaginacaoCriteriaTest extends EntityManagerTest {

    @Test
    void devePaginarResultados() {
        // arrange
//        var jpql = """
//                select c
//                from Categoria c
//                order by c.nome
//                """;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Categoria> criteria = builder.createQuery(Categoria.class);
        Root<Categoria> root = criteria.from(Categoria.class);

        // act
        criteria
                .select(root);

        TypedQuery<Categoria> typedQuery = entityManager.createQuery(criteria);
        typedQuery.setFirstResult(0);
        typedQuery.setMaxResults(2);

        List<Categoria> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(categoria -> System.out.println("ID: " + categoria.getId() + ", Nome: " + categoria.getNome()));
    }
}
