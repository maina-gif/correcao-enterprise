package com.wayne.wayneen.enterpriseswyne.model;

public class Candidato {
    private int id;
    private String nome;
    private String email;
    private String cargoPretendido;
    private String linkCurriculo;

    public Candidato() {}

    public Candidato(int id, String nome, String email, String cargoPretendido, String linkCurriculo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cargoPretendido = cargoPretendido;
        this.linkCurriculo = linkCurriculo;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getCargoPretendido() { return cargoPretendido; }

    public void setCargoPretendido(String cargoPretendido) { this.cargoPretendido = cargoPretendido; }

    public String getLinkCurriculo() { return linkCurriculo; }

    public void setLinkCurriculo(String linkCurriculo) { this.linkCurriculo = linkCurriculo; }
}
