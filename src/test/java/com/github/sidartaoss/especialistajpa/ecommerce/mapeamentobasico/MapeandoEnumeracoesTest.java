package com.github.sidartaoss.especialistajpa.ecommerce.mapeamentobasico;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Sexo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapeandoEnumeracoesTest extends EntityManagerTest  {

    @Test
    void deveTestarEnum() {
        // arrange
//        var id = 4;
        var nome = "José Mineiro";
        var sexo = Sexo.MASCULINO;
        var cpf = "16937830025";
        var cliente = new Cliente(nome, sexo, cpf);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Cliente clienteInserido = entityManager.find(Cliente.class, cliente.getId());
        assertNotNull(clienteInserido);
        assertEquals(nome, clienteInserido.getNome());
        assertEquals(sexo, clienteInserido.getSexo());
    }
}