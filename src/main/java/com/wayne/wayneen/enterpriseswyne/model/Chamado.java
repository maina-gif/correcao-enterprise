package com.wayne.wayneen.enterpriseswyne.model;

public class Chamado {
    private int id;
    private String titulo;
    private String descricao;
    private String status;
    private String prioridade;
    private String funcionario;
    private String dataAbertura;

    public Chamado() {}

    public Chamado(String titulo, String descricao, String status, String prioridade, String funcionario, String dataAbertura) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.prioridade = prioridade;
        this.funcionario = funcionario;
        this.dataAbertura = dataAbertura;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }

    public String getFuncionario() { return funcionario; }
    public void setFuncionario(String funcionario) { this.funcionario = funcionario; }

    public String getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(String dataAbertura) { this.dataAbertura = dataAbertura; }
}
