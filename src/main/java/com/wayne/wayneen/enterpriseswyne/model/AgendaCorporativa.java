package com.wayne.wayneen.enterpriseswyne.model;


import java.time.LocalDate;

public class AgendaCorporativa {
    private int id;
    private String titulo;
    private String descricao;
    private LocalDate dataEvento;
    private String tipoEvento;
    private String local;
    private String responsavel;

    public AgendaCorporativa() {}

    public AgendaCorporativa(String titulo, String descricao, LocalDate dataEvento, String tipoEvento, String local, String responsavel) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataEvento = dataEvento;
        this.tipoEvento = tipoEvento;
        this.local = local;
        this.responsavel = responsavel;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDate getDataEvento() { return dataEvento; }
    public void setDataEvento(LocalDate dataEvento) { this.dataEvento = dataEvento; }
    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
}
