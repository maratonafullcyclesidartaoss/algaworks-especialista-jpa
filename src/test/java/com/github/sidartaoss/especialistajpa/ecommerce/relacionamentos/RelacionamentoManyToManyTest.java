package com.github.sidartaoss.especialistajpa.ecommerce.relacionamentos;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RelacionamentoManyToManyTest extends EntityManagerTest {

    @Test
    void deveVerificarRelacionamento() {
        // arrange
        Produto produto = entityManager.find(Produto.class, 1);
        Categoria categoria = entityManager.find(Categoria.class, 1);

        // act
        entityManager.getTransaction().begin();
        // NÃO vai salvar o registro porque categoria não é o OWNER do relacionamento. Neste casos,
        // produto é o OWNER. Só vai salvar no banco de dados quando o OWNER do relacionamento estiver setando o
        // atributo NON-OWNER.
        // categoria.setProdutos(Arrays.asList(produto));
        produto.setCategorias(Arrays.asList(categoria));
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Categoria categoriaVerificacao = entityManager.find(Categoria.class, categoria.getId());
        assertFalse(categoriaVerificacao.getProdutos().isEmpty());
    }
}