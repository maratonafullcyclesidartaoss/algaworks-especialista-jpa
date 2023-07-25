package com.github.sidartaoss.especialistajpa.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "cliente",
        uniqueConstraints = {@UniqueConstraint(name = "unq_cpf_cliente", columnNames = { "cpf" }) },
        indexes = {@Index( name = "idx_nome_cliente", columnList = "nome") })
@SecondaryTable(name = "cliente_detalhe",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "cliente_id"),
                foreignKey = @ForeignKey(name = "fk_cliente_detalhe_cliente"))
public class Cliente extends EntidadeBaseInteger {

    @NotBlank
    @Column(length = 100, nullable = false)
    private String nome;

    @NotBlank
    @Column(length = 14, nullable = false)
    private String cpf;

    @ElementCollection
    @CollectionTable(name = "cliente_contato",
            joinColumns = @JoinColumn(name = "cliente_id",
                    foreignKey = @ForeignKey(name = "fk_cliente_contato_cliente")))
    @MapKeyColumn(name = "tipo")
    @Column(name = "descricao")
    private Map<String, String> contatos;

    @Transient
    private String primeiroNome;

    @Column(table = "cliente_detalhe", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Column(name = "data_nascimento", table = "cliente_detalhe")
    private Instant dataNascimento;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;

    public Cliente() {
    }

    public Cliente(String nome, Sexo sexo, String cpf) {
        setNome(nome);
        setSexo(sexo);
        setCpf(cpf);
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

    public void alterarNome(String nome) {
        setNome(nome);
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        Objects.requireNonNull(sexo);
        this.sexo = sexo;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public Map<String, String> getContatos() {
        return Collections.unmodifiableMap(contatos);
    }

    public void setContatos(Map<String, String> contatos) {
        if (contatos == null || contatos.isEmpty()) {
            throw new IllegalArgumentException("Informe ao menos um contato.");
        }
        this.contatos = contatos;
    }

    public void informarContatos(Map<String, String> contatos) {
        setContatos(contatos);
    }

    public Instant getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Instant dataNascimento) {
        Objects.requireNonNull(dataNascimento);
        var data = LocalDate.ofInstant(dataNascimento, ZoneId.of("UTC"));
        var hoje = LocalDate.now();
        if (data.isEqual(hoje) || data.isAfter(hoje)) {
            throw new IllegalArgumentException("Data de nascimento deve ser anterior à data atual.");
        }
        this.dataNascimento = dataNascimento;
    }

    public void informarDataDeNascimento(Instant dataNascimento) {
        setDataNascimento(dataNascimento);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("Informe o CPF.");
        }
        this.cpf = cpf;
    }

    @PostLoad
    public void configurarPrimeiroNome() {
        this.primeiroNome = extrairPrimeiroNome(getNome());
    }

    private String extrairPrimeiroNome(String nome) {
        int index = nome.indexOf(" ");
        if (index > -1) {
            return nome.substring(0, index);
        }
        return nome;
    }
}
