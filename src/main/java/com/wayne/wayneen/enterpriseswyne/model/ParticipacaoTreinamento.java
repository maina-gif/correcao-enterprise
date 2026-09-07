package com.wayne.wayneen.enterpriseswyne.model;

import java.time.LocalDate;

public class ParticipacaoTreinamento {
    private int id;
    private int idFuncionario;
    private int idTreinamento;
    private LocalDate dataParticipacao;

    public ParticipacaoTreinamento() {
    }

    public ParticipacaoTreinamento(int id, int idFuncionario, int idTreinamento, LocalDate dataParticipacao) {
        this.id = id;
        this.idFuncionario = idFuncionario;
        this.idTreinamento = idTreinamento;
        this.dataParticipacao = dataParticipacao;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }
    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public int getIdTreinamento() {
        return idTreinamento;
    }
    public void setIdTreinamento(int idTreinamento) {
        this.idTreinamento = idTreinamento;
    }

    public LocalDate getDataParticipacao() {
        return dataParticipacao;
    }
    public void setDataParticipacao(LocalDate dataParticipacao) {
        this.dataParticipacao = dataParticipacao;
    }
}
