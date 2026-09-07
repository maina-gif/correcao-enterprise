package com.wayne.wayneen.enterpriseswyne.model;

import java.time.LocalDate;
import java.util.Objects;

public class EventoCalendario {
    private Long id;
    private String titulo;
    private LocalDate data;
    private String tipo;
    private String origem;
    private String descricao;

    public EventoCalendario() {}

    public EventoCalendario(Long id, String titulo, LocalDate data, String tipo, String origem, String descricao) {
        this.id = id;
        this.titulo = titulo;
        this.data = data;
        this.tipo = tipo;
        this.origem = origem;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override public boolean equals(Object o){ return o instanceof EventoCalendario && Objects.equals(id, ((EventoCalendario)o).id); }
    @Override public int hashCode(){ return Objects.hash(id); }
}
