package com.github.sidartaoss.especialistajpa.ecommerce.conhecendoentitymanager;

import com.github.sidartaoss.especialistajpa.ecommerce.EntityManagerTest;
import com.github.sidartaoss.especialistajpa.ecommerce.model.Pedido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlushTest extends EntityManagerTest {

    @Test
    void deveChamarFlush() {
        // arrange
        var idDoPedido = 5;
        // act
        assertThrows(RuntimeException.class, () -> {
            try {
                entityManager.getTransaction().begin();
                Pedido pedido = entityManager.find(Pedido.class, idDoPedido);
                pedido.pagar();

                entityManager.flush();

                if (pedido.getPagamento() == null) {
                    throw new RuntimeException("Pedido ainda não foi pago.");
                }

                Pedido pedidoPago = entityManager.createQuery("select p from Pedido p where p.id = 1", Pedido.class).getSingleResult();
                assertEquals(pedido.getStatus(), pedidoPago.getStatus());

//                entityManager.getTransaction().commit();
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                throw e;
            }
        });
//        entityManager.getTransaction().begin();
//        Pedido pedido = entityManager.find(Pedido.class, 1);
//        pedido.pagar();

        // Outra estratégia para o flush, que despeja as atualizações no banco de dados,
        // é fazer uma consulta JPQL ou Criteria.
        // Quando executa uma query, o JPA não vai buscar os dados no cache de primeiro nível.
        // Quando a query está relacionada a alguma mudança/atualização, é como se executasse o flush:
        // tudo que está na memória do EntityManager relacionado a essa query é jogado para o banco.
        // Uma consulta obriga o JPA a sincronizar o que ele tem na memória com o banco de dados.
        // Isso quer dizer que ele vai no banco e faz o update.
        // Neste caso, para pegar o dado atualizado, não seria necessário atualizar com um flush,
        // porque o flush seria executado automaticamente logo antes de executar o JPQL.
//        Pedido pedidoPago = entityManager.createQuery("select p from Pedido p where p.id = 1", Pedido.class).getSingleResult();
//        assertEquals(pedido.getStatus(), pedidoPago.getStatus());

//        entityManager.getTransaction().commit();

        // assert
    }
}
