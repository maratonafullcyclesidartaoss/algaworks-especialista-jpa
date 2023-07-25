package com.github.sidartaoss.especialistajpa.ecommerce.criteria;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FuncoesCriteriaTest extends EntityManagerTest {

    @Test
    void deveAplicarFuncaoAgregacao() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria
                .multiselect(
                        builder.count(root.get(Pedido_.id)),
                        builder.avg(root.get(Pedido_.valorTotal)),
                        builder.sum(root.get(Pedido_.valorTotal)),
                        builder.min(root.get(Pedido_.valorTotal)),
                        builder.max(root.get(Pedido_.valorTotal)));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteria);

        List<Object[]> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                "count: " + arr[0]
                        + ", avg: " + arr[1]
                        + ", sum: " + arr[2]
                        + ", min: " + arr[3]
                        + ", max: " + arr[4]
        ));
    }

    @Test
    void deveAplicarFuncaoNativa() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria
                .multiselect(root.get(Pedido_.id),
                        builder.function("dayname", String.class, root.get(Pedido_.dataCriacao)))
                .where(builder.isTrue(builder.function(
                        "acima_media_faturamento", Boolean.class, root.get(Pedido_.valorTotal))));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteria);

        List<Object[]> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                arr[0]
                        + ", dayname: " + arr[1]
        ));
    }

    @Test
    void deveAplicarFuncaoColecao() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria
                .multiselect(root.get(Pedido_.id),
                        builder.size(root.get(Pedido_.itens)))
                .where(builder.greaterThan(builder.size(root.get(Pedido_.itens)), 1));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteria);

        List<Object[]> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                arr[0]
                        + ", size: " + arr[1]
        ));
    }

    @Test
    void deveAplicarFuncaoNumero() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria
                .multiselect(root.get(Pedido_.id),
                        builder.abs(builder.prod(root.get(Pedido_.id), -1)),
                        builder.mod(root.get(Pedido_.id), 2),
                        builder.sqrt(root.get(Pedido_.valorTotal)))
                .where(builder
                        .greaterThan(builder.sqrt(root.get(Pedido_.valorTotal)), 10.0));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteria);

        List<Object[]> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                arr[0]
                        + ", abs: " + arr[1]
                        + ", mod: " + arr[2]
                        + ", sqrt: " + arr[3]
        ));
    }

    @Test
    void deveAplicarFuncaoData() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<Pedido> root = criteria.from(Pedido.class);
        Join<Pedido, Pagamento> joinPagamento = root.join(Pedido_.pagamento);
        Join<Pedido, PagamentoBoleto> joinPagamentoBoleto = builder
                .treat(joinPagamento, PagamentoBoleto.class);

        // act
        criteria
                .multiselect(root.get(Pedido_.id),
                        builder.currentDate(),
                        builder.currentTime(),
                        builder.currentTimestamp())
                .where(
                        builder.between(builder.currentDate(),
                                root.get(Pedido_.dataCriacao).as(java.sql.Date.class),
                                joinPagamentoBoleto.get(PagamentoBoleto_.dataVencimento)
                                                            .as(java.sql.Date.class)),
                        builder.equal(root.get(Pedido_.status), StatusPedido.AGUARDANDO));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteria);

        List<Object[]> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                arr[0]
                        + ", current_date: " + arr[1]
                        + ", current_time: " + arr[2]
                        + ", current_timestamp: " + arr[3]
        ));
    }

    @Test
    void deveAplicarFuncaoString() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<Cliente> root = criteria.from(Cliente.class);

        // act
        criteria
                .multiselect(
                        root.get(Cliente_.nome),
                        builder.concat("Nome do cliente: ", root.get(Cliente_.nome)),
                        builder.length(root.get(Cliente_.nome)),
                        builder.locate(root.get(Cliente_.nome), "a"),
                        builder.substring(root.get(Cliente_.nome), 1, 2),
                        builder.lower(root.get(Cliente_.nome)),
                        builder.upper(root.get(Cliente_.nome)),
                        builder.trim(root.get(Cliente_.nome)))
                .where(builder.equal(builder.substring(root.get(Cliente_.nome), 1, 2), "Ma"));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteria);

        List<Object[]> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
               arr[0]
                    + ", concat: " + arr[1]
                    + ", length: " + arr[2]
                    + ", locate: " + arr[3]
                    + ", substring: " + arr[4]
                    + ", lower: " + arr[5]
                    + ", upper: " + arr[6]
                    + ", trim: " + arr[7]
        ));
    }
}
