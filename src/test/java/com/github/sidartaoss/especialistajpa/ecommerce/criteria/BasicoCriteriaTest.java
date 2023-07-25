package com.github.sidartaoss.especialistajpa.ecommerce.criteria;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.dto.ProdutoDTO;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicoCriteriaTest extends EntityManagerTest {

    @Test
    void deveUsarDistinct() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);
        root.join(Pedido_.itens);

        // act
        criteria
                .select(root).distinct(true);

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteria);
        List<Pedido> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId()));
    }

    @Test
    void deveOrdenarResultados() {
        // arrange
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteria = builder.createQuery(Cliente.class);
        Root<Cliente> root = criteria.from(Cliente.class);

        // act
        criteria
//                .orderBy(builder.asc(root.get(Cliente_.nome)));
                .orderBy(builder.desc(root.get(Cliente_.nome)));

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteria);
        List<Cliente> lista = typedQuery.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Nome: " + p.getNome()));
    }

    @Test
    void deveProjetarOResultadoDTO() {
        // arrange

//        var jpql = """
//                select id, nome from Produto
//                where id = :id
//                """;

        var idDoPedido = 1;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProdutoDTO> query = builder.createQuery(ProdutoDTO.class);
        Root<Produto> root = query.from(Produto.class);
        query
                .select(builder.construct(ProdutoDTO.class, root.get("id"), root.get("nome")))
                .where(builder.equal(root.get("id"), idDoPedido));

        // act
        TypedQuery<ProdutoDTO> consultaTipada = entityManager.createQuery(query);
        List<ProdutoDTO> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(1, lista.get(0).getId());
        assertEquals("Kindle", lista.get(0).getNome());

        lista.forEach(dto -> System.out.println("ID: " + dto.getId() + ", Nome: " + dto.getNome()));
    }

    @Test
    void deveProjetarOResultadoComTuple() {
        // arrange

//        var jpql = """
//                select id, nome from Produto
//                where id = :id
//                """;

        var idDoPedido = 1;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<Produto> root = query.from(Produto.class);
        query
//                .multiselect(root.get("id").alias("id"), root.get("nome").alias("nome"))
                .select(builder.tuple(root.get("id").alias("id"), root.get("nome").alias("nome")))
                .where(builder.equal(root.get("id"), idDoPedido));

        // act
        TypedQuery<Tuple> consultaTipada = entityManager.createQuery(query);
        List<Tuple> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(1, lista.get(0).get("id"));
        assertEquals("Kindle", lista.get(0).get("nome"));

        lista.forEach(tuple -> System.out.println("ID: " + tuple.get("id") + ", Nome: " + tuple.get("nome")));
    }

    @Test
    void deveProjetarOResultado() {
        // arrange

//        var jpql = """
//                select id, nome from Produto
//                """;

        var idDoPedido = 1;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<Produto> root = query.from(Produto.class);
        query
                .multiselect(root.get("id"), root.get("nome"))
                .where(builder.equal(root.get("id"), idDoPedido));

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(query);
        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(1, lista.get(0)[0]);
        assertEquals("Kindle", lista.get(0)[1]);

        lista.forEach(arr -> System.out.println("ID: " + arr[0] + ", Nome: " + arr[1]));
    }

    @Test
    void deveSelecionarUmAtributoParaRetornoValorTotal() {
        // arrange
        var idDoPedido = 1;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = builder.createQuery(BigDecimal.class);
        Root<Pedido> root = query.from(Pedido.class);
        query
                .select(root.get("valorTotal"))
                .where(builder.equal(root.get("id"), idDoPedido));

        // act
        TypedQuery<BigDecimal> consultaTipada = entityManager.createQuery(query);
        List<BigDecimal> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(new BigDecimal("2398.00"), lista.get(0));
    }

    @Test
    void deveSelecionarUmAtributoParaRetorno() {
        // arrange
        var idDoPedido = 1;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> query = builder.createQuery(Cliente.class);
        Root<Pedido> root = query.from(Pedido.class);
        query
                .select(root.get("cliente"))
                .where(builder.equal(root.get("id"), idDoPedido));

        // act
        TypedQuery<Cliente> consultaTipada = entityManager.createQuery(query);
        List<Cliente> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals("Fernando Medeiros", lista.get(0).getNome());
    }

    @Test
    void deveBuscarPorIdentificador() {
        // arrange
        var idDoPedido = 1;
        var jpql = """
                select p
                from Pedido p
                where p.id = :id
                """;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
        Root<Pedido> root = query.from(Pedido.class);
        query
                .select(root)
                .where(builder.equal(root.get("id"), idDoPedido));

        // act
        TypedQuery<Pedido> consultaTipada = entityManager.createQuery(query);
        List<Pedido> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}
