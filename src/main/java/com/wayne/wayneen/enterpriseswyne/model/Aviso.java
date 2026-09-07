package com.wayne.wayneen.enterpriseswyne.model;

import java.time.LocalDate;

public class Aviso {
    private int id;
    private String titulo;
    private String descricao;
    private LocalDate data;
    private String tipo; // Aniversário, Férias, Comunicado

    public Aviso(int id, String titulo, String descricao, LocalDate data, String tipo) {}

    public Aviso(String titulo, String descricao, LocalDate data, String tipo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.tipo = tipo;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
