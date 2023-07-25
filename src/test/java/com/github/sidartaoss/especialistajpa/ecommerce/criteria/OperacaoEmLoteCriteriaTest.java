package com.github.sidartaoss.especialistajpa.ecommerce.criteria;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Categoria;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Categoria_;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto_;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class OperacaoEmLoteCriteriaTest extends EntityManagerTest {

    @Test
    void deveAtualizarEmLote() {
        // arrange
        entityManager.getTransaction().begin();
        // act

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Produto> criteria = builder.createCriteriaUpdate(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        criteria.set(root.get(Produto_.preco),
                builder.prod(root.get(Produto_.preco), new BigDecimal("1.1")));

        Subquery<Integer> subquery= criteria.subquery(Integer.class);
        Root<Produto> subqueryRoot = subquery.correlate(root);
        Join<Produto, Categoria> joinCategoria = subqueryRoot.join(Produto_.categorias);
        subquery.select(builder.literal(1));
        subquery.where(builder.equal(joinCategoria.get(Categoria_.id), 2));

        criteria.where(builder.exists(subquery));

        Query query = entityManager.createQuery(criteria);
        query.executeUpdate();

        entityManager.getTransaction().commit();
        // assert
    }
}
