package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FuncoesTest extends EntityManagerTest {

    @Test
    void deveAplicarFuncoesDeAgregacaoSum() {
        // arrange
        // avg, count, min, max, sum

        var jpql = """
                select sum (p.valorTotal)
                from Pedido p
                """;

        // act
        TypedQuery<Number> consultaTipada = entityManager.createQuery(jpql, Number.class);

        List<Number> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(System.out::println);
    }

    @Test
    void deveAplicarFuncoesDeAgregacaoMin() {
        // arrange
        // avg, count, min, max, sum

        var jpql = """
                select min (p.valorTotal)
                from Pedido p
                """;

        // act
        TypedQuery<Number> consultaTipada = entityManager.createQuery(jpql, Number.class);

        List<Number> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(System.out::println);
    }

    @Test
    void deveAplicarFuncoesDeAgregacaoCount() {
        // arrange
        // avg, count, min, max, sum

        var jpql = """
                select count (p.id)
                from Pedido p
                where p.dataCriacao <= current_date
                """;

        // act
        TypedQuery<Number> consultaTipada = entityManager.createQuery(jpql, Number.class);

        List<Number> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(System.out::println);
    }

    @Test
    void deveAplicarFuncoesDeAgregacaoAvg() {
        // arrange
        // avg, count, min, max, sum

        var jpql = """
                select avg (p.valorTotal)
                from Pedido p
                where p.dataCriacao <= current_date
                """;

        // act
        TypedQuery<Number> consultaTipada = entityManager.createQuery(jpql, Number.class);

        List<Number> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(System.out::println);
    }

    @Test
    void deveAplicarFuncoesNativasDiaDaSemanaEmQueFaturamentoEhAcimaDaMedia() {
        // arrange
        var jpql = """
                select function('dayname', p.dataCriacao) 
                from Pedido p
                where function('acima_media_faturamento', p.valorTotal) = 1
                """;

        // act
        TypedQuery<String> consultaTipada = entityManager.createQuery(jpql, String.class);

        List<String> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(System.out::println);
    }

    @Test
    void deveAplicarFuncoesNativasAcimaDaMediaDeFaturamento() {
        // arrange
        var jpql = """
                select p
                from Pedido p
                where function('acima_media_faturamento', p.valorTotal) = 1
                """;

        // act
        TypedQuery<Pedido> consultaTipada = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(pedido -> System.out.println(
                "Pedido " + pedido.getId() + " - acima da média de faturamento - "
                + pedido.getValorTotal()));
    }

    @Test
    void deveAplicarFuncaoColecaoSize() {
        // arrange
        var jpql = """
                select size(p.itens)
                from Pedido p
                where size(p.itens) > 1
                """;

        // act
        TypedQuery<Integer> consultaTipada = entityManager.createQuery(jpql, Integer.class);

        List<Integer> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(System.out::println);
    }

    @Test
    void deveAplicarFuncaoNumeroAbsModSqrtPedidoEFiltro() {
        // arrange
        var jpql = """
                select abs(p.valorTotal), mod(p.id, 2), sqrt(p.valorTotal) 
                from Pedido p
                where abs(p.valorTotal) > 990 
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
    }

    @Test
    void deveAplicarFuncaoNumeroAbsModSqrtPedido() {
        // arrange
        var jpql = """
                select abs(p.valorTotal), mod(p.id, 2), sqrt(p.valorTotal) 
                from Pedido p
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
    }

    @Test
    void deveAplicarFuncaoNumeroAbsModSqrt() {
        // arrange
        var jpql = """
                select abs(-10), mod(3, 2), sqrt(9) 
                from Pedido p
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
    }

//    @Test
    void deveAplicarFuncaoParaTodosOsPedidosFeitosANoite() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // arrange
        var jqpl = """
                select hour(p.dataCriacao),
                       minute(p.dataCriacao),
                       second(p.dataCriacao)
                from Pedido p
                where hour(p.dataCriacao) > 18
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
    }

    @Test
    void deveAplicarFuncaoHoraMinutoSegundo() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // arrange
        var jqpl = """
                select hour(p.dataCriacao),
                       minute(p.dataCriacao),
                       second(p.dataCriacao)
                from Pedido p
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
    }

    @Test
    void deveAplicarFuncaoMesAnoDia() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // arrange
        var jqpl = """
                select year(p.dataCriacao),
                       month(p.dataCriacao),
                       day(p.dataCriacao)
                from Pedido p
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
    }

    @Test
    void deveAplicarFuncaoMesAno() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // arrange
        var jqpl = """
                select year(p.dataCriacao),
                       month(p.dataCriacao),
                       current_timestamp
                from Pedido p
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
    }

    @Test
    void deveAplicarFuncaoDataAno() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // arrange
        var jqpl = """
                select year(p.dataCriacao),
                       current_time,
                       current_timestamp
                from Pedido p
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
    }

    @Test
    void deveAplicarFuncaoData() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // arrange
        var jqpl = """
                select current_date,
                       current_time,
                       current_timestamp
                from Pedido p
                """;

        // act
        TypedQuery<Object[]> consultaTipada = entityManager.createQuery(jqpl, Object[].class);

        List<Object[]> lista = consultaTipada.getResultList();

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
    }
}
