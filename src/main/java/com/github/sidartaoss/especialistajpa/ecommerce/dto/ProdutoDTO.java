package com.github.sidartaoss.especialistajpa.ecommerce.dto;

import java.util.Objects;

public class ProdutoDTO {

    public ProdutoDTO() {
    }

    public ProdutoDTO(Integer id, String nome) {
        Objects.requireNonNull(id);
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Informe o nome.");
        }
        this.id = id;
        this.nome = nome;
    }

    private Integer id;
    private String nome;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
