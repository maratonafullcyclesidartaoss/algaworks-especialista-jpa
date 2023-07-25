package com.github.sidartaoss.especialistajpa.ecommerce.mapeamentoavancado;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PropriedadesTransientesTest extends EntityManagerTest {

    @Test
    void deveValidarPrimeiroNome() {
        // arrange
        var id = 1;
        var nome = "Fernando Medeiros";

        // act
        Cliente clienteConsultado = entityManager.find(Cliente.class, id);

        // assert
        Assertions.assertNotNull(clienteConsultado);
        Assertions.assertEquals(
                this.extrairPrimeiroNome(nome),
                clienteConsultado.getPrimeiroNome());
    }

    private String extrairPrimeiroNome(String nome) {
        int index = nome.indexOf(" ");
        if (index > -1) {
            return nome.substring(0, index);
        }
        return nome;
    }
}
