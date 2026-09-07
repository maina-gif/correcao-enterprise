package com.wayne.wayneen.enterpriseswyne.model;

import java.time.LocalDateTime;

public class LogAuditoria {
    private int id;
    private String usuario;
    private String acao;
    private String modulo;
    private LocalDateTime dataHora;

    public LogAuditoria(int id, String usuario, String acao, String modulo, LocalDateTime dataHora) {
        this.id = id;
        this.usuario = usuario;
        this.acao = acao;
        this.modulo = modulo;
        this.dataHora = dataHora;
    }

    public LogAuditoria(String usuario, String acao, String modulo) {
        this.usuario = usuario;
        this.acao = acao;
        this.modulo = modulo;
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getAcao() { return acao; }
    public String getModulo() { return modulo; }
    public LocalDateTime getDataHora() { return dataHora; }

    public void setId(int id) { this.id = id; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setAcao(String acao) { this.acao = acao; }
    public void setModulo(String modulo) { this.modulo = modulo; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}
