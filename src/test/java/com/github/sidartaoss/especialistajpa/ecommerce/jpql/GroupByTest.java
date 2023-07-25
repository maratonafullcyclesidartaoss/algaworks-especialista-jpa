package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GroupByTest extends EntityManagerTest {

    @Test
    void deveUsarExpressaoCasePagamento() {
        // arrange
        var jpql = """
                select p.id,
                  case type(p.pagamento)
                       when PagamentoBoleto then 'Pago com boleto.'
                       when PagamentoCartao then 'Pago com cartão.'
                       else 'Não pago ainda.'
                end
                from Pedido p
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void deveUsarExpressaoCaseStatus() {
        // arrange
        var jpql = """
                select p.id,
                  case p.status
                       when 'PAGO' then 'Está pago. '
                       when 'CANCELADO' then 'Foi cancelado. '
                       else 'Está aguardando '
                end
                from Pedido p
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void deveCondicionarAgrupamentoComHaving() {
        // arrange
        // Total de vendas entre as categorias que mais vendem
        var jpql = """
                select cat.nome, sum(i.precoProduto * i.quantidade)
                from ItemPedido i
                join i.produto pro
                join pro.categorias cat
                group by cat.id
                having sum (i.precoProduto * i.quantidade) > 1500
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void deveAgruparEFiltrarResultado() {
        // arrange

        // Quantidade de produtos por categoria.
        // Total de vendas por mês.
        // Total de vendas por categoria.

        // Total de vendas por mês somente do ano corrente
        var jpql = """
                select concat(year(p.dataCriacao), '/', function('monthname', p.dataCriacao)), sum(p.valorTotal)
                from Pedido p
                where year(p.dataCriacao) = year(current_date) and p.status = 'PAGO'
                group by concat(year(p.dataCriacao), '/', function('monthname', p.dataCriacao))
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void deveAgruparResultadoTotalDeVendasPorCategoria() {
        // arrange

        // Quantidade de produtos por categoria.
        // Total de vendas por mês.
        // Total de vendas por categoria.

        var jpql = """
                select c.nome, sum(i.precoProduto * i.quantidade)
                from ItemPedido i
                join i.produto pro
                join pro.categorias c
                group by c.id
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void deveAgruparResultadoTotalDeVendasPorMes() {
        // arrange

        // Quantidade de produtos por categoria.
        // Total de vendas por mês.
        // Total de vendas por categoria.

        var jpql = """
                select concat(year(p.dataCriacao), ' ', function('monthname', p.dataCriacao)),
                       sum(p.valorTotal)
                from Pedido p
                group by concat(year(p.dataCriacao), ' ', function('monthname', p.dataCriacao))
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void deveAgruparResultadoQuantidadeDeProdutosPorCategoria() {
        // arrange

        // Quantidade de produtos por categoria.
        // Total de vendas por mês.
        // Total de vendas por categoria.

        var jpql = """
                select c.nome, count(p.id)
                from Categoria c
                join c.produtos p
                group by c.id
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }
}
