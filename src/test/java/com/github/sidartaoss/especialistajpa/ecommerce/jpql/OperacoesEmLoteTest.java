package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.stream.Collectors;

class OperacoesEmLoteTest extends EntityManagerTest {

    private static final int LIMITE_INSERCOES = 4;

//    @Test
    void deveRemomverEmLote() throws IOException {
        // arrange
        var jpql = """
                delete
                from Produto p
                where p.id between 8 and 12
                """;

        // act
        entityManager.getTransaction().begin();
        Query consultaRemocao = entityManager.createQuery(jpql);
        consultaRemocao.executeUpdate();
        entityManager.getTransaction().commit();

        // assert
    }

//    @Test
    void deveAtualizarEmLoteCategoriaLivros() throws IOException {
        // arrange
        var idDaCategoria = 2;
        var jpql = """
                update Produto p
                set p.preco = p.preco + (p.preco * 0.1)
                where exists (
                   select c2.id
                   from p.categorias c2
                   where c2.id = :idDaCategoria
                )
                """;

        // act
        entityManager.getTransaction().begin();

        Query consultaParaAtualizar = entityManager.createQuery(jpql)
                .setParameter("idDaCategoria", idDaCategoria);
        consultaParaAtualizar.executeUpdate();

        entityManager.getTransaction().commit();

        // assert
    }

//    @Test
    void deveAtualizarEmLote() throws IOException {
        // arrange
        var idDaCategoria = 2;
        var jpql = """
                update Produto p
                set p.preco = p.preco + 1
                where p.id between 1 and 10
                """;

        // act
        entityManager.getTransaction().begin();

        Query consultaParaAtualizar = entityManager.createQuery(jpql);
//                .setParameter("categoria", idDaCategoria);
        consultaParaAtualizar.executeUpdate();

        entityManager.getTransaction().commit();

        // assert
    }

//    @Test
    void deveInserirEmLote() throws IOException {
        // arrange
        try (InputStream in = OperacoesEmLoteTest.class.getClassLoader()
                .getResourceAsStream("produtos/importar.txt");
             BufferedReader leitor = new BufferedReader(new InputStreamReader(in))) {

            int contadorInsercoes = 0;

            // act
            entityManager.getTransaction().begin();
            for (String linha : leitor.lines().collect(Collectors.toList())) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] produtoColuna = linha.split(";");
                var nome = produtoColuna[0];
                var descricao = produtoColuna[1];
                var preco = new BigDecimal(produtoColuna[2]);
                Produto produto = new Produto(nome, descricao, preco);
                entityManager.persist(produto);
                if (++contadorInsercoes == LIMITE_INSERCOES) {
                    entityManager.flush();
                    entityManager.clear();
                    contadorInsercoes = 0;

                    System.out.println("----------------------------------------");
                }
            }
            entityManager.getTransaction().commit();
        }

        // assert
    }
}
