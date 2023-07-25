package com.github.sidartaoss.especialistajpa.ecommerce.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SqlResultSetMappings({
        @SqlResultSetMapping(name = "produto_loja.Produto",
                entities = { @EntityResult(entityClass = Produto.class) }),
            @SqlResultSetMapping(name = "item_pedido-produto.ItemPedido-Produto",
                    entities = { @EntityResult(entityClass = ItemPedido.class),
                            @EntityResult(entityClass = Produto.class)})
})
@NamedQueries({
        @NamedQuery(name = "Produto.listar", query = "select p from Produto p"),
        @NamedQuery(name = "Produto.listarPeloCodigo", query = "select p from Produto p where p.id = :id"),
        @NamedQuery(name = "Produto.listarPorCategoria",
                query = """
                select p 
                from Produto p 
                where exists (
                   select c2.id
                   from Categoria c2
                   join c2.produtos p2 
                   where p2 = p 
                     and c2.id = :categoria
                )
                """)
})
@Entity
@Table(name = "produto",
        uniqueConstraints = {@UniqueConstraint(name = "unq_nome_produto", columnNames = { "nome" }) },
        indexes = {@Index( name = "idx_nome_produto", columnList = "nome") })
public class Produto extends EntidadeBaseInteger {

    @Column(length = 100, nullable = false)
    private String nome;

//    @Column(columnDefinition = "varchar(275) default 'descricao'")
    @Lob
    @Column(columnDefinition = "longtext null")
    private String descricao;

    // precision = 10. 10 contando com as casas decimais: antes da vírgula: 8 dígitos, após a vírgula: 2 dígitos?
    // preco decimal(10, 2)
//    @Column(precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "data_criacao", updatable = false, nullable = false)
    private Instant dataCriacao;

    @Column(name = "data_ultima_atualizacao", insertable = false)
    private Instant dataUltimaAtualizacao;

    @ManyToMany
    @JoinTable(name = "produto_categoria",
            joinColumns = @JoinColumn(name = "produto_id", nullable = false, foreignKey = @ForeignKey(name = "fk_produto_categoria_produto")),
            inverseJoinColumns = @JoinColumn(name = "categoria_id", nullable = false, foreignKey = @ForeignKey(name = "fk_produto_categoria_categoria")))
    private List<Categoria> categorias;

    @OneToOne(mappedBy = "produto")
    private Estoque estoque;

    @ElementCollection
    @CollectionTable(name = "produto_tag",
            joinColumns = @JoinColumn(name = "produto_id",
            foreignKey = @ForeignKey(name = "fk_produto_tag_produto")))
    @Column(name = "tag", length = 50, nullable = false)
    private List<String> tags;

    @ElementCollection
    @CollectionTable(name = "produto_atributo",
            joinColumns = @JoinColumn(name = "produto_id",
            foreignKey = @ForeignKey(name = "fk_produto_atributo_produto")))
    private List<Atributo> atributos;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "longblob null")
    private byte[] foto;

    public Produto() {
    }

    public Produto(String nome) {
        setNome(nome);
    }

    public Produto(String nome, String descricao, BigDecimal preco) {
        this(nome);
        setDescricao(descricao);
        setPreco(preco);
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

    public void alterarMarca(String nome) {
        setNome(nome);
    }

    public void alterarDescricao(String descricao) {
        setDescricao(descricao);
    }

    public void redefinirPreco(BigDecimal preco) {
        setPreco(preco);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Informe a descrição.");
        }
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        Objects.requireNonNull(preco);
        this.preco = preco;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public void alterarPreco(BigDecimal preco) {
        setPreco(preco);
    }

    public void atualizarDescricao(String descricao) {
        setDescricao(descricao);
    }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Instant dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Instant getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(Instant dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public void setTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            throw new IllegalArgumentException("Informe ao menos uma tag.");
        }
        this.tags = tags;
    }

    public void informarTags(List<String> tags) {
        setTags(tags);
    }

    public List<Atributo> getAtributos() {
        return Collections.unmodifiableList(atributos);
    }

    public void setAtributos(List<Atributo> atributos) {
        if (atributos == null || atributos.isEmpty()) {
            throw new IllegalArgumentException("Informe ao menos um atributo.");
        }
        this.atributos = atributos;
    }

    public void informarAtributos(List<Atributo> atributos) {
        setAtributos(atributos);
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        if (foto == null || foto.length == 0) {
            throw new IllegalArgumentException("Informe o arquivo da foto.");
        }
        this.foto = foto;
    }

    public boolean contemNome() {
        return nome != null && !nome.isBlank();
    }

    public boolean contemDescricao() {
        return descricao != null && !descricao.isBlank();
    }

    public void inserirFoto(byte[] foto) {
        setFoto(foto);
    }

    @PrePersist
    public void aoPersistir() {
        this.dataCriacao = Instant.now();
    }

    @Override
    public String toString() {
        return "Produto{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", dataCriacao=" + dataCriacao +
                ", dataUltimaAtualizacao=" + dataUltimaAtualizacao +
                ", categorias=" + categorias +
                ", estoque=" + estoque +
                ", tags=" + tags +
                ", atributos=" + atributos +
                ", foto=" + Arrays.toString(foto) +
                "} " + super.toString();
    }
}
