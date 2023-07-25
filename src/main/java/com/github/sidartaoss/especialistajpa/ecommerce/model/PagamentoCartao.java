package com.github.sidartaoss.especialistajpa.ecommerce.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
//@Table(name = "pagamento_cartao")
@DiscriminatorValue("cartao")
public class PagamentoCartao extends Pagamento {

    @Column(name = "numero_cartao", length = 50)
    private String numeroCartao;

    public PagamentoCartao() {
    }

    public PagamentoCartao(Pedido pedido, String numeroCartao) {
        Objects.requireNonNull(pedido);
        this.setPedido(pedido);
        this.setNumeroCartao(numeroCartao);
        this.setStatus(StatusPagamento.PROCESSANDO);
    }

    public PagamentoCartao(Pedido pedido, StatusPagamento status, String numeroCartao) {
        this(pedido, numeroCartao);
        setStatus(status);
    }

    public void cancelar() {
        if (isJaCanceladoOuJaRecebido()) {
            throw new IllegalArgumentException("Não pode cancelar o pagamento, porque já cancelou ou o pagamento já foi recebido.");
        }
        this.setStatus(StatusPagamento.CANCELADO);
    }

    public void receber(StatusPagamento status) {
        if (isJaCanceladoOuJaRecebido()) {
            throw new IllegalArgumentException("Não pode receber o pagamento, porque já cancelou ou o pagamento já foi recebido.");
        }
        this.setStatus(StatusPagamento.RECEBIDO);
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        if (numeroCartao == null || numeroCartao.isBlank()) {
            throw new IllegalArgumentException("Informe o número do cartão.");
        }
        this.numeroCartao = numeroCartao;
    }

    @Override
    public String toString() {
        return "PagamentoCartao{" +
                "numeroCartao='" + numeroCartao + '\'' +
                "} " + super.toString();
    }

    private boolean isJaCanceladoOuJaRecebido() {
        return this.getStatus() == StatusPagamento.RECEBIDO
                || this.getStatus() == StatusPagamento.CANCELADO;
    }
}
