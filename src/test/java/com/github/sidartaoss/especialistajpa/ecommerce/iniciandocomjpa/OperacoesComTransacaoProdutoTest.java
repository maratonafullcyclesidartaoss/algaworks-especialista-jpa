package com.github.sidartaoss.especialistajpa.ecommerce.iniciandocomjpa;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OperacoesComTransacaoProdutoTest extends EntityManagerTest {

    @Test
    void deveImpedirOperacaoComBancoDeDados() {
        // arrange
        var id = 1;
        var nomeOriginal = "Kindle";
        var nome = "Kindle Paperwhite Segunda Geração";
        var produto = entityManager.find(Produto.class, id);
        entityManager.detach(produto);

        // act
        entityManager.getTransaction().begin();
        produto.alterarMarca(nome);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Produto produtoVerificacao = entityManager.find(Produto.class, id);
        assertEquals(nomeOriginal, produtoVerificacao.getNome());
        assertNotNull(produtoVerificacao.getDescricao());
        assertNotNull(produtoVerificacao.getPreco());
    }

    @Test
    void deveMostrarDiferencaPersistMerge() {
        /** PERSIST **/
        // arrange
//        var id = 5;
        var nome = "Smartphone One Plus";
        var descricao = "O processador mais rápido.";
        var preco = new BigDecimal("2000.00");
        Produto produtoPersist = new Produto(nome, descricao, preco);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(produtoPersist);
        produtoPersist.alterarMarca("Smartphone Two Plus");
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Produto produtoInserido = entityManager.find(Produto.class, produtoPersist.getId());
        assertEquals(nome.replace("One", "Two"), produtoInserido.getNome());
        assertEquals(descricao, produtoInserido.getDescricao());
        assertEquals(preco, produtoInserido.getPreco());

        /** MERGE **/
//        id = 6;
        nome = "Notebook Dell";
        descricao = "O melhor da categoria.";
        preco = new BigDecimal("4000.00");
        Produto produtoMerge = new Produto(nome, descricao, preco);

        // act
        entityManager.getTransaction().begin();
        // O merge não pega exatamente a instância do parâmetro 'produtoMerge' para passar para o EntityManager
        // deixar na memória e gerenciar esse objeto. O merge pega a instância 'produtoMerge', faz uma cópia dela
        // e a cópia vai ser jogada para o EntityManager poder gerenciar.
        // Então, quando setar uma nova marca, não vai acontecer nada:
        // entityManager.merge(produtoMerge);
        // Por quê? Porque 'produtoMerge' NÃO é a cópia da
        // instância que foi jogada no EntityManager para o EntityManager gerenciar.
        // O que acontece é que o método merge retorna a cópia da instância gerenciada. Essa é a instância que deve
        // ser usada para atualizar:
        // produtoMerge = entityManager.merge(produtoMerge);
        produtoMerge = produtoMerge = entityManager.merge(produtoMerge);
        produtoMerge.alterarMarca("Notebook Dell 2");
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Produto produtoAlterado = entityManager.find(Produto.class, produtoMerge.getId());
        assertEquals(nome.concat(" 2"), produtoAlterado.getNome());
        assertEquals(descricao, produtoAlterado.getDescricao());
        assertEquals(preco, produtoAlterado.getPreco());
    }

    @Test
    void deveInserirObjetoComMerge() {
        // arrange
//        var id = 4;
        var nome = "Microfone Rode Videmic";
        var descricao = "A melhor qualidade de som.";
        var preco = new BigDecimal("1000.00");
        Produto produto = new Produto(nome, descricao, preco);

        // act
        entityManager.getTransaction().begin();
        produto = entityManager.merge(produto);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Produto produtoInserido = entityManager.find(Produto.class, produto.getId());
        assertEquals(nome, produtoInserido.getNome());
        assertEquals(descricao, produtoInserido.getDescricao());
        assertEquals(preco, produtoInserido.getPreco());
    }

    @Test
    void deveAtualizarObjetoGerenciado() {
        // arrange
        var id = 1;
        var nome = "Kindle Paperwhite Segunda Geração";
        var produto = entityManager.find(Produto.class, id);

        // act
        entityManager.getTransaction().begin();
        produto.alterarMarca(nome);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Produto produtoVerificacao = entityManager.find(Produto.class, id);
        assertEquals(nome, produtoVerificacao.getNome());
        assertNotNull(produtoVerificacao.getDescricao());
        assertNotNull(produtoVerificacao.getPreco());
    }

    @Test
    void deveAtualizarObjeto() {
        // arrange

        // Atualizando objeto não-gerenciado pelo EntityManager. É possível, MAS:
        // Caso não preencha todos os atributos do objeto, vai atualizar os demais atributos para null.
        // Por isso, neste caso, são informados todos os atributos com valores para atualização.
        // Caso desejar atualizar somente alguns atributos, deve-se chamar o método find do EntityManager e setar
        // os atributos que desejar alterar.
        var nome = "Kindle Paperwhite";
        var descricao = "Conheça o novo Kindle";
        var preco = new BigDecimal("599.00");
        var produto = new Produto(nome, descricao, preco);

        // act
        entityManager.getTransaction().begin();
        produto = entityManager.merge(produto);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        assertEquals(nome, produtoVerificacao.getNome());
        assertEquals(descricao, produtoVerificacao.getDescricao());
        assertEquals(preco, produtoVerificacao.getPreco());
    }

    @Test
    void deveRemoverObjeto() {
        // arrange
        var id = 5;
        Produto produto = entityManager.find(Produto.class, id);

        // action
        entityManager.getTransaction().begin();
        entityManager.remove(produto);
        entityManager.getTransaction().commit();

        // assert
        Produto produtoVerificacao = entityManager.find(Produto.class, id);
        assertNull(produtoVerificacao);
    }

    @Test
    void deveInserirOPrimeiroObjeto() {
        // arrange
//        var id = 2;
        var nome = "Macbook Pro";
        var descricao = "notebook";
        var preco = BigDecimal.valueOf(15400);
        Produto produto = new Produto(nome, descricao, preco);

        // action
        entityManager.getTransaction().begin();
        entityManager.persist(produto);
        entityManager.getTransaction().commit();

        // ao comitar, o registro vai para a memória do EntityManager. Ao chamar persist, o método, além de preparar
        // o objeto para ser persistido na base de dados, o método joga o objeto para a memória do EntityManager.
        // Ao persistir, o EntityManager joga os dados do objeto para o banco de dados, mas continua com o objeto
        // na memória gerenciando a entidade.
        // Ao consultar, o find não vai até o banco de dados, ele busca o registro na memória do EntityManager, como
        // uma otimização de recursos.
        // Então, para ir na base de dados, para confirmar se a inserção aconteceu com sucesso,
        // é necessário limpar a memória do EntityManager.

        entityManager.clear();

        // assert
        Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        assertNotNull(produtoVerificacao);
    }

    @Test
    void deveAbrirEFecharATransacao() {
        // arrange
//        Produto produto = new Produto();

        // action
        entityManager.getTransaction().begin();
//        entityManager.persist(produto);
//        entityManager.merge(produto);
//        entityManager.remove(produto);
        entityManager.getTransaction().commit();

        // assert
    }
}
