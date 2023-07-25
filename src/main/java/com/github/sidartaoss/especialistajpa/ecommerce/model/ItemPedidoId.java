package com.github.sidartaoss.especialistajpa.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ItemPedidoId implements Serializable {

    @Serial
    private static final long serialVersionUID = -3467021915320016002L;

    public ItemPedidoId() {
    }

    public ItemPedidoId(Integer pedidoId, Integer produtoId) {
        Objects.requireNonNull(pedidoId);
        Objects.requireNonNull(produtoId);
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
    }

    @Column(name = "pedido_id")
    private Integer pedidoId;

    @Column(name = "produto_id")
    private Integer produtoId;

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Integer getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Integer produtoId) {
        this.produtoId = produtoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedidoId that = (ItemPedidoId) o;
        return Objects.equals(pedidoId, that.pedidoId) && Objects.equals(produtoId, that.produtoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pedidoId, produtoId);
    }
}
