package com.github.sidartaoss.especialistajpa.ecommerce.model;

import com.github.sidartaoss.especialistajpa.ecommerce.notafiscal.listener.GerarNotaFiscalListener;
import com.github.sidartaoss.especialistajpa.ecommerce.pedido.PedidoJaPagoOuCanceladoException;
import jakarta.persistence.*;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.hibernate.engine.spi.PersistentAttributeInterceptor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EntityListeners({GerarNotaFiscalListener.class})
@Entity
@Table(name = "pedido")
public class Pedido extends EntidadeBaseInteger implements PersistentAttributeInterceptable {

    //    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pedido_cliente"))
    private Cliente cliente;

    @Column(name = "data_criacao", updatable = false, nullable = false)
    private Instant dataCriacao;

    @Column(name = "data_ultima_atualizacao", insertable = false)
    private Instant dataUltimaAtualizacao;

    @Column(name = "data_conclusao")
    private Instant dataConclusao;

    @Column(name = "nota_fiscal_id")
    private Integer notaFiscalId;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private StatusPedido status;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Embedded
    private EnderecoEntrega enderecoEntrega;

    //    @OneToMany(mappedBy = "pedido", cascade = CascadeType.PERSIST)
//    @OneToMany(mappedBy = "pedido", cascade = CascadeType.MERGE)
//    @OneToMany(mappedBy = "pedido", cascade = CascadeType.REMOVE)
//    @OneToMany(mappedBy = "pedido", cascade = CascadeType.PERSIST, orphanRemoval = true)
//    @OneToMany(mappedBy = "pedido", orphanRemoval = true)
    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens = new ArrayList<>();

    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    private Pagamento pagamento;

    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    private NotaFiscal notaFiscal;

    @Transient
    private PersistentAttributeInterceptor persistentAttributeInterceptor;

    @Override
    public PersistentAttributeInterceptor $$_hibernate_getInterceptor() {
        return this.persistentAttributeInterceptor;
    }

    @Override
    public void $$_hibernate_setInterceptor(PersistentAttributeInterceptor persistentAttributeInterceptor) {
        this.persistentAttributeInterceptor = persistentAttributeInterceptor;
    }

    public Pedido() {
    }

