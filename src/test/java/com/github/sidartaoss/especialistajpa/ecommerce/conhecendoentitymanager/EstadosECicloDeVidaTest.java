package com.github.sidartaoss.especialistajpa.ecommerce.conhecendoentitymanager;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Categoria;
import org.junit.jupiter.api.Test;

class EstadosECicloDeVidaTest extends EntityManagerTest {

    @Test
    void deveAnalisarEstados() {
        // arrange
        // estado transiente ou novo
        var nome = "Vestuário";
        Categoria categoriaNovo = new Categoria(nome);
        // passar para o estado gerenciado: o retorno passa a ser gerenciado
        Categoria categoriaGerenciadaMerge = entityManager.merge(categoriaNovo);

        // estado gerenciado
        Categoria categoriaGerenciada = entityManager.find(Categoria.class, 1);

        // estado removed
        entityManager.remove(categoriaGerenciada);

        // voltar para o estado gerenciado
        entityManager.persist(categoriaGerenciada);

        // desanexar
        entityManager.detach(categoriaGerenciada);

        // act

        // assert
    }
}
