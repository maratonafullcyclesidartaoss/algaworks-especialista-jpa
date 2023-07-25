package com.github.sidartaoss.especialistajpa.ecommerce.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categoria",
        uniqueConstraints = {@UniqueConstraint(name = "unq_nome_categoria", columnNames = { "nome" }) },
        indexes = {@Index( name = "idx_nome_categoria", columnList = "nome") })
public class Categoria {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
//    @SequenceGenerator(name = "seq", sequenceName = "sequencias_chave_primaria")
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tabela")
//    @TableGenerator(
//            name = "tabela",
//            table = "hibernate_sequences",
//            pkColumnName = "sequence_name",
//            pkColumnValue = "categoria",
//            valueColumnName = "next_val",
//            initialValue = 0,
//            allocationSize = 50
//    )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_pai_id",
            foreignKey = @ForeignKey(name = "fk_categoria_categoriapai"))
    private Categoria categoriaPai;

    @OneToMany(mappedBy = "categoriaPai")
    private List<Categoria> categorias;

    @ManyToMany(mappedBy = "categorias")
    private List<Produto> produtos;

    public Categoria() {
    }

    public Categoria(String nome) {
        setNome(nome);
    }

    public Categoria(String nome, Categoria categoriaPai) {
        this(nome);
        setCategoriaPai(categoriaPai);
    }

    public Categoria(Integer id, String nome, Categoria categoriaPai) {
        this(nome, categoriaPai);
        setId(id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Informe o nome.");
        }
        this.nome = nome;
    }

    private void atualizarNome(String nome) {
        setNome(nome);
    }

    public Categoria getCategoriaPai() {
        return categoriaPai;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public void setCategoriaPai(Categoria categoriaPai) {
        Objects.requireNonNull(categoriaPai);
        this.categoriaPai = categoriaPai;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(id, categoria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoriaPai=" + categoriaPai +
                '}';
    }
}
