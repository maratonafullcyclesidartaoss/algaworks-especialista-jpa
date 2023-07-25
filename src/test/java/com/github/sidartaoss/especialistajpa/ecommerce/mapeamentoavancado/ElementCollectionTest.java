package com.github.sidartaoss.especialistajpa.ecommerce.mapeamentoavancado;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Atributo;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ElementCollectionTest extends EntityManagerTest {

    @Test
    void deveAplicarTags() {
        // arrange
        var idDoProduto = 1;

        // act
        entityManager.getTransaction().begin();
        Produto produtoConsultado = entityManager.find(Produto.class, idDoProduto);

        produtoConsultado.informarTags(List.of("e-book", "livro-digital"));
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Produto produtoParaVerificacao = entityManager.find(Produto.class, idDoProduto);
        assertNotNull(produtoParaVerificacao);
        assertFalse(produtoParaVerificacao.getTags().isEmpty());
    }

    @Test
    void deveAplicarAtributos() {
        // arrange
        var idDoProduto = 1;
        List<Atributo> atributos = List.of(
                new Atributo("tela", "320x600"));

        // act
        entityManager.getTransaction().begin();
        Produto produtoConsultado = entityManager.find(Produto.class, idDoProduto);

        produtoConsultado.informarAtributos(atributos);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Produto produtoParaVerificacao = entityManager.find(Produto.class, idDoProduto);
        assertNotNull(produtoParaVerificacao);
        assertFalse(produtoParaVerificacao.getAtributos().isEmpty());
    }

    @Test
    void deveAplicarContatos() {
        // arrange
        var idDoCliente = 1;
        Map<String, String> contatos = Map.of(
                "email", "joao@dasilva.com");

        // act
        entityManager.getTransaction().begin();
        Cliente clienteConsultado = entityManager.find(Cliente.class, idDoCliente);

        clienteConsultado.informarContatos(contatos);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Cliente clienteParaVerificacao = entityManager.find(Cliente.class, idDoCliente);
        assertNotNull(clienteParaVerificacao);
        assertFalse(clienteParaVerificacao.getContatos().isEmpty());
        assertEquals("joao@dasilva.com", clienteParaVerificacao.getContatos().get("email"));
    }
}
