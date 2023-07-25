package com.github.sidartaoss.especialistajpa.ecommerce.iniciandocomjpa;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConsultandoRegistrosProdutoTest extends EntityManagerTest {

    @Test
    void deveBuscarPorIdentificador() {
        // arrange
        var id = 1;

        // action
        /**
         * 2 formas de ir no banco e buscar pelo identificador: .find e .getReference
         * **/
        Produto produto = entityManager.find(Produto.class, id);
        /** .getReference é lazy: só vai executar a consulta quando invocar uma propriedade da entidade,
         *  por exemplo: produto.getNome() **/
//        Produto produto = entityManager.getReference(Produto.class, produtoId);

        // assert
        assertNotNull(produto);
        assertEquals("Kindle", produto.getNome());
    }

    @Test
    void deveAtualizarAReferencia() {
        // arrange
        var id = 1;
        Produto produto = entityManager.find(Produto.class, id);
        produto.alterarMarca("Microfone Samsung");

        // action
        /**
         * outra forma de ir no banco e buscar:
         * ir no banco e reiniciar a entidade que acabou de buscar do banco de dados
         * **/
        entityManager.refresh(produto);

        // assert
        assertEquals("Kindle", produto.getNome());
    }
}