package com.wayne.wayneen.enterpriseswyne;

import java.time.LocalDate;

public class Equipamento {
    private int id;
    private String tipo;
    private String numeroSerie;
    private String funcionarioResponsavel;
    private String status;
    private LocalDate dataAquisicao;

    // Construtor vazio (necessário para o JavaFX e JDBC)
    public Equipamento() {}

    // Construtor completo (sem ID, usado ao cadastrar)
    public Equipamento(String tipo, String numeroSerie, String funcionarioResponsavel, String status, LocalDate dataAquisicao) {
        this.tipo = tipo;
        this.numeroSerie = numeroSerie;
        this.funcionarioResponsavel = funcionarioResponsavel;
        this.status = status;
        this.dataAquisicao = dataAquisicao;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }

    public String getFuncionarioResponsavel() { return funcionarioResponsavel; }
    public void setFuncionarioResponsavel(String funcionarioResponsavel) { this.funcionarioResponsavel = funcionarioResponsavel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDataAquisicao() { return dataAquisicao; }
    public void setDataAquisicao(LocalDate dataAquisicao) { this.dataAquisicao = dataAquisicao; }

    // Opcional: útil para logs e debug
    @Override
    public String toString() {
        return tipo + " - " + numeroSerie + " (" + status + ")";
    }
}
