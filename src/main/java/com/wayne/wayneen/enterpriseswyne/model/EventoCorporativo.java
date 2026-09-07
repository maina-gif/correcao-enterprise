package com.wayne.wayneen.enterpriseswyne;

import java.time.LocalDate;

public class EventoCorporativo {

    private int id;
    private String titulo;
    private String descricao;
    private LocalDate data;
    private String tipo;
    private String local;
    private String responsavel;

    public EventoCorporativo() {}

    public EventoCorporativo(String titulo, String descricao, LocalDate data, String tipo, String local, String responsavel) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.tipo = tipo;
        this.local = local;
        this.responsavel = responsavel;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    @Override
    public String toString() {
        return titulo + " (" + data + ")";
    }
}
