package com.wayne.wayneen.enterpriseswyne.model;

import java.time.LocalDate;

public class Treinamento {
    private int id;
    private String titulo;
    private String tipo; // Interno ou Externo
    private String local;
    private LocalDate data;

    public Treinamento() {}

    public Treinamento(int id, String titulo, String tipo, String local, LocalDate data) {
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.local = local;
        this.data = data;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTipo() { return tipo; }

    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getLocal() { return local; }

    public void setLocal(String local) { this.local = local; }

    public LocalDate getData() { return data; }

    public void setData(LocalDate data) { this.data = data; }
}
