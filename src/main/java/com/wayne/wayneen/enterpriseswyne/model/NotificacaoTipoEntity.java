package com.wayne.wayneen.enterpriseswyne.model;

public class NotificacaoTipoEntity {
    private Long id;
    private String codigo;   // ex.: "INFO", "WARN", "ERROR", "ALERTA_RH"
    private String nome;     // rótulo amigável: "Informação", "Atenção", "Erro", "Alerta RH"
    private String corHex;   // ex.: "#2196F3", "#FFC107", "#E53935"
    private Integer ordem;   // para ordenar no ComboBox e listas
    private boolean ativo;   // permitir (des)ativar tipos sem apagar

    public NotificacaoTipoEntity() {}

    public NotificacaoTipoEntity(Long id, String codigo, String nome, String corHex, Integer ordem, boolean ativo) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.corHex = corHex;
        this.ordem = ordem;
        this.ativo = ativo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCorHex() { return corHex; }
    public void setCorHex(String corHex) { this.corHex = corHex; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        // útil para exibir no ComboBox
        return (nome != null && !nome.isEmpty()) ? nome : codigo;
    }
}
