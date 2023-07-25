package com.github.sidartaoss.especialistajpa.ecommerce.mapeamentoavancado;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.NotaFiscal;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SalvandoArquivosTest extends EntityManagerTest {

    @Test
    void deveSalvarXmlNota() throws IOException {
        // arrange
        var idDoPedido = 3;
        Pedido pedidoConsultado = entityManager.find(Pedido.class, idDoPedido);

        var dataEmissao = Instant.now();
        var xml = carregarArquivo("nota-fiscal.xml");
        NotaFiscal notaFiscal = new NotaFiscal(pedidoConsultado,
                xml,
                dataEmissao);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(notaFiscal);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        var idDaNotaFiscal = notaFiscal.getId();
        NotaFiscal notaFiscalParaVerificacao = entityManager.find(NotaFiscal.class, idDaNotaFiscal);
        assertNotNull(notaFiscalParaVerificacao);
        assertNotNull(notaFiscalParaVerificacao.getXml());
        assertTrue(notaFiscalParaVerificacao.getXml().length > 0);

//        try (OutputStream outputStream = new FileOutputStream(Files.createFile(Paths.get(System.getProperty("user.home") + "/nota-fiscal.xml")).toFile())) {
//            outputStream.write(notaFiscalParaVerificacao.getXml());
//        }
    }

    @Test
    void deveSalvarFoto() throws IOException {
        // arrange
        var nome = "Leite";
        var descricao = "Leite em Pó";
        var preco = new BigDecimal("7.90");
        Produto produto = new Produto(nome, descricao, preco);
        var foto = carregarArquivo("leite-em-po.jpg");
        produto.inserirFoto(foto);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(produto);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        var idDoProduto = produto.getId();
        Produto produtoParaVerificacao = entityManager.find(Produto.class, idDoProduto);
        assertNotNull(produtoParaVerificacao);
        assertNotNull(produtoParaVerificacao.getFoto());
        assertTrue(produtoParaVerificacao.getFoto().length > 0);

    }

    private byte[] carregarArquivo(String nome) throws IOException {
        return SalvandoArquivosTest.class.getResourceAsStream("/" + nome).readAllBytes();
    }
}
