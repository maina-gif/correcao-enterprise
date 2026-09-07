package com.wayne.wayneen.enterpriseswyne;

import java.time.LocalDate;
import java.util.Objects;

public class Documento {

    private int id;
    private int funcionarioId;
    private String titulo;
    private LocalDate dataValidade;
    private String caminhoArquivo;
    private String tipo; // Tipo do documento

    public Documento() {}

    public Documento(int funcionarioId, String titulo, LocalDate dataValidade, String caminhoArquivo, String tipo) {
        this.funcionarioId = funcionarioId;
        this.titulo = titulo;
        this.dataValidade = dataValidade;
        this.caminhoArquivo = caminhoArquivo;
        this.tipo = tipo;
    }

    // getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(int funcionarioId) { this.funcionarioId = funcionarioId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }

    public String getCaminhoArquivo() { return caminhoArquivo; }
    public void setCaminhoArquivo(String caminhoArquivo) { this.caminhoArquivo = caminhoArquivo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // Método legado — mantenha se realmente necessário
    @Deprecated
    public void setValidade(LocalDate validade) {
        this.dataValidade = validade;
    }

    @Override
    public String toString() {
        return "Documento{" +
                "id=" + id +
                ", funcionarioId=" + funcionarioId +
                ", titulo='" + titulo + '\'' +
                ", dataValidade=" + dataValidade +
                ", caminhoArquivo='" + caminhoArquivo + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Documento)) return false;
        Documento that = (Documento) o;
        return id == that.id &&
                funcionarioId == that.funcionarioId &&
                Objects.equals(titulo, that.titulo) &&
                Objects.equals(dataValidade, that.dataValidade) &&
                Objects.equals(caminhoArquivo, that.caminhoArquivo) &&
                Objects.equals(tipo, that.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, funcionarioId, titulo, dataValidade, caminhoArquivo, tipo);
    }
}
