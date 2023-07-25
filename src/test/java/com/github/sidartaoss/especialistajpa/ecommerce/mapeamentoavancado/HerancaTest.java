package com.github.sidartaoss.especialistajpa.ecommerce.mapeamentoavancado;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HerancaTest extends EntityManagerTest {

    @Test
    void deveSalvarCliente() {
        // arrange
        var nome = "Sebastião Melo da Silva";
        var sexo = Sexo.MASCULINO;
        var cpf = "51224978005";
        Cliente cliente = new Cliente(nome, sexo, cpf);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Cliente clienteParaVerificacao = entityManager.find(Cliente.class, cliente.getId());
        assertNotNull(clienteParaVerificacao);
        assertNotNull(clienteParaVerificacao.getId());
    }

    @Test
    void deveBuscarPagamentos() {
        // arrange

        // act
        List<Pagamento> pagamentos = entityManager.createQuery("select p from Pagamento p")
                .getResultList();

        // assert
        assertFalse(pagamentos.isEmpty());
    }

    @Test
    void deveIncluirPagamentoPedido() {
        // arrange
        var idDoPedido = 5;
        Pedido pedido = entityManager.find(Pedido.class, idDoPedido);

        var numeroCartao = "5113344939717406";
        Pagamento pagamento = new PagamentoCartao(pedido, numeroCartao);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(pagamento);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Pedido pedidoParaVerificacao = entityManager.find(Pedido.class, idDoPedido);
        assertNotNull(pedidoParaVerificacao);
        assertNotNull(pedidoParaVerificacao.getPagamento());
    }
}
