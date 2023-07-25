package com.github.sidartaoss.especialistajpa.ecommerce.criteria;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubqueriesCriteriaTest extends EntityManagerTest {

    @Test
    void devePesquisarComAny02() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        // act
        criteria.select(root);

        Subquery<BigDecimal> subquery = criteria.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(builder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteria.where(builder.notEqual(
                root.get(Produto_.preco), builder.any(subquery)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteria);
        List<Produto> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(produto -> System.out.println("ID: " + produto.getId()));
    }

    @Test
    void devePesquisarComAny() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        // act
        criteria.select(root);

        Subquery<BigDecimal> subquery = criteria.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(builder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteria.where(builder.equal(
                        root.get(Produto_.preco), builder.any(subquery)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteria);
        List<Produto> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(produto -> System.out.println("ID: " + produto.getId()));
    }

    @Test
    void devePesquisarComAl02() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        // act
        criteria.select(root);

        Subquery<BigDecimal> subquery = criteria.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(builder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteria.where(builder.greaterThan(
                root.get(Produto_.preco), builder.all(subquery)),
                builder.exists(subquery));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteria);
        List<Produto> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(produto -> System.out.println("ID: " + produto.getId()));
    }

    @Test
    void devePesquisarComAll() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        // act
        criteria.select(root);

        Subquery<BigDecimal> subquery = criteria.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(builder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteria.where(builder.equal(
                root.get(Produto_.preco), builder.all(subquery)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteria);
        List<Produto> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(pedido -> System.out.println("ID: " + pedido.getId()));
    }

    @Test
    void devePesquisarComExists() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        // act
        criteria.select(root);

        Subquery<Integer> subquery = criteria.subquery(Integer.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(builder.literal(1));
        subquery.where(builder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteria.where(builder.exists(subquery));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteria);
        List<Produto> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(pedido -> System.out.println("ID: " + pedido.getId()));
    }

    @Test
    void devePesquisarComIN() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria.select(root);

        Subquery<Integer> subquery = criteria.subquery(Integer.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        Join<ItemPedido, Pedido> subqueryJoinPedido = subqueryRoot.join(ItemPedido_.pedido);
        Join<ItemPedido, Produto> subqueryJoinProduto = subqueryRoot.join(ItemPedido_.produto);
        subquery.select(subqueryJoinPedido.get(Pedido_.id));

        subquery.where(builder.greaterThan(
                subqueryJoinProduto.get(Produto_.preco), new BigDecimal("100.00")));

        criteria.where(root.get(Pedido_.id).in(subquery));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(pedido -> System.out.println("ID: " + pedido.getId()));
    }

    @Test
    void devePesquisarSubqueries03() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteria = builder.createQuery(Cliente.class);
        Root<Cliente> root = criteria.from(Cliente.class);

        // act
        criteria.select(root);

        Subquery<BigDecimal> subquery = criteria.subquery(BigDecimal.class);
        Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
        subquery.select(builder.sum(subqueryRoot.get(Pedido_.valorTotal)))
                .where(builder.equal(root, subqueryRoot.get(Pedido_.cliente)));

        criteria
                .where(builder.greaterThan(subquery, new BigDecimal("1300.00")));

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteria);
        List<Cliente> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(cliente -> System.out.println("ID: " + cliente.getNome() + ", " + "Nome: " + cliente.getNome()));
    }

    @Test
    void devePesquisarSubqueries02() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria.select(root);

        Subquery<BigDecimal> subquery = criteria.subquery(BigDecimal.class);
        Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
        subquery.select(builder.avg(subqueryRoot.get(Pedido_.valorTotal)).as(BigDecimal.class));

        criteria.where(builder.greaterThan(root.get(Pedido_.valorTotal), subquery));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    @Test
    void devePesquisarSubqueries() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        // act
        criteria.select(root);

        Subquery<BigDecimal> subquery = criteria.subquery(BigDecimal.class);
        Root<Produto> subqueryRoot = subquery.from(Produto.class);
        subquery.select(builder.max(subqueryRoot.get(Produto_.preco)));

        criteria.where(builder.equal(root.get(Produto_.preco), subquery));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteria);
        List<Produto> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Nome: " + p.getNome() + ", Preço: " + p.getPreco()));
    }
}
