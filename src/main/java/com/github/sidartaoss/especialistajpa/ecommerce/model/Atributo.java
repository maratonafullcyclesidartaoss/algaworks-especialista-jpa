package com.github.sidartaoss.especialistajpa.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Atributo {

    @Column(length = 100, nullable = false)
    private String nome;

    private String valor;

    public Atributo() {
    }

    public Atributo(String nome, String valor) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Informe o nome.");
        }
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Informe o valor.");
        }
        this.nome = nome;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Atributo{" +
                "nome='" + nome + '\'' +
                ", valor='" + valor + '\'' +
                '}';
    }
}
