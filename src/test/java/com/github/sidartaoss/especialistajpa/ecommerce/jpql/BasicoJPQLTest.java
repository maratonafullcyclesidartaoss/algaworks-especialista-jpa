package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.dto.ProdutoDTO;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicoJPQLTest extends EntityManagerTest {

    @Test
    void deveUsarDistinct() {
        // arrange
        var produtos = List.of(1, 2, 3, 4);
        var jpql = """
                select distinct p
                from Pedido p
                join p.itens i
                join i.produto pro
                where pro.id in (:produtos)
                """;

        // act
        TypedQuery<Pedido> consultaTipada = entityManager.createQuery(jpql, Pedido.class)
                .setParameter("produtos", produtos);

        List<Pedido> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        System.out.println(lista.size());
    }

    @Test
    void deveOrdenarResultados() {
        // arrange
        var jpql = """
                select c
                 from Cliente c
                order by c.nome desc
                """;

        // act
        TypedQuery<Cliente> consultaTipadaDeClientes = entityManager.createQuery(jpql, Cliente.class);
        List<Cliente> listaDeClientes = consultaTipadaDeClientes.getResultList();

        // assert
        assertNotNull(listaDeClientes);
        assertFalse(listaDeClientes.isEmpty());

        listaDeClientes.forEach(c -> System.out.println(c.getId() + ", " + c.getNome()));
    }

    @Test
    void deveProjetarNoDTO() {
        // arrange
        var idDoPedido = 1;
        var nome = "Kindle";
        var jpql = """
                select new com.github.sidartaoss.especialistajpa.ecommerce.dto.ProdutoDTO(p.id, p.nome)
                from Produto p
                where id = :id
                """;

        // act & assert
        TypedQuery<ProdutoDTO> queryTipadaDoDtoDeProduto = entityManager.createQuery(jpql, ProdutoDTO.class)
                .setParameter("id", idDoPedido);

        ProdutoDTO produtoDTO = queryTipadaDoDtoDeProduto.getSingleResult();
        assertNotNull(produtoDTO);
        assertEquals(idDoPedido, produtoDTO.getId());
        assertEquals(nome, produtoDTO.getNome());
    }

    @Test
    void deveProjetarOResultado() {
        // arrange
        var idDoPedido = 1;
        var tamanhoDaProjecao = 2;
        var jpql = """
                select p.id, p.nome
                from Produto p
                where id = :id
                """;

        // act & assert
        TypedQuery<Object[]> queryTipadaDaProjecaoDeProduto = entityManager.createQuery(jpql, Object[].class)
                .setParameter("id", idDoPedido);

        List<Object[]> listaDaProjecaoDeProduto = queryTipadaDaProjecaoDeProduto.getResultList();
        assertEquals(tamanhoDaProjecao, listaDaProjecaoDeProduto.get(0).length);
        listaDaProjecaoDeProduto.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void deveSelecionarUmAtributoParaRetorno() {
        // arrange
        var idDoPedido = 1;
        var nome = "Kindle";
        var jpql = """
                select p.nome from Produto p where p.id = :id
                """;

        TypedQuery<String> queryTipada = entityManager.createQuery(jpql, String.class)
                .setParameter("id", idDoPedido);

        // act & assert
        String retorno = queryTipada.getSingleResult();
        assertNotNull(retorno);
        assertEquals(nome, retorno);

        // act & assert
        var jpqlClienteDoPedido = """
                select p.cliente 
                from Pedido p
                where p.id = :id
                """;

        TypedQuery<Cliente> queryClienteDoPedido = entityManager.createQuery(jpqlClienteDoPedido, Cliente.class)
                .setParameter("id", idDoPedido);

        Cliente clienteDoPedido = queryClienteDoPedido.getSingleResult();
        assertNotNull(clienteDoPedido);
    }

    @Test
    void deveBuscarPorIdentificador() {
        // arrange
        // entityManager.find(Pedido.class, 1)
        // JPQL select p from Pedido p join p.itens i where p.id = :id

        // JPQL select p from Pedido p join p.itens i where i.precoProduto > 10

        // SQL select p.* from pedido p where p.id = ?

        var idDoPedido = 1;
        String query = """
                select p
                from Pedido p
                where p.id = :id
                """;
        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery(query, Pedido.class);

        typedQuery.setParameter("id", idDoPedido);

        // act & assert
        Pedido pedidoConsultado = typedQuery.getSingleResult();
        Assertions.assertNotNull(pedidoConsultado);

//        List<Pedido> lista = typedQuery.getResultList();
//        assertFalse(lista.isEmpty());

    }

    @Test
    void deveMostrarDiferencaQueries() {
        // arrange
        var idDoPedido = 1;
        var jpql = """
        select p
        from Pedido p
        where p.id = :id
        """;

        // act & assert
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class)
                .setParameter("id", idDoPedido);
        Pedido pedidoConsultadoTipado = typedQuery.getSingleResult();
        assertNotNull(pedidoConsultadoTipado);

        // act & assert
        Query query = entityManager.createQuery(jpql)
                .setParameter("id", idDoPedido);
        Pedido pedidoConsultadoObjeto = (Pedido) query.getSingleResult();
        assertNotNull(pedidoConsultadoObjeto);

        // act & assert
        List<Pedido> lista = query.getResultList();
        assertFalse(lista.isEmpty());
    }
}
