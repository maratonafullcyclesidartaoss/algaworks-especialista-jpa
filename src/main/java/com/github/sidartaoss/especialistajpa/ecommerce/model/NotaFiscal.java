package com.github.sidartaoss.especialistajpa.ecommerce.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "nota_fiscal")
public class NotaFiscal extends EntidadeBaseInteger {

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_nota_fiscal_pedido"))
//    @JoinTable(name = "pedido_nota_fiscal",
//            joinColumns = @JoinColumn(name = "nota_fiscal_id", unique = true),
//            inverseJoinColumns = @JoinColumn(name = "pedido_id", unique = true))
    private Pedido pedido;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "longblob not null")
    private byte[] xml;

    @Column(name = "data_emissao", nullable = false)
    private Instant dataEmissao;

    public NotaFiscal() {
    }

    public NotaFiscal(Pedido pedido, byte[] xml, Instant dataEmissao) {
        Objects.requireNonNull(pedido);
        this.pedido = pedido;
        setXml(xml);
        setDataEmissao(dataEmissao);
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public byte[] getXml() {
        return xml;
    }

    public void setXml(byte[] xml) {
        this.xml = xml;
    }

    public Instant getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Instant dataEmissao) {
        Objects.requireNonNull(dataEmissao);
        if (LocalDate.ofInstant(dataEmissao, ZoneId.of("UTC"))
                .isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de emissão não pode ser anterior a hoje.");
        }
        this.dataEmissao = dataEmissao;
    }

    @Override
    public String toString() {
        return "NotaFiscal{" +
                "pedido=" + pedido +
                ", xml=" + Arrays.toString(xml) +
                ", dataEmissao=" + dataEmissao +
                "} " + super.toString();
    }
}
