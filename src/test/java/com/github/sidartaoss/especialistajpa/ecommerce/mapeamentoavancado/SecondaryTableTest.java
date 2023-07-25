package com.github.sidartaoss.especialistajpa.ecommerce.mapeamentoavancado;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Sexo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecondaryTableTest extends EntityManagerTest {

    @Test
    void deveSalvarCliente() {
        // arrange
        var nome = "José Pereira da Silva";
        var sexo = Sexo.MASCULINO;
        var cpf = "04653818045";
        Cliente cliente = new Cliente(nome, sexo, cpf);
        var dataNascimento = LocalDate.of(1977, 11, 3).atStartOfDay().toInstant(ZoneOffset.UTC);
        cliente.informarDataDeNascimento(dataNascimento);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();

        // assert
        Cliente clienteConsultado = entityManager.find(Cliente.class, cliente.getId());
        assertNotNull(clienteConsultado);
        assertNotNull(clienteConsultado.getSexo());
    }
}
