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

class GroupByCriteriaTest extends EntityManagerTest {

    @Test
    void deveCondicionarAgrupamentoComHaving() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<ItemPedido> root = criteria.from(ItemPedido.class);
        Join<ItemPedido, Produto> joinProduto = root.join(ItemPedido_.produto);
        Join<Produto, Categoria> joinCategoria = joinProduto.join(Produto_.categorias);

        // act
        criteria
                .multiselect(
                        joinCategoria.get(Categoria_.nome),
                        builder.sum(root.get(ItemPedido_.precoProduto)),
                        builder.avg(root.get(ItemPedido_.precoProduto)))
                .groupBy(joinCategoria.get(Categoria_.id))
                .having(builder.greaterThan(builder.avg(root.get(ItemPedido_.precoProduto)).as(BigDecimal.class), new BigDecimal("700")));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteria);
        List<Object[]> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                "Nome Categoria: " + arr[0] +
                        ", SUM: " + arr[1] +
                        ", AVG: " + arr[2]));
    }

    @Test
    void deveAgruparResultadoComFuncoes() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<Pedido> root = criteria.from(Pedido.class);

        Expression<Integer> anoCriacaoPedido = builder.function("year", Integer.class, root.get(Pedido_.dataCriacao));
        Expression<String> nomeMesCriacaoPedido = builder.function("monthname", String.class, root.get(Pedido_.dataCriacao));

        Expression<String> anoMesConcat = builder.concat(
                builder.concat(anoCriacaoPedido.as(String.class), "/"),
                nomeMesCriacaoPedido);

        // act
        criteria
                .multiselect(
                        anoMesConcat,
                        builder.sum(root.get(Pedido_.valorTotal)))

                .groupBy(anoMesConcat);

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteria);

        List<Object[]> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println("Ano/Mês: " + arr[0] + ", Total: " + arr[1]));
    }

    @Test
    void deveAgruparResultado01() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<Categoria> root = criteria.from(Categoria.class);
        Join<Categoria, Produto> joinProduto = root.join(Categoria_.produtos, JoinType.LEFT);

        // act
        criteria
                .multiselect(
                        root.get(Categoria_.nome),
                        builder.count(joinProduto.get(Produto_.id)))
                .groupBy(root.get(Categoria_.id));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteria);

        List<Object[]> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println("Nome: " + arr[0] + ", Count: " + arr[1]));
    }

    @Test
    void deveAgruparResultado02() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<ItemPedido> root = criteria.from(ItemPedido.class);
        Join<ItemPedido, Produto> joinProduto = root.join(ItemPedido_.produto);
        Join<Produto, Categoria> joinCategoria = joinProduto.join(Produto_.categorias);

        // act
        criteria
                .multiselect(
                        joinCategoria.get(Categoria_.nome),
                        builder.sum(root.get(ItemPedido_.precoProduto)))
                .groupBy(joinCategoria.get(Categoria_.id));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteria);

        List<Object[]> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println("Nome: " + arr[0] + ", Soma: " + arr[1]));
    }
}
