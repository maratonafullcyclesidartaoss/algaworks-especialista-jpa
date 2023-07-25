package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubqueriesTest extends EntityManagerTest {

    @Test
    void devePesquisarComAnyTodosOsProdutosQueJaForamVendidosPorUmPrecoDiferenteDoAtual() {
        // arrange
        // podemos usar o ANY e o SOME
        // todos os produtos que já foram vendidos por um preço diferente do atual.
        var jpql = """
                select p
                from Produto p
                where p.preco <> any (
                   select precoProduto
                   from ItemPedido
                   where produto = p
                )
                """;

        // act
        TypedQuery<Produto> consultaTipada = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(produto -> System.out.println("ID: " + produto.getId() + ", Nome: " + produto.getNome()));
    }

    @Test
    void devePesquisarComAny() {
        // arrange
        // todos os produtos que já foram vendidos pelo menos uma vez pelo preço atual.
        var jpql = """
                select p
                from Produto p
                where p.preco = any (
                   select precoProduto
                   from ItemPedido
                   where produto = p
                )
                """;

        // act
        TypedQuery<Produto> consultaTipada = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(produto -> System.out.println("ID: " + produto.getId() + ", Nome: " + produto.getNome()));
    }

    @Test
    void devePesquisarComMax() {
        // arrange
        // todos os produtos que sempre não foram vendidos mais depois que preço subiu.
        var jpql = """
                select p
                from Produto p
                where p.preco > (
                   select max(precoProduto)
                   from ItemPedido
                   where produto = p
                )
                """;

        // act
        TypedQuery<Produto> consultaTipada = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(produto -> System.out.println("ID: " + produto.getId() + ", Nome: " + produto.getNome()));
    }

    @Test
    void devePesquisarComAllProdutosQueNaoForamVendidosMaisDepoisQuePrecoSubiu() {
        // arrange
        // todos os produtos que sempre não foram vendidos mais depois que preço subiu.
        var jpql = """
                select p
                from Produto p
                where p.preco > all (
                   select precoProduto
                   from ItemPedido
                   where produto = p
                )
                """;

        // act
        TypedQuery<Produto> consultaTipada = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(produto -> System.out.println("ID: " + produto.getId() + ", Nome: " + produto.getNome()));
    }

    @Test
    void devePesquisarComAll() {
        // arrange
        // todos os produtos que sempre foram vendidos pelo preço atual.
        var jpql = """
                select p
                from Produto p
                where p.preco = all (
                   select precoProduto
                   from ItemPedido
                   where produto = p
                )
                """;

        // act
        TypedQuery<Produto> consultaTipada = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(produto -> System.out.println("ID: " + produto.getId() + ", Nome: " + produto.getNome()));
    }

    @Test
    void devePesquisarComExists() {
        // arrange
        // todos os produtos que já foram vendidos pelo menos uma vez
        var jpql = """
                select p
                from Produto p
                where exists (
                   select ip2.id
                   from ItemPedido ip2
                   join ip2.produto p2
                   where p2 = p
                )
                """;

        // act
        TypedQuery<Produto> consultaTipada = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(produto -> System.out.println("ID: " + produto.getId() + ", Nome: " + produto.getNome()));
    }

    @Test
    void devePesquisarComINPedidosOndeProdutosDosItensEstaoAcimaDe100Reais() {
        // arrange
        // pedidos cujos produtos dos itens estão acima de 100 reais
        var jpql = """
                select distinct p
                from Pedido p
                where p.id in (
                   select p2.id
                   from ItemPedido i2
                   join i2.pedido p2
                   join i2.produto pro2
                   where pro2.preco > 100
                )
                """;

        // act
        TypedQuery<Pedido> consultaTipada = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(pedido -> System.out.println("ID do Pedido: " + pedido.getId()));
    }

    @Test
    void devePesquisarBonsClientesQueCompraramAcimaDe100Reais() {
        // arrange
        var jpql = """
                select c 
                from Cliente c 
                where 500 < (
                   select sum(p.valorTotal)
                   from Pedido p
                   where p.cliente = c
                )
                """;

        // act
        TypedQuery<Cliente> consultaTipada = entityManager.createQuery(jpql, Cliente.class);

        List<Cliente> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println("ID: " + c.getId() + ", Nome: " + c.getNome()));
    }

    @Test
    void devePesquisarSubQueriesBonsClientesQueCompraramAcimaDe100Reais() {
        // arrange
        var jpql = """
                select c 
                from Cliente c 
                where 500 < (
                   select sum(p.valorTotal)
                   from c.pedidos p
                )
                """;

        // act
        TypedQuery<Cliente> consultaTipada = entityManager.createQuery(jpql, Cliente.class);

        List<Cliente> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println("ID: " + c.getId() + ", Nome: " + c.getNome()));
    }

    @Test
    void devePesquisarSubQueriesTodosOsPedidosAcimaDaMediaDeVendas() {
        // arrange
        // o produto ou os produtos mais caros da base.
        var jpql = """
                select p    
                from Pedido p
                where p.valorTotal > (
                   select avg (valorTotal) 
                   from Pedido
                )
                """;

        // act
        TypedQuery<Pedido> consultaTipada = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Valor total: " + p.getValorTotal()));
    }

    @Test
    void devePesquisarSubQueries() {
        // arrange
        // o produto ou os produtos mais caros da base.
        var jpql = """
                select p    
                from Produto p
                where p.preco = (
                   select max(preco) 
                   from Produto
                )
                """;

        // act
        TypedQuery<Produto> consultaTipada = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Preço: " + p.getPreco()));
    }
}