    private Pedido(Builder builder) {
        this.setId(builder.id);
        this.cliente = builder.cliente;
        this.dataConclusao = builder.dataConclusao;
        this.notaFiscalId = builder.notaFiscalId;
        this.status = builder.status;
        this.enderecoEntrega = builder.enderecoEntrega;
    }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Instant dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Instant getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Instant dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public EnderecoEntrega getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(EnderecoEntrega enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Informe ao menos um item.");
        }
        this.itens = itens;
    }

    public Pagamento getPagamento() {
        if (this.persistentAttributeInterceptor != null) {
            return (Pagamento) persistentAttributeInterceptor.readObject(this, "pagamento", this.pagamento);
        }
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        if (this.persistentAttributeInterceptor != null) {

        }
        this.pagamento = pagamento;
    }

    public NotaFiscal getNotaFiscal() {
        if (this.persistentAttributeInterceptor != null) {
            return (NotaFiscal) persistentAttributeInterceptor.readObject(this, "notaFiscal", this.notaFiscal);
        }
        return notaFiscal;
    }

    public void setNotaFiscal(NotaFiscal notaFiscal) {
        if (this.persistentAttributeInterceptor != null) {

        }
        this.notaFiscal = notaFiscal;
    }

    public void pagar() {
        if (isJaCanceladoOuJaPago()) {
            throw new PedidoJaPagoOuCanceladoException("Pedido não pode ser pago porque já foi pago ou cancelado.");
        }
        this.status = StatusPedido.PAGO;
    }

    public void cancelar() {
        if (isJaCanceladoOuJaPago()) {
            throw new PedidoJaPagoOuCanceladoException("Pedido não pode ser cancelado porque já foi cancelado ou está pago.");
        }
        this.status = StatusPedido.CANCELADO;
    }

    public Instant getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(Instant dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public void informarItens(List<ItemPedido> itens) {
        setItens(itens);
    }

    public void removerItens() {
        this.itens.clear();
    }

    @PrePersist
    public void aoPersistir() {
        this.calcularTotal();
        this.dataCriacao = Instant.now();
    }

    @PreUpdate
    public void aoAtualizar() {
        this.calcularTotal();
        this.dataUltimaAtualizacao = Instant.now();
    }

    public boolean isPago() {
        return status == StatusPedido.PAGO;
    }

    public static class Builder {

        private Integer id;
        private final Cliente cliente;
        private Instant dataConclusao;
        private final Integer notaFiscalId;
        private final StatusPedido status;
        private EnderecoEntrega enderecoEntrega;

        public Builder(Cliente cliente, Integer notaFiscalId, StatusPedido status) {
            Objects.requireNonNull(cliente);
            Objects.requireNonNull(notaFiscalId);
            Objects.requireNonNull(status);
            this.cliente = cliente;
            this.notaFiscalId = notaFiscalId;
            this.status = status;
        }

        public Builder id(Integer id) {
            Objects.requireNonNull(id);
            this.id = id;
            return this;
        }

        public Builder dataConclusao(Instant dataConclusao) {
            Objects.requireNonNull(dataConclusao);
            this.dataConclusao = dataConclusao;
            return this;
        }

        public Builder enderecoEntrega(EnderecoEntrega enderecoEntrega) {
            Objects.requireNonNull(enderecoEntrega);
            this.enderecoEntrega = enderecoEntrega;
            return this;
        }

        public Pedido build() {
            return new Pedido(this);
        }
    }

    @Embeddable
    public static class EnderecoEntrega {

        @Column(length = 9)
        private String cep;

        @Column(length = 100)
        private String logradouro;

        @Column(length = 10)
        private String numero;

        @Column(length = 50)
        private String complemento;

        @Column(length = 50)
        private String bairro;

        @Column(length = 50)
        private String cidade;

        @Column(length = 2)
        private String estado;

        public EnderecoEntrega() {
        }

        private EnderecoEntrega(Builder builder) {
            this.cep = builder.cep;
            this.logradouro = builder.logradouro;
            this.numero = builder.numero;
            this.complemento = builder.complemento;
            this.bairro = builder.bairro;
            this.cidade = builder.cidade;
            this.estado = builder.estado;
        }

        public String getCep() {
            return cep;
        }

        public void setCep(String cep) {
            this.cep = cep;
        }

        public String getLogradouro() {
            return logradouro;
        }

        public void setLogradouro(String logradouro) {
            this.logradouro = logradouro;
        }

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public String getComplemento() {
            return complemento;
        }

        public void setComplemento(String complemento) {
            this.complemento = complemento;
        }

        public String getBairro() {
            return bairro;
        }

        public void setBairro(String bairro) {
            this.bairro = bairro;
        }

        public String getCidade() {
            return cidade;
        }

        public void setCidade(String cidade) {
            this.cidade = cidade;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public static class Builder {
            private String cep;
            private final String logradouro;
            private final String numero;
            private String complemento;
            private final String bairro;
            private final String cidade;
            private final String estado;

            public Builder(String logradouro, String numero, String bairro, String cidade, String estado) {
                if (logradouro == null || logradouro.isBlank()) {
                    throw new IllegalArgumentException("Informe o logradouro.");
                }
                if (numero == null || numero.isBlank()) {
                    throw new IllegalArgumentException("Informe o número.");
                }
                if (bairro == null || bairro.isBlank()) {
                    throw new IllegalArgumentException("Informe o bairro.");
                }
                if (cidade == null || cidade.isBlank()) {
                    throw new IllegalArgumentException("Informe a cidade.");
                }
                if (estado == null || estado.isBlank()) {
                    throw new IllegalArgumentException("Informe o estado.");
                }
                this.logradouro = logradouro;
                this.numero = numero;
                this.bairro = bairro;
                this.cidade = cidade;
                this.estado = estado;
            }

            public Builder complemento(String complemento) {
                if (complemento == null || complemento.isBlank()) {
                    throw new IllegalArgumentException("Informe o complemento.");
                }
                this.complemento = complemento;
                return this;
            }

            public Builder cep(String cep) {
                if (cep == null || cep.isBlank()) {
                    throw new IllegalArgumentException("Informe o CEP.");
                }
                this.cep = cep;
                return this;
            }

            public Pedido.EnderecoEntrega build() {
                return new Pedido.EnderecoEntrega(this);
            }
        }
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "cliente=" + cliente +
                ", dataCriacao=" + dataCriacao +
                ", dataUltimaAtualizacao=" + dataUltimaAtualizacao +
                ", dataConclusao=" + dataConclusao +
                ", notaFiscalId=" + notaFiscalId +
                ", status=" + status +
                ", valorTotal=" + valorTotal +
                ", enderecoEntrega=" + enderecoEntrega +
                ", itens=" + itens +
                ", pagamento=" + pagamento +
                ", notaFiscal=" + notaFiscal +
                "} " + super.toString();
    }

    private boolean isJaCanceladoOuJaPago() {
        return status == StatusPedido.CANCELADO
                || status == StatusPedido.PAGO;
    }

    private void calcularTotal() {
        valorTotal = itens.stream()
                .map(item -> item.getPrecoProduto().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
