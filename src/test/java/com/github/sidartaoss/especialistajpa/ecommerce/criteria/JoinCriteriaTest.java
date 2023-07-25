package com.github.sidartaoss.especialistajpa.ecommerce.criteria;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JoinCriteriaTest extends EntityManagerTest {

    @Test
    void devePassarParametroDate() {
        // arrange
        var dataInicial = LocalDate.now()
                .minusDays(30)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NotaFiscal> criteria = builder.createQuery(NotaFiscal.class);
        Root<NotaFiscal> root = criteria.from(NotaFiscal.class);

        ParameterExpression<LocalDate> parameterDataInicial = builder
                .parameter(LocalDate.class, "dataInicial");

        // act
        criteria
                .select(root)
                .where(builder.greaterThan(root.get("dataEmissao"), parameterDataInicial));

        TypedQuery<NotaFiscal> typedQuery = entityManager.createQuery(criteria);
        typedQuery.setParameter("dataInicial", dataInicial);

        List<NotaFiscal> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    @Test
    void devePassarParametro() {
        // arrange
        var idDoPedido = 1;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        ParameterExpression<Integer> parameterIdDoPedido = builder
                .parameter(Integer.class, "idDoPedido");

        // act
        criteria
                .select(root)
                .where(builder.equal(root.get("id"), parameterIdDoPedido));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        typedQuery.setParameter("idDoPedido", idDoPedido);

        Pedido pedido = typedQuery.getSingleResult();

        // assert
        assertNotNull(pedido);
        assertFalse(pedido.getItens().isEmpty());
    }

    @Test
    void deveUsarJoinFetch() {
        // arrange

        var jpql = """
                select p
                from Pedido p
                join p.pagamento pag
                """;

        var idDoPedido = 1;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);
        root.fetch("cliente");
        root.fetch("notaFiscal", JoinType.LEFT);
        root.fetch("pagamento", JoinType.LEFT);
//        Join<Pedido, Cliente> joinCliente = (Join<Pedido, Cliente>) root.<Pedido, Cliente>fetch("cliente");

        // act
        criteria
                .select(root)
                .where(builder.equal(root.get("id"), idDoPedido));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        Pedido pedido = typedQuery.getSingleResult();

        // assert
        assertNotNull(pedido);
        assertFalse(pedido.getItens().isEmpty());
    }

    @Test
    void deveFazerLeftOuterJoin() {
        // arrange

        var jpql = """
                select p
                from Pedido p
                join p.pagamento pag
                """;

        var totalDePagamentos = 5;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);
        Join<Pedido, Pagamento> joinPagamento = root.join("pagamento", JoinType.LEFT);

        // act
        criteria
                .select(root);

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(totalDePagamentos, lista.size());
    }

    @Test
    void deveFazerJoinComOn() {
        // arrange

        var jpql = """
                select p
                from Pedido p
                join p.pagamento pag
                """;

        var totalDePagamentos = 2;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);
        Join<Pedido, Pagamento> joinPagamento = root.join("pagamento");
        joinPagamento.on(builder.equal(joinPagamento.get("status"), StatusPagamento.PROCESSANDO));

        // act
        criteria
                .select(root);

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(totalDePagamentos, lista.size());
    }

    @Test
    void deveFazerJoinSelectComJoinsAninhados() {
        // arrange

        var jpql = """
                select p
                from Pedido p
                join p.pagamento pag
                """;

        var totalDePagamentosProcessando = 2;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pagamento> criteria = builder.createQuery(Pagamento.class);
        Root<Pedido> root = criteria.from(Pedido.class);
        Join<Pedido, Pagamento> joinPagamento = root.join(Pedido_.pagamento);
        Join<Pedido, ItemPedido> joinItens = root.join(Pedido_.itens);
        Join<ItemPedido, Produto> joinItemProduto = joinItens.join(ItemPedido_.produto);

        // act
        criteria
                .select(joinPagamento)
                .where(builder.equal(joinPagamento.get("status"), StatusPagamento.PROCESSANDO));

        TypedQuery<Pagamento> typedQuery = entityManager.createQuery(criteria);
        List<Pagamento> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(totalDePagamentosProcessando, lista.size());
    }

    @Test
    void deveFazerJoinSelectComJoin() {
        // arrange

        var jpql = """
                select p
                from Pedido p
                join p.pagamento pag
                """;

        var totalDePagamentosProcessando = 2;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pagamento> criteria = builder.createQuery(Pagamento.class);
        Root<Pedido> root = criteria.from(Pedido.class);
        Join<Pedido, Pagamento> joinPagamento = root.join(Pedido_.pagamento);

        // act
        criteria
                .select(joinPagamento)
                .where(builder.equal(joinPagamento.get("status"), StatusPagamento.PROCESSANDO));

        TypedQuery<Pagamento> typedQuery = entityManager.createQuery(criteria);
        List<Pagamento> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(totalDePagamentosProcessando, lista.size());
    }

    @Test
    void deveFazerJoin() {
        // arrange

        var jpql = """
                select p
                from Pedido p
                join p.pagamento pag
                """;

        var totalDePagamentos = 4;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);
        Join<Pedido, Pagamento> join = root.join("pagamento");

        // act
        criteria
                .select(root);
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(totalDePagamentos, lista.size());
    }
}
