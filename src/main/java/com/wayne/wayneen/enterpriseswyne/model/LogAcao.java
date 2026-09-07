package com.wayne.wayneen.enterpriseswyne.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa uma ação registrada no sistema para fins de auditoria.
 */
public class LogAcao {

    private Long id;               // opcional: útil para persistência/edição
    private String usuario;
    private String acao;
    private String modulo;         // campo faltante (usado na UI/relatórios)
    private LocalDateTime momento;

    public LogAcao() {
    }

    public LogAcao(String usuario, String acao, String modulo, LocalDateTime momento) {
        this.usuario = usuario;
        this.acao = acao;
        this.modulo = modulo;
        this.momento = momento;
    }

    public LogAcao(Long id, String usuario, String acao, String modulo, LocalDateTime momento) {
        this.id = id;
        this.usuario = usuario;
        this.acao = acao;
        this.modulo = modulo;
        this.momento = momento;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public LocalDateTime getMomento() {
        return momento;
    }

    public void setMomento(LocalDateTime momento) {
        this.momento = momento;
    }

    @Override
    public String toString() {
        String m = (momento == null) ? "-" : momento.toString();
        String u = (usuario == null) ? "desconhecido" : usuario;
        String md = (modulo == null || modulo.isBlank()) ? "Geral" : modulo;
        String a = (acao == null) ? "" : acao;
        return "[" + m + "] (" + md + ") " + u + ": " + a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogAcao)) return false;
        LogAcao logAcao = (LogAcao) o;
        return Objects.equals(id, logAcao.id) &&
                Objects.equals(usuario, logAcao.usuario) &&
                Objects.equals(acao, logAcao.acao) &&
                Objects.equals(modulo, logAcao.modulo) &&
                Objects.equals(momento, logAcao.momento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usuario, acao, modulo, momento);
    }
}
