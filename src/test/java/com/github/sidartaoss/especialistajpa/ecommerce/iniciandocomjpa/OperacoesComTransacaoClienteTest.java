package com.github.sidartaoss.especialistajpa.ecommerce.iniciandocomjpa;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Cliente;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Sexo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperacoesComTransacaoClienteTest extends EntityManagerTest {

    @Test
    void deveMostrarDiferencaPersistMerge() {
        /** PERSIST **/
        // arrange
//        var id = 5;
        var nome = "Sindomar Junior da Silva";
        var sexo = Sexo.MASCULINO;
//        Cliente clientePersist = new Cliente(id, nome);
        var cpf = "25690287032";
        Cliente clientePersist = new Cliente(nome, sexo, cpf);

        // act
        entityManager.getTransaction().begin();
        entityManager.persist(clientePersist);
        clientePersist.alterarNome("Miguel Junior da Silva");
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Cliente clienteInserido = entityManager.find(Cliente.class, clientePersist.getId());
        assertEquals("Miguel Junior da Silva", clienteInserido.getNome());

        /** MERGE **/
//        id = 6;
        nome = "Alice Pereira da Silva";
        sexo = Sexo.FEMININO;
        cpf = "51249398002";
        Cliente clienteMerge = new Cliente(nome, sexo, cpf);

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
        clienteMerge = entityManager.merge(clienteMerge);
        clienteMerge.alterarNome("Paolina Gomes da Silva");
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Cliente clienteAlterado = entityManager.find(Cliente.class, clienteMerge.getId());
        assertEquals("Paolina Gomes da Silva", clienteAlterado.getNome());
    }

    @Test
    void deveInserirObjetoComMerge() {
        // arrange
//        var id = 4;
        var nome = "Ademar de Barros da Silva";
        var sexo = Sexo.MASCULINO;
        var cpf = "89512303094";
        Cliente cliente = new Cliente(nome, sexo, cpf);

        // act
        entityManager.getTransaction().begin();
        cliente = entityManager.merge(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Cliente clienteInserido = entityManager.find(Cliente.class, cliente.getId());
        assertEquals(nome, clienteInserido.getNome());
    }

    @Test
    void deveAtualizarObjetoGerenciado() {
        // arrange
        var id = 1;
        var nome = "Joaquina da Silva";
        var cliente = entityManager.find(Cliente.class, id);

        // act
        entityManager.getTransaction().begin();
        cliente.alterarNome(nome);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Cliente clienteAlterado = entityManager.find(Cliente.class, id);
        assertEquals(nome, clienteAlterado.getNome());
    }

    @Test
    void deveAtualizarObjeto() {
        // arrange

        // Atualizando objeto não-gerenciado pelo EntityManager. É possível, MAS:
        // Caso não preencha todos os atributos do objeto, vai atualizar os demais atributos para null.
        // Por isso, neste caso, são informados todos os atributos com valores para atualização.
        // Caso desejar atualizar somente alguns atributos, deve-se chamar o método find do EntityManager e setar
        // os atributos que desejar alterar.
        var nome = "Maristela da Silva";
        var sexo = Sexo.FEMININO;
        var cpf = "30859898032";
        var cliente = new Cliente(nome, sexo, cpf);

        // act
        entityManager.getTransaction().begin();
        cliente = entityManager.merge(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        // assert
        Cliente clienteVerificacao = entityManager.find(Cliente.class, cliente.getId());
        assertEquals(nome, clienteVerificacao.getNome());
    }

    @Test
    void deveRemoverObjeto() {
        // arrange
        var id = 3;
        Cliente cliente = entityManager.find(Cliente.class, id);

        // action
        entityManager.getTransaction().begin();
        entityManager.remove(cliente);
        entityManager.getTransaction().commit();

        // assert
        Cliente clienteVerificado = entityManager.find(Cliente.class, id);
        assertNull(clienteVerificado);
    }

    @Test
    void deveInserirOPrimeiroObjeto() {
        // arrange
//        var id = 2;
        var nome = "Sebastião Mello";
        var sexo = Sexo.MASCULINO;
        var cpf = "49044715046";
        Cliente cliente = new Cliente(nome, sexo, cpf);

        // action
        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
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
        Cliente clienteInserido = entityManager.find(Cliente.class, cliente.getId());
        assertNotNull(clienteInserido);
    }
}
