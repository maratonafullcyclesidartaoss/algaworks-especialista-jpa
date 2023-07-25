package com.github.sidartaoss.especialistajpa.ecommerce.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "estoque",
        uniqueConstraints = { @UniqueConstraint(name = "unq_produto_id_estoque", columnNames = { "produto_id" }) },
        indexes = {@Index( name = "idx_produto_id_estoque", columnList = "produto_id") })
public class Estoque extends EntidadeBaseInteger {

    @OneToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_estoque_produto"))
    private Produto produto;

    private Integer quantidade;

    public Estoque() {
    }

    public Estoque(Integer id, Produto produto, Integer quantidade) {
        setId(id);
        setProduto(produto);
        setQuantidade(quantidade);
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        Objects.requireNonNull(produto);
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.validarQuantidade(quantidade);
        this.quantidade = quantidade;
    }

    public void aumentarQuantidade(Integer quantidade) {
        this.validarQuantidade(quantidade);
        if (isQuantidadeInformadaMenorIgualAQuantidadeEmEstoque(quantidade)) {
            throw new IllegalArgumentException("Quantidade informada deve ser maior que quantidade em estoque.");
        }
        this.quantidade = quantidade;
    }

    public void diminuirQuantidade(Integer quantidade) {
        this.validarQuantidade(quantidade);
        if (isQuantidadeInformadaMaiorIgualAQuantidadeEmEstoque(quantidade)) {
            throw new IllegalArgumentException("Quantidade informada deve ser menor que quantidade em estoque.");
        }
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "Estoque{" +
                "produto=" + produto +
                ", quantidade=" + quantidade +
                "} " + super.toString();
    }

    private boolean isQuantidadeInformadaMaiorIgualAQuantidadeEmEstoque(Integer quantidade) {
        return quantidade.intValue() >= this.getQuantidade().intValue();
    }

    private boolean isQuantidadeInformadaMenorIgualAQuantidadeEmEstoque(Integer quantidade) {
        return quantidade.intValue() <= this.getQuantidade().intValue();
    }

    private void validarQuantidade(Integer quantidade) {
        Objects.requireNonNull(quantidade);
        if (quantidade.intValue() < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        }
    }
}
