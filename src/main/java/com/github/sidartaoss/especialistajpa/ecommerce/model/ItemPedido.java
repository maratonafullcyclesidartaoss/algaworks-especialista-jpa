package com.github.sidartaoss.especialistajpa.ecommerce.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

//@IdClass(ItemPedidoId.class)
@Entity
@Table(name = "item_pedido")
public class ItemPedido {

//    @Id
//    @Column(name = "pedido_id")
//    private Integer pedidoId;
//
//    @Id
//    @Column(name = "produto_id")
//    private Integer produtoId;

    @EmbeddedId
    private ItemPedidoId id;

    @MapsId("pedidoId")
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @ManyToOne(fetch = FetchType.LAZY)
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "pedido_id", nullable = false, foreignKey = @ForeignKey(name = "fk_item_pedido_pedido"))
    private Pedido pedido;

    @MapsId("produtoId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_pedido_produto"))
    private Produto produto;

    @Column(name = "preco_produto", nullable = false)
    private BigDecimal precoProduto;

    @Column(nullable = false)
    private Integer quantidade;

    public ItemPedido() {
    }

    public ItemPedido(Pedido pedido, Produto produto) {
        Objects.requireNonNull(pedido);
        Objects.requireNonNull(produto);
        this.id = new ItemPedidoId(Objects.requireNonNull(pedido.getId()),
                Objects.requireNonNull(produto.getId()));
    }

    public ItemPedido(Pedido pedido, Produto produto, BigDecimal precoProduto, Integer quantidade) {
        Objects.requireNonNull(pedido);
        Objects.requireNonNull(produto);
        this.pedido = pedido;
        this.produto = produto;
        setPrecoProduto(precoProduto);
        setQuantidade(quantidade);
        this.id = new ItemPedidoId();
    }

    public ItemPedido(ItemPedidoId id, Pedido pedido, Produto produto, BigDecimal precoProduto, Integer quantidade) {
        this(pedido, produto, precoProduto, quantidade);
        this.id = Objects.requireNonNull(id);
    }

    public ItemPedidoId getId() {
        return id;
    }

    public void setId(ItemPedidoId id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(BigDecimal precoProduto) {
        Objects.requireNonNull(precoProduto);
        if (precoProduto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço do produto deve ser maior que 0.");
        }
        this.precoProduto = precoProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        Objects.requireNonNull(quantidade);
        if (quantidade.intValue() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que 0.");
        }
        this.quantidade = quantidade;
    }

    public void alterarPrecoDoProduto(BigDecimal precoProduto) {
        setPrecoProduto(precoProduto);
    }

    public void alterarQuantidade(Integer quantidade) {
        setQuantidade(quantidade);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "id=" + id +
                ", pedido=" + pedido +
                ", produto=" + produto +
                ", precoProduto=" + precoProduto +
                ", quantidade=" + quantidade +
                '}';
    }
}
