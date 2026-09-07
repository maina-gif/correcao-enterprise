package com.wayne.wayneen.enterpriseswyne;

import java.time.LocalDate;

public class Evento {
    private int id;
    private String titulo;
    private String descricao;
    private LocalDate data;
    private String local;
    private String tipo; // Campo adicionado

    public Evento() {}

    public Evento(String titulo, String descricao, LocalDate data, String local, String tipo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.local = local;
        this.tipo = tipo;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }
    public void setLocal(String local) {
        this.local = local;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
