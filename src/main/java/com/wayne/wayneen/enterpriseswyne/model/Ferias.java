package com.wayne.wayneen.enterpriseswyne.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Representa um período de férias de um funcionário.
 */
public class Ferias implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int funcionarioId;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String observacao;

    // =========================
    // Construtores
    // =========================
    public Ferias() {
        // necessário para frameworks e JavaFX
    }

    public Ferias(int id, int funcionarioId, LocalDate dataInicio, LocalDate dataFim, String observacao) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        setDataInicio(dataInicio);
        setDataFim(dataFim); // validação garante fim >= início
        this.observacao = observacao;
    }

    // =========================
    // Getters / Setters
    // =========================
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(int funcionarioId) { this.funcionarioId = funcionarioId; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) {
        if (dataInicio == null) {
            throw new IllegalArgumentException("dataInicio não pode ser nula.");
        }
        this.dataInicio = dataInicio;
        // Se já houver dataFim, revalida o intervalo
        if (this.dataFim != null && this.dataFim.isBefore(this.dataInicio)) {
            throw new IllegalArgumentException("dataFim não pode ser anterior a dataInicio.");
        }
    }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) {
        if (dataFim == null) {
            throw new IllegalArgumentException("dataFim não pode ser nula.");
        }
        if (this.dataInicio != null && dataFim.isBefore(this.dataInicio)) {
            throw new IllegalArgumentException("dataFim não pode ser anterior a dataInicio.");
        }
        this.dataFim = dataFim;
    }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    // =========================
    // Utilidades
    // =========================

    /**
     * Retorna o total de dias corridos do período (inclusivo).
     * Ex.: início=10 e fim=15 => 6 dias.
     */
    public long getTotalDias() {
        if (dataInicio == null || dataFim == null) return 0;
        return ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
    }

    /**
     * Verifica se este período se sobrepõe a outro.
     */
    public boolean sobrepoe(Ferias outra) {
        if (outra == null) return false;
        return !this.dataFim.isBefore(outra.dataInicio) && !outra.dataFim.isBefore(this.dataInicio);
    }

    // =========================
    // equals / hashCode / toString
    // =========================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ferias)) return false;
        Ferias ferias = (Ferias) o;
        // Considera igualdade por id quando disponível;
        // caso contrário, por (funcionarioId, dataInicio, dataFim)
        if (this.id != 0 && ferias.id != 0) {
            return this.id == ferias.id;
        }
        return funcionarioId == ferias.funcionarioId &&
                Objects.equals(dataInicio, ferias.dataInicio) &&
                Objects.equals(dataFim, ferias.dataFim);
    }

    @Override
    public int hashCode() {
        return (id != 0)
                ? Integer.hashCode(id)
                : Objects.hash(funcionarioId, dataInicio, dataFim);
    }

    @Override
    public String toString() {
        return "Ferias{" +
                "id=" + id +
                ", funcionarioId=" + funcionarioId +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", observacao='" + observacao + '\'' +
                ", totalDias=" + getTotalDias() +
                '}';
    }
}
