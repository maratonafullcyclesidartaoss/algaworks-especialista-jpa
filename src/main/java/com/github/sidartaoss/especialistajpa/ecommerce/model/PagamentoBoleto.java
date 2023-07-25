package com.github.sidartaoss.especialistajpa.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.Instant;

@Entity
//@Table(name = "pagamento_boleto")
@DiscriminatorValue("boleto")
public class PagamentoBoleto extends Pagamento {

    @Column(name = "codigo_barras", length = 100)
    private String codigoBarras;

    @Column(name = "data_vencimento")
    private Instant dataVencimento;

    public PagamentoBoleto() {
    }

    public void cancelar() {
        if (isStatusInvalido()) {
            throw new IllegalArgumentException("Status atual inválido para cancelar pagamento.");
        }
        this.setStatus(StatusPagamento.CANCELADO);
    }

    public void receber(StatusPagamento status) {
        if (isStatusInvalido()) {
            throw new IllegalArgumentException("Status atual inválido para receber pagamento.");
        }
        this.setStatus(StatusPagamento.RECEBIDO);
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        if (codigoBarras == null || codigoBarras.isBlank()) {
            throw new IllegalArgumentException("Informe o código de barras.");
        }
        this.codigoBarras = codigoBarras;
    }

    public Instant getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Instant dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    @Override
    public String toString() {
        return "PagamentoBoleto{" +
                "codigoBarras='" + codigoBarras + '\'' +
                ", dataVencimento=" + dataVencimento +
                "} " + super.toString();
    }

    private boolean isStatusInvalido() {
        return this.getStatus() == StatusPagamento.RECEBIDO
                || this.getStatus() == StatusPagamento.CANCELADO;
    }
}
