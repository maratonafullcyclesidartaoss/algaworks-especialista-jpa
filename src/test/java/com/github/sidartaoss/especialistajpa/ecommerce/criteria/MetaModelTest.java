package com.github.sidartaoss.especialistajpa.ecommerce.criteria;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.NotaFiscal;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MetaModelTest extends EntityManagerTest {

    @Test
    void deveUtilizarMetaModel() {
        // arrange

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        // act
        criteria
                .select(root)
                .where(builder.or(
                        builder.like(root.get(Produto_.nome), "%C%"),
                        builder.like(root.get(Produto_.descricao), "%C%")
                ));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteria);

        List<Produto> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}
