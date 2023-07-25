package com.github.sidartaoss.especialistajpa.ecommerce.consultasnativas;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConsultaNativaTest extends EntityManagerTest {

    @Test
    void deveExecutarSQLResultSetMapping02() {
        // arrange
        var sql = """
                select *
                from item_pedido ip
                join produto p on ip.produto_id = p.id
                """;

        Query query = entityManager.createNativeQuery(sql, "item_pedido-produto.ItemPedido-Produto");

        List<Produto> lista = query.getResultList();

        // act
        lista.stream().forEach(
                produto -> System.out.println(
                        String.format("Produto => ID: %s, Nome: %s",
                                produto.getId(),
                                produto.getNome())));

        // assert
    }

    @Test
    void deveExecutarSQLResultSetMapping() {
        // arrange
        var sql = """
                select id,
                       nome,
                       descricao,
                       null data_criacao,
                       null data_ultima_atualizacao,
                       preco,
                       null foto
                from erp_produto
                """;

        Query query = entityManager.createNativeQuery(sql, "produto_loja.Produto");

        List<Produto> lista = query.getResultList();

        // act
        lista.stream().forEach(
                produto -> System.out.println(
                        String.format("Produto => ID: %s, Nome: %s",
                                produto.getId(),
                                produto.getNome())));

        // assert
    }

    @Test
    void deveExecutarSQLRetornandoUmaEntidade() {
        // arrange
        var sql = """
                select id,
                       nome,
                       descricao,
                       null data_criacao,
                       null data_ultima_atualizacao,
                       preco,
                       null foto
                from erp_produto
                """;

        Query query = entityManager.createNativeQuery(sql, Produto.class);

        List<Produto> lista = query.getResultList();

        // act
        lista.stream().forEach(
                produto -> System.out.println(
                        String.format("Produto => ID: %s, Nome: %s",
                                produto.getId(),
                                produto.getNome())));

        // assert
    }

    @Test
    void deveExecutarSQL() {
        // arrange
        var sql = """
                select id, nome
                from produto
                """;

        Query query = entityManager.createNativeQuery(sql);

        List<Object[]> lista = query.getResultList();

        // act
        lista.stream().forEach(arr -> System.out.println(String.format("Produto => ID: %s, Nome: %s", arr[0], arr[1])));

        // assert

    }
}
