package com.wayne.wayneen.enterpriseswyne.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notificacao {
    private Long id;
    private String mensagem;
    private LocalDateTime dataHora;
    private final BooleanProperty lida = new SimpleBooleanProperty(false);

    public Notificacao() {}

    public Notificacao(Long id, String mensagem, LocalDateTime dataHora, boolean lida) {
        this.id = id;
        this.mensagem = mensagem;
        this.dataHora = dataHora;
        this.lida.set(lida);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public boolean isLida() { return lida.get(); }
    public void setLida(boolean lida) { this.lida.set(lida); }
    public BooleanProperty lidaProperty() { return lida; }

    public String getDataHoraFormatada() {
        if (dataHora == null) return "";
        return dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
