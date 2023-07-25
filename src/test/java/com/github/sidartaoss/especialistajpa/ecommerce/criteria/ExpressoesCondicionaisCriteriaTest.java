package com.github.sidartaoss.especialistajpa.ecommerce.criteria;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpressoesCondicionaisCriteriaTest extends EntityManagerTest {

    @Test
    void deveUsarExpressaoIN02() {
        // arrange
        var idDoCliente = 1;
        var idDoCliente2 = 2;
        Cliente cliente01 = entityManager.find(Cliente.class, idDoCliente);
        Cliente cliente02 = new Cliente();
        cliente02.setId(idDoCliente2);

        List<Cliente> clientes = List.of(cliente01, cliente02);

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria.select(root)
                .where(root.get(Pedido_.cliente).in(clientes));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(pedido -> System.out.println());
    }

    @Test
    void deveUsarExpressaoIN() {
        // arrange
        List<Integer> ids = List.of(1, 3, 4, 6);

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria.select(root)
                .where(root.get(Pedido_.id).in(ids));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(pedido -> System.out.println());
    }

    @Test
    void deveUsarExpressaoCase() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria.multiselect(
                root.get(Pedido_.id),
                builder.selectCase(root.get(Pedido_.pagamento).type().as(String.class))
                        .when("boleto", "Foi pago com boleto.")
                        .when("cartao", "Foi pago com cartão.")
                        .otherwise("Não identificado")
                );

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteria);
        List<Object[]> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void deveUsarExpressaoDeDiferente() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria
                .select(root)
                .where(builder.notEqual(root.get(Pedido_.valorTotal), new BigDecimal("499.00")));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Total: " + p.getValorTotal()));
    }

    @Test
    void deveUsarBetweenDatas() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria
                .select(root)
                .where(builder.between(root.get(Pedido_.dataCriacao),
                        LocalDate.now().minusDays(5).atStartOfDay().toInstant(ZoneOffset.UTC),
                        LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Total: " + p.getValorTotal()));
    }

    @Test
    void deveUsarBetweenNumeros() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        // act
        criteria
                .select(root)
                .where(builder.between(root.get(Pedido_.valorTotal),
                        new BigDecimal("499.00"), new BigDecimal("2398.00")));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Total: " + p.getValorTotal()));
    }

    @Test
    void deveUsarMaiorMenor() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        // act
        criteria
                .select(root)
                .where(builder.greaterThanOrEqualTo(root.get(Produto_.preco), new BigDecimal("799.00")),
                       builder.lessThanOrEqualTo(root.get(Produto_.preco), new BigDecimal("3500.00")));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteria);
        List<Produto> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Nome: " + p.getNome()));
    }

    @Test
    void deveUsarIsEmpty() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        // act
        criteria
                .select(root)
//                .where(root.get(Produto_.foto).isNull());
                .where(builder.isEmpty(root.get(Produto_.categorias)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteria);
        List<Produto> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    @Test
    void deveUsarIsNull() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        // act
        criteria
                .select(root)
//                .where(root.get(Produto_.foto).isNull());
                .where(builder.isNull(root.get(Produto_.foto)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteria);
        List<Produto> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    @Test
    void deveUsarExpressaoCondicionalLike() {
        // arrange

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteria = builder.createQuery(Cliente.class);
        Root<Cliente> root = criteria.from(Cliente.class);

        var filter = "%a%";

        // act
        criteria
                .select(root)
                .where(builder.like(root.get(Cliente_.nome), filter));

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteria);
        List<Cliente> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}
