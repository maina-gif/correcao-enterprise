package com.wayne.wayneen.enterpriseswyne.model;

import java.time.LocalDate;

public class Solicitacao {
    private int id;
    private String titulo;
    private String descricao;
    private String tipo;
    private LocalDate dataSolicitacao;
    private String status;
    private String responsavel;

    public Solicitacao() {}

    public Solicitacao(String titulo, String descricao, String tipo, LocalDate dataSolicitacao, String status, String responsavel) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.dataSolicitacao = dataSolicitacao;
        this.status = status;
        this.responsavel = responsavel;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDate getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDate dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
}