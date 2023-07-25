package com.github.sidartaoss.especialistajpa.ecommerce.mapeamentobasico;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import com.github.sidartaoss.especialistajpa.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MapeamentoObjetoEmbutidoTest extends EntityManagerTest {

    @Test
    void deveAnalisarMapeamentoObjetoEmbutido() {
        // arrange
        var logradouro = "Rua abc";
        var numero = "111";
        var bairro = "Bairro Centro";
        var cidade = "São Paulo";
        var estado = "SP";
        var cep = "90999300";
        var enderecoEntrega = new Pedido.EnderecoEntrega.Builder(logradouro, numero, bairro, cidade, estado)
                .cep(cep)
                .build();
        var cliente = entityManager.find(Cliente.class, 1);
        var notaFiscalId = 1;
        var status = StatusPedido.AGUARDANDO;
        Pedido pedido = new Pedido.Builder(cliente, notaFiscalId, status)
                .enderecoEntrega(enderecoEntrega)
                .build();

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        var pedidoParaVerificacao = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoParaVerificacao);
        assertEquals(cliente.getId(), pedido.getCliente().getId());
        assertNotNull(pedidoParaVerificacao.getDataCriacao());
        assertEquals(notaFiscalId, pedidoParaVerificacao.getNotaFiscalId());
        assertEquals(new BigDecimal("0.00"), pedidoParaVerificacao.getValorTotal());
        assertEquals(StatusPedido.AGUARDANDO, pedidoParaVerificacao.getStatus());
        assertNotNull(pedidoParaVerificacao.getEnderecoEntrega());
        assertEquals(logradouro, pedidoParaVerificacao.getEnderecoEntrega().getLogradouro());
        assertEquals(numero, pedidoParaVerificacao.getEnderecoEntrega().getNumero());
        assertEquals(bairro, pedidoParaVerificacao.getEnderecoEntrega().getBairro());
        assertEquals(cidade, pedidoParaVerificacao.getEnderecoEntrega().getCidade());
        assertEquals(estado, pedidoParaVerificacao.getEnderecoEntrega().getEstado());
        assertEquals(cep, pedidoParaVerificacao.getEnderecoEntrega().getCep());
        assertNull(pedidoParaVerificacao.getEnderecoEntrega().getComplemento());
    }
}