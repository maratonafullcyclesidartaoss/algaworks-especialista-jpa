package com.github.sidartaoss.especialistajpa.ecommerce.jpql;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DynamicQueryTest extends EntityManagerTest {

    @Test
    void deveExecutarConsultaDinamica() {
        // arrange
        var nome = "K";
        Produto produtoConsultado = new Produto(nome);

        // act
        List<Produto> lista = pesquisar(produtoConsultado);

        // assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals("Kindle", lista.get(0).getNome());
    }

    private List<Produto> pesquisar(Produto produto) {
        StringBuilder jpql = new StringBuilder("select p from Produto p where 1 = 1");

        if (produto.contemNome()) {
            jpql.append(" and p.nome like concat('%', :nome, '%')");
        }
        if (produto.contemDescricao()) {
            jpql.append(" and p.descricao like concat('%', :descricao, '%')");
        }

        TypedQuery<Produto> consultaTipada = entityManager.createQuery(jpql.toString(), Produto.class);

        if (produto.contemNome()) {
            consultaTipada.setParameter("nome", produto.getNome());
        }
        if (produto.contemDescricao()) {
            consultaTipada.setParameter("descricao", produto.getDescricao());
        }
        return consultaTipada.getResultList();
    }
}
